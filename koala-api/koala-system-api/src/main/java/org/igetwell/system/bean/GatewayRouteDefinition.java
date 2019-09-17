package org.igetwell.system.bean;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建路由模型
 *
 *  扩展此类支持序列化，可以继承RouteDefinition
 *  See RouteDefinition.class
 */
@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayRouteDefinition {

    /**
     * 路由ID
     */
    private String id;
    /**
     * 路由名称
     */
    private String routeName;
    /**
     * 路由断言集合配置
     */
    private List<GatewayPredicateDefinition> predicates = new ArrayList<>();
    /**
     * 路由过滤器集合配置
     */
    private List<GatewayFilterDefinition> filters = new ArrayList<>();
    /**
     * 路由规则转发的目标uri
     */
    private String uri;
    /**
     * 路由执行的顺序
     */
    private int order = 0;
}
