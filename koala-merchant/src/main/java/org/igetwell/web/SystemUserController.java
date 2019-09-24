package org.igetwell.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.service.ISystemUserService;
import org.igetwell.system.dto.SystemUserPageDto;
import org.igetwell.system.feign.SystemUserClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/systemUser")
public class SystemUserController {

    @Resource
    private SystemUserClient systemUserClient;

    @Resource
    private ISystemUserService iSystemUserService;

    @PostMapping("/getList")
    public ResponseEntity getList(@RequestBody SystemUserPageDto dto){
        return systemUserClient.getList(dto);
    }

    @PostMapping("/test")
    public ResponseEntity test(){
        return systemUserClient.test();
    }

    @PostMapping("/test2")
    public ResponseEntity test2(){
        return iSystemUserService.test();
    }
}
