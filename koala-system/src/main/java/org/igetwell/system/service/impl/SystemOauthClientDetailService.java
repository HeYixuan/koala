package org.igetwell.system.service.impl;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.SystemOauthClientDetailsBean;
import org.igetwell.system.dto.SystemOauthClientDetailsPageDto;
import org.igetwell.system.entity.SystemOauthClientDetails;
import org.igetwell.system.mapper.SystemOauthClientDetailsMapper;
import org.igetwell.system.service.ISystemOauthClientDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemOauthClientDetailService implements ISystemOauthClientDetailService {

    @Resource
    private SystemOauthClientDetailsMapper systemOauthClientDetailsMapper;

    @Override
    public SystemOauthClientDetails get(String clientId) {
        return systemOauthClientDetailsMapper.get(clientId);
    }

    @Override
    public List<SystemOauthClientDetailsBean> getList(Pagination pagination, SystemOauthClientDetailsPageDto dto) {
        return systemOauthClientDetailsMapper.getList(pagination, dto);
    }

    @Override
    public ResponseEntity deleteById(String clientId) {
        int i = systemOauthClientDetailsMapper.deleteById(clientId);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity insert(SystemOauthClientDetails systemOauthClientDetails) {
        int i = systemOauthClientDetailsMapper.insert(systemOauthClientDetails);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity update(SystemOauthClientDetails systemOauthClientDetails) {
        int i = systemOauthClientDetailsMapper.update(systemOauthClientDetails);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }
}
