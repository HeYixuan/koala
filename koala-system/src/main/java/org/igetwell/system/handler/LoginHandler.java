package org.igetwell.system.handler;

import org.igetwell.system.entity.SystemUser;

/**
 * 登录处理器
 */
public interface LoginHandler {

	/***
	 * 数据合法性校验
	 * @param identify 通过用户传入获取唯一标识
	 * @return
	 */
	boolean check(String identify);

	/**
	 * 通过用户传入获取唯一标识
	 *
	 * @param identify
	 * @return
	 */
	String identify(String identify);

	/**
	 * 通过用户传入获取唯一标识 获取用户信息
	 * @param identify
	 * @return
	 */
	SystemUser get(String identify);


	/**
	 * 处理方法
	 * @param params
	 * @return
	 */
	SystemUser handleMethod(String params);
}
