package org.igetwell.web;

import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.uitls.CheckSignature;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.wechat.sdk.service.IWxAppService;
import org.igetwell.wechat.sdk.service.IWxOpenComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/WeChat")
@Slf4j
public class WeChatController extends BaseController {

    @Autowired
    private IWxOpenComponentService wxOpenComponentService;

    @Autowired
    private IWxAppService iWxAppService;



    /**
     * 校验微信服务器回调
     * @param signature
     * @param echostr
     * @param timestamp
     * @param nonce
     */
    @GetMapping("/callback")
    public void callback(String signature, String echostr, String timestamp, String nonce) {
        if (StringUtils.isEmpty(signature.trim()) || StringUtils.isEmpty(echostr.trim()) || StringUtils.isEmpty(timestamp.trim()) || StringUtils.isEmpty(nonce.trim())){
            return;
        }
        boolean bool = CheckSignature.checkSignature(signature, timestamp, nonce);
        if (bool){
            render(echostr);
        }
    }

    @PostMapping("/getAccessToken")
    public void getAccessToken(){
        iWxAppService.getAccessToken("wx2ffe2417c6b42f78", "2c3b915dd8360206d7a0688c0b59a234");
    }

    @PostMapping("/uploadLogo")
    public ResponseEntity uploadLogo(MultipartFile file){
        return iWxAppService.uploadLogo(file);
    }


    /**
     * 微信开放平台处理授权事件推送Ticket
     * @param request
     * @param nonce
     * @param timestamp
     * @param msg_signature
     */
    @RequestMapping(value = "/event/authorize")
    public void acceptAuthorizeEvent(HttpServletRequest request, String nonce, String timestamp, String msg_signature){
        try {
            wxOpenComponentService.processAuthorizeEvent(request, nonce, timestamp, msg_signature);
            render("success");
        } catch (Exception e) {
            log.error("微信开放平台处理授权事件推送Ticket失败.", e);
        }
    }

    /**
     * 全网发布接入检测消息
     */
    @RequestMapping(value = "/{appid}/checkAllNetWork")
    public void checkWechatAllNetwork(@PathVariable("appid") String appid, String nonce, String timestamp, String msg_signature){
        try {
            wxOpenComponentService.checkWechatAllNetwork(request.get(), response.get(), nonce, timestamp, msg_signature);
        } catch (Exception e) {
            log.error("全网发布接入检测消息失败.", e);
        }
    }

    @GetMapping("/getComponentAccessToken")
    public void getComponentAccessToken() throws Exception{
        wxOpenComponentService.getComponentAccessToken();
    }

    @GetMapping("/refreshToken")
    public void refreshToken() throws Exception{
        wxOpenComponentService.refreshToken("wx9a6700dcfc8867aa", "wx570bc396a51b8ff8", "refreshtoken@@@l0_OcQQWx71St66F2crOzAWW6w77Ha6uKG3Ic7ZuVuM");
    }

    @GetMapping("/getAuthorized")
    public void getAuthorized() throws Exception{
        wxOpenComponentService.getAuthorized("wx9a6700dcfc8867aa", "wx570bc396a51b8ff8");
    }

    @GetMapping("/preAuth")
    public ModelAndView preAuth(String redirectUri){
        try {
            String redirectUrl = wxOpenComponentService.getPreAuthUrl(redirectUri, "3", null);
            //response.get().sendRedirect(redirectUrl);//不能直接返回很坑的
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("authUrl", redirectUrl);
            modelAndView.setViewName("/index");
            return modelAndView;
        } catch (Exception e) {
            log.error("获取预授权失败.", e);
        }
        return null;
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "abc";
    }
}
