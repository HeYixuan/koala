package org.igetwell.wechat.sdk.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClients;
import org.igetwell.wechat.sdk.bean.qrcode.AppletsQrcode;
import org.igetwell.wechat.sdk.bean.qrcode.QrcodeTicket;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 二维码API
 */
@Slf4j
public class QrcodeApi extends API {

    /**
     * 创建二维码
     * @param accessToken accessToken
     * @param postJson json 数据
     * @return QrcodeTicket
     */
    private static QrcodeTicket qrcodeCreate(String accessToken,String postJson){
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI+"/cgi-bin/qrcode/create")
                .addParameter(ACCESS_TOKEN, API.accessToken(accessToken))
                .setEntity(new StringEntity(postJson, Charset.forName("UTF-8")))
                .build();
        return HttpClients.execute(httpUriRequest,QrcodeTicket.class);
    }

    /**
     * 创建临时二维码
     * @param accessToken accessToken
     * @param expire_seconds 最大不超过604800秒（即30天）
     * @param scene_id		  场景值ID，32位非0整型  最多10万个
     * @return QrcodeTicket
     */
    public static QrcodeTicket qrcodeCreateTemp(String accessToken,int expire_seconds,long scene_id){
        String json = String.format("{\"expire_seconds\": %d, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": %d}}}",expire_seconds, scene_id);
        return qrcodeCreate(accessToken,json);
    }

    /**
     * 创建带参数的临时二维码
     * 具体信息可以查看<a href="https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1443433542">微信公众号文档</a>
     * @param accessToken accessToken
     * @param expire_seconds 最大不超过604800秒（即30天）
     * @param scene_str		 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
     * @return QrcodeTicket
     */
    public static QrcodeTicket qrcodeCreateTemp(String accessToken, int expire_seconds, String scene_str){
        String json = String.format("{\"expire_seconds\": %d, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"%s\"}}}", expire_seconds, scene_str);
        return qrcodeCreate(accessToken,json);
    }

    /**
     * 创建持久二维码
     * @param accessToken accessToken
     * @param scene_id	场景值ID 1-100000
     * @return QrcodeTicket
     */
    public static QrcodeTicket qrcodeCreateFinal(String accessToken,int scene_id){
        String json = String.format("{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\":%d}}}", scene_id);
        return qrcodeCreate(accessToken,json);
    }

    /**
     * 创建持久二维码
     * @param accessToken accessToken
     * @param scene_str	场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
     * @return QrcodeTicket
     */
    public static QrcodeTicket qrcodeCreateFinal(String accessToken,String scene_str){
        String json = String.format("{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"%s\"}}}", scene_str);
        return qrcodeCreate(accessToken,json);
    }

    /**
     * 下载二维码
     * @param ticket  内部自动 UrlEncode
     * @return BufferedImage
     */
    public static BufferedImage showqrcode(String ticket){
        HttpUriRequest httpUriRequest = RequestBuilder.get()
                .setUri(MP_URI + "/cgi-bin/showqrcode")
                .addParameter("ticket", ticket)
                .build();
        CloseableHttpResponse httpResponse = HttpClients.execute(httpUriRequest);
        return getImage(httpResponse);
    }

    /**
     * 获取小程序页面二维码 <br>
     * 小程序码使用 使用 WxaAPI.getwxacode　或　WxaAPI.getwxacodeunlimit
     * @since 2.8.8
     * @param accessToken accessToken
     * @param applets wxaqrcode
     * @return BufferedImage
     */
    public static BufferedImage wxaappCreatewxaqrcode(String accessToken, AppletsQrcode applets){
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI + "/cgi-bin/wxaapp/createwxaqrcode")
                .addParameter(ACCESS_TOKEN, API.accessToken(accessToken))
                .setEntity(new StringEntity(GsonUtils.toJson(applets), Charset.forName("utf-8")))
                .build();
        CloseableHttpResponse httpResponse = HttpClients.execute(httpUriRequest);
        return getImage(httpResponse);
    }

    private static BufferedImage getImage(CloseableHttpResponse httpResponse) {
        try {
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status == 200) {
                byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
                return ImageIO.read(new ByteArrayInputStream(bytes));
            }
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return null;
    }
}
