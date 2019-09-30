package org.igetwell.system.mapper;

import org.igetwell.common.uitls.Pagination;
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
     * 获取菜单树
     * @return
     */
    List<MenuTree> getMenuTree();

    /**
     * 获取菜单树列表
     * @return
     */
    List<MenuTree> getMenus();

    List<MenuTree> getMenus(Pagination pagination);

    SystemMenu get(String menuName);

    int deleteById(Long id);

    int insert(SystemMenu systemMenu);

    int update(SystemMenu systemMenu);
}