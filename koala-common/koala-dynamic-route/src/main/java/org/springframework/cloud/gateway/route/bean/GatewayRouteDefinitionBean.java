package org.springframework.cloud.gateway.route.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.io.Serializable;

/**
 * 扩展此类支持序列化a
 * See RouteDefinition.class
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GatewayRouteDefinitionBean extends RouteDefinition implements Serializable {
	/**
	 * 路由名称
	 */
	private String routeName;
}
