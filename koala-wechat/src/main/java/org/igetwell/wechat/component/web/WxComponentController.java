package org.igetwell.wechat.component.web;

import org.igetwell.wechat.BaseController;
import org.igetwell.wechat.component.service.IWxComponentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 第三方开放平台全网发布预授权
 */
@RestController
@RequestMapping("/wx/component")
public class WxComponentController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(WxComponentController.class);

    @Autowired
    private IWxComponentService iWxComponentService;


    /**
     * 微信开放平台处理授权事件推送Ticket
     * @param nonce
     * @param timestamp
     * @param msg_signature
     */
    @RequestMapping(value = "/event/authorize")
    public void acceptAuthorizeEvent(String nonce, String timestamp, String msg_signature){
        try {
            iWxComponentService.processAuthorizeEvent(request.get(), nonce, timestamp, msg_signature);
            render("success");
        } catch (Exception e) {
            logger.error("[微信开放平台]-处理授权事件推送Ticket失败.", e);
        }
    }

    /**
     * 全网发布接入检测消息
     */
    @RequestMapping(value = "/{appid}/checkNetwork")
    public void checkNetwork(@PathVariable("appid") String appid, String nonce, String timestamp, String msg_signature){
        try {
            iWxComponentService.checkNetwork(request.get(), response.get(), nonce, timestamp, msg_signature);
        } catch (Exception e) {
            logger.error("[微信开放平台]-全网发布接入检测消息失败.", e);
        }
    }

    @PostMapping("/authorized")
    public ModelAndView authorized() throws Exception {
        //"wxd709ce21db85e926"
        String redirectUrl = iWxComponentService.createPreAuthUrl("open.easy.echosite.cn", "1", "wxd709ce21db85e926");
        //String redirectUrl = iWxComponentService.getMobilePreAuthUrl("open.easy.echosite.cn", "1", null);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("redirectUrl", redirectUrl);
        modelAndView.setViewName("/authorized");
        return modelAndView;
    }

    /**
     * 获取授权方的帐号基本信息
     */
    @PostMapping("/getAuthorized")
    public void getAuthorized() throws Exception {
        iWxComponentService.getAuthorized("wxd709ce21db85e926");
    }
}
