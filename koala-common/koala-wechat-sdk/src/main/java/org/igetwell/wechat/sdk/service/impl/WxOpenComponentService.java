package org.igetwell.wechat.sdk.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.igetwell.common.constans.cache.RedisKey;
import org.igetwell.common.uitls.*;
import org.igetwell.common.uitls.aes.WXBizMsgCrypt;

import org.igetwell.wechat.sdk.*;
import org.igetwell.wechat.sdk.service.IWxOpenComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        componentVerifyTicket = (String) redisUtils.get(RedisKey.COMPONENT_VERIFY_TICKET);
    }*/

    /**
     * 是否强制获取微信第三方开放平台token
     * @param forceRefresh
     * @return
     * @throws Exception
     */
    @Override
    public String getComponentAccessToken(boolean forceRefresh) throws Exception {
        if (forceRefresh){
            componentVerifyTicket = (String) redisUtils.get(RedisKey.COMPONENT_VERIFY_TICKET);
            if(StringUtils.isEmpty(componentVerifyTicket)){
                throw new Exception("获取微信开放平台验证票据失败");
            }
            Map<String, String> params = new ConcurrentHashMap<>();
            params.put("component_appid", componentAppId);
            params.put("component_appsecret", componentAppSecret);
            params.put("component_verify_ticket", componentVerifyTicket);
            String response = HttpClientUtils.getInstance().sendHttpPost(API_COMPONENT_TOKEN_URL, GsonUtils.toJson(params));
            ComponentAccessToken componentAccessToken = GsonUtils.fromJson(response, ComponentAccessToken.class);
            if (!StringUtils.isEmpty(componentAccessToken) || !StringUtils.isEmpty(componentAccessToken.getComponentAccessToken())){
                redisUtils.set(RedisKey.COMPONENT_ACCESS_TOKEN, componentAccessToken.getComponentAccessToken(), componentAccessToken.getExpiresIn()); //写入redis
                wxOpenConfigStorage.updateComponentAccessToken(componentAccessToken); //写入内存
            }
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
        AuthorizationInfo authorizationInfo = GsonUtils.fromJson(response, AuthorizationInfo.class);
        if (StringUtils.isEmpty(authorizationInfo)){
            return;
        }

        if (!StringUtils.isEmpty(authorizationInfo.getAuthorizationInfo().getAuthorizerAccessToken()) && !StringUtils.isEmpty(authorizationInfo.getAuthorizationInfo().getAuthorizerRefreshToken())) {
            redisUtils.set(RedisKey.COMPONENT_AUTHORIZATION + authorizationInfo.getAuthorizationInfo().getAuthorizerAppid(), GsonUtils.toJson(authorizationInfo.getAuthorizationInfo()), authorizationInfo.getAuthorizationInfo().getExpiresIn());
            wxOpenConfigStorage.updateComponentAuthorization(authorizationInfo.getAuthorizationInfo()); //写入内存
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
     * 保存微信开放平台Ticket
     *
     * @param xml
     */
    private void processAuthorizationEvent(String xml) {
        Element element = XmlUtils.parseXml(xml);
        String infoType = XmlUtils.elementText(element,"InfoType");
        if (!StringUtils.isEmpty(infoType)){
            //验证票据component_verify_ticket
            if (infoType.equalsIgnoreCase("component_verify_ticket")){
                String ticket = XmlUtils.elementText(element,"ComponentVerifyTicket");
                log.info("第三方平台全网发布-----------------------解密后 ComponentVerifyTicket={}", ticket);
                if(!StringUtils.isEmpty(ticket)){
                    //设置10分钟
                    redisUtils.set(RedisKey.COMPONENT_VERIFY_TICKET, ticket, RedisKey.COMPONENT_VERIFY_TICKET_EXPIRE);
                }
            }
            //取消授权
            if (infoType.equalsIgnoreCase("unauthorized")){

            }
            //授权成功或更新授权
            if (infoType.equalsIgnoreCase("authorized") ||infoType.equalsIgnoreCase("updateauthorized")){
                String authorizationCode = XmlUtils.elementText(element, "AuthorizationCode");
                String preAuthCode = XmlUtils.elementText(element, "PreAuthCode");
                long expired =  Long.valueOf(XmlUtils.elementText(element, "AuthorizationCodeExpiredTime"));
                redisUtils.set(RedisKey.COMPONENT_AUTHORIZATION_CODE, authorizationCode, expired);
                redisUtils.set(RedisKey.COMPONENT_PRE_AUTH_CODE, preAuthCode, expired);

            }
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

        Element element = XmlUtils.parseXml(xml);
        String msgType = XmlUtils.elementText(element, "MsgType");
        String toUserName = XmlUtils.elementText(element,"ToUserName");
        String fromUserName = XmlUtils.elementText(element,"FromUserName");


        log.info("MsgType：{}, toUserName:{}, fromUserName", msgType, toUserName, fromUserName);


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
        String response = HttpClientUtils.getInstance().sendHttpPost(String.format(API_SEND_MESSAGE_URL, wxOpenConfigStorage.getAuthorizerAccessToken()), GsonUtils.toJson(params));
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

        /*ComponentAuthorization authorization = new ComponentAuthorization();
        authorization.setAuthorizerAccessToken("1231311sa");
        System.err.println(GsonUtils.toJson(authorization));
        Map<String, String> params = new ConcurrentHashMap<>();    
        params.put("component_Appid", "1213131");
        params.put("authorization_code", "AB12");
        System.err.println(GsonUtils.toJson(params));*/
        /*AuthorizationInfo info = new AuthorizationInfo();
        ComponentAuthorization componentAuthorization = new ComponentAuthorization();
        componentAuthorization.setAuthorizerAccessToken("123131");
        info.setAuthorizationInfo(componentAuthorization);
        System.err.println(GsonUtils.toJson(info));*/

        String s = "{\"authorization_info\": {\"authorizer_appid\": \"wxf8b4f85f3a794e77\",\"authorizer_access_token\": \"QXjUqNqfYVH0yBE1iI_7vuN_9gQbpjfK7hYwJ3P7xOa88a89-Aga5x1NMYJyB8G2yKt1KCl0nPC3W9GJzw0Zzq_dBxc8pxIGUNi_bFes0qM\",\"expires_in\": 7200,\"authorizer_refresh_token\": \"dTo-YCXPL4llX-u1W1pPpnp8Hgm4wpJtlR6iV0doKdY\",\"func_info\": [{\"funcscope_category\": {\"id\": 1}},{\"funcscope_category\": {\"id\": 2}},{\"funcscope_category\": {\"id\": 3}}]}}";

        AuthorizationInfo authorization = GsonUtils.fromJson(s, AuthorizationInfo.class);
        System.err.println(GsonUtils.toJson(authorization.getAuthorizationInfo()));
    }
}
