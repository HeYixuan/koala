package org.igetwell.system.service;

import org.igetwell.system.entity.SystemGatewayRoute;
import org.springframework.cloud.gateway.route.bean.GatewayRouteDefinitionBean;
import reactor.core.publisher.Mono;
import java.util.List;

public interface ISystemGatewayRouteService {

    /**
     * 获取全部路由
     * @return
     */
    List<SystemGatewayRoute> routes();


    /**
     * 添加路由
     *
     * @param route
     */
    void addRoute(GatewayRouteDefinitionBean route);

    /**
     * 更新路由信息
     *
     * @param route 路由信息
     * @return
     */
    Mono<Void> updateRoutes(GatewayRouteDefinitionBean route);

    /**
     * 更新路由
     *
     * @param route
     */
    void updateRoute(GatewayRouteDefinitionBean route);

    /**
     * 删除路由
     * @param id
     */
    void deleteRoute(Long id);

    /**
     * 刷新网关
     * 注:不要频繁调用!
     * 1.资源权限发生变化时可以调用
     * 2.流量限制变化时可以调用
     * 3.IP访问发生变化时可以调用
     * 4.智能路由发生变化时可以调用
     */
    void refreshGateway();
}
