package org.igetwell.system.feign;

import org.igetwell.system.bean.GatewayRouteDefinitionBean;
import org.igetwell.system.entity.SystemGatewayRoute;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "koala-system")
public interface SystemGatewayRouteClient {
    /**
     * 获取全部路由
     * @return
     */
    @PostMapping("/gateway/routes")
    List<SystemGatewayRoute> routes();

    /**
     * 添加路由
     *
     * @param route
     */
    @PostMapping("/gateway/addRoute")
    void addRoute(@RequestBody GatewayRouteDefinitionBean route);

    /**
     * 更新路由
     *
     * @param route
     */
    @PostMapping("/gateway/updateRoute")
    void updateRoute(@RequestBody GatewayRouteDefinitionBean route);

    /**
     * 删除路由
     * @param id
     */
    @PostMapping("/gateway/deleteRoute/{id}")
    void deleteRoute(@PathVariable("id") Long id);

    /**
     * 刷新网关
     * 注:不要频繁调用!
     * 1.资源权限发生变化时可以调用
     * 2.流量限制变化时可以调用
     * 3.IP访问发生变化时可以调用
     * 4.智能路由发生变化时可以调用
     */
    @PostMapping("/gateway/refreshGateway")
    void refreshGateway();
}
