package org.igetwell.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.entity.SystemUser;
import org.igetwell.system.feign.SystemUserClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/systemUser")
public class SystemUserController {

    @Resource
    private SystemUserClient systemUserClient;

    @PostMapping("/getList")
    public List<SystemUser> getList(){
        List<SystemUser> systemUserList = systemUserClient.getList();
        return systemUserList;
    }

    @PostMapping("/test")
    public ResponseEntity test(){
        return systemUserClient.test();
    }
}
