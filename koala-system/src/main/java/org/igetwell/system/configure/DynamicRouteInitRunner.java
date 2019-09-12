package org.igetwell.system.configure;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.constans.cache.CacheKey;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.system.service.ISystemGatewayRouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.bean.GatewayRouteDefinitionBean;
import org.springframework.cloud.gateway.route.support.DynamicRouteInitEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Async;

import java.net.URI;
import java.util.*;

/**
 * 容器启动后保存配置文件里面的路由信息到Redis
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class DynamicRouteInitRunner {
	private final RedisTemplate redisTemplate;
	private final ISystemGatewayRouteService iSystemGatewayRouteService;

	@Async
	@Order
	@EventListener({WebServerInitializedEvent.class, DynamicRouteInitEvent.class})
	public void initRoute() {
		Boolean result = redisTemplate.delete(CacheKey.ROUTE_KEY);
		log.info("初始化网关路由 {} ", result);

		iSystemGatewayRouteService.routes().forEach(route -> {

			GatewayRouteDefinitionBean gatewayRouteDefinition = new GatewayRouteDefinitionBean();
			BeanUtils.copyProperties(route, gatewayRouteDefinition);
			gatewayRouteDefinition.setUri(URI.create(route.getUri()));
			List<PredicateDefinition> predicates = GsonUtils.fromJson(route.getPredicates(), ArrayList.class);
			List<FilterDefinition> filters = GsonUtils.fromJson(route.getFilters(), ArrayList.class);
			gatewayRouteDefinition.setPredicates(predicates);
			gatewayRouteDefinition.setFilters(filters);
			log.info("加载路由ID：{},{}", route.getId(), gatewayRouteDefinition);
			redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(GatewayRouteDefinitionBean.class));
			redisTemplate.opsForHash().put(CacheKey.ROUTE_KEY, route.getId(), gatewayRouteDefinition);
		});
		log.debug("初始化网关路由结束 ");
	}

	/**
	 * 配置文件设置为空redis 加载的为准
	 * @return
	 */
	@Bean
	public PropertiesRouteDefinitionLocator propertiesRouteDefinitionLocator() {
		return new PropertiesRouteDefinitionLocator(new GatewayProperties());
	}
}
