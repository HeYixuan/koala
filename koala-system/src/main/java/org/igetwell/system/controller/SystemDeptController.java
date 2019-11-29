package org.igetwell.system.controller;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.oauth.security.KoalaUser;
import org.igetwell.oauth.security.SpringSecurityUtils;
import org.igetwell.system.entity.SystemDept;
import org.igetwell.system.service.ISystemDeptService;
import org.igetwell.system.vo.DeptTree;
import org.igetwell.system.vo.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SystemDeptController {

    @Autowired
    private ISystemDeptService iSystemDeptService;


    @PostMapping("/systemDept/getList")
    public ResponseEntity getList(){
        KoalaUser koalaUser = SpringSecurityUtils.getUser();
        List<DeptTree> list = null;
        if (StringUtils.isEmpty(koalaUser) || StringUtils.isEmpty(koalaUser.getTenantId())){
            list = iSystemDeptService.getList(null);
        }else {
            list = iSystemDeptService.getList(koalaUser.getTenantId());
        }
        List<DeptTree> nodeTree = TreeUtils.buildDept(list);
        return ResponseEntity.ok(nodeTree);
    }

    @PostMapping("/systemDept/deleteById/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        iSystemDeptService.deleteById(id);
        return ResponseEntity.ok();
    }

    @PostMapping("/systemDept/add")
    public ResponseEntity insert(@RequestBody SystemDept systemDept){
        return iSystemDeptService.insert(systemDept);
    }

    @PostMapping("/systemDept/update")
    public ResponseEntity update(@RequestBody SystemDept systemDept){
        return iSystemDeptService.update(systemDept);
    }
}
