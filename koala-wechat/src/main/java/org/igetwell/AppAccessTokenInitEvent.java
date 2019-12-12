package org.igetwell;

import org.springframework.context.ApplicationEvent;

/**
 * 微信公众号令牌初始化事件
 */
public class AppAccessTokenInitEvent extends ApplicationEvent {
	public AppAccessTokenInitEvent(Object source) {
		super(source);
	}
}
