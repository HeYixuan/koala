package org.igetwell.system.controller;

import org.igetwell.common.uitls.GsonUtils;
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
        System.err.println(GsonUtils.toJson(koalaUser));
        return new ResponseEntity("menu");
    }

    /**
     * 获取菜单树
     * @return
     */
    @PostMapping("/systemMenu/menuTree")
    public List<MenuTree> getMenuTree(){
        List<MenuTree> list = iSystemMenuService.getMenuTree();
        List<MenuTree> nodeTree = TreeUtils.treeMenu(list);
        System.err.println(GsonUtils.toJson(nodeTree));
        return nodeTree;
    }

    /**
     * 获取菜单树列表
     * @return
     */
    @PostMapping("/systemMenu/menus")
    public List<MenuTree> getMenus(){
        List<MenuTree> list = iSystemMenuService.getMenus();
        List<MenuTree> nodeTree = TreeUtils.treeMenu(list);
        System.err.println(GsonUtils.toJson(nodeTree));
        return nodeTree;
    }

    @PostMapping("/systemMenu/setting")
    public ResponseEntity setting(){
        return new ResponseEntity("setting");
    }
}
