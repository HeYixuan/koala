package org.igetwell.system.mapper;

import org.igetwell.system.entity.SystemMenu;
import org.igetwell.system.vo.MenuTree;

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

    /**
     * 获取菜单
     * @return
     */
    List<MenuTree> getMenu();

    /**
     * 获取所有菜单
     * @return
     */
    List<MenuTree> getMenus();

    SystemMenu get(Long id);

    int deleteById(Long id);

    int insert(SystemMenu systemMenu);

    int update(SystemMenu systemMenu);
}