package org.igetwell.common.data.tenant;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 租户信息拦截
 */
@Configuration
public class KoalaFeignTenantConfiguration {
	@Bean
	public RequestInterceptor koalaFeignTenantInterceptor() {
		return new KoalaFeignTenantInterceptor();
	}
}
