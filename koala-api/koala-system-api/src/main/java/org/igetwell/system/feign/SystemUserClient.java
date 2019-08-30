package org.igetwell.system.feign;


import org.igetwell.system.entity.SystemUser;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(contextId = "remoteUserService", value = "system-center")
public interface SystemUserClient {

    /**
     * 登录(根据租户ID和用户名查询)
     * @param tenant 租户ID
     * @param username 用户名
     * @return
     */
    SystemUser loadByUsername(String tenant, String username);

    /**
     * 获取用户列表
     * @return
     */
    List<SystemUser> getList();
}
