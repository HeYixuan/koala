package org.igetwell.system.mapper;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.system.dto.SystemTenantPageDto;
import org.igetwell.system.entity.SystemTenant;

import java.util.List;

public interface SystemTenantMapper {

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

    int deleteById(Long id);

    int insert(SystemTenant systemTenant);

    int update(SystemTenant systemTenant);
}