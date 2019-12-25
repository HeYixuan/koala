package org.igetwell.wechat.component.service.impl;

import org.igetwell.common.constans.cache.RedisKey;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.wechat.component.service.IWxComponentAppService;
import org.igetwell.wechat.component.service.IWxComponentService;
import org.igetwell.wechat.sdk.bean.component.WechatUser;
import org.igetwell.wechat.sdk.api.ComponentAPI;
import org.igetwell.wechat.sdk.api.WechatUserAPI;
import org.igetwell.wechat.sdk.bean.component.ComponentAppAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 第三方平台代公众号发起网页授权业务
 */
@Service
public class WxComponentAppService implements IWxComponentAppService {

    private Logger logger = LoggerFactory.getLogger(WxComponentAppService.class);

    static final String AUTHORIZED_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s&component_appid=%s#wechat_redirect";

    @Value("${componentAppId}")
    private String componentAppId;

    @Value("${wechat.appId}")
    private String appId;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IWxComponentService iWxComponentService;

    /**
     * 第三方开放平台代公众号发起网页授权获取授权码
     * @param redirectUri
     * @return
     * @throws Exception
     */
    public String authorized(String redirectUri) throws Exception {
        logger.info("[微信开放平台]-代公众号发起网页授权获取授权码开始, appId={}.", appId);
        if (StringUtils.isEmpty(componentAppId) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(redirectUri)) {
            logger.error("[微信开放平台]-代公众号发起网页授权获取授权码失败.请求参数为空.");
            throw new Exception("[微信开放平台]-代公众号发起网页授权获取授权码失败.请求参数为空.");
        }
        Long state = System.nanoTime();
        redisUtils.set(String.format(RedisKey.COMPONENT_APP_STATE, appId), state, 120);
        redirectUri = ComponentAPI.authorized(componentAppId, appId, "snsapi_userinfo", state, redirectUri);
        logger.info("[微信开放平台]-代公众号发起网页授权获取授权码结束.");
        return redirectUri;
    }

    /**
     * 第三方开放平台代公众号发起网页授权通过授权码换取令牌
     * @param appId
     * @param authorizedCode
     * @param state
     * @return
     * @throws Exception
     */
    public ComponentAppAccessToken getAccessToken(String appId, String authorizedCode, Long state) throws Exception {
        logger.info("[微信开放平台]-代公众号发起网页授权通过授权码换取令牌开始, appId={}.", appId);
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(authorizedCode) || StringUtils.isEmpty(state)) {
            logger.error("[微信开放平台]-代公众号发起网页授权通过授权码换取令牌失败.请求参数为空.");
            throw new Exception("[微信开放平台]-代公众号发起网页授权通过授权码换取令牌失败.请求参数为空.");
        }
        Long stateCache = redisUtils.get(String.format(RedisKey.COMPONENT_APP_STATE, appId));
        if (stateCache == null && stateCache != state) {
            logger.info("[微信开放平台]-代公众号发起网页授权通过授权码换取令牌失败.请求失效.");
            throw new Exception("[微信开放平台]-代公众号发起网页授权通过授权码换取令牌失败.请求失效.");
        }
        ComponentAppAccessToken accessToken = ComponentAPI.oauthAppToken(iWxComponentService.getComponentAccessToken(), componentAppId, appId, authorizedCode);
        if (accessToken == null || StringUtils.isEmpty(accessToken.getAccessToken()) || StringUtils.isEmpty(accessToken.getRefreshToken())) {
            logger.error("[微信开放平台]-代公众号发起网页授权通过授权码换取令牌失败.");
            throw new Exception("[微信开放平台]-代公众号发起网页授权通过授权码换取令牌失败.");
        }
        redisUtils.del(String.format(RedisKey.COMPONENT_APP_STATE, appId));
        redisUtils.set(String.format(RedisKey.COMPONENT_APP_ACCESS_TOKEN, appId, accessToken.getOpenid()), accessToken);
        logger.info("[微信开放平台]-代公众号发起网页授权通过授权码换取令牌结束.");
        return accessToken;
    }

    /**
     * 从缓存中获取令牌
     * @param appId
     * @return
     */
    public ComponentAppAccessToken getAccessToken(String appId, String openId) throws Exception {
        logger.info("[微信开放平台]-代公众号发起网页授权从缓存中获取令牌开始.");
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(openId)) {
            logger.error("[微信开放平台]-代公众号发起网页授权从缓存中获取令牌失败. 请求参数appId为空.");
            throw new Exception("[微信开放平台]-代公众号发起网页授权从缓存中获取令牌失败. 请求参数appId为空.");
        }
        ComponentAppAccessToken accessToken = redisUtils.get(String.format(RedisKey.COMPONENT_APP_ACCESS_TOKEN, appId, openId));
        if (accessToken == null || StringUtils.isEmpty(accessToken.getAccessToken()) || StringUtils.isEmpty(accessToken.getRefreshToken())) {
            logger.error("[微信开放平台]-代公众号发起网页授权从缓存中获取令牌失败.");
            throw new Exception("[微信开放平台]-代公众号发起网页授权从缓存中获取令牌失败.");
        }
        logger.info("[微信开放平台]-代公众号发起网页授权从缓存中获取令牌结束.");
        return accessToken;
    }

    /**
     * 第三方开放平台代公众号发起网页授权刷新令牌
     * @param appId
     * @param refreshToken
     * @throws Exception
     */
    public void refreshToken(String appId, String refreshToken) throws Exception {
        logger.info("[微信开放平台]-代公众号授权刷新令牌开始，appId={}.", appId);
        if (StringUtils.isEmpty(componentAppId) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(refreshToken)) {
            logger.error("[微信开放平台]-代公众号发起网页授权刷新令牌失败.请求参数为空.");
            throw new Exception("[微信开放平台]-代公众号发起网页授权刷新令牌失败.请求参数为空.");
        }
        ComponentAppAccessToken accessToken = ComponentAPI.refreshAppToken(iWxComponentService.getComponentAccessToken(), componentAppId, appId, refreshToken);
        if (accessToken == null || StringUtils.isEmpty(accessToken.getAccessToken()) || StringUtils.isEmpty(accessToken.getRefreshToken())) {
            logger.info("[微信开放平台]-代公众号发起网页授权刷新令牌失败.");
            throw new Exception("[微信开放平台]-代公众号发起网页授权刷新令牌失败.");
        }
        redisUtils.set(String.format(RedisKey.COMPONENT_APP_ACCESS_TOKEN, appId, accessToken.getOpenid()), accessToken);
        logger.info("[微信开放平台]-代公众号发起网页授权刷新令牌结束.");
    }

    /**
     * 第三方开放平台代公众号发起网页授权获取用户基本信息
     * @param openId
     * @return
     * @throws Exception
     */
    public WechatUser getWxUser(String appId, String openId) throws Exception {
        logger.info("[微信开放平台]-代公众号发起网页授权获取用户基本信息开始，appId={}.", appId);
        WechatUser wxUser = WechatUserAPI.getWxUser(getAccessToken(appId, openId).getAccessToken(), openId);
        if (wxUser == null || StringUtils.isEmpty(wxUser.getOpenid()) && StringUtils.isEmpty(wxUser.getUnionid())){
            logger.error("[微信开放平台]-代公众号发起网页授权通过令牌获取微信用户基本信息失败.");
            throw new Exception("[微信开放平台]-代公众号发起网页授权通过令牌获取微信用户基本信息失败.");
        }
        redisUtils.set(openId, wxUser);
        logger.info("[微信开放平台]-代公众号发起网页授权获取用户基本信息结束.");
        return wxUser;
    }
}
