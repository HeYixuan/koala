package org.igetwell.system.service;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.SystemOauthClientDetailsBean;
import org.igetwell.system.dto.SystemOauthClientDetailsPageDto;
import org.igetwell.system.entity.SystemOauthClientDetails;

import java.util.List;

public interface ISystemOauthClientDetailService {

    /**
     * 根据应用ID查询客户端信息
     * @param clientId
     * @return
     */
    SystemOauthClientDetails get(String clientId);

    /**
     * 查询客户端列表
     * @param pagination
     * @param dto
     * @return
     */
    List<SystemOauthClientDetailsBean> getList(Pagination pagination, SystemOauthClientDetailsPageDto dto);

    void deleteById(String clientId);

    ResponseEntity insert(SystemOauthClientDetails systemOauthClientDetails);

    ResponseEntity update(SystemOauthClientDetails systemOauthClientDetails);
}
