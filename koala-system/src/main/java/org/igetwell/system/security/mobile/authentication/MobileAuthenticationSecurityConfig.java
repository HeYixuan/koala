package org.igetwell.system.security.mobile.authentication;

import org.igetwell.oauth.security.handler.AuthenticationFailureHandler;
import org.igetwell.oauth.security.handler.AuthenticationSuccessHandler;
import org.igetwell.system.security.provider.KoalaSpringSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 手机号验证码登录配置入口
 */
@Component
public class MobileAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationEventPublisher defaultAuthenticationEventPublisher;
    @Autowired
    private KoalaSpringSecurityService koalaSpringSecurityService;

    @Override
    public void configure(HttpSecurity http) {
        MobileAuthenticationFilter mobileAuthenticationFilter = new MobileAuthenticationFilter();
        mobileAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        mobileAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        mobileAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        mobileAuthenticationFilter.setEventPublisher(defaultAuthenticationEventPublisher);

        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        mobileAuthenticationProvider.setKoalaSpringSecurityService(koalaSpringSecurityService);

        http
                // 注册到AuthenticationManager中去
                .authenticationProvider(mobileAuthenticationProvider)
                // 添加到 UsernamePasswordAuthenticationFilter 之后
                // 貌似所有的入口都是 UsernamePasswordAuthenticationFilter
                // 然后UsernamePasswordAuthenticationFilter的provider不支持这个地址的请求
                // 所以就会落在我们自己的认证过滤器上。完成接下来的认证
                .addFilterAfter(mobileAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
