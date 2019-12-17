package org.igetwell.wechat.component.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 第三方平台全网检测和授权业务
 */
public interface IWxComponentService {

    /**
     * 获取第三方平台令牌
     * @return
     * @throws Exception
     */
    String getComponentAccessToken() throws Exception;

    /**
     * 是否强制获取第三方平台令牌
     * @param forceRefresh
     * @return
     * @throws Exception
     */
    void getComponentAccessToken(boolean forceRefresh) throws Exception;

    /**
     * 微信开放平台处理授权事件的推送
     * @param request
     * @param nonce
     * @param timestamp
     * @param msgSignature
     * @throws Exception
     */
    void processAuthorizeEvent(HttpServletRequest request, String nonce, String timestamp, String msgSignature) throws Exception;

    /**
     * 全网发布接入检测消息
     * @param request
     * @param response
     * @param nonce
     * @param timestamp
     * @param msgSignature
     * @throws Exception
     */
    void checkNetwork(HttpServletRequest request, HttpServletResponse response, String nonce, String timestamp, String msgSignature) throws Exception;
    /**
     * 开放平台获取预授权码
     * @return
     * @throws Exception
     */
    String getPreAuthCode() throws Exception;

    /**
     * 授权后回调URI,获取授权码
     */
    void setAuthCode(String authorizationCode, Long expiresIn) throws Exception;

    /**
     * 从缓存中获取授权后回调URI的授权码
     */
    String getAuthCode() throws Exception;

    /**
     * 使用授权码换取授权方令牌
     * @param authorizationCode  授权code
     */
    String authorize(String authorizationCode) throws Exception;


    /**
     * 获取/刷新授权方接口调用令牌
     * @param appId 授权方appId
     * @param refreshToken
     * @return
     */
    void refreshToken(String appId, String refreshToken) throws Exception;

    /**
     * 从缓存中获取授权方令牌
     * @param appId
     * @return
     */
    String getAccessToken(String appId) throws Exception;

    /**
     * 获取授权方的帐号基本信息
     * @param appId
     */
    void getAuthorized(String appId) throws Exception;

    /**
     * 将小程序和公众号授权给第三方平台 获取预授权链接（网页端预授权）
     * 注：auth_type、biz_appid两个字段互斥。
     */
    String createPreAuthUrl(String redirectURI) throws Exception;

    /**
     * 将小程序和公众号授权给第三方平台 获取预授权链接（网页端预授权）
     * @param redirectUri
     * @param authType 要授权的帐号类型：1则商户点击链接后，手机端仅展示公众号、2表示仅展示小程序，3表示公众号和小程序都展示。
     *                 如果未指定，则默认小程序和公众号都展示。第三方平台开发者可以使用本字段来控制授权的帐号类型。
     * @param bizAppid 指定授权唯一的小程序或公众号
     * 注：auth_type、biz_appid两个字段互斥。
     * @return
     */
    String createPreAuthUrl(String redirectUri, String authType, String bizAppid) throws Exception;

    /**
     * 将小程序和公众号授权给第三方平台 获取预授权链接（手机端预授权）
     * 注：auth_type、biz_appid两个字段互斥。
     */
    String createMobilePreAuthUrl(String redirectUri) throws Exception;


    /**
     * 将小程序和公众号授权给第三方平台 获取预授权链接（手机端预授权）
     * @param redirectUri
     * @param authType 要授权的帐号类型：1则商户点击链接后，手机端仅展示公众号、2表示仅展示小程序，3表示公众号和小程序都展示。
     *                 如果未指定，则默认小程序和公众号都展示。第三方平台开发者可以使用本字段来控制授权的帐号类型。
     * @param bizAppid 指定授权唯一的小程序或公众号
     * 注：auth_type、biz_appid两个字段互斥。
     * @return
     */
    String createMobilePreAuthUrl(String redirectUri, String authType, String bizAppid) throws Exception;

    /**
     * 将公众号/小程序绑定到开放平台帐号下
     */
    boolean bind(String appId) throws Exception;

    /**
     * 将公众号/小程序从开放平台帐号下解绑
     */
    boolean unbind(String appId) throws Exception;
}
