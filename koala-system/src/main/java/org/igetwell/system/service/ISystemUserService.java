package org.igetwell.system.service;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.SystemUserBean;
import org.igetwell.system.dto.SystemUserPageDto;
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

    /**
     * 登录(根据手机号查询)
     * @param mobile
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

    ResponseEntity insert(SystemUser systemUser);

    void deleteById(Long id);

    ResponseEntity update(SystemUser systemUser);

}
