package org.igetwell.system.controller;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.SystemUserBean;
import org.igetwell.system.dto.SystemUserPageDto;
import org.igetwell.system.entity.SystemUser;
import org.igetwell.system.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemUserController {

    @Autowired
    private ISystemUserService iSystemUserService;

    /**
     * 登录(根据租户ID和用户名查询)
     * @param tenant 租户ID
     * @param username 用户名
     * @return
     */
    @PostMapping("/systemUser/loadByUsername/{tenant}/{username}")
    public SystemUser loadByUsername(@PathVariable("tenant") String tenant, @PathVariable("username") String username){
        SystemUser systemUser = iSystemUserService.loadByUsername(tenant, username);
        return systemUser;
    }

    /**
     * 登录(根据手机号查询)
     * @param mobile
     * @return
     */
    @PostMapping("/systemUser/loadByMobile/{mobile}/")
    public SystemUser loadByMobile(@PathVariable("mobile") String mobile){
        SystemUser systemUser = iSystemUserService.loadByMobile(mobile);
        return systemUser;
    }

    @PostMapping("/systemUser/getList")
    public ResponseEntity getList(@RequestBody SystemUserPageDto dto){
        Pagination<SystemUserBean> pagination = new Pagination<SystemUserBean>();
        iSystemUserService.getList(pagination, dto);
        return ResponseEntity.ok(pagination);
    }

    @PostMapping("/systemUser/add")
    public ResponseEntity insert(@RequestBody SystemUser systemUser){
        return iSystemUserService.insert(systemUser);
    }

    @PostMapping("/systemUser/deleteById/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        return iSystemUserService.deleteById(id);
    }

    @PostMapping("/systemUser/update")
    public ResponseEntity update(@RequestBody SystemUser systemUser){
        return iSystemUserService.update(systemUser);
    }

    @PostMapping("/systemUser/test")
    public ResponseEntity test(){
        return ResponseEntity.ok("abcd");
    }
}
