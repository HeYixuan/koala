package org.igetwell.system.controller;

import org.igetwell.system.entity.SystemUser;
import org.igetwell.system.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/systemUser")
public class SystemUserController {

    @Autowired
    private ISystemUserService iSystemUserService;

    @PostMapping("/getList")
    public List<SystemUser> getList(){
        return iSystemUserService.getList();
    }
}
