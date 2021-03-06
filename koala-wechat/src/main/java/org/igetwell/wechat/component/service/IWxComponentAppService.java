package org.igetwell.wechat.component.service;

import org.igetwell.wechat.sdk.bean.component.ComponentAppAccessToken;
import org.igetwell.wechat.sdk.bean.component.WechatUser;
/**
 * 第三方平台代公众号发起网页授权业务(目前无法测试成功)
 */
public interface IWxComponentAppService {

    /**
     * 第三方开放平台代公众号发起网页授权获取授权码
     * @param redirectUri
     * @return
     * @throws Exception
     */
    String authorized(String redirectUri) throws Exception;

    /**
     * 第三方开放平台代公众号发起网页授权通过授权码换取令牌
     * @param appId
     * @param authorizedCode
     * @param state
     * @return
     * @throws Exception
     */
    ComponentAppAccessToken getAccessToken(String appId, String authorizedCode, Long state) throws Exception;

    /**
     * 从缓存中获取令牌
     * @param appId
     * @return
     */
    ComponentAppAccessToken getAccessToken(String appId, String openId) throws Exception;

    /**
     * 第三方开放平台代公众号发起网页授权刷新令牌
     * @param appId
     * @param refreshToken
     * @throws Exception
     */
    void refreshToken(String appId, String refreshToken) throws Exception;

    /**
     * 第三方开放平台代公众号发起网页授权获取用户基本信息
     * @param openId
     * @return
     * @throws Exception
     */
    WechatUser getWxUser(String appId, String openId) throws Exception;
}
