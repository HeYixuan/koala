package org.igetwell.wechat.component.web;

import org.igetwell.wechat.component.service.IWxComponentAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/component/app")
public class WxComponentAppController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(WxComponentController.class);

    @Autowired
    private IWxComponentAppService iWxComponentAppService;

    @PostMapping("/authorized")
    public String authorized() throws Exception {
        return iWxComponentAppService.authorized("https://open.easy.echosite.cn");
    }
}
