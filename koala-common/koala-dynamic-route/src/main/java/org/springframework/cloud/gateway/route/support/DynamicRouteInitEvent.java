package org.springframework.cloud.gateway.route.support;

import org.springframework.context.ApplicationEvent;

/**
 * @author jekey
 * 路由初始化事件
 */
public class DynamicRouteInitEvent extends ApplicationEvent {
	public DynamicRouteInitEvent(Object source) {
		super(source);
	}
}
