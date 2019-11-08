package org.igetwell.system.controller;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.dto.SystemOssDto;
import org.igetwell.system.entity.SystemOss;
import org.igetwell.system.service.ISystemOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemOssController {

    @Autowired
    private ISystemOssService iSystemOssService;

    @PostMapping("/oss/get/{id}")
    public ResponseEntity get(@PathVariable("id") Long id){
        SystemOss oss = iSystemOssService.get(id);
        return ResponseEntity.ok(oss);
    }

    @PostMapping("/oss/getList")
    public ResponseEntity getList(@RequestBody SystemOssDto oss){
        Pagination<SystemOss> pagination = new Pagination<SystemOss>();
        pagination.setOffset(oss.getPageNo());
        pagination.setLimit(oss.getPageSize());
        iSystemOssService.getList(pagination, oss);
        return ResponseEntity.ok(pagination);
    }

    @PostMapping("/oss/add")
    public ResponseEntity insert(@RequestBody SystemOss oss){
        return iSystemOssService.insert(oss);
    }

    @PostMapping("/oss/deleteById/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        return iSystemOssService.deleteById(id);
    }

    @PostMapping("/oss/update")
    public ResponseEntity update(@RequestBody SystemOss oss){
        return iSystemOssService.update(oss);
    }
}
