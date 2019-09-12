package org.igetwell.web;

import org.igetwell.system.entity.SystemMenu;
import org.igetwell.system.feign.SystemMenuClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SystemMenuController {

    @Autowired
    private SystemMenuClient systemMenuClient;

    /**
     * 根据角色ID查询所有菜单
     * @param id
     * @return
     */
    @PostMapping("/systemMenu/loadByRole/{id}")
    public List<SystemMenu> loadByRole(@PathVariable("id") Long id){
        return systemMenuClient.loadByRole(id);
    }

    /**
     * 查询菜单ID不在中间表的菜单数据
     * @return
     */
    @PostMapping("/systemMenu/loadUnbound}")
    public List<SystemMenu> loadUnbound(){
        return systemMenuClient.loadUnbound();
    }
}
