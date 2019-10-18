package org.igetwell.wechat.sdk.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.constans.cache.RedisKey;
import org.igetwell.common.uitls.*;
import org.igetwell.common.uitls.aes.WXBizMsgCrypt;

import org.igetwell.wechat.sdk.ComponentAccessToken;
import org.igetwell.wechat.sdk.ComponentAuthorization;
import org.igetwell.wechat.sdk.PreAuthAuthorization;
import org.igetwell.wechat.sdk.service.IWxOpenComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WxOpenComponentService implements IWxOpenComponentService {
    @Value("${componentAppId}")
    private String componentAppId;

    @Value("${componentAppSecret}")
    private String componentAppSecret;

    @Value("${componentToken}")
    private String componentToken;

    @Value("${encodingAesKey}")
    private String encodingAesKey;

    private String componentVerifyTicket;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public WxOpenInMemoryConfigStorage wxOpenConfigStorage;


    /*@PostConstruct
    public void init(){
        componentVerifyTicket = (String) redisTemplate.opsForValue().get(RedisKey.COMPONENT_VERIFY_TICKET);
    }*/

    @Override
    public String getComponentAccessToken(boolean forceRefresh) {
        if (forceRefresh){
            componentVerifyTicket = (String) redisUtils.get(RedisKey.COMPONENT_VERIFY_TICKET);
            Map<String, String> params = new ConcurrentHashMap<>();
            params.put("component_appid", componentAppId);
            params.put("component_appsecret", componentAppSecret);
            params.put("component_verify_ticket", componentVerifyTicket);
            String response = HttpClientUtils.getInstance().sendHttpPost(API_COMPONENT_TOKEN_URL, GsonUtils.toJson(params));
            ComponentAccessToken wxOpenComponentAccessToken = GsonUtils.fromJson(response, ComponentAccessToken.class);
            redisUtils.set(RedisKey.COMPONENT_ACCESS_TOKEN, wxOpenComponentAccessToken.getComponentAccessToken(), wxOpenComponentAccessToken.getExpiresIn()); //写入redis
            wxOpenConfigStorage.updateComponentAccessToken(wxOpenComponentAccessToken); //写入内存
        }
        return wxOpenConfigStorage.getComponentAccessToken();
    }

    @Override
    public String getPreAuthUrl(String redirectURI) throws Exception {
        return getPreAuthUrl(redirectURI, null, null);
    }

    @Override
    public String getPreAuthUrl(String redirectURI, String authType, String bizAppid) throws Exception {
        return createPreAuthUrl(redirectURI, authType, bizAppid, false);
    }

    @Override
    public String getMobilePreAuthUrl(String redirectURI) throws Exception {
        return getMobilePreAuthUrl(redirectURI, null, null);
    }

    @Override
    public String getMobilePreAuthUrl(String redirectURI, String authType, String bizAppid) throws Exception {
        return createPreAuthUrl(redirectURI, authType, bizAppid, true);
    }

    /**
     * 使用授权码换取公众号的授权信息
     * @param authorizationCode  授权code
     */
    @Override
    public void getQueryAuth(String authorizationCode) {
        Map<String, String> params = new ConcurrentHashMap<>();
        params.put("component_appid", componentAppId);
        params.put("authorization_code", authorizationCode);
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(API_QUERY_AUTH_URL, wxOpenConfigStorage.getComponentAccessToken()), GsonUtils.toJson(params));
        ComponentAuthorization authorization = GsonUtils.fromJson(response, ComponentAuthorization.class);
        if (StringUtils.isEmpty(authorization)){
            return;
        }

        if (!StringUtils.isEmpty(authorization.getAuthorizerAccessToken()) && !StringUtils.isEmpty(authorization.getAuthorizerRefreshToken())) {
            redisUtils.set(RedisKey.COMPONENT_AUTHORIZATION + authorization.getAuthorizerAppid(), GsonUtils.toJson(authorization), authorization.getExpiresIn());
        }

    }


    /**
     * 创建预授权链接
     *
     * @param redirectURI
     * @param authType
     * @param bizAppid
     * @param isMobile    是否移动端预授权
     * @return
     */
    private String createPreAuthUrl(String redirectURI, String authType, String bizAppid, boolean isMobile) throws Exception{

        String preAuthCode = (String) redisUtils.get(RedisKey.COMPONENT_PRE_AUTH_CODE);
        if (StringUtils.isEmpty(preAuthCode)){
            Map<String, String> params = new ConcurrentHashMap<>();
            params.put("component_appid", componentAppId); //这个AppId可以从缓存里面取
            String response = HttpClientUtils.getInstance().sendHttpPost(String.format(API_CREATE_PREAUTHCODE_URL, wxOpenConfigStorage.getComponentAccessToken()), GsonUtils.toJson(params));
            PreAuthAuthorization preAuth = GsonUtils.fromJson(response, PreAuthAuthorization.class);
            redisUtils.set(RedisKey.COMPONENT_PRE_AUTH_CODE, preAuth.getPreAuthCode(), preAuth.getExpiresIn());
            preAuthCode = preAuth.getPreAuthCode();
        }

        String preAuthUrl;
        if (isMobile){
            preAuthUrl = String.format(COMPONENT_MOBILE_LOGIN_PAGE_URL, componentAppId, preAuthCode, URLEncoder.encode(redirectURI, "UTF-8"), authType, bizAppid);
        } else {
            preAuthUrl = String.format(COMPONENT_LOGIN_PAGE_URL, componentAppId, preAuthCode, URLEncoder.encode(redirectURI, "UTF-8"), authType, bizAppid);
        }
        return preAuthUrl;
    }


    /**
     * 微信开放平台处理授权事件的推送
     *
     * @param request
     * @throws Exception
     */
    public void processAuthorizeEvent(HttpServletRequest request, String nonce, String timestamp, String msgSignature) throws Exception {
        if (StringUtils.isEmpty(nonce) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(msgSignature)) {
            throw new Exception("-------接收微信服务器回调字符串失败---------");
        }
        String xml = HttpUtils.readData(request);
        log.info("第三方平台全网发布-----------------------原始 Xml={}", xml);
        //String appId = getAuthorizerAppidFromXml(xml);// 此时加密的xml数据中ToUserName是非加密的，解析xml获取即可
        WXBizMsgCrypt pc = new WXBizMsgCrypt(componentToken, encodingAesKey, componentAppId);
        log.info("第三方平台全网发布-----------------------解密 WXBizMsgCrypt 成功.");
        xml = pc.DecryptMsg(msgSignature, timestamp, nonce, xml);
        log.info("第三方平台全网发布-----------------------解密后 Xml={}", xml);
        processAuthorizationEvent(xml);

    }

    /**
     * 获取授权的appId
     * @param xml
     * @return
     */
    private String getAuthorizerAppidFromXml(String xml) {
        Document doc;
        doc = XmlUtils.parse(xml);
        Element element = doc.getDocumentElement();
        String toUserName = XmlUtils.elementText(element,"ToUserName");
        return toUserName;
    }

    /**
     * 保存微信开放平台Ticket
     *
     * @param xml
     */
    private void processAuthorizationEvent(String xml) {
        Document doc;
        doc = XmlUtils.parse(xml);
        Element element = doc.getDocumentElement();
        String ticket = XmlUtils.elementText(element,"ComponentVerifyTicket");
        log.info("第三方平台全网发布-----------------------解密后 ComponentVerifyTicket={}", ticket);
        if(!StringUtils.isEmpty(ticket)){
            //设置10分钟
            redisUtils.set(RedisKey.COMPONENT_VERIFY_TICKET, ticket, RedisKey.COMPONENT_VERIFY_TICKET_EXPIRE);
        }

    }


    /**
     * 全网发布接入检测消息
     * @param request
     * @param response
     * @param nonce
     * @param timestamp
     * @param msgSignature
     * @throws Exception
     */
    public void checkWechatAllNetwork(HttpServletRequest request, HttpServletResponse response, String nonce, String timestamp, String msgSignature) throws Exception {
        String xml = HttpUtils.readData(request);
        log.info("全网发布接入检测消息反馈开始--------nonce:{}-------timestamp:{}---------msgSignature:{}.");
        WXBizMsgCrypt pc = new WXBizMsgCrypt(componentToken, encodingAesKey, componentAppId);
        xml = pc.DecryptMsg(msgSignature, timestamp, nonce, xml);

        Document doc = XmlUtils.parse(xml);
        Element element = doc.getDocumentElement();
        String msgType = XmlUtils.elementText(element, "MsgType");
        String toUserName = XmlUtils.elementText(element,"ToUserName");
        String fromUserName = XmlUtils.elementText(element,"FromUserName");
        log.info("全网发布接入检测--step.1-----------msgType={}----------toUserName={}---------fromUserName={}", msgType, toUserName, fromUserName);
        log.info("---全网发布接入检测--step.2-----------xml="+xml);
        if(MessageUtils.MESSAGE_EVENT.equals(msgType)){
            log.info("---全网发布接入检测--step.3-----------事件消息--------");
            String event = XmlUtils.elementText(element,"Event");
            replyEventMessage(response, event, toUserName, fromUserName);
        }else if(MessageUtils.MESSAGE_TEXT.equals(msgType)){
            log.info("---全网发布接入检测--step.3-----------文本消息--------");
            String content = XmlUtils.elementText(element,"Content");
            processTextMessage(response, content, toUserName, fromUserName);
        }

    }


    /**
     * 微信全网接入  文本消息
     * @param response
     * @param toUserName
     * @param fromUserName
     */
    private void processTextMessage(HttpServletResponse response, String content, String toUserName, String fromUserName){
        if("TESTCOMPONENT_MSG_TYPE_TEXT".equals(content)){
            String returnContent = content+"_callback";
            replyTextMessage(response,returnContent, toUserName, fromUserName);
        }else if(StringUtils.startsWithIgnoreCase(content, "QUERY_AUTH_CODE")){
            //先回复空串
            output(response, "");
            //接下来客服API再回复一次消息
            //此时 content字符的内容为是 QUERY_AUTH_CODE:adsg5qe4q35
            replyApiTextMessage(content.split(":")[1], toUserName);
        }
    }

    /**
     * 方法描述: 类型为enevt的时候，拼接
     * @param response
     * @param event
     * @param toUserName  发送接收人
     * @param fromUserName  发送人
     */
    public void replyEventMessage(HttpServletResponse response, String event, String toUserName, String fromUserName) {
        String content = event + "from_callback";
        replyTextMessage(response,content, toUserName, fromUserName);
    }



    private void replyApiTextMessage(String authorizationCode, String fromUserName) {
        // 得到微信授权成功的消息后，应该立刻进行处理！！相关信息只会在首次授权的时候推送过来
        log.info("------step.4----使用客服消息接口回复粉丝----逻辑开始-------------------------");
        getQueryAuth(authorizationCode);
        String msg = authorizationCode + "_from_api";
        Map<String, Object> params = new HashMap<>();
        params.put("touser", fromUserName);
        params.put("msgtype", "text");
        Map<String, Object> text = new HashMap<>();
        text.put("content", msg);
        params.put("text", text);
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(API_SEND_MESSAGE_URL, wxOpenConfigStorage.getComponentAccessToken()), GsonUtils.toJson(params));
        if (log.isDebugEnabled()){
            log.debug("api reply message to to wechat whole network test respose = "+ response);
        }
    }
    /**
     * 回复微信服务器"文本消息"
     * @param response
     * @param content
     * @param toUserName
     * @param fromUserName
     */
    private void replyTextMessage(HttpServletResponse response, String content, String toUserName, String fromUserName) {
        Long createTime = System.currentTimeMillis() / 1000;
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA["+fromUserName+"]]></ToUserName>");
        sb.append("<FromUserName><![CDATA["+toUserName+"]]></FromUserName>");
        sb.append("<CreateTime>"+createTime+"</CreateTime>");
        sb.append("<MsgType><![CDATA[text]]></MsgType>");
        sb.append("<Content><![CDATA["+content+"]]></Content>");
        sb.append("</xml>");
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(componentToken, encodingAesKey, componentAppId);
            String text = pc.EncryptMsg(sb.toString(), createTime.toString(), "easemob");
            output(response, text);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void output(HttpServletResponse response,String text){
        try {
            PrintWriter pw = response.getWriter();
            pw.write(text);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String [] args) throws Exception {
        /*WxOpenComponentService service = new WxOpenComponentService();
        service.createPreAuthUrl(null, null,null,false);*/
        ComponentAuthorization authorization = new ComponentAuthorization();
        authorization.setAuthorizerAccessToken("1231311sa");
        System.err.println(GsonUtils.toJson(authorization));
        Map<String, String> params = new ConcurrentHashMap<>();    
        params.put("component_Appid", "1213131");
        params.put("authorization_code", "AB12");
        System.err.println(GsonUtils.toJson(params));
        /*String xml = null;
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(COMPONENT_TOKEN, COMPONENT_ENCODINGAESKEY,
                    "wx4addc310e841f58b");
            xml = pc.decryptMsg("cbff9928c63f34e6fe0f3d63bf7ad8c5ab2322a3", "1481013633", "1836283428", "<xml>    <AppId><![CDATA[wx4addc310e841f58b]]></AppId>    <Encrypt><![CDATA[ouuB6+xRWHaoVpPqvgLJXztr3vnxrF9rWR0K8X7nU3DfjJBgc5NtxJM7C00Q8Ogx7vJSKkiv4T54EPIPn1cDORqZ/x0MVedRAKrRYRX7f5Cju+nBCF7+5YoXLbuT2HigTA1NrGz3aiYTTnwehlrquO9LwwggriDuvhETseHaa2lCoN9A18hFbMPcTIZobeNEVh97omziTBbq6bOOpWl1QSmjk89GnmhUN7f2MXqY+SQiLg1xdk6Tk6jh2MtCAmJdLQOKq+FFVLogarqBwn2G03y4/zzKwriDjYR80Yo4iHnz+7Vewlp6n0yw8BNXE5smnRe5FFzO4IMAODxKe8pPahKTOYrjqOez+AmER+7EW6N/6OGWsTqNfTmFSl1z/W3amXhBIXQXu+CJy/7lcwEYkGvaFA2PVSf1nwfqaWFxP72gpYHUemQe00aMkbtFi8wvNw8k6cDNuZ0CTp7zJLV/Lg==]]></Encrypt></xml>");
        } catch (AesException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("第三方平台全网发布-----------------------解密后 Xml="+xml);*/
    }
}
