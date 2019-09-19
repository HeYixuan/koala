package org.igetwell.system.service.impl;

import org.igetwell.system.entity.SystemUser;
import org.igetwell.system.mapper.SystemUserMapper;
import org.igetwell.system.service.ISystemUserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemUserService implements ISystemUserService {

    @Resource
    private SystemUserMapper systemUserMapper;

    /**
     * 登录(根据租户ID和用户名查询)
     * @param tenant 租户ID
     * @param username 用户名
     * @return
     */
    @Override
    public SystemUser loadByUsername(String tenant, String username){
        return systemUserMapper.loadByUsername(tenant, username);
    }

    /**
     * 检查手机号
     * @param mobile
     * @return
     */
    @Override
    public SystemUser checkMobile(String mobile) {
        return systemUserMapper.checkMobile(mobile);
    }


    @Override
    public List<SystemUser> getList() {
        List<SystemUser> systemUserList = systemUserMapper.getList();
        return systemUserList;
    }
}
