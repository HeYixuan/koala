package org.igetwell.system.service;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.dto.SystemTenantPageDto;
import org.igetwell.system.entity.SystemTenant;

import java.util.List;

public interface ISystemTenantService {

    /**
     * 根据租户ID查询
     * @param tenantId
     * @return
     */
    SystemTenant get(String tenantId);

    /**
     * 查询租户列表
     * @param pagination
     * @param dto
     * @return
     */
    List<SystemTenant> getList(Pagination pagination, SystemTenantPageDto dto);

    ResponseEntity deleteById(Long id);

    ResponseEntity insert(SystemTenant systemTenant);

    ResponseEntity update(SystemTenant systemTenant);
}
