package org.igetwell.system.controller;

import org.igetwell.system.bean.GatewayRouteDefinitionBean;
import org.igetwell.system.entity.SystemGatewayRoute;
import org.igetwell.system.service.ISystemGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SystemGatewayRouteController {

    @Autowired
    private ISystemGatewayRouteService iSystemGatewayRouteService;

    /**
     * 获取全部路由
     * @return
     */
    @PostMapping("/gateway/routes")
    public List<SystemGatewayRoute> routes(){
        return iSystemGatewayRouteService.routes();
    }

    /**
     * 添加路由
     *
     * @param route
     */
    @PostMapping("/gateway/addRoute")
    public void addRoute(@RequestBody GatewayRouteDefinitionBean route){
        iSystemGatewayRouteService.addRoute(route);
    }

    /**
     * 更新路由
     *
     * @param route
     */
    @PostMapping("/gateway/updateRoute")
    public void updateRoute(@RequestBody GatewayRouteDefinitionBean route){
        iSystemGatewayRouteService.updateRoute(route);
    }

    /**
     * 删除路由
     * @param id
     */
    @PostMapping("/gateway/deleteRoute/{id}")
    public void deleteRoute(@PathVariable("id") Long id){
        iSystemGatewayRouteService.deleteRoute(id);
    }

    /**
     * 刷新网关
     * 注:不要频繁调用!
     * 1.资源权限发生变化时可以调用
     * 2.流量限制变化时可以调用
     * 3.IP访问发生变化时可以调用
     * 4.智能路由发生变化时可以调用
     */
    @PostMapping("/gateway/refreshGateway")
    public void refreshGateway(){

    }
}
