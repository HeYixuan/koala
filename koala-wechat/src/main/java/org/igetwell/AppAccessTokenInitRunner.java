package org.igetwell;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.wechat.app.service.IWxAppService;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

/**
 * 容器启动后保存配置文件里面的路由信息到Redis
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class AppAccessTokenInitRunner {
	private final IWxAppService iWxAppService;

	@Async
	@Order
	@EventListener({WebServerInitializedEvent.class})
	public void initRoute() throws Exception {

//		iWxAppService.getAccessToken(true);
//		String accessToken = iWxAppService.getAccessToken();
//		if (accessToken == null || StringUtils.isEmpty(accessToken.trim())){
//			iWxAppService.getAccessToken(true);
//		}
	}
}
