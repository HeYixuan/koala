package org.igetwell.wechat.component.web;

import org.igetwell.wechat.component.service.IWxComponentAppService;
import org.igetwell.wechat.component.service.IWxComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/test//wx/component/app")
public class TestComponentAppController {

    @Autowired
    private IWxComponentService iWxComponentService;
    @Autowired
    private IWxComponentAppService iWxComponentAppService;

    @GetMapping("/authorized")
    public ModelAndView authorized() throws Exception {
        //"wxd709ce21db85e926"
        String redirectUrl = iWxComponentService.createPreAuthUrl("open.easy.echosite.cn", "1", "wxd709ce21db85e926");
        //String redirectUrl = iWxComponentService.getMobilePreAuthUrl("open.easy.echosite.cn", "1", null);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("authUrl", redirectUrl);
        modelAndView.setViewName("/authroized");
        return modelAndView;
    }

    @GetMapping("/getAuthorized")
    public void getAuthorized() throws Exception {
        iWxComponentService.getAuthorized("wxd709ce21db85e926");
    }
}
