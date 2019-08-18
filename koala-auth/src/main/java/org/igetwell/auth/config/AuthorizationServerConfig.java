/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package org.igetwell.auth.config;


import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.igetwell.auth.security.SpringSecurityService;
import org.igetwell.auth.service.KoalaClientDetailsService;
import org.igetwell.common.constans.SecurityConstants;
import org.igetwell.common.data.tenant.TenantContextHolder;
import org.igetwell.system.entity.SystemUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
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
 * 认证服务器配置
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	private final DataSource dataSource;
	private final SpringSecurityService springSecurityService;
	private final AuthenticationManager authenticationManagerBean;
	private final RedisConnectionFactory redisConnectionFactory;


	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
				.tokenStore(tokenStore())
				.tokenEnhancer(tokenEnhancer())
				.userDetailsService(springSecurityService)
				.authenticationManager(authenticationManagerBean)
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

			final Map<String, Object> additionalInfo = new HashMap<>(8);
			SystemUser systemUser = (SystemUser) authentication.getUserAuthentication().getPrincipal();
			additionalInfo.put(SecurityConstants.DETAILS_USER_ID, systemUser.getId());
			additionalInfo.put(SecurityConstants.DETAILS_USERNAME, systemUser.getUsername());
			additionalInfo.put(SecurityConstants.DETAILS_DEPT_ID, systemUser.getDeptId());
			additionalInfo.put(SecurityConstants.DETAILS_TENANT_ID, systemUser.getTenantId());
			//additionalInfo.put(SecurityConstants.DETAILS_LICENSE, SecurityConstants.KOALA_LICENSE);
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
			return accessToken;
		};
	}
}
