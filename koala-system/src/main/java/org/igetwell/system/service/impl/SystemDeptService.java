package org.igetwell.system.service.impl;

import org.igetwell.system.mapper.SystemDeptMapper;
import org.igetwell.system.service.ISystemDeptService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemDeptService implements ISystemDeptService {

    @Resource
    private SystemDeptMapper systemDeptMapper;

    @Override
    public List<Long> getDeptAncestors(Long deptId) {
        return systemDeptMapper.getDeptAncestors(deptId);
    }

    @Override
    public List<Long> getAncestors(Long deptId) {
        return systemDeptMapper.getAncestors(deptId);
    }
}
