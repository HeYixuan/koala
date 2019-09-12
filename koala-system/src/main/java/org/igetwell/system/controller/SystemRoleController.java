package org.igetwell.system.controller;

import org.igetwell.system.entity.SystemRole;
import org.igetwell.system.service.ISystemRoleService;
import org.igetwell.system.vo.SystemRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class SystemRoleController {

    @Autowired
    private ISystemRoleService iSystemRoleService;

    /**
     * 获取所有角色
     * @return
     */
    @PostMapping("/systemRole/getList")
    public List<SystemRole> getList(){
        return iSystemRoleService.getList();
    }

    /**
     * 获取角色名
     * @param idList
     * @return
     */
    @PostMapping("/systemRole/getRoleNames")
    public List<SystemRoleVo> getRoleNames(@RequestBody List<String> idList){
        return iSystemRoleService.getRoleNames(idList);
    }


    /**
     * 根据用户ID查询所有角色
     * @param id
     * @return
     */
    @PostMapping("/systemRole/loadByUser/{id}")
    public List<SystemRoleVo> loadByUser(@PathVariable("id") Long id){
        return iSystemRoleService.loadByUser(id);
    }

    /**
     * 根据租户ID和用户ID查询所有角色
     * @param tenant
     * @param id
     * @return
     */
    @PostMapping("/systemRole/loadByTenant/{tenant}/{id}")
    public List<SystemRoleVo> loadByTenant(@PathVariable("tenant") String tenant, @PathVariable("id") Long id){
        return iSystemRoleService.loadByTenant(tenant, id);
    }
}
