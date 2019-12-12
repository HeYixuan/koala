package org.igetwell.wechat.sdk.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClientUtils;
import org.igetwell.wechat.sdk.bean.component.WechatUser;

/**
 * 第三方开放平台代公众号授权获取用户基本信息
 */
public class WechatUserAPI extends API {

    /**
     * 第三方开放平台代公众号授权获取用户基本信息
     * @param accessToken
     * @param openId
     * @return
     */
    public static WechatUser getWxUser(String accessToken, String openId) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .get()
                .setUri(BASE_URI + "/sns/userinfo")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .addParameter("openid", openId)
                .addParameter("lang", "zh_CN")
                .build();
        String response = HttpClientUtils.getInstance().sendHttpPost(httpUriRequest.getURI().toString());
        return GsonUtils.fromJson(response, WechatUser.class);
    }
}
