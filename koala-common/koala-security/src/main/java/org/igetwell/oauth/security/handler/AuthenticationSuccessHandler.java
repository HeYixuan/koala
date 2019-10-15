package org.igetwell.oauth.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.uitls.WebUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationSuccessHandler extends AbstractAuthenticationSuccessEventHandler {

    /**
     * 处理登录成功方法
     * 获取到登录的authentication 对象
     * @param authentication 登录对象
     */
    @Override
    public void handle(Authentication authentication) {
       log.info("用户授权信息：[{}],真实IP是：[{}]", authentication.getDetails(), WebUtils.getIP());
    }
}
