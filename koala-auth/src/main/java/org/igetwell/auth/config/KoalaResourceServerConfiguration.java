/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.igetwell.auth.config;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 自定义登录成功配置
 *
 * @author Chill
 */
@Configuration
@AllArgsConstructor
@EnableResourceServer
public class KoalaResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	/**
	 * 自定义登录成功处理器
	 */
	private AuthenticationSuccessHandler appLoginInSuccessHandler;

	@Override
	@SneakyThrows
	public void configure(HttpSecurity http) {
		http.headers().frameOptions().disable();
		http.formLogin()
			.successHandler(appLoginInSuccessHandler)
			.and()
			.authorizeRequests()
			.antMatchers("/actuator/**", "/token/**", "/mobile/**", "/v2/api-docs", "/v2/api-docs-ext").permitAll()
			.anyRequest().authenticated().and()
			.csrf().disable();
	}

}
