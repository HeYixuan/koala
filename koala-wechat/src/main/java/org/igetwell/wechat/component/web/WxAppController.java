package org.igetwell.wechat.component.web;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/app")
public class WxAppController extends BaseController {

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
        render(echostr);
    }
}
