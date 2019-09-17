package org.springframework.cloud.gateway.route.support;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.constans.cache.CacheKey;
import org.igetwell.system.bean.GatewayRouteDefinitionBean;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * redis 保存路由信息，优先级比配置文件高
 */
@Slf4j
@Component
@AllArgsConstructor
public class RedisRouteDefinitionWriter implements RouteDefinitionRepository {
	private final RedisTemplate redisTemplate;

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return route.flatMap(r -> {
			GatewayRouteDefinitionBean bean = new GatewayRouteDefinitionBean();
			BeanUtils.copyProperties(r, bean);
			log.info("保存路由信息{}", bean);
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.opsForHash().put(CacheKey.ROUTE_KEY, r.getId(), bean);
			return Mono.empty();
		});
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		routeId.subscribe(id -> {
			log.info("删除路由信息{}", id);
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.opsForHash().delete(CacheKey.ROUTE_KEY, id);
		});
		return Mono.empty();
	}


	/**
	 * 动态路由入口
	 *
	 * @return
	 */
	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(GatewayRouteDefinitionBean.class));
		List<GatewayRouteDefinitionBean> values = redisTemplate.opsForHash().values(CacheKey.ROUTE_KEY);
		List<RouteDefinition> definitionList = new ArrayList<>();
		values.forEach(bean -> {
			RouteDefinition routeDefinition = new RouteDefinition();
			BeanUtils.copyProperties(bean, routeDefinition);
			definitionList.add(bean);
		});
		log.debug("redis 中路由定义条数： {}， {}", definitionList.size(), definitionList);
		return Flux.fromIterable(definitionList);
	}
}
