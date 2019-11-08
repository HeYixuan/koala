package org.igetwell.system.mapper;

import org.igetwell.system.entity.SystemRole;
import org.igetwell.system.vo.RoleTree;
import org.igetwell.system.vo.SystemRoleVo;

import java.util.List;

public interface SystemRoleMapper {

    /**
     * 获取所有角色
     * @return
     */
    List<SystemRole> getList();

    /**
     * 获取所有角色(树节点)
     * @return
     */
    List<RoleTree> getRoles(String tenantId);

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

    /**
     * 根据角色ID查询数据权限类型
     * @param id
     * @return
     */
    Integer getDsType(Long id);

    /**
     * 根据租户ID和角色ID查询数据权限类型
     * @param tenant
     * @param id
     * @return
     */
    Integer getDataScopeType(String tenant, Long id);

    int deleteById(Long id);

    int insert(SystemRole systemRole);

    int update(SystemRole systemRole);
}