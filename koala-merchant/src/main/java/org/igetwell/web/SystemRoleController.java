package org.igetwell.web;

import org.igetwell.system.entity.SystemRole;
import org.igetwell.system.feign.SystemRoleClient;
import org.igetwell.system.vo.SystemRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/systemRole")
public class SystemRoleController {

    @Autowired
    private SystemRoleClient systemRoleClient;

    /**
     * 获取所有角色
     * @return
     */
    @PostMapping("/getList")
    public List<SystemRole> getList(){
        return systemRoleClient.getList();
    }

    /**
     * 获取角色名
     * @param idList
     * @return
     */
    @PostMapping("/getRoleNames")
    public List<SystemRoleVo> getRoleNames(@RequestBody List<String> idList){
        return systemRoleClient.getRoleNames(idList);
    }


    /**
     * 根据用户ID查询所有角色
     * @param id
     * @return
     */
    @PostMapping("/loadByUser/{id}")
    public List<SystemRoleVo> loadByUser(@PathVariable("id") Long id){
        return systemRoleClient.loadByUser(id);
    }

    /**
     * 根据租户ID和用户ID查询所有角色
     * @param tenant
     * @param id
     * @return
     */
    @PostMapping("/loadByTenant/{tenant}/{id}")
    public List<SystemRoleVo> loadByTenant(@PathVariable("tenant") String tenant, @PathVariable("id") Long id){
        return systemRoleClient.loadByTenant(tenant, id);
    }
}
