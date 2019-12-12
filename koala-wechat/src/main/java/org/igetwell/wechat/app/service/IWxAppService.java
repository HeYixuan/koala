package org.igetwell.wechat.app.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IWxAppService {

    /**
     * 获取微信公众号令牌
     * @return
     * @throws Exception
     */
    String getAccessToken() throws Exception;

    /**
     * 是否强制刷新微信公众号令牌
     * @param forceRefresh
     * @throws Exception
     */
    void getAccessToken(boolean forceRefresh) throws Exception;

    ResponseEntity uploadLogo(MultipartFile file) throws Exception;

}
