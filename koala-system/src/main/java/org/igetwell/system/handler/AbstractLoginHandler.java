package org.igetwell.system.handler;

import org.igetwell.system.entity.SystemUser;


public abstract class AbstractLoginHandler implements LoginHandler {

	/***
	 * 数据合法性校验
	 * @param identify 通过用户传入获取唯一标识
	 * @return
	 */
	public boolean check(String identify){
		return true;
	}


	/**
	 * 处理方法
	 * @param params
	 * @return
	 */
	public SystemUser handleMethod(String params){
		if (!check(params)) {
			return null;
		}

		String identify = identify(params);
		return get(identify);
	}
}
