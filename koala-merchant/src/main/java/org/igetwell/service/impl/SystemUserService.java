package org.igetwell.service.impl;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.service.ISystemUserService;
import org.igetwell.system.feign.SystemUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemUserService implements ISystemUserService {

    @Autowired
    SystemUserClient systemUserClient;

    @Override
    public ResponseEntity test() {
        return systemUserClient.test();
    }
}
