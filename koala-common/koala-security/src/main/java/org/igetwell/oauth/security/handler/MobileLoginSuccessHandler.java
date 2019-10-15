package org.igetwell.oauth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.HashMap;

/**
 * 手机号登录成功，返回oauth token
 */
@Slf4j
public class MobileLoginSuccessHandler implements AuthenticationSuccessHandler {
	private static final String BASIC_ = "Basic ";
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ClientDetailsService clientDetailsService;
	@Lazy
	@Autowired
	private AuthorizationServerTokenServices defaultAuthorizationServerTokenServices;

	/**
	 * Called when a user has been successfully authenticated.
	 * 调用spring security oauth API 生成 oAuth2AccessToken
	 *
	 * @param request        the request which caused the successful authentication
	 * @param response       the response
	 * @param authentication the <tt>Authentication</tt> object which was created during
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith(BASIC_)) {
			throw new UnapprovedClientAuthenticationException("请求头中client信息为空");
		}

		try {
			String[] clients = extractAndDecodeHeader(header);
			assert clients.length == 2;
			String clientId = clients[0];

			ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

			//校验secret
			if (!passwordEncoder.matches(clients[1], clientDetails.getClientSecret())) {
				throw new InvalidClientException("Given client ID does not match authenticated client");

			}

			TokenRequest tokenRequest = new TokenRequest(new HashMap(), clientId, clientDetails.getScope(), "mobile");

			//校验scope
			new DefaultOAuth2RequestValidator().validateScope(tokenRequest, clientDetails);
			OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
			OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
			OAuth2AccessToken oAuth2AccessToken = defaultAuthorizationServerTokenServices.createAccessToken(oAuth2Authentication);
			log.info("获取token 成功：{}", oAuth2AccessToken.getValue());

			response.setCharacterEncoding("UTF-8");
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			PrintWriter printWriter = response.getWriter();
			printWriter.append(objectMapper.writeValueAsString(oAuth2AccessToken));
		} catch (IOException e) {
			throw new BadCredentialsException(
					"Failed to decode basic authentication token");
		}

	}


	/**
	 * 从header 请求中的clientId/clientsecect
	 *
	 * @param header header中的参数
	 * @throws RuntimeException if the Basic header is not present or is not valid
	 *                          Base64
	 */
	@SneakyThrows
	public String[] extractAndDecodeHeader(String header) {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
        String clients;
		try {
			clients = new String(Base64.getDecoder().decode(base64Token));
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(
					"Failed to decode basic authentication token");
		}

		int delimit = clients.indexOf(":");
		if (delimit == -1) {
			throw new RuntimeException("Invalid basic authentication token");
		}
        return clients.split(":");
	}

	/**
	 * 从header 请求中的clientId/clientSecret
	 *
	 * @param request
	 * @return
	 */
	@SneakyThrows
	public String[] extractAndDecodeHeader(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith(BASIC_)) {
			throw new RuntimeException("请求头中client信息为空");
		}

		return extractAndDecodeHeader(header);
	}


}
