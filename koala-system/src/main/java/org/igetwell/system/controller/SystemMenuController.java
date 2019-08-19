package org.igetwell.system.controller;

import org.igetwell.common.uitls.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class SystemMenuController {

    @PostMapping("/menu")
    public ResponseEntity menu(){
        return new ResponseEntity("menu");
    }

    @PostMapping("/setting")
    public ResponseEntity setting(){
        return new ResponseEntity("setting");
    }
}
