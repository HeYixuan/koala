package org.igetwell.system.mapper;


import org.igetwell.system.entity.SystemOauthClientDetails;

public interface SystemOauthClientDetailsMapper {

    int deleteById(Long id);

    int insert(SystemOauthClientDetails systemOauthClientDetails);

    int update(SystemOauthClientDetails systemOauthClientDetails);
}