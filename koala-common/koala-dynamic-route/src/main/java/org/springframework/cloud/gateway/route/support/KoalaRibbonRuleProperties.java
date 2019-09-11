package org.springframework.cloud.gateway.route.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Ribbon 配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("ribbon.rule")
public class KoalaRibbonRuleProperties {
	/**
	 * 是否开启，默认：true
	 */
	private boolean enabled = Boolean.TRUE;
	/**
	 * 是否开启灰度路由，默认:false
	 */
	private boolean grayEnabled;
	/**
	 * 优先的ip列表，支持通配符，例如：10.20.0.8*、10.20.0.*
	 */
	private List<String> priorIpPattern = new ArrayList<>();
}
