package org.igetwell.wechat.component.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.BaseController;
import org.igetwell.wechat.component.service.IWxComponentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    /**
     * 创建预授权链接进行授权
     */
    @GetMapping("/authorized")
    public ModelAndView authorized() throws Exception {
        String redirectUrl = iWxComponentService.createPreAuthUrl("https://open.easy.echosite.cn/wx/component/main", "1", "wx2681cc8716638f35");
        System.err.println(redirectUrl);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("redirectUrl", redirectUrl);
        modelAndView.setViewName("/authorized");
        return modelAndView;
    }

    /**
     * 授权后回调URI,获取授权码
     */
//    @GetMapping("/main")
//    public ResponseEntity main(@RequestParam("auth_code") String auth_code, @RequestParam("expires_in") Long expires_in) throws Exception {
//        iWxComponentService.setAuthCode(auth_code, expires_in);
//        String authCode = iWxComponentService.getAuthCode();
//        iWxComponentService.authorize(authCode);
//        return ResponseEntity.ok();
//    }
    @GetMapping("/main")
    public ModelAndView main(@RequestParam("auth_code") String auth_code, @RequestParam("expires_in") Long expires_in) throws Exception {
        iWxComponentService.setAuthCode(auth_code, expires_in);
        String authCode = iWxComponentService.getAuthCode();
        iWxComponentService.authorize(authCode);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("authCode", authCode);
        modelAndView.setViewName("/index");
        return modelAndView;
    }

    /**
     * 获取授权方的帐号基本信息
     */
    @GetMapping("/getAuthorized")
    public void getAuthorized() throws Exception {
        iWxComponentService.getAuthorized("wx2681cc8716638f35");
    }

    @PostMapping("/bind")
    public ResponseEntity bind() throws Exception {
        boolean bool = iWxComponentService.bind("wx2681cc8716638f35");
        return ResponseEntity.ok(bool);
    }

    @PostMapping("/unbind")
    public ResponseEntity unbind() throws Exception {
        boolean bool = iWxComponentService.unbind("wx2681cc8716638f35");
        return ResponseEntity.ok(bool);
    }
}
