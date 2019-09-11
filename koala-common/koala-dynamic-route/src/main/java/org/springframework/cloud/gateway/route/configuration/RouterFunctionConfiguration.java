package org.springframework.cloud.gateway.route.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.handler.HystrixFallbackHandler;
import org.springframework.cloud.gateway.route.handler.ImageCodeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * 路由配置信息
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {
	private final HystrixFallbackHandler hystrixFallbackHandler;
	private final ImageCodeHandler imageCodeHandler;

	@Bean
	public RouterFunction routerFunction() {
		return RouterFunctions.route(
			RequestPredicates.path("/fallback")
				.and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)), hystrixFallbackHandler)
			.andRoute(RequestPredicates.GET("/code")
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), imageCodeHandler);
	}

}
