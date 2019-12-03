package org.igetwell.wechat.sdk.service.impl;

import org.apache.commons.lang.StringUtils;
import org.igetwell.common.constans.cache.RedisKey;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClientUtils;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.wechat.sdk.ComponentWebAccessToken;
import org.igetwell.wechat.sdk.WechatUser;
import org.igetwell.wechat.sdk.service.IWxOpenComponentWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WxOpenComponentWebService implements IWxOpenComponentWebService {

    String connect = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";

    String WEB_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    String WEB_REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";

    String WEB_USER_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 应用唯一标识
     */
    @Value("${wechat.appId}")
    private String appId;

    /**
     * 应用密钥
     */
    @Value("${wechat.secret}")
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
        WEB_ACCESS_TOKEN_URL = String.format(WEB_ACCESS_TOKEN_URL, appId, secret, code);
        String response = HttpClientUtils.getInstance().sendHttpPost(WEB_ACCESS_TOKEN_URL);
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

        WEB_REFRESH_TOKEN_URL = String.format(WEB_REFRESH_TOKEN_URL, appId, refreshToken);
        String response = HttpClientUtils.getInstance().sendHttpPost(WEB_REFRESH_TOKEN_URL);
        ComponentWebAccessToken accessToken = GsonUtils.fromJson(response, ComponentWebAccessToken.class);
    }

    /**
     * 通过accessToken和openid获取用户的基础信息，包括头像、昵称、性别、地区
     * @param accessToken
     * @param openId
     */
    public WechatUser getUser(String accessToken, String openId) {
        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(openId)) {
            return null;
        }
        WechatUser wechatUser = redisUtils.get(openId);
        if (wechatUser == null) {
            WEB_USER_URL = String.format(WEB_USER_URL, accessToken, openId);
            String response = HttpClientUtils.getInstance().sendHttpPost(WEB_USER_URL);
            wechatUser = GsonUtils.fromJson(response, WechatUser.class);
            redisUtils.set(String.format(RedisKey.WECHAT_WEB_USER, openId), wechatUser, 2592000);
        }
        return wechatUser;
    }

}
