package org.igetwell.oauth.configure;

import org.igetwell.oauth.security.SpringSecurityService;
import org.igetwell.oauth.security.filter.MyFilterSecurityInterceptor;
import org.igetwell.oauth.security.handler.*;
import org.igetwell.oauth.security.mobile.authentication.MobileAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1) // WebSecurityConfigurerAdapter 默认为100 这里配置为2设置比资源认证器高
public class SpringSecurityWebAppConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SpringSecurityService springSecurityService;

    @Autowired
    private OAuth2AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;

    @Autowired
    private MyFilterSecurityInterceptor filterSecurityInterceptor;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .cors()
                .and()
                .csrf()
                .disable();
        http.apply(mobileSecurityConfigurer());
        http
                .addFilterBefore(filterSecurityInterceptor, FilterSecurityInterceptor.class)
                .authorizeRequests()
                .antMatchers("/login", "/oauth/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login") // 指定验证登录的 url
                .failureForwardUrl("/login?error")
                .and()
                .logout().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/static/**", "/favicon.ico","/error", "/webjars/**");
    }


    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(springSecurityService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public org.springframework.security.web.authentication.AuthenticationSuccessHandler mobileLoginSuccessHandler() {
        return new MobileLoginSuccessHandler();
    }

    /**
     * 手机号验证码登录
     * @return
     */
    @Bean
    public MobileAuthenticationSecurityConfig mobileSecurityConfigurer() {
        return new MobileAuthenticationSecurityConfig();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
