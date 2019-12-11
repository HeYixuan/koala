package org.igetwell.wechat.sdk.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClients;
import org.igetwell.common.uitls.ParamMap;
import org.igetwell.wechat.sdk.bean.component.*;
import org.springframework.util.StringUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 开放平台
 */
public class ComponentAPI extends API {


    /**
     * 第三方开放平台获取令牌
     * @param componentAppId
     * @param componentAppSecret
     * @param componentVerifyTicket
     * @return
     */
    public static ComponentAccessToken oauthToken(String componentAppId, String componentAppSecret, String componentVerifyTicket){
        Map<String, String> param = ParamMap.create("component_appid", componentAppId)
                .put("component_appsecret", componentAppSecret)
                .put("component_verify_ticket", componentVerifyTicket).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/component/api_component_token")
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, ComponentAccessToken.class);
    }

    /**
     * 第三方开放平台获取预授权码
     * @param componentAccessToken
     * @param componentAppId
     * @return
     */
    public static PreAuthAuthorization preAuthCode(String componentAccessToken, String componentAppId){
        Map<String, String> param = ParamMap.create("component_appid", componentAppId).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/component/api_create_preauthcode")
                .addParameter(COMPONENT_ACCESS_TOKEN, componentAccessToken(componentAccessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, PreAuthAuthorization.class);
    }

    /**
     * 使用授权码获取授权信息
     * @param componentAccessToken
     * @param componentAppId
     * @param authorizationCode
     * @return
     */
    public static Authorization authorize(String componentAccessToken, String componentAppId, String authorizationCode){
        Map<String, String> param = ParamMap.create("component_appid", componentAppId)
                .put("authorization_code", authorizationCode).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/component/api_query_auth")
                .addParameter(COMPONENT_ACCESS_TOKEN, componentAccessToken(componentAccessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, Authorization.class);
    }

    /**
     * 获取/刷新接口调用令牌
     * @param componentAccessToken
     * @param componentAppId
     * @param appId
     * @param refreshToken
     * @return
     */
    public static ComponentRefreshAccessToken refreshToken(String componentAccessToken, String componentAppId, String appId, String refreshToken){
        Map<String, String> param = ParamMap.create("component_appid", componentAppId)
                .put("authorizer_appid", appId)
                .put("authorizer_refresh_token", refreshToken).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/component/api_authorizer_token")
                .addParameter(COMPONENT_ACCESS_TOKEN, componentAccessToken(componentAccessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, ComponentRefreshAccessToken.class);
    }

    /**
     * 创建开放平台帐号并绑定公众号/小程序
     * @param accessToken
     * @param appId
     */
    public static void create(String accessToken, String appId){
        Map<String, String> param = ParamMap.create("appid", appId).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/open/create")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        HttpClients.execute(httpUriRequest);
    }

    /**
     * 将公众号/小程序绑定到开放平台帐号下
     * @param accessToken
     * @param componentAppId
     * @param appId
     */
    public static void bind(String accessToken, String componentAppId, String appId){
        Map<String, String> param = ParamMap.create("appid", appId).put("open_appid", componentAppId).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/open/bind")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        HttpClients.execute(httpUriRequest);
    }

    /**
     * 将公众号/小程序从开放平台帐号下解绑
     * @param accessToken
     * @param componentAppId
     * @param appId
     */
    public static void unbind(String accessToken, String componentAppId, String appId){
        Map<String, String> param = ParamMap.create("appid", appId).put("open_appid", componentAppId).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/open/unbind")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        HttpClients.execute(httpUriRequest);
    }

    /**
     * 获取公众号/小程序所绑定的开放平台帐号
     * @param accessToken
     * @param appId
     */
    public static void get(String accessToken, String appId){
        Map<String, String> param = ParamMap.create("appid", appId).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/open/get")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        HttpClients.execute(httpUriRequest);
    }


    /**
     * 第三方开放平台代公众号授权获取令牌
     * @param componentAppId
     * @param componentAccessToken
     * @param appId
     * @param code
     * @return
     */
    public static ComponentAppAccessToken oauthAppToken(String componentAccessToken, String componentAppId, String appId, String code){
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setUri(BASE_URI + "/sns/oauth2/component/access_token")
                .addParameter("component_appid", componentAppId)
                .addParameter(COMPONENT_ACCESS_TOKEN, componentAccessToken(componentAccessToken))
                .addParameter("grant_type", "authorization_code")
                .addParameter("code", code)
                .addParameter("appid", appId)
                .build();
        return HttpClients.execute(httpUriRequest, ComponentAppAccessToken.class);
    }

    /**
     * 第三方开放平台代公众号授权刷新令牌
     * @param componentAppId
     * @param componentAccessToken
     * @param appId
     * @param refreshToken
     * @return
     */
    public static ComponentAppAccessToken refreshAppToken(String componentAccessToken, String componentAppId, String appId, String refreshToken){
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setUri(BASE_URI + "/sns/oauth2/component/refresh_token")
                .addParameter("component_appid", componentAppId)
                .addParameter(COMPONENT_ACCESS_TOKEN, componentAccessToken(componentAccessToken))
                .addParameter("grant_type", "refresh_token")
                .addParameter("refresh_token", refreshToken)
                .addParameter("appid", appId)
                .build();
        return HttpClients.execute(httpUriRequest, ComponentAppAccessToken.class);
    }


    /**
     * 将小程序和公众号授权给第三方平台
     * @param redirectUri
     * @param authType authType 要授权的帐号类型：1则商户点击链接后，手机端仅展示公众号、2表示仅展示小程序，3表示公众号和小程序都展示。
     *                 如果未指定，则默认小程序和公众号都展示。第三方平台开发者可以使用本字段来控制授权的帐号类型。
     * @param bizAppid 指定授权唯一的小程序或公众号
     * @param isMobile 是否手机模式
     *  注：auth_type、biz_appid两个字段互斥。
     * @return
     * @throws Exception
     */
    public static String createPreAuthUrl(String componentAppId, String bizAppid, String preAuthCode, String authType, boolean isMobile, String redirectUri) {
        if (isMobile) {
            return createMobilePreAuthUrl(componentAppId, bizAppid, preAuthCode, authType, redirectUri);
        } else {
           return createPreAuthUrl(componentAppId, bizAppid, preAuthCode, authType, redirectUri);
        }
    }

    /**
     * 小程序或者公众号授权给第三方平台授权连接
     * @param componentAppId
     * @param bizAppid
     * @param preAuthCode
     * @param authType
     * @param redirectUri
     * @return
     * @throws Exception
     */
    private static String createPreAuthUrl(String componentAppId, String bizAppid, String preAuthCode, String authType, String redirectUri) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(MP_URI)
                    .append("/cgi-bin/componentloginpage?")
                    .append("component_appid=").append(componentAppId)
                    .append("&pre_auth_code=").append(preAuthCode)
                    .append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
            if (StringUtils.isEmpty(authType.trim())) {
                stringBuilder.append("&biz_appid=").append(bizAppid);
            }
            if (StringUtils.isEmpty(bizAppid.trim())) {
                stringBuilder.append("&auth_type=").append(authType);
            }
            return stringBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            stringBuilder.setLength(0);
        }
    }

    /**
     * 小程序或者公众号授权给第三方平台
     * 手机端打开授权链接
     * @param componentAppId
     * @param bizAppid
     * @param preAuthCode
     * @param authType
     * @param redirectUri
     * @return
     */
    private static String createMobilePreAuthUrl(String componentAppId, String bizAppid, String preAuthCode, String authType, String redirectUri) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(MP_URI)
                    .append("/safe/bindcomponent?action=bindcomponent&no_scan=1")
                    .append("&component_appid=").append(componentAppId)
                    .append("&pre_auth_code=").append(preAuthCode)
                    .append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
            if (StringUtils.isEmpty(authType.trim())) {
                stringBuilder.append("&biz_appid=").append(bizAppid);
            }
            if (StringUtils.isEmpty(bizAppid.trim())) {
                stringBuilder.append("&auth_type=").append(authType);
            }
            stringBuilder.append("#wechat_redirect");
            return stringBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            stringBuilder.setLength(0);
        }
    }

}
