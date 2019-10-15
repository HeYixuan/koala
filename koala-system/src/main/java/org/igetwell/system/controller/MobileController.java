package org.igetwell.system.controller;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.service.IMobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileController {

    @Autowired
    private IMobileService iMobileService;

    /**
     * 短信登陆发送手机验证码
     * @param mobile
     * @return
     */
    @PostMapping("/mobile/{mobile}")
    public ResponseEntity sendSmsCode(@PathVariable("mobile")  String mobile) {
        return iMobileService.sendSmsCode(mobile);
    }
}
