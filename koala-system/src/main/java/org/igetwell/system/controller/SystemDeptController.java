package org.igetwell.system.controller;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.entity.SystemDept;
import org.igetwell.system.service.ISystemDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemDeptController {

    @Autowired
    private ISystemDeptService iSystemDeptService;

    @PostMapping("/systemDept/deleteById/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        return iSystemDeptService.deleteById(id);
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
