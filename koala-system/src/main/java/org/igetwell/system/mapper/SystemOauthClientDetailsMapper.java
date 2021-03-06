package org.igetwell.system.mapper;


import org.igetwell.common.uitls.Pagination;
import org.igetwell.system.bean.SystemOauthClientDetailsBean;
import org.igetwell.system.dto.SystemOauthClientDetailsPageDto;
import org.igetwell.system.entity.SystemOauthClientDetails;

import java.util.List;

public interface SystemOauthClientDetailsMapper {

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

    int deleteById(String clientId);

    int insert(SystemOauthClientDetails systemOauthClientDetails);

    int update(SystemOauthClientDetails systemOauthClientDetails);
}