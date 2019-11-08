package org.igetwell.common.data.scope.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;
import org.igetwell.common.data.scope.annotation.DataScopeAuth;
import org.igetwell.common.data.scope.datascope.DataScope;
import org.igetwell.common.data.scope.enums.DataScopeEnum;
import org.igetwell.common.data.scope.props.DataScopeProperties;
import org.igetwell.common.uitls.CharacterUtils;
import org.igetwell.common.uitls.ClassUtils;
import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.SpringContextHolder;
import org.igetwell.oauth.security.KoalaUser;
import org.igetwell.oauth.security.SpringSecurityUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;


/**
 * mybatis 数据权限拦截器
 */
@Slf4j
@RequiredArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor implements Interceptor {
	private ConcurrentMap<String, DataScopeAuth> dataAuthMap = new ConcurrentHashMap<>(8);

	private final JdbcTemplate jdbcTemplate;

	private final DataScopeProperties dataScopeProperties;

	@Override
    @SneakyThrows
	public Object intercept(Invocation invocation) {

		//获取StatementHandler，默认是RoutingStatementHandler
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		//获取参数
		ParameterHandler parameterHandler = statementHandler.getParameterHandler();
		//获取statementHandler包装类
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		// 先判断是不是SELECT操作
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())
				|| StatementType.CALLABLE == mappedStatement.getStatementType()) {
			return invocation.proceed();
		}

		//获取分页对象
		RowBounds rowBounds = (RowBounds) metaObject.getValue("delegate.rowBounds");
		//获取SQL
		//BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
		String originalSql = statementHandler.getBoundSql().getSql();

		//注解为空并且数据权限方法名未匹配到,则放行
		String mapperId = mappedStatement.getId();
		String className = mapperId.substring(0, mapperId.lastIndexOf("."));
		String mapperName = ClassUtils.getShortName(className);
		String methodName = mapperId.substring(mapperId.lastIndexOf(".") + 1);
		boolean mapperSkip = dataScopeProperties.getMapperKey().stream().anyMatch(methodName::contains)
				|| dataScopeProperties.getMapperExclude().stream().anyMatch(mapperName::contains);
		if (mapperSkip) {
			//获取请求时的参数
			if (rowBounds instanceof Pagination){
				Pagination pagination = (Pagination) rowBounds;
				log.info("mybatis intercept sql:{}", originalSql);
				int total = getTotal(parameterHandler, invocation, originalSql);
				pagination.setTotal(total);
			}
			return invocation.proceed();
		}

		//查找注解中包含DataAuth类型的参数
		DataScopeAuth dataAuth = findDataAuthAnnotation(mappedStatement);
		KoalaUser koalaUser = SpringSecurityUtils.getUser();
		if (dataAuth == null && koalaUser == null) {
			return invocation.proceed();
		}

		List<Long> deptIds = new ArrayList<>();
		// 优先获取赋值数据
		//String column = dataAuth.column();
		String column = "dept_id";
		if (StringUtils.isNotEmpty(koalaUser.getDeptId())) {
			List<String> roleIdList = koalaUser.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList());

			Map<String,Object> params = jdbcTemplate.queryForMap("SELECT ds_type, ds_scope FROM sys_role where id = ?", new Object[]{koalaUser.getRoleId()});
			int dsType = (int) params.get("ds_type");

			// 查询全部
			if (DataScopeEnum.ALL.getType() == dsType) {
				return invocation.proceed();
			}
			// 自定义
			if (DataScopeEnum.CUSTOM.getType() == dsType) {
			    String dsScope = (String) params.get("ds_scope");
                deptIds.addAll(Arrays.stream(dsScope.split(",")).map(Long::parseLong).collect(Collectors.toList()));
			}

			if (DataScopeEnum.OWN.getType() == dsType) {
				deptIds.add(koalaUser.getId());
			}
			// 只查询本级
			if (DataScopeEnum.OWN_DEPT.getType() == dsType) {
				deptIds.add(Long.valueOf(koalaUser.getDeptId()));
			}
			// 查询本级及其下级
			if (DataScopeEnum.OWN_DEPT_CHILD.getType() == dsType) {
				List<Long> deptIdList = jdbcTemplate.queryForList("SELECT child FROM sys_dept_relation WHERE parent = ?", Long.class, koalaUser.getDeptId());
				deptIds.addAll(deptIdList);
			}

		}

		/*String join = StringUtils.join(deptIds, ",");
		originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + column + " in (" + join + ")";
		metaObject.setValue("delegate.boundSql.sql", originalSql);*/

		String join = StringUtils.join(deptIds, ",");
		originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + column + " in (" + join + ")";

		if (rowBounds instanceof Pagination){
			Pagination pagination = (Pagination) rowBounds;
			log.info("mybatis intercept sql:{}", originalSql);
			int total = getTotal(parameterHandler, invocation, originalSql);
			pagination.setTotal(total);
			String pageSql = originalSql + " limit " + pagination.getOffset() + ", " + pagination.getLimit();
			metaObject.setValue("delegate.boundSql.sql", pageSql);
		}else {
			metaObject.setValue("delegate.boundSql.sql", originalSql);
		}
		return invocation.proceed();
	}

	/**
	 * 生成拦截对象的代理
	 *
	 * @param target 目标对象
	 * @return 代理对象
	 */
	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	/**
	 * mybatis配置的属性
	 *
	 * @param properties mybatis配置的属性
	 */
	@Override
	public void setProperties(Properties properties) {

	}

	/**
	 * 查找参数是否包括DataScope对象
	 *
	 * @param parameterObj 参数列表
	 * @return DataScope
	 */
	private DataScope findDataScopeObject(Object parameterObj) {
		if (parameterObj instanceof DataScope) {
			return (DataScope) parameterObj;
		} else if (parameterObj instanceof Map) {
			for (Object val : ((Map<?, ?>) parameterObj).values()) {
				if (val instanceof DataScope) {
					return (DataScope) val;
				}
			}
		}
		return null;
	}


	/**
	 * 获取数据权限注解信息
	 *
	 * @param mappedStatement mappedStatement
	 * @return DataAuth
	 */
	private DataScopeAuth findDataAuthAnnotation(MappedStatement mappedStatement) {
		String id = mappedStatement.getId();
		return dataAuthMap.computeIfAbsent(id, (key) -> {
			String className = key.substring(0, key.lastIndexOf("."));
			String mapperBean = CharacterUtils.firstCharToLower(ClassUtils.getShortName(className));
			Object mapper = SpringContextHolder.getBean(mapperBean);
			String methodName = key.substring(key.lastIndexOf(".") + 1);
			Class<?>[] interfaces = ClassUtils.getAllInterfaces(mapper);
			for (Class<?> mapperInterface : interfaces) {
				for (Method method : mapperInterface.getDeclaredMethods()) {
					if (methodName.equals(method.getName()) && method.isAnnotationPresent(DataScopeAuth.class)) {
						return method.getAnnotation(DataScopeAuth.class);
					}
				}
			}
			return null;
		});
	}

	/**
	 * 获取总计录 缓存具有相同SQL语句和参数的总数
	 *
	 * @param parameterHandler
	 * @param invocation
	 * @param originalSql
	 * @return
	 * @throws Exception
	 */
	private int getTotal(ParameterHandler parameterHandler, Invocation invocation, String originalSql) throws Exception {
		String countSql = "select count(1) from (" + originalSql + ") temp";
		Connection connection = (Connection) invocation.getArgs()[0];
		PreparedStatement prepareStatement = connection.prepareStatement(countSql);
		parameterHandler.setParameters(prepareStatement);
		ResultSet rs = prepareStatement.executeQuery();
		int count = 0;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		rs.close();
		prepareStatement.close();
		return count;
	}

}
