package org.igetwell.common.data.scope.interceptor;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.igetwell.common.data.scope.annotation.DataScopeAuth;
import org.igetwell.common.data.scope.datascope.DataScope;
import org.igetwell.common.data.scope.enums.DataScopeEnum;
import org.igetwell.common.data.scope.props.DataScopeProperties;
import org.igetwell.common.security.KoalaUser;
import org.igetwell.common.security.SpringSecurityUtils;
import org.igetwell.common.uitls.ClassUtils;
import org.igetwell.common.uitls.SpringContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
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
public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {
	private ConcurrentMap<String, DataScopeAuth> dataAuthMap = new ConcurrentHashMap<>(8);
	//private final DataSource dataSource;
	private final JdbcTemplate jdbcTemplate;
	private final DataScopeProperties dataScopeProperties;

	@Override
	@SneakyThrows
	public Object intercept(Invocation invocation) {
		StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		this.sqlParser(metaObject);
		// 先判断是不是SELECT操作
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())
				|| StatementType.CALLABLE == mappedStatement.getStatementType()) {
			return invocation.proceed();
		}

		//查找注解中包含DataAuth类型的参数
		DataScopeAuth dataAuth = findDataAuthAnnotation(mappedStatement);

		//注解为空并且数据权限方法名未匹配到,则放行
		String mapperId = mappedStatement.getId();
		String className = mapperId.substring(0, mapperId.lastIndexOf("."));
		String mapperName = ClassUtils.getShortName(className);
		String methodName = mapperId.substring(mapperId.lastIndexOf(".") + 1);
		boolean mapperSkip = dataScopeProperties.getMapperKey().stream().noneMatch(methodName::contains)
				|| dataScopeProperties.getMapperExclude().stream().anyMatch(mapperName::contains);
		if (dataAuth == null && mapperSkip) {
			return invocation.proceed();
		}

		BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
		String originalSql = boundSql.getSql();
		Object parameterObject = boundSql.getParameterObject();

		//查找参数中包含DataScope类型的参数
		DataScope dataScope = findDataScopeObject(parameterObject);
		if (dataScope == null) {
			return invocation.proceed();
		}

		String scopeName = dataScope.getScopeName();
		List<Long> deptIds = dataScope.getDeptIds();
		// 优先获取赋值数据
		if (CollectionUtils.isEmpty(deptIds)) {
			KoalaUser koalaUser = SpringSecurityUtils.getUser();
			if (koalaUser == null) {
				throw new Exception("DataScopeInterceptor auto datascope, set up security details true");
			}

			List<String> roleIdList = koalaUser.getAuthorities()
					.stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList());

			/*Entity query = Db.use(dataSource)
					.query("SELECT * FROM sys_role where role_id IN (" + StringUtils.join(roleIdList, ",") + ")")
					.stream().min(Comparator.comparingInt(o -> o.getInt("ds_type"))).get();*/

			Integer dsType = jdbcTemplate.queryForObject("SELECT * FROM sys_role where id = ?", new Object[]{koalaUser.getRoleId()}, Integer.class);
			//Integer dsType = query.getInt("ds_type");
			// 查询全部
			if (DataScopeEnum.ALL.getType() == dsType) {
				return invocation.proceed();
			}
			// 自定义
			if (DataScopeEnum.CUSTOM.getType() == dsType) {
				/*String dsScope = query.getStr("ds_scope");
				deptIds.addAll(Arrays.stream(dsScope.split(","))
						.map(Integer::parseInt).collect(Collectors.toList()));*/
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
				/*List<Integer> deptIdList = Db.use(dataSource)
						.findBy("sys_dept_relation", "ancestor", koalaUser.getDeptId())
						.stream().map(entity -> entity.getInt("descendant"))
						.collect(Collectors.toList());
				deptIds.addAll(deptIdList);*/

				List<Long> deptIdList = jdbcTemplate.queryForList("SELECT child FROM sys_dept_relation WHERE parent = ?", Long.class, koalaUser.getDeptId());
				deptIds.addAll(deptIdList);
			}

		}
		String join = StringUtils.join(deptIds, ",");
		originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + scopeName + " in (" + join + ")";
		metaObject.setValue("delegate.boundSql.sql", originalSql);
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
			String mapperBean = com.baomidou.mybatisplus.core.toolkit.StringUtils.firstCharToLower(ClassUtils.getShortName(className));
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

}
