package org.igetwell.system.mapper;

import org.igetwell.system.entity.SystemTenant;

public interface SystemTenantMapper {

    int deleteById(Long id);

    int insert(SystemTenant systemTenant);

    int update(SystemTenant systemTenant);
}