package org.igetwell.wechat.sdk.service;

import org.igetwell.wechat.sdk.ComponentAccessToken;

public interface IWxOpenConfigStorage {

    /**
     * 更新第三方平台access_token
     * @param componentAccessToken
     */
    void updateComponentAccessToken(ComponentAccessToken componentAccessToken);

    /**
     * 更新第三方平台access_token
     *
     * @param componentAccessToken 新的accessToken值
     * @param expiresInSeconds     过期时间，以秒为单位
     */
    void updateComponentAccessToken(String componentAccessToken, long expiresInSeconds);
}
