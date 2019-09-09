package org.springframework.cloud.openfeign;

import feign.hystrix.HystrixFeign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * HystrixFeignTarget 配置
 */
@Configuration
@ConditionalOnClass(HystrixFeign.class)
@ConditionalOnProperty(value = "feign.hystrix.enabled", matchIfMissing = true)
public class KoalaHystrixFeignTargeterConfiguration {

	@Bean
	@Primary
	public Targeter koalaFeignTarget() {
		return new KoalaHystrixTargeter();
	}
}
