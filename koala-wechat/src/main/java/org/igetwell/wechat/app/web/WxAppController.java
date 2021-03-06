package org.igetwell.wechat.app.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.wechat.app.service.IWxAppService;
import org.igetwell.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/wx/app")
public class WxAppController extends BaseController {

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
//        boolean bool = CheckSignature.checkSignature(signature, timestamp, nonce);
//        if (StringUtils.isEmpty(signature) || StringUtils.isEmpty(echostr) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(nonce)){
//            return;
//        }
        render(echostr);
    }


    @PostMapping("/getAccessToken")
    public void getAccessToken() throws Exception {
        iWxAppService.getAccessToken(true);
    }

    @PostMapping("/uploadLogo")
    public ResponseEntity uploadLogo(MultipartFile file) throws Exception {
        return iWxAppService.uploadLogo(file);
    }

}
