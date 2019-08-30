package org.igetwell.system.controller;

import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.oauth.security.KoalaUser;
import org.igetwell.oauth.security.SpringSecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class SystemMenuController {

    @PostMapping("/menu")
    public ResponseEntity menu(){
        KoalaUser koalaUser = SpringSecurityUtils.getUser();
        System.err.println(GsonUtils.toJson(koalaUser));
        return new ResponseEntity("menu");
    }

    @PostMapping("/setting")
    public ResponseEntity setting(){
        return new ResponseEntity("setting");
    }
}
