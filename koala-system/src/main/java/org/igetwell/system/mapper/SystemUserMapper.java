package org.igetwell.system.mapper;

import java.util.List;
import java.util.Map;

import org.igetwell.system.entity.SystemUser;

public interface SystemUserMapper {

    /**
     * 登录(根据租户ID和用户名查询)
     * @param tenant 租户ID
     * @param username 用户名
     * @return
     */
    SystemUser loadByUsername(String tenant, String username);

    List<SystemUser> getList();

    int deleteById(Long id);

    int update(Map map);
}