package org.igetwell.common.data.scope.configure;

import lombok.AllArgsConstructor;
import org.igetwell.common.data.scope.interceptor.DataScopeInterceptor;
import org.igetwell.common.data.scope.props.DataScopeProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据权限配置类
 *
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(DataScopeProperties.class)
public class DataScopeConfiguration {

	@Bean
	@ConditionalOnMissingBean(DataScopeInterceptor.class)
	public DataScopeInterceptor interceptor(JdbcTemplate template, DataScopeProperties dataScopeProperties) {
		return new DataScopeInterceptor(template, dataScopeProperties);
	}

}