package org.igetwell.system.controller;

import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.oauth.security.KoalaUser;
import org.igetwell.oauth.security.SpringSecurityUtils;
import org.igetwell.system.entity.SystemMenu;
import org.igetwell.system.service.ISystemMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SystemMenuController {

    @Autowired
    private ISystemMenuService iSystemMenuService;

    /**
     * 根据角色ID查询所有菜单
     * @param id
     * @return
     */
    @PostMapping("/systemMenu/loadByRole/{id}")
    public List<SystemMenu> loadByRole(@PathVariable("id") Long id){
        return iSystemMenuService.loadByRole(id);
    }

    /**
     * 查询菜单ID不在中间表的菜单数据
     * @return
     */
    @PostMapping("/systemMenu/loadUnbound}")
    public List<SystemMenu> loadUnbound(){
        return iSystemMenuService.loadUnbound();
    }

    @PostMapping("/system/menu")
    public ResponseEntity menu(){
        KoalaUser koalaUser = SpringSecurityUtils.getUser();
        System.err.println(GsonUtils.toJson(koalaUser));
        return new ResponseEntity("menu");
    }

    @PostMapping("/system/setting")
    public ResponseEntity setting(){
        return new ResponseEntity("setting");
    }
}
