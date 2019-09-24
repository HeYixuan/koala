package org.igetwell.common.data.scope.mybatis;

import org.igetwell.common.data.scope.interceptor.DataScopeInterceptor;
import org.igetwell.common.data.scope.interceptor.PaginationResultSetHandlerInterceptor;
import org.igetwell.common.data.scope.props.DataScopeProperties;
import org.igetwell.common.data.tenant.KoalaTenantHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DataScopeProperties.class)
//@MapperScan("org.igetwell.*.mapper")
public class MybatisPlusConfig implements InitializingBean {

	/**
	 * 创建租户维护处理器对象
	 *
	 * @return 处理后的租户维护处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public KoalaTenantHandler koalaTenantHandler() {
		return new KoalaTenantHandler();
	}

	/**
	 * 分页插件
	 *
	 * @param tenantHandler 租户处理器
	 * @return PaginationInterceptor
	 *//*
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "mybatisPlus.tenantEnable", havingValue = "true", matchIfMissing = true)
	public PaginationInterceptor paginationInterceptor(KoalaTenantHandler tenantHandler) {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		List<ISqlParser> sqlParserList = new ArrayList<>();
		TenantSqlParser tenantSqlParser = new TenantSqlParser();
		tenantSqlParser.setTenantHandler(tenantHandler);
		sqlParserList.add(tenantSqlParser);
		paginationInterceptor.setSqlParserList(sqlParserList);
		return paginationInterceptor;
	}*/

	/**
	 * 数据权限插件
	 *
	 * @param jdbcTemplate 数据源
	 * @return DataScopeInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public DataScopeInterceptor dataScopeInterceptor(JdbcTemplate jdbcTemplate, DataScopeProperties dataScopeProperties) {
		return new DataScopeInterceptor(jdbcTemplate, dataScopeProperties);
	}

	/**
	 * 分页结果集插件
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public PaginationResultSetHandlerInterceptor paginationInterceptor() {
		return new PaginationResultSetHandlerInterceptor();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("==========被加载了");
	}
}
