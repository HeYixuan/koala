package org.igetwell.system.service.impl;

import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.SystemUserBean;
import org.igetwell.system.dto.SystemUserPageDto;
import org.igetwell.system.entity.SystemUser;
import org.igetwell.system.mapper.SystemUserMapper;
import org.igetwell.system.service.ISystemUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
     * 登录(根据手机号查询)
     * @param mobile
     * @return
     */
    public SystemUser loadByMobile(String mobile){
        return systemUserMapper.loadByMobile(mobile);
    }

    /**
     * 获取用户列表
     * @param pagination
     * @param dto
     * @return
     */
    @Override
    public List<SystemUserBean> getList(Pagination pagination, SystemUserPageDto dto) {
        List<SystemUserBean> systemUserList = systemUserMapper.getList(pagination, dto);
        return systemUserList;
    }

    @Override
    public ResponseEntity insert(SystemUser systemUser){
        if (!checkParam(systemUser)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "用户名不可为空!");
        }
        SystemUser sysUser = loadByUsername(systemUser.getTenantId(), systemUser.getUsername());
        if (!StringUtils.isEmpty(sysUser)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此用户已存在!");
        }
        int i = systemUserMapper.insert(systemUser);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity deleteById(Long id){
        return ResponseEntity.ok();
    }

    @Override
    public ResponseEntity update(SystemUser systemUser){
        if (!checkParam(systemUser)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "用户名不可为空!");
        }
        SystemUser sysUser = loadByUsername(systemUser.getTenantId(), systemUser.getUsername());
        if (StringUtils.isEmpty(sysUser)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此用户信息不存在!");
        }
        int i = systemUserMapper.update(systemUser);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    private boolean checkParam(SystemUser systemUser){
        if (StringUtils.isEmpty(systemUser) || !StringUtils.hasText(systemUser.getTenantId()) || !StringUtils.hasText(systemUser.getUsername())){
            return false;
        }
        return true;
    }
}
