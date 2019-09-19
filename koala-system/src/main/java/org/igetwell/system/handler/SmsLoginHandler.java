package org.igetwell.system.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.system.entity.SystemUser;
import org.igetwell.system.service.ISystemUserService;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@AllArgsConstructor
public class SmsLoginHandler extends AbstractLoginHandler {

	private final ISystemUserService iSystemUserService;


	/**
	 * 验证码登录传入手机号
	 *
	 * @param mobile
	 * @return
	 */
	@Override
	public String identify(String mobile) {
		return mobile;
	}

	@Override
	public SystemUser get(String mobile) {
		SystemUser systemUser = iSystemUserService.checkMobile(mobile);
		if (systemUser == null){
			log.info("手机号未注册:{}", mobile);
			return null;
		}
		return systemUser;
	}

}
