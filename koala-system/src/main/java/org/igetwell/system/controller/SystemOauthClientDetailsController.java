package org.igetwell.system.controller;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.SystemOauthClientDetailsBean;
import org.igetwell.system.dto.SystemOauthClientDetailsPageDto;
import org.igetwell.system.entity.SystemOauthClientDetails;
import org.igetwell.system.service.ISystemOauthClientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemOauthClientDetailsController {

    @Autowired
    private ISystemOauthClientDetailService iSystemOauthClientDetailService;

    @PostMapping("/systemClient/get/{clientId}")
    public ResponseEntity get(@PathVariable("clientId") String clientId){
        SystemOauthClientDetails oauthClientDetails = iSystemOauthClientDetailService.get(clientId);
        return ResponseEntity.ok(oauthClientDetails);
    }

    @PostMapping("/systemClient/getList")
    public ResponseEntity getList(@RequestBody SystemOauthClientDetailsPageDto dto){
        Pagination<SystemOauthClientDetailsBean> pagination = new Pagination<SystemOauthClientDetailsBean>();
        iSystemOauthClientDetailService.getList(pagination, dto);
        return ResponseEntity.ok(pagination);
    }

    @PostMapping("/systemClient/add")
    public ResponseEntity insert(@RequestBody SystemOauthClientDetails oauthClientDetails){
        return iSystemOauthClientDetailService.insert(oauthClientDetails);
    }

    @PostMapping("/systemClient/deleteById/{clientId}")
    public ResponseEntity deleteById(@PathVariable("clientId") String clientId){
        return iSystemOauthClientDetailService.deleteById(clientId);
    }

    @PostMapping("/systemClient/update")
    public ResponseEntity update(SystemOauthClientDetails oauthClientDetails){
        return iSystemOauthClientDetailService.update(oauthClientDetails);
    }
}
