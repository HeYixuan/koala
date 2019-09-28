package org.igetwell.system.service.impl;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.entity.SystemMenu;
import org.igetwell.system.mapper.SystemMenuMapper;
import org.igetwell.system.service.ISystemMenuService;
import org.igetwell.system.vo.MenuTree;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemMenuService implements ISystemMenuService {

    @Resource
    private SystemMenuMapper systemMenuMapper;

    @Override
    public List<SystemMenu> loadByRole(Long id) {
        return systemMenuMapper.loadByRole(id);
    }

    @Override
    public List<SystemMenu> loadUnbound() {
        return systemMenuMapper.loadUnbound();
    }

    @Override
    public List<MenuTree> getMenuTree() {
        List<MenuTree> list = systemMenuMapper.getMenuTree();
        return list;
    }

    @Override
    public List<MenuTree> getMenus() {
        List<MenuTree> list = systemMenuMapper.getMenus();
        return list;
    }

    public List<MenuTree> getMenus(Pagination pagination){
        return systemMenuMapper.getMenus(pagination);
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        int i = systemMenuMapper.deleteById(id);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity insert(SystemMenu systemMenu) {
        int i = systemMenuMapper.insert(systemMenu);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity update(SystemMenu systemMenu) {
        int i = systemMenuMapper.update(systemMenu);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }
}
