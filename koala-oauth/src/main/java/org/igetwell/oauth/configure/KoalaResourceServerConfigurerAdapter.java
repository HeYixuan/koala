package org.igetwell.oauth.configure;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.oauth.security.handler.AuthenticationAccessDeniedHandler;
import org.igetwell.oauth.security.handler.AuthenticationFailureHandler;
import org.igetwell.oauth.security.handler.AuthenticationSuccessHandler;
import org.igetwell.oauth.security.handler.OAuth2AuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 资源服务器
 */
@Slf4j
@Configuration
@EnableResourceServer
public class KoalaResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {


    @Autowired
    private OAuth2AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;


    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;

    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(lettuceConnectionFactory);
    }

    private BearerTokenExtractor tokenExtractor = new BearerTokenExtractor();

    @Override
    @SneakyThrows
    public void configure(HttpSecurity http) {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .antMatchers("/login/**","/oauth/**", "/systemUser/**", "/systemRole/**","/systemMenu/**", "/gateway/**").permitAll()
                // 监控端点内部放行
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
                .logout().permitAll()
                // /logout退出清除cookie
                .addLogoutHandler(new CookieClearingLogoutHandler("token", "remember-me"))
                .logoutSuccessHandler(new LogoutSuccessHandler())
                .and()
                // 认证鉴权错误处理,为了统一异常处理。每个资源服务器都应该加上。
                .exceptionHandling()
                .accessDeniedHandler(authenticationAccessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .csrf().disable()
                // 禁用httpBasic
                .httpBasic().disable();
    }

    public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
        public LogoutSuccessHandler() {
            // 重定向到原地址
            this.setUseReferer(true);
        }

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            try {
                // 解密请求头
                authentication =  tokenExtractor.extract(request);
                if(authentication!=null && authentication.getPrincipal()!=null){
                    String tokenValue = authentication.getPrincipal().toString();
                    log.debug("revokeToken tokenValue:{}",tokenValue);
                    // 移除token
                    redisTokenStore().removeAccessToken(redisTokenStore().readAccessToken(tokenValue));
                }
            }catch (Exception e){
                log.error("revokeToken error:{}",e);
            }
            super.onLogoutSuccess(request, response, authentication);
        }
    }
}
