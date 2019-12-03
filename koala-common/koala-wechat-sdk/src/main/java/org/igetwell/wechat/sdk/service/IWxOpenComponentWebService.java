package org.igetwell.wechat.sdk.service;

import org.igetwell.wechat.sdk.WechatUser;

public interface IWxOpenComponentWebService {


    /**
     * 生成二维码扫码登录
     * @return
     */
    String webScanLogin(String redirectUri);

    /**
     * 通过code获取accessToken
     * @param code
     * @return
     */
    void getAccessToken(String code);

    /**
     * 刷新accessToken有效期
     * @param refreshToken
     */
    void refreshToken(String refreshToken);


    /**
     * 通过accessToken和openid获取用户的基础信息，包括头像、昵称、性别、地区
     * @param accessToken
     * @param openId
     */
    WechatUser getUser(String accessToken, String openId);

}
