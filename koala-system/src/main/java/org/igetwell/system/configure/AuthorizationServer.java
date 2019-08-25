package org.igetwell.system.configure;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.igetwell.common.constans.SecurityConstants;
import org.igetwell.common.data.tenant.TenantContextHolder;
import org.igetwell.common.security.KoalaUser;
import org.igetwell.system.security.KoalaClientDetailsService;
import org.igetwell.system.security.SpringSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证授权服务端
 */
@Configuration
@EnableAuthorizationServer
@EnableResourceServer
@AllArgsConstructor
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    private final SpringSecurityService springSecurityService;

    private final RedisConnectionFactory redisConnectionFactory;



    @Override // 配置框架应用上述实现
    public void configure(AuthorizationServerEndpointsConfigurer endpoints){
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancer())
                .userDetailsService(springSecurityService)
                .authenticationManager(authenticationManager)
                .reuseRefreshTokens(false);
    }


    /**
     * 配置客户端信息
     */
    @Override
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        KoalaClientDetailsService clientDetailsService = new KoalaClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }


    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix(SecurityConstants.KOALA_PREFIX + SecurityConstants.OAUTH_PREFIX);
        tokenStore.setAuthenticationKeyGenerator(new DefaultAuthenticationKeyGenerator() {
            @Override
            public String extractKey(OAuth2Authentication authentication) {
                return super.extractKey(authentication) + ":" + TenantContextHolder.getTenantId();
            }
        });
        return tokenStore;
    }

    /**
     * token增强，客户端模式不增强。
     *
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            if (SecurityConstants.CLIENT_CREDENTIALS
                    .equals(authentication.getOAuth2Request().getGrantType())) {
                return accessToken;
            }

            final Map<String, Object> additionalInfo = new HashMap<>(5);
            KoalaUser koalaUser = (KoalaUser) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put(SecurityConstants.DETAILS_USER_ID, koalaUser.getId());
            additionalInfo.put(SecurityConstants.DETAILS_USERNAME, koalaUser.getUsername());
            additionalInfo.put(SecurityConstants.DETAILS_DEPT_ID, koalaUser.getDeptId());
            additionalInfo.put(SecurityConstants.DETAILS_TENANT_ID, koalaUser.getTenantId());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }
}
