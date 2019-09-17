package org.springframework.cloud.gateway.route.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 路由限流配置
 */
@Configuration
public class RateLimiterConfiguration {

	/**
	 * IP限流
	 * @return
	 */
	@Bean(value = "remoteAddrKeyResolver")
	public KeyResolver remoteAddrKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
	}

	/**
	 * 用户限流，使用这种方式限流，请求路径中必须携带userId参数
	 * @return
	 */
	@Bean(value = "remoteUserKeyResolver")
	public KeyResolver userKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
	}

	/**
	 * 接口限流，获取请求地址的uri作为限流key
	 * @return
	 */
	@Bean(value = "remoteApiKeyResolver")
	public KeyResolver apiKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getPath().value());
	}
}
