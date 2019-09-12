package org.igetwell.system.mapper;

import org.igetwell.system.entity.SystemGatewayRoute;

import java.util.List;

public interface SystemGatewayRouteMapper {

    /**
     * 获取全部路由
     * @return
     */
    List<SystemGatewayRoute> getList();

    /**
     * 根据ID删除路由
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 新增路由
     * @param route
     * @return
     */
    int addRoute(SystemGatewayRoute route);

    /**
     * 更新路由
     * @param route
     * @return
     */
    int updateRoute(SystemGatewayRoute route);

}