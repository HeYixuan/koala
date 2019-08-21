package org.igetwell.system.service;

import org.igetwell.system.entity.SystemUser;
import java.util.List;

public interface ISystemUserService {

    /**
     * 登录(根据租户ID和用户名查询)
     * @param tenant 租户ID
     * @param username 用户名
     * @return
     */
    SystemUser loadByUsername(String tenant, String username);

    List<SystemUser> getList();

}
