package org.igetwell.wechat.sdk.service.impl;

import org.apache.commons.lang.StringUtils;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClientUtils;
import org.igetwell.wechat.sdk.ComponentWebAccessToken;
import org.igetwell.wechat.sdk.WechatUser;
import org.igetwell.wechat.sdk.service.IWxOpenComponentWebService;

public class WxOpenComponentWebService implements IWxOpenComponentWebService {

    String connect = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";

    String WEB_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    String WEB_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";

    String WEB_USER = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

    /**
     * 应用唯一标识
     */
    private String appId;

    /**
     * 应用密钥
     */
    private String secret;


    /**
     * 生成二维码扫码登录
     * @return
     */
    public String webScanLogin(String redirectUri) {
        long state = System.nanoTime();
        return String.format(connect, appId, redirectUri, "snsapi_login", state);
    }

    /**
     * 通过code获取accessToken
     * @param code
     * @return
     */
    public void getAccessToken(String code) {
        if (StringUtils.isBlank(code)) {
            return;
        }
        WEB_ACCESS_TOKEN = String.format(WEB_ACCESS_TOKEN, appId, secret, code);
        String response = HttpClientUtils.getInstance().sendHttpPost(WEB_ACCESS_TOKEN);
        ComponentWebAccessToken accessToken = GsonUtils.fromJson(response, ComponentWebAccessToken.class);
    }

    /**
     * 刷新accessToken有效期
     * @param refreshToken
     */
    public void refreshToken(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            return;
        }
        WEB_REFRESH_TOKEN = String.format(WEB_REFRESH_TOKEN, appId, refreshToken);
        String response = HttpClientUtils.getInstance().sendHttpPost(WEB_REFRESH_TOKEN);
        ComponentWebAccessToken accessToken = GsonUtils.fromJson(response, ComponentWebAccessToken.class);
    }

    /**
     * 通过accessToken和openid获取用户的基础信息，包括头像、昵称、性别、地区
     * @param accessToken
     * @param openId
     */
    public void getUser(String accessToken, String openId) {
        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(openId)) {
            return;
        }
        WEB_USER = String.format(WEB_USER, accessToken, openId);
        String response = HttpClientUtils.getInstance().sendHttpPost(WEB_USER);
        WechatUser wechatUser = GsonUtils.fromJson(response, WechatUser.class);
    }

}
