package org.igetwell.wechat.component.web;

import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.wechat.BaseController;
import org.igetwell.wechat.component.service.IWxComponentAppService;
import org.igetwell.wechat.sdk.bean.component.WechatUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 第三方开放平台代公众号发起网页授权
 */
@RestController
@RequestMapping("/wx/component/app")
public class WxComponentAppController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(WxComponentController.class);

    @Autowired
    private IWxComponentAppService iWxComponentAppService;

    @GetMapping("/authorized")
    public void authorized() throws Exception {
        String redirectUrl = iWxComponentAppService.authorized("https://open.easy.echosite.cn/wx/component/app/main");
        response.get().sendRedirect(redirectUrl);
    }

    /**
     * 第三方开放平台代公众号发起网页授权通过授权码换取令牌
     * @param appId
     * @param code
     * @param state
     * @return
     * @throws Exception
     */
//    @GetMapping("/main")
//    public ModelAndView getAccessToken(@RequestParam("appid") String appId, @RequestParam("code") String code, @RequestParam("state") Long state) throws Exception {
//        iWxComponentAppService.getAccessToken(appId, code, state);
//        WechatUser wechatUser = iWxComponentAppService.getWxUser(appId, "oNDnvs_jSr-Ugb5mhO_J-3cbyCOo");
//        System.err.println(GsonUtils.toJson(wechatUser));
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("/index");
//        return modelAndView;
//    }
    @GetMapping("/main")
    public ResponseEntity getAccessToken(@RequestParam("appid") String appId, @RequestParam("code") String code, @RequestParam("state") Long state) throws Exception {
        iWxComponentAppService.getAccessToken(appId, code, state);
        WechatUser wechatUser = iWxComponentAppService.getWxUser(appId, "oNDnvs_jSr-Ugb5mhO_J-3cbyCOo");
        System.err.println(GsonUtils.toJson(wechatUser));
        return ResponseEntity.ok();
    }
}
