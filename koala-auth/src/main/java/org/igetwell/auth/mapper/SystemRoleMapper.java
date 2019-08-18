package org.igetwell.auth.mapper;

import org.igetwell.system.entity.SystemRole;
import org.igetwell.system.vo.SystemRoleVo;

import java.util.List;

public interface SystemRoleMapper {

    /**
     * 获取所有角色
     * @return
     */
    List<SystemRole> getList();

    /**
     * 获取角色名
     * @param idList
     * @return
     */
    List<SystemRoleVo> getRoleNames(List<String> idList);


    /**
     * 根据用户ID查询所有角色
     * @param id
     * @return
     */
    List<SystemRoleVo> loadByUser(Long id);

    /**
     * 根据租户ID和用户ID查询所有角色
     * @param tenant
     * @param id
     * @return
     */
    List<SystemRoleVo> loadByTenant(String tenant, Long id);
}