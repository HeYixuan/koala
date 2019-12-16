package org.igetwell.wechat.sdk.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClients;
import org.igetwell.wechat.sdk.bean.card.activate.ActivateSet;
import org.igetwell.wechat.sdk.bean.card.activate.CardActivate;
import org.igetwell.wechat.sdk.bean.card.code.consume.SelfConsume;
import org.igetwell.wechat.sdk.bean.card.code.consume.SupplyPay;
import org.igetwell.wechat.sdk.bean.card.code.get.ConsumeCode;
import org.igetwell.wechat.sdk.bean.card.code.get.ConsumeCodeResponse;
import org.igetwell.wechat.sdk.bean.card.create.WxCardCreate;
import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsume;
import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsumeResponse;
import org.igetwell.wechat.sdk.bean.card.code.decrypt.CodeDecrypt;
import org.igetwell.wechat.sdk.bean.card.code.decrypt.CodeDecryptResponse;
import org.igetwell.wechat.sdk.bean.card.mpNews.MpNews;
import org.igetwell.wechat.sdk.bean.card.mpNews.MpNewsResponse;
import org.igetwell.wechat.sdk.bean.card.qrcode.QrCodeCreate;
import org.igetwell.wechat.sdk.bean.card.shelves.Shelves;
import org.igetwell.wechat.sdk.bean.card.shelves.ShelvesResponse;
import org.igetwell.wechat.sdk.bean.card.stock.CardStock;
import org.igetwell.wechat.sdk.bean.card.white.White;
import org.igetwell.wechat.sdk.response.BaseResponse;
import org.igetwell.wechat.sdk.response.Card;
import org.igetwell.wechat.sdk.bean.card.qrcode.QrCodeCreateResponse;

import java.nio.charset.Charset;

public class CardAPI extends API {

    /**
     * 创建卡券
     * @param accessToken
     * @param card
     * @return
     */
    public static Card create(String accessToken, WxCardCreate<?> card) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/create")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(card), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, Card.class);
    }

    /**
     * 创建卡券
     * @param accessToken
     * @param card
     * @return
     */
    public static Card create(String accessToken, String card) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/create")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(card, Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, Card.class);
    }

    /**
     * 创建投放(领取)卡券二维码
     * @param accessToken
     * @param create
     * @return
     */
    public static QrCodeCreateResponse createQrCode(String accessToken, QrCodeCreate create) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/qrcode/create")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(create), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, QrCodeCreateResponse.class);
    }

    /**
     * 创建投放(领取)卡券二维码
     * @param accessToken
     * @param create
     * @return
     */
    public static QrCodeCreateResponse createQrCode(String accessToken, String create) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/qrcode/create")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(create, Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, QrCodeCreateResponse.class);
    }


    /**
     * 设置测试白名单
     * @param accessToken
     * @param white
     * @return
     */
    public static BaseResponse white(String accessToken, White white){
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/testwhitelist/set")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(white), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, BaseResponse.class);
    }

    /**
     * 设置测试白名单
     * @param accessToken
     * @param white
     * @return
     */
    public static BaseResponse white(String accessToken, String white){
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/testwhitelist/set")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(white, Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, BaseResponse.class);
    }

    /**
     * 线下核销卡券Code
     * @param accessToken accessToken
     * @param codeConsume codeConsume
     * @return result
     */
    public static CodeConsumeResponse codeConsume(String accessToken, CodeConsume codeConsume) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/code/consume")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(codeConsume), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, CodeConsumeResponse.class);
    }

    /**
     * 线下核销卡券Code
     * @param accessToken accessToken
     * @param codeConsume codeConsume
     * @return result
     */
    public static CodeConsumeResponse codeConsume(String accessToken, String codeConsume) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/code/consume")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(codeConsume, Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, CodeConsumeResponse.class);
    }

    /**
     * 查询核销卡券Code
     * @param accessToken accessToken
     * @param consumeCode consumeCode
     * @return result
     */
    public static ConsumeCodeResponse getConsumeCode(String accessToken, ConsumeCode consumeCode) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/code/get")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(consumeCode), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, ConsumeCodeResponse.class);
    }

    /**
     * 查询核销卡券Code
     * @param accessToken accessToken
     * @param consumeCode consumeCode
     * @return result
     */
    public static ConsumeCodeResponse getConsumeCode(String accessToken, String consumeCode) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/code/get")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(consumeCode, Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, ConsumeCodeResponse.class);
    }

    /**
     * Code解码<br>
     * 1.只能解码本公众号卡券获取的加密code。 <br>
     * 2.开发者若从url上获取到加密code,请注意先进行urldecode，否则报错。<br>
     * 3.encrypt_code是卡券的code码经过加密处理得到的加密code码，与code一一对应。<br>
     * 4.开发者只能解密本公众号的加密code，否则报错。
     * @param accessToken accessToken
     * @param codeDecrypt codeDecrypt
     * @return result
     */
    public static CodeDecryptResponse codeDecrypt(String accessToken, CodeDecrypt codeDecrypt) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/code/get")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(codeDecrypt), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, CodeDecryptResponse.class);
    }

    /**
     * Code解码<br>
     * 1.只能解码本公众号卡券获取的加密code。 <br>
     * 2.开发者若从url上获取到加密code,请注意先进行urldecode，否则报错。<br>
     * 3.encrypt_code是卡券的code码经过加密处理得到的加密code码，与code一一对应。<br>
     * 4.开发者只能解密本公众号的加密code，否则报错。
     * @param accessToken accessToken
     * @param codeDecrypt codeDecrypt
     * @return result
     */
    public static CodeDecryptResponse codeDecrypt(String accessToken, String codeDecrypt) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/code/decrypt")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(codeDecrypt, Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, CodeDecryptResponse.class);
    }



    /**
     * 创建货架投放接口
     * @param accessToken
     * @param shelves
     * @return
     */
    public static ShelvesResponse createShelves(String accessToken, Shelves shelves) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/code/decrypt")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(shelves), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, ShelvesResponse.class);
    }

    /**
     * 创建货架投放接口
     * @param accessToken
     * @param shelves
     * @return
     */
    public static ShelvesResponse createShelves(String accessToken, String shelves) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/code/decrypt")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(shelves, Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, ShelvesResponse.class);
    }

    /**
     * 设置支持微信买单接口
     * @param accessToken
     * @param supplyPay
     * @return
     */
    public static BaseResponse supplyPay(String accessToken, SupplyPay supplyPay) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/paycell/set")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(supplyPay), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, BaseResponse.class);
    }

    /**
     * 设置自助核销
     * @param accessToken
     * @param consume
     * @return
     */
    public static BaseResponse selfConsume(String accessToken, SelfConsume consume) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/selfconsumecell/set")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(consume), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, BaseResponse.class);
    }

    /**
     * 修改库存
     * @param accessToken
     * @param stock
     * @return
     */
    public static BaseResponse stock(String accessToken, CardStock stock) {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/card/modifystock")
                .addParameter(ACCESS_TOKEN, accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(stock), Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest, BaseResponse.class);
    }

    /**
     * 图文消息群发卡券
     * @param accessToken
     * @param create
     * @return
     */
    public static MpNewsResponse mpNewsGetHtml(String accessToken, MpNews create){
        return null;
    }


    /**
     * 接口激活
     * @param accessToken
     * @param activate
     * @return
     */
    public static BaseResponse activate(String accessToken, CardActivate activate) {
        return null;
    }

    /**
     * 激活设置字段
     * @param accessToken
     * @param activateSet
     * @return
     */
    public static BaseResponse activateSet(String accessToken, ActivateSet activateSet) {
        return null;
    }

}
