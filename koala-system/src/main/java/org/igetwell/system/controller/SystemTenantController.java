package org.igetwell.system.controller;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.dto.SystemTenantPageDto;
import org.igetwell.system.entity.SystemTenant;
import org.igetwell.system.service.ISystemTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemTenantController {

    @Autowired
    private ISystemTenantService iSystemTenantService;

    @PostMapping("/systemTenant/get/{tenantId}")
    public ResponseEntity get(@PathVariable("tenantId") String tenantId){
        SystemTenant systemTenant = iSystemTenantService.get(tenantId);
        return ResponseEntity.ok(systemTenant);
    }

    @PostMapping("/systemTenant/getList")
    public ResponseEntity getList(@RequestBody SystemTenantPageDto dto){
        Pagination<SystemTenant> pagination = new Pagination<SystemTenant>();
        iSystemTenantService.getList(pagination, dto);
        return ResponseEntity.ok(pagination);
    }

    @PostMapping("/systemTenant/add")
    public ResponseEntity insert(@RequestBody SystemTenant systemTenant){
        return iSystemTenantService.insert(systemTenant);
    }

    @PostMapping("/systemTenant/deleteById/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        return iSystemTenantService.deleteById(id);
    }

    @PostMapping("/systemTenant/update")
    public ResponseEntity update(@RequestBody SystemTenant systemTenant){
        return iSystemTenantService.update(systemTenant);
    }
}
