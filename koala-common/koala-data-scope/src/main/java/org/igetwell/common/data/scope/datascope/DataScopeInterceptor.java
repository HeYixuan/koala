package org.igetwell.common.data.scope.datascope;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.igetwell.common.data.scope.enums.DataScopeEnum;
import org.igetwell.common.security.KoalaUser;
import org.igetwell.common.security.SpringSecurityUtils;
import org.springframework.security.core.GrantedAuthority;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;


/**
 *
 * mybatis 数据权限拦截器
 */
@Slf4j
@AllArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {
	private final DataSource dataSource;

	@Override
	@SneakyThrows
	public Object intercept(Invocation invocation) {
		StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		this.sqlParser(metaObject);
		// 先判断是不是SELECT操作
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
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
		List<Integer> deptIds = dataScope.getDeptIds();
		// 优先获取赋值数据
		if (CollUtil.isEmpty(deptIds)) {
			KoalaUser koalaUser = SpringSecurityUtils.getUser();
			if (koalaUser == null) {
				throw new Exception("DataScopeInterceptor auto datascope, set up security details true");
			}

			List<String> roleIdList = koalaUser.getAuthorities()
					.stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList());

			Entity query = Db.use(dataSource)
					.query("SELECT * FROM sys_role where role_id IN (" + StringUtils.join(roleIdList, ",") + ")")
					.stream().min(Comparator.comparingInt(o -> o.getInt("ds_type"))).get();

			Integer dsType = query.getInt("ds_type");
			// 查询全部
			if (DataScopeEnum.ALL.getType() == dsType) {
				return invocation.proceed();
			}
			// 自定义
			if (DataScopeEnum.CUSTOM.getType() == dsType) {
				String dsScope = query.getStr("ds_scope");
				deptIds.addAll(Arrays.stream(dsScope.split(","))
						.map(Integer::parseInt).collect(Collectors.toList()));
			}
			// 查询本级及其下级
			if (DataScopeEnum.OWN_DEPT_CHILD.getType() == dsType) {
				List<Integer> deptIdList = Db.use(dataSource)
						.findBy("sys_dept_relation", "ancestor", koalaUser.getDeptId())
						.stream().map(entity -> entity.getInt("descendant"))
						.collect(Collectors.toList());
				deptIds.addAll(deptIdList);
			}
			// 只查询本级
			if (DataScopeEnum.OWN.getType() == dsType) {
				deptIds.add(Integer.valueOf(koalaUser.getDeptId()));
			}
		}
		String join = CollectionUtil.join(deptIds, ",");
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

}
