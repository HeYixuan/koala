package org.igetwell.system.controller;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.oauth.security.KoalaUser;
import org.igetwell.oauth.security.SpringSecurityUtils;
import org.igetwell.system.entity.SystemRole;
import org.igetwell.system.service.ISystemRoleService;
import org.igetwell.system.vo.RoleTree;
import org.igetwell.system.vo.SystemRoleVo;
import org.igetwell.system.vo.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
     * 获取所有角色(树节点)
     * @return
     */
    @PostMapping("/systemRole/getRoles")
    public ResponseEntity getRoles(String tenantId){
        KoalaUser koalaUser = SpringSecurityUtils.getUser();
        List<RoleTree> list = null;
        if (StringUtils.isEmpty(koalaUser) || StringUtils.isEmpty(koalaUser.getTenantId())){
            list = iSystemRoleService.getRoles(null);
        }else {
            list = iSystemRoleService.getRoles(koalaUser.getTenantId());
        }
        List<RoleTree> nodeTree = TreeUtils.buildRole(list);
        return ResponseEntity.ok(nodeTree);
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

    @PostMapping("/systemRole/add")
    public ResponseEntity insert(@RequestBody SystemRole systemRole){
        return iSystemRoleService.insert(systemRole);
    }

    @PostMapping("/systemRole/deleteById/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        iSystemRoleService.deleteById(id);
        return ResponseEntity.ok();
    }

    @PostMapping("/systemRole/update")
    public ResponseEntity update(@RequestBody SystemRole systemRole){
        return iSystemRoleService.update(systemRole);
    }
}
