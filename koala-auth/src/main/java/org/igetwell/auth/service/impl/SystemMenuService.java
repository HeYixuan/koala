package org.igetwell.auth.service.impl;

import org.igetwell.auth.mapper.SystemMenuMapper;
import org.igetwell.auth.service.ISystemMenuService;
import org.igetwell.system.entity.SystemMenu;
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
}
