package org.igetwell.wechat.sdk.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IWxAppService {

    /**
     * 获取accessToken
     * @param appId
     * @param secret
     */
    void getAccessToken(String appId, String secret);

    ResponseEntity uploadLogo(MultipartFile file);

}
