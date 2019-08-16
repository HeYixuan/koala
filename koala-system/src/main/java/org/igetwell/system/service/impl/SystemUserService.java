package org.igetwell.system.service.impl;

import org.igetwell.system.entity.SystemUser;
import org.igetwell.system.mapper.SystemUserMapper;
import org.igetwell.system.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 */
@Service
public class SystemUserService implements ISystemUserService {
    @Autowired
    private SystemUserMapper systemUserMapper;

    @Override
    public List<SystemUser> getList() {
        return systemUserMapper.getList();
    }
}
