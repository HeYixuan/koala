package org.igetwell.auth.mapper;

import org.igetwell.system.entity.SystemMenu;

import java.util.List;

public interface SystemMenuMapper {

    /**
     * 根据角色ID查询所有菜单
     * @param id
     * @return
     */
    List<SystemMenu> loadByRole(Long id);

    /**
     * 查询菜单ID不在中间表的菜单数据
     * @return
     */
    List<SystemMenu> loadUnbound();
}