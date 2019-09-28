package org.igetwell.system.service.impl;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.dto.SystemTenantPageDto;
import org.igetwell.system.entity.SystemTenant;
import org.igetwell.system.mapper.SystemTenantMapper;
import org.igetwell.system.service.ISystemTenantService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemTenantService implements ISystemTenantService {

    @Resource
    private SystemTenantMapper systemTenantMapper;

    @Override
    public SystemTenant get(String tenantId) {
        return systemTenantMapper.get(tenantId);
    }

    @Override
    public List<SystemTenant> getList(Pagination pagination, SystemTenantPageDto dto) {
        return systemTenantMapper.getList(pagination, dto);
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        int i = systemTenantMapper.deleteById(id);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity insert(SystemTenant systemTenant) {
        int i = systemTenantMapper.insert(systemTenant);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity update(SystemTenant systemTenant) {
        int i = systemTenantMapper.update(systemTenant);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }
}
