package org.igetwell.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 社交登录类型
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
	/**
	 * 账号密码登录
	 */
	PWD("PWD", "账号密码登录"),

	/**
	 * 验证码登录
	 */
	MOBILE("MOBILE", "手机验证码登录"),


	/**
	 * 微信登录
	 */
	WX("WX", "微信登录");

	/**
	 * 类型
	 */
	private String type;
	/**
	 * 描述
	 */
	private String message;
}
