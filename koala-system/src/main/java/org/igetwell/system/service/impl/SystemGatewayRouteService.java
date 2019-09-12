package org.igetwell.system.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.constans.cache.CacheKey;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.system.entity.SystemGatewayRoute;
import org.igetwell.system.mapper.SystemGatewayRouteMapper;
import org.igetwell.system.service.ISystemGatewayRouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.bean.GatewayRouteDefinitionBean;
import org.springframework.cloud.gateway.route.support.DynamicRouteInitEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SystemGatewayRouteService implements ISystemGatewayRouteService {

    private final SystemGatewayRouteMapper systemGatewayRouteMapper;
    private final RedisTemplate redisTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    public List<SystemGatewayRoute> routes() {
        return systemGatewayRouteMapper.getList();
    }

    @Override
    public void addRoute(GatewayRouteDefinitionBean route) {
        SystemGatewayRoute gatewayRoute = transToGatewayRoutes(route);
        systemGatewayRouteMapper.addRoute(gatewayRoute);
    }

    @Override
    public Mono<Void> updateRoutes(GatewayRouteDefinitionBean route) {
        try {
            // 清空Redis 缓存
            boolean bool = redisTemplate.delete(CacheKey.ROUTE_KEY);
            log.info("清空网关路由 {} ", bool);
            SystemGatewayRoute gatewayRoute = transToGatewayRoutes(route);
            systemGatewayRouteMapper.updateRoute(gatewayRoute);
            redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(SystemGatewayRoute.class));
            redisTemplate.opsForHash().put(CacheKey.ROUTE_KEY, gatewayRoute.getId(), gatewayRoute);
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            log.debug("=============更新网关路由结束===============");
        } catch (Exception e){
            log.error("路由配置解析失败", e);
            // 回滚路由，重新加载即可
            this.applicationEventPublisher.publishEvent(new DynamicRouteInitEvent(this));
            throw new RuntimeException("路由配置解析失败");
        }
        return Mono.empty();
    }

    @Override
    public void updateRoute(GatewayRouteDefinitionBean route) {
        try {
            // 清空Redis 缓存
            boolean bool = redisTemplate.delete(CacheKey.ROUTE_KEY);
            log.info("清空网关路由 {} ", bool);
            SystemGatewayRoute gatewayRoute = transToGatewayRoutes(route);
            systemGatewayRouteMapper.updateRoute(gatewayRoute);
            redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(SystemGatewayRoute.class));
            redisTemplate.opsForHash().put(CacheKey.ROUTE_KEY, gatewayRoute.getId(), gatewayRoute);
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            log.debug("=============更新网关路由结束===============");
        } catch (Exception e){
            log.error("路由配置解析失败", e);
            // 回滚路由，重新加载即可
            this.applicationEventPublisher.publishEvent(new DynamicRouteInitEvent(this));
            throw new RuntimeException("路由配置解析失败");
        }

    }

    @Override
    public void deleteRoute(Long id) {
        systemGatewayRouteMapper.deleteById(id);
    }

    /**
     * 转化路由对象 SystemGatewayRoute
     * @param gatewayRouteDefinition
     * @return
     */
    private SystemGatewayRoute transToGatewayRoutes(GatewayRouteDefinitionBean gatewayRouteDefinition){
        SystemGatewayRoute gatewayRoute = new SystemGatewayRoute();
        BeanUtils.copyProperties(gatewayRouteDefinition, gatewayRoute);
        //设置路由ID
        if (!StringUtils.isEmpty(gatewayRoute.getId())){
            gatewayRoute.setId(java.util.UUID.randomUUID().toString().toUpperCase().replace("-",""));
        }
        String predicates = GsonUtils.toJson(gatewayRouteDefinition.getPredicates());
        String filters = GsonUtils.toJson(gatewayRouteDefinition.getFilters());

        gatewayRoute.setPredicates(predicates);
        gatewayRoute.setFilters(filters);
        return gatewayRoute;
    }


    /**
     * 刷新网关
     * 注:不要频繁调用!
     * 1.资源权限发生变化时可以调用
     * 2.流量限制变化时可以调用
     * 3.IP访问发生变化时可以调用
     * 4.智能路由发生变化时可以调用
     */
    @Override
    public void refreshGateway() {
        try {
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            log.info("refreshGateway:success");
        } catch (Exception e) {
            log.error("refreshGateway error:{}", e.getMessage());
        }
    }
}
