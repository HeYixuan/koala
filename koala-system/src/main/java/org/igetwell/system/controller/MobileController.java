package org.igetwell.system.controller;

import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.service.IMobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MobileController {

    @Resource
    private Sequence sequence;

    @Autowired
    private IMobileService iMobileService;

    /**
     * 短信登陆发送手机验证码
     * @param mobile
     * @return
     */
    @PostMapping("/mobile/{mobile}")
    public ResponseEntity send(@PathVariable("mobile")  String mobile) {
        return iMobileService.send(mobile);
    }


    @PostMapping("/mobile/test")
    public ResponseEntity sendSmsCode() {
        System.err.println("test1:" + sequence.nextNo());
        System.err.println("test2:" + sequence.nextValue());
        return ResponseEntity.ok();
    }

}
