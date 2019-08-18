package org.igetwell.auth.service.impl;

import org.igetwell.auth.mapper.SystemRoleMapper;
import org.igetwell.auth.service.ISystemRoleService;
import org.igetwell.system.entity.SystemRole;
import org.igetwell.system.vo.SystemRoleVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemRoleService implements ISystemRoleService {

    @Resource
    private SystemRoleMapper systemRoleMapper;

    @Override
    public List<SystemRole> getList() {
        return systemRoleMapper.getList();
    }

    @Override
    public List<SystemRoleVo> getRoleNames(List<String> idList) {
        return systemRoleMapper.getRoleNames(idList);
    }

    @Override
    public List<SystemRoleVo> loadByUser(Long id) {
        return systemRoleMapper.loadByUser(id);
    }

    @Override
    public List<SystemRoleVo> loadByTenant(String tenant, Long id) {
        return systemRoleMapper.loadByTenant(tenant, id);
    }
}
