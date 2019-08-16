package org.igetwell.system.mapper;

import java.util.List;
import java.util.Map;

import org.igetwell.system.entity.SystemUser;

public interface SystemUserMapper {

    List<SystemUser> getList();

    int deleteById(Long id);

    int update(Map map);
}