package org.igetwell.wechat.sdk.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClientUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.wechat.sdk.card.bean.create.WxCardCreate;
import org.igetwell.wechat.sdk.response.CardResponseEntity;

import java.nio.charset.Charset;

public class CardAPI extends API {

    /**
     * 批量查询卡券列表
     * @param accessToken accessToken
     * @param batchget batchget
     * @return result
     */
    /*public static ResponseEntity batchGet(String accessToken, BatchGet batchget) {
        return batchGet(accessToken, GsonUtils.toJson(batchget));
    }*/

    /**
     * 批量查询卡券列表
     * @param accessToken accessToken
     * @param postJson postJson
     * @return result
     */
    public static ResponseEntity batchGet(String accessToken, String postJson) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/batchget")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(postJson, Charset.forName("UTF-8")))
                .build();
        String response = HttpClientUtils.getInstance().sendHttpPost(httpUriRequest.getURI().toString());
        return GsonUtils.fromJson(response, ResponseEntity.class);
    }


    /**
     * 创建卡券
     * @param accessToken accessToken
     * @param postJson postJson
     * @return result
     */
    public static CardResponseEntity create(String accessToken, String postJson) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/create")
                .addParameter(ACCESS_TOKEN, API.accessToken(accessToken))
                .setEntity(new StringEntity(postJson, Charset.forName("UTF-8")))
                .build();
        String response = HttpClientUtils.getInstance().sendHttpPost(httpUriRequest.getURI().toString());
        return GsonUtils.fromJson(response, CardResponseEntity.class);
    }

    /**
     * 创建卡券
     * @param accessToken accessToken
     * @param card card
     * @return result
     */
    public static CardResponseEntity create(String accessToken, WxCardCreate<?> card) {
        return create(accessToken, GsonUtils.toJson(card));
    }

    public static void main(String[] args) {
        String a = "{'a':'a'}";
        batchGet("aa#112131", a);
        System.err.println(a);
    }

}
