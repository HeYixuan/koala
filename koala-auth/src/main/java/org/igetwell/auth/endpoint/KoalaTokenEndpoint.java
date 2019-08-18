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

package org.igetwell.auth.endpoint;

import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.igetwell.common.constans.SecurityConstants;
import org.igetwell.common.constans.cache.CacheKey;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.ResponseEntity;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/6/24
 * 删除token端点
 */
@RestController
@AllArgsConstructor
@RequestMapping("/token")
public class KoalaTokenEndpoint {
	private static final String OAUTH_ACCESS = SecurityConstants.KOALA_PREFIX + SecurityConstants.OAUTH_PREFIX + "access:";
	private final ClientDetailsService clientDetailsService;
	private final RedisTemplate redisTemplate;
	private final TokenStore tokenStore;
	private final CacheManager cacheManager;

	/**
	 * 认证页面
	 *
	 * @param modelAndView
	 * @return ModelAndView
	 */
	@GetMapping("/login")
	public ModelAndView require(ModelAndView modelAndView) {
		modelAndView.setViewName("ftl/login");
		return modelAndView;
	}

	/**
	 * 确认授权页面
	 *
	 * @param request
	 * @param session
	 * @param modelAndView
	 * @return
	 */
	@GetMapping("/confirm_access")
	public ModelAndView confirm(HttpServletRequest request, HttpSession session, ModelAndView modelAndView) {
		Map<String, Object> scopeList = (Map<String, Object>) request.getAttribute("scopes");
		modelAndView.addObject("scopeList", scopeList.keySet());

		Object auth = session.getAttribute("authorizationRequest");
		if (auth != null) {
			AuthorizationRequest authorizationRequest = (AuthorizationRequest) auth;
			ClientDetails clientDetails = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
			modelAndView.addObject("app", clientDetails.getAdditionalInformation());
			//modelAndView.addObject("user", SecurityUtils.getUser());
		}

		modelAndView.setViewName("ftl/confirm");
		return modelAndView;
	}

	/**
	 * 退出token
	 *
	 * @param authHeader Authorization
	 */
	@DeleteMapping("/logout")
	public ResponseEntity logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		if (StringUtils.isEmpty(authHeader)) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "退出失败，token 为空");
		}

		String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StringUtils.EMPTY).trim();
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
		if (accessToken == null || StringUtils.isEmpty(accessToken.getValue())) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "退出失败，token 无效");
		}

		OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);
		// 清空用户信息
		cacheManager.getCache(CacheKey.USER_DETAILS)
				.evict(auth2Authentication.getName());

		// 清空access token
		tokenStore.removeAccessToken(accessToken);

		// 清空 refresh token
		OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
		tokenStore.removeRefreshToken(refreshToken);
		return new ResponseEntity();
	}

	/**
	 * 令牌管理调用
	 *
	 * @param token token
	 * @return
	 */
	@DeleteMapping("/{token}")
	public ResponseEntity delToken(@PathVariable("token") String token) {
		OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
		tokenStore.removeAccessToken(oAuth2AccessToken);
		return new ResponseEntity();
	}

}
