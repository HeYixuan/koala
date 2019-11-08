package org.igetwell.system.mapper;

import java.util.List;
import org.igetwell.common.uitls.Pagination;
import org.igetwell.system.bean.SystemUserBean;
import org.igetwell.system.dto.SystemUserPageDto;
import org.igetwell.system.entity.SystemUser;

public interface SystemUserMapper {

    /**
     * 登录(根据租户ID和用户名查询)
     * @param tenant 租户ID
     * @param username 用户名
     * @return
     */
    SystemUser loadByUsername(String tenant, String username);

    /**
     * 登录(根据租户ID和用户名查询)
     * @param mobile 用户名
     * @return
     */
    SystemUser loadByMobile(String mobile);

    /**
     * 获取用户列表
     * @param pagination
     * @param dto
     * @return
     */
    List<SystemUserBean> getList(Pagination pagination, SystemUserPageDto dto);

    SystemUserBean get(Long id);

    int insert(SystemUser systemUser);

    int deleteById(Long id);

    int update(SystemUser systemUser);
}