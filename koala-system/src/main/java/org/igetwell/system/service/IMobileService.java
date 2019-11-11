package org.igetwell.system.service;

import org.igetwell.common.uitls.ResponseEntity;

public interface IMobileService {


    /**
     * 发送手机验证码
     * @param mobile
     * @return
     */
    ResponseEntity send(String mobile);
}
