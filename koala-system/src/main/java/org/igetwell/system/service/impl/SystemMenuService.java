package org.igetwell.system.service.impl;

import org.igetwell.system.entity.SystemMenu;
import org.igetwell.system.mapper.SystemMenuMapper;
import org.igetwell.system.service.ISystemMenuService;
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
