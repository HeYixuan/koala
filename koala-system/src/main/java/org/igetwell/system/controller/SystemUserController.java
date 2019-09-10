package org.igetwell.system.controller;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.entity.SystemUser;
import org.igetwell.system.feign.SystemUserClient;
import org.igetwell.system.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class SystemUserController implements SystemUserClient{

    @Autowired
    private ISystemUserService iSystemUserService;

    /**
     * 登录(根据租户ID和用户名查询)
     * @param tenant 租户ID
     * @param username 用户名
     * @return
     */
    @PostMapping("/loadByUsername/{tenant}/{username}")
    public SystemUser loadByUsername(String tenant, String username){
        SystemUser systemUser = iSystemUserService.loadByUsername(tenant, username);
        return systemUser;
    }

    @PostMapping("/getList")
    public List<SystemUser>  getList(){
        //PageHelper.startPage(1,10);
        List<SystemUser> systemUserList = iSystemUserService.getList();
        Pagination<SystemUser> pagination = new Pagination<>(systemUserList);
        return systemUserList;
    }


    /*@PostMapping("/getList")
    public ResponseEntity<SystemUser> getList(){
        PageHelper.startPage(1,10);
        DataScope dataScope = new DataScope();
        List<SystemUser> systemUserList = iSystemUserService.getList(dataScope);
        Pagination<SystemUser> pagination = new Pagination<>(systemUserList);
        return new ResponseEntity(pagination);
    }*/
}
