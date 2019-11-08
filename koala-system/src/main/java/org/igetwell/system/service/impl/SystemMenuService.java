package org.igetwell.system.service.impl;

import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.entity.SystemMenu;
import org.igetwell.system.mapper.SystemMenuMapper;
import org.igetwell.system.service.ISystemMenuService;
import org.igetwell.system.vo.MenuTree;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public List<MenuTree> getMenu() {
        List<MenuTree> list = systemMenuMapper.getMenu();
        return list;
    }

    @Override
    public List<MenuTree> getMenus() {
        List<MenuTree> list = systemMenuMapper.getMenus();
        return list;
    }

    @Override
    public SystemMenu get(Long id){
        return systemMenuMapper.get(id);
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
        if (!checkParam(systemMenu)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "菜单名称不可为空!");
        }
        int i = systemMenuMapper.insert(systemMenu);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity update(SystemMenu systemMenu) {
        if (!checkParam(systemMenu)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "菜单名称不可为空!");
        }
        SystemMenu sysMenu = get(systemMenu.getId());
        if (StringUtils.isEmpty(sysMenu)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "菜单信息不存在!");
        }
        int i = systemMenuMapper.update(systemMenu);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    private boolean checkParam(SystemMenu systemMenu){
        if (StringUtils.isEmpty(systemMenu) || !StringUtils.hasText(systemMenu.getName()) || !StringUtils.hasText(systemMenu.getName())){
            return false;
        }
        return true;
    }
}
