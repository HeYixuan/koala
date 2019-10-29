package org.igetwell.wechat.sdk.api;

import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClientUtils;
import org.igetwell.wechat.sdk.card.activate.ActivateSet;
import org.igetwell.wechat.sdk.card.activate.CardActivate;
import org.igetwell.wechat.sdk.card.bean.create.WxCardCreate;
import org.igetwell.wechat.sdk.card.code.CodeGet;
import org.igetwell.wechat.sdk.card.code.CodeGetResponse;
import org.igetwell.wechat.sdk.card.code.consume.CodeConsume;
import org.igetwell.wechat.sdk.card.code.consume.CodeConsumeResponse;
import org.igetwell.wechat.sdk.card.code.decrypt.CodeDecrypt;
import org.igetwell.wechat.sdk.card.code.decrypt.CodeDecryptResponse;
import org.igetwell.wechat.sdk.card.mpNews.MpNews;
import org.igetwell.wechat.sdk.card.mpNews.MpNewsResponse;
import org.igetwell.wechat.sdk.card.qrcode.QrCodeCreate;
import org.igetwell.wechat.sdk.card.storage.StorageCreate;
import org.igetwell.wechat.sdk.card.storage.StorageCreateResponse;
import org.igetwell.wechat.sdk.card.whitelist.TestWhiteList;
import org.igetwell.wechat.sdk.response.BaseResponse;
import org.igetwell.wechat.sdk.response.CardResponse;
import org.igetwell.wechat.sdk.card.qrcode.QrCodeCreateResponse;

public class CardAPI extends API {


    //创建卡券接口
    static String CREATE = "https://api.weixin.qq.com/card/create?access_token=%s";
    //创建二维码接口
    static String CREATE_QR_CODE = "https://api.weixin.qq.com/card/qrcode/create?access_token=%s";
    //创建货架接口
    static String CREATE_SHELF = "https://api.weixin.qq.com/card/landingpage/create?access_token=%s";
    //图文消息群发卡券
    static String MP_NEWS = "https://api.weixin.qq.com/card/mpnews/gethtml?access_token=%s";
    //设置测试白名单
    static String WHITE_LIST = "https://api.weixin.qq.com/card/testwhitelist/set?access_token=%s";
    //查询CODE
    static String CODE_GET = "https://api.weixin.qq.com/card/code/get?access_token=%s";
    //查询CODE
    static String CODE_CONSUME = "https://api.weixin.qq.com/card/code/consume?access_token=%s";
    //Code解码
    static String CODE_DECRYPT = "https://api.weixin.qq.com/card/code/decrypt?access_token=%s";
    //接口激活
    static String ACTIVATE = "https://api.weixin.qq.com/card/membercard/activate?access_token=%s";
    //激活设置字段
    static String ACTIVATE_SET = "https://api.weixin.qq.com/card/membercard/activateuserform/set?access_token=%s";


    /**
     * 创建卡券
     * @param accessToken
     * @param card
     * @return
     */
    public static CardResponse create(String accessToken, WxCardCreate<?> card) {
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(CREATE, accessToken), GsonUtils.toJson(card));
        return GsonUtils.fromJson(response, CardResponse.class);
    }

    /**
     * 创建领取卡券二维码
     * @param accessToken
     * @param create
     * @return
     */
    public static QrCodeCreateResponse createQrCode(String accessToken, QrCodeCreate create) {
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(CREATE_QR_CODE, accessToken), GsonUtils.toJson(create));
        return GsonUtils.fromJson(response, QrCodeCreateResponse.class);
    }

    /**
     * 创建货架
     * @param accessToken
     * @param create
     * @return
     */
    public static StorageCreateResponse createShelf(String accessToken, StorageCreate create) {
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(CREATE_SHELF, accessToken), GsonUtils.toJson(create));
        return GsonUtils.fromJson(response, StorageCreateResponse.class);
    }

    /**
     * 图文消息群发卡券
     * @param accessToken
     * @param create
     * @return
     */
    public static MpNewsResponse mpNewsGetHtml(String accessToken, MpNews create){
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(MP_NEWS, accessToken), GsonUtils.toJson(create));
        return GsonUtils.fromJson(response, MpNewsResponse.class);
    }

    /**
     * 设置测试白名单
     * @param accessToken
     * @param whiteList
     * @return
     */
    public static BaseResponse testWhiteListSet(String accessToken, TestWhiteList whiteList){
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(WHITE_LIST, accessToken), GsonUtils.toJson(whiteList));
        return GsonUtils.fromJson(response, BaseResponse.class);
    }

    /**
     * 查询Code
     * @param accessToken accessToken
     * @param codeGet codeGet
     * @return result
     */
    public static CodeGetResponse codeGet(String accessToken, CodeGet codeGet) {
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(CODE_GET, accessToken), GsonUtils.toJson(codeGet));
        return GsonUtils.fromJson(response, CodeGetResponse.class);
    }

    /**
     * 核销Code
     * @param accessToken accessToken
     * @param codeConsume codeConsume
     * @return result
     */
    public static CodeConsumeResponse codeConsume(String accessToken, CodeConsume codeConsume) {
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(CODE_CONSUME, accessToken), GsonUtils.toJson(codeConsume));
        return GsonUtils.fromJson(response, CodeConsumeResponse.class);
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
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(CODE_DECRYPT, accessToken), GsonUtils.toJson(codeDecrypt));
        return GsonUtils.fromJson(response, CodeDecryptResponse.class);
    }

    /**
     * 接口激活
     * @param accessToken
     * @param activate
     * @return
     */
    public static BaseResponse activate(String accessToken, CardActivate activate) {
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(ACTIVATE, accessToken), GsonUtils.toJson(activate));
        return GsonUtils.fromJson(response, BaseResponse.class);
    }

    /**
     * 激活设置字段
     * @param accessToken
     * @param activateSet
     * @return
     */
    public static BaseResponse activateSet(String accessToken, ActivateSet activateSet) {
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(ACTIVATE_SET, accessToken), GsonUtils.toJson(activateSet));
        return GsonUtils.fromJson(response, BaseResponse.class);
    }

}
