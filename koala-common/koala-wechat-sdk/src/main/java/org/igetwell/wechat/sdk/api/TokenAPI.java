package org.igetwell.wechat.sdk.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.igetwell.common.uitls.HttpClients;
import org.igetwell.wechat.sdk.bean.token.Token;

/**
 * ACCESS_TOKEN API
 */
public class TokenAPI extends API {
    /**
     * 获取access_token
     * @param appId appId
     * @param secret secret
     * @return Token
     */
    public static Token token(String appId, String secret){
        HttpUriRequest httpUriRequest = RequestBuilder.get()
                .setUri(BASE_URI + "/cgi-bin/token")
                .addParameter("grant_type","client_credential")
                .addParameter("appid", appId)
                .addParameter("secret", secret)
                .build();
        return HttpClients.execute(httpUriRequest, Token.class);
    }

}
