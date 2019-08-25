package org.igetwell.system.security.provider;

import lombok.Data;

/**
 * 客户端详情
 *
 * @author Chill
 */
@Data
public class ClientDetails implements IClientDetails {

	/**
	 * 客户端id
	 */
	private String clientId;
	/**
	 * 客户端密钥
	 */
	private String clientSecret;

	/**
	 * 令牌过期秒数
	 */
	private Integer accessTokenValidity;
	/**
	 * 刷新令牌过期秒数
	 */
	private Integer refreshTokenValidity;

}
