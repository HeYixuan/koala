package org.igetwell.wechat.component.service;

import org.igetwell.wechat.sdk.WechatUser;

/**
 * 第三方平台代公众号授权业务
 */
public interface IWxComponentAppService {

    /**
     * 第三方开放平台代公众号授权获取授权码
     * @param redirectUri
     * @return
     * @throws Exception
     */
    String getAuthorized(String redirectUri) throws Exception;

    /**
     * 第三方开放平台代公众号通过授权码换取令牌
     * @param appId
     * @param authorizedCode
     * @param state
     * @return
     * @throws Exception
     */
    void getAccessToken(String appId, String authorizedCode, Long state) throws Exception;

    /**
     * 第三方开放平台代公众号刷新令牌
     * @param appId
     * @param refreshToken
     * @throws Exception
     */
    void refreshToken(String appId, String refreshToken) throws Exception;

    /**
     * 第三方开放平台代公众号授权获取用户基本信息
     * @param accessToken
     * @param openId
     * @return
     * @throws Exception
     */
    WechatUser getWxUser(String accessToken, String openId) throws Exception;
}
