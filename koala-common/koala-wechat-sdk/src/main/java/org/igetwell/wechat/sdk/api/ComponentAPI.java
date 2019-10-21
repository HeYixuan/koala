package org.igetwell.wechat.sdk.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClientUtils;
import org.igetwell.common.uitls.ParamMap;
import org.igetwell.wechat.sdk.ComponentAccessToken;
import org.igetwell.wechat.sdk.ComponentAuthorization;
import org.igetwell.wechat.sdk.ComponentRefreshAccessToken;
import org.igetwell.wechat.sdk.PreAuthAuthorization;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * 开放平台
 */
public class ComponentAPI extends API {


    /**
     * 获取令牌
     * @param componentAppId
     * @param componentAppSecret
     * @param componentVerifyTicket
     * @return
     */
    public ComponentAccessToken oauthToken(String componentAppId, String componentAppSecret, String componentVerifyTicket){
        Map<String, String> param = ParamMap.create("component_appid", componentAppId)
                .put("component_appsecret", componentAppSecret)
                .put("component_verify_ticket", componentVerifyTicket).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/component/api_component_token")
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        String response = HttpClientUtils.getInstance().sendHttpPost(httpUriRequest.getURI().toString());
        return GsonUtils.fromJson(response, ComponentAccessToken.class);
    }

    /**
     * 获取预授权码
     * @param accessToken
     * @param componentAppId
     * @return
     */
    public PreAuthAuthorization preAuthAuthorization(String accessToken, String componentAppId){
        Map<String, String> param = ParamMap.create("component_appid", componentAppId).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/component/api_create_preauthcode")
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        String response = HttpClientUtils.getInstance().sendHttpPost(httpUriRequest.getURI().toString());
        return GsonUtils.fromJson(response, PreAuthAuthorization.class);
    }

    /**
     * 使用授权码获取授权信息
     * @param accessToken
     * @param componentAppId
     * @param authorizationCode
     * @return
     */
    public ComponentAuthorization authorize(String accessToken, String componentAppId, String authorizationCode){
        Map<String, String> param = ParamMap.create("component_appid", componentAppId)
                .put("authorization_code", authorizationCode).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/component/api_query_auth")
                .addParameter(COMPONENT_ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        String response = HttpClientUtils.getInstance().sendHttpPost(httpUriRequest.getURI().toString());
        return GsonUtils.fromJson(response, ComponentAuthorization.class);
    }

    /**
     * 获取/刷新接口调用令牌
     * @param accessToken
     * @param componentAppId
     * @param appId
     * @param refreshToken
     * @return
     */
    public ComponentRefreshAccessToken refreshToken(String accessToken, String componentAppId, String appId, String refreshToken){
        Map<String, String> param = ParamMap.create("component_appid", componentAppId)
                .put("authorizer_appid", appId)
                .put("authorizer_refresh_token", refreshToken).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/component/api_authorizer_token")
                .addParameter(COMPONENT_ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        String response = HttpClientUtils.getInstance().sendHttpPost(httpUriRequest.getURI().toString());
        return GsonUtils.fromJson(response, ComponentRefreshAccessToken.class);
    }

    /**
     * 创建开放平台帐号并绑定公众号/小程序
     * @param accessToken
     * @param appId
     */
    public void create(String accessToken, String appId){
        Map<String, String> param = ParamMap.create("appid", appId).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/open/create")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        String response = HttpClientUtils.getInstance().sendHttpPost(httpUriRequest.getURI().toString());
    }

    /**
     * 将公众号/小程序绑定到开放平台帐号下
     * @param accessToken
     * @param componentAppId
     * @param appId
     */
    public void bind(String accessToken, String componentAppId, String appId){
        Map<String, String> param = ParamMap.create("appid", appId).put("open_appid", componentAppId).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/open/bind")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        String response = HttpClientUtils.getInstance().sendHttpPost(httpUriRequest.getURI().toString());
    }

    /**
     * 将公众号/小程序从开放平台帐号下解绑
     * @param accessToken
     * @param componentAppId
     * @param appId
     */
    public void unbind(String accessToken, String componentAppId, String appId){
        Map<String, String> param = ParamMap.create("appid", appId).put("open_appid", componentAppId).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/open/unbind")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        String response = HttpClientUtils.getInstance().sendHttpPost(httpUriRequest.getURI().toString());
    }

    /**
     * 获取公众号/小程序所绑定的开放平台帐号
     * @param accessToken
     * @param appId
     */
    public void get(String accessToken, String appId){
        Map<String, String> param = ParamMap.create("appid", appId).getData();
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/open/get")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(param), Charset.forName("UTF-8")))
                .build();
        String response = HttpClientUtils.getInstance().sendHttpPost(httpUriRequest.getURI().toString());
    }

}
