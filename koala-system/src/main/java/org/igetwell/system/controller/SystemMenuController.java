package org.igetwell.system.controller;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.oauth.security.KoalaUser;
import org.igetwell.oauth.security.SpringSecurityUtils;
import org.igetwell.system.entity.SystemMenu;
import org.igetwell.system.service.ISystemMenuService;
import org.igetwell.system.vo.MenuTree;
import org.igetwell.system.vo.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/systemMenu/menu")
    public ResponseEntity menu(){
        KoalaUser koalaUser = SpringSecurityUtils.getUser();
        return new ResponseEntity("menu");
    }

    /**
     * 获取菜单(左侧菜单)
     * @return
     */
    @PostMapping("/systemMenu/getMenu")
    public ResponseEntity getMenu(){
        List<MenuTree> list = iSystemMenuService.getMenu();
        List<MenuTree> nodeTree = TreeUtils.buildMenu(list);
        return ResponseEntity.ok(nodeTree);
    }

    /**
     * 获取所有菜单列表
     * @return
     */
    @PostMapping("/systemMenu/getMenus")
    public ResponseEntity getMenus(){
        List<MenuTree> list = iSystemMenuService.getMenus();
        List<MenuTree> nodeTree = TreeUtils.buildMenu(list);
        return ResponseEntity.ok(nodeTree);
    }

    @PostMapping("/systemMenu/get/{id}")
    public ResponseEntity insert(@PathVariable("id") Long id){
        SystemMenu menu = iSystemMenuService.get(id);
        return ResponseEntity.ok(menu);
    }

    @PostMapping("/systemMenu/add")
    public ResponseEntity insert(@RequestBody SystemMenu systemMenu){
        return iSystemMenuService.insert(systemMenu);
    }

    @PostMapping("/systemMenu/deleteById/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        iSystemMenuService.deleteById(id);
        return ResponseEntity.ok();
    }

    @PostMapping("/systemMenu/update")
    public ResponseEntity update(@RequestBody SystemMenu systemMenu){
        return iSystemMenuService.update(systemMenu);
    }

    @PostMapping("/systemMenu/setting")
    public ResponseEntity setting(){
        return new ResponseEntity("setting");
    }
}
