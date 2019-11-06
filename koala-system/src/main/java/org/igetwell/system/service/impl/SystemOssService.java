package org.igetwell.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.dto.SystemOssDto;
import org.igetwell.system.entity.SystemOss;
import org.igetwell.system.mapper.SystemOssMapper;
import org.igetwell.system.service.ISystemOssService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemOssService implements ISystemOssService {

    @Resource
    private SystemOssMapper systemOssMapper;

    @Override
    public SystemOss get(Long id) {
        return systemOssMapper.get(id);
    }

    @Override
    public List<SystemOss> getList(Pagination pagination, SystemOssDto oss) {
        return systemOssMapper.getList(pagination, oss);
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        int i = systemOssMapper.deleteById(id);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity insert(SystemOss oss) {
        if (!checkParam(oss, oss.getTenantId())){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "租户不可为空!");
        }
        if (!checkParam(oss, oss.getBucketName())){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "空间名不可为空!");
        }
        if (!checkParam(oss, oss.getAccessKey())){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "accessKey不可为空!");
        }
        if (!checkParam(oss, oss.getSecretKey())){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "secretKey不可为空!");
        }
        int i = systemOssMapper.insert(oss);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity update(SystemOss oss) {
        if (!checkParam(oss, oss.getId())){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "ID不可为空!");
        }
        if (!checkParam(oss, oss.getTenantId())){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "租户不可为空!");
        }
        if (!checkParam(oss, oss.getBucketName())){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "空间名不可为空!");
        }
        if (!checkParam(oss, oss.getAccessKey())){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "accessKey不可为空!");
        }
        if (!checkParam(oss, oss.getSecretKey())){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "secretKey不可为空!");
        }

        int i = systemOssMapper.update(oss);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    private boolean checkParam(Object object, Object param){
        if (StringUtils.isEmpty(object) || StringUtils.isEmpty(param)){
            return false;
        }
        return true;
    }
}
