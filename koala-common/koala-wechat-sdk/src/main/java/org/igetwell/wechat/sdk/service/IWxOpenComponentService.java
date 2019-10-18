package org.igetwell.wechat.sdk.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public interface IWxOpenComponentService {

    String API_COMPONENT_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
    String API_CREATE_PREAUTHCODE_URL = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=%s";
    String API_QUERY_AUTH_URL = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth";
    String API_AUTHORIZER_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token";
    String API_GET_AUTHORIZER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info";
    String API_GET_AUTHORIZER_OPTION_URL = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_option";
    String API_SET_AUTHORIZER_OPTION_URL = "https://api.weixin.qq.com/cgi-bin/component/api_set_authorizer_option";

    String COMPONENT_LOGIN_PAGE_URL = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=%s&pre_auth_code=%s&redirect_uri=%s&auth_type=%s&biz_appid=%s";

    /**
     * 手机端打开授权链接
     */
    String COMPONENT_MOBILE_LOGIN_PAGE_URL = "https://mp.weixin.qq.com/safe/bindcomponent?action=bindcomponent&no_scan=1&auth_type=3&component_appid=%s&pre_auth_code=%s&redirect_uri=%s&auth_type=%s&biz_appid=%s#wechat_redirect";
    String CONNECT_OAUTH2_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s&component_appid=%s#wechat_redirect";

    /**
     * 用code换取oauth2的access token
     */
    String OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=%s&code=%s&grant_type=authorization_code&component_appid=%s";
    /**
     * 刷新oauth2的access token
     */
    String OAUTH2_REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/component/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s&component_appid=%s";

    String MINIAPP_JSCODE_2_SESSION = "https://api.weixin.qq.com/sns/component/jscode2session?appid=%s&js_code=%s&grant_type=authorization_code&component_appid=%s";

    String CREATE_OPEN_URL= "https://api.weixin.qq.com/cgi-bin/open/create";

    /**
     * 快速创建小程序接口
     */
    String FAST_REGISTER_WEAPP_URL = "https://api.weixin.qq.com/cgi-bin/component/fastregisterweapp?action=create";
    String FAST_REGISTER_WEAPP_SEARCH_URL = "https://api.weixin.qq.com/cgi-bin/component/fastregisterweapp?action=search";

    /**
     * 客服接口-发消息
     */
    String API_SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";

    /**
     * 获取第三方平台access_token
     * @param forceRefresh
     * @return
     */
    String getComponentAccessToken(boolean forceRefresh);

    /**
     * 获取用户授权页URL（来路URL和成功跳转URL 的域名都需要为三方平台设置的 登录授权的发起页域名）
     */
    String getPreAuthUrl(String redirectURI) throws Exception;

    /**
     * authType 要授权的帐号类型：1则商户点击链接后，手机端仅展示公众号、2表示仅展示小程序，3表示公众号和小程序都展示。如果为未指定，则默认小程序和公众号都展示。第三方平台开发者可以使用本字段来控制授权的帐号类型。
     * bizAppid 指定授权唯一的小程序或公众号
     * 注：auth_type、biz_appid两个字段互斥。
     */
    String getPreAuthUrl(String redirectURI, String authType, String bizAppid) throws Exception;

    /**
     * 获取预授权链接（手机端预授权）
     *
     * @param redirectURI
     * @return
     */
    String getMobilePreAuthUrl(String redirectURI) throws Exception;

    /**
     * 获取预授权链接（手机端预授权）
     *
     * @param redirectURI
     * @param authType
     * @param bizAppid
     * @return
     */
    String getMobilePreAuthUrl(String redirectURI, String authType, String bizAppid) throws Exception;

    /**
     * 使用授权码换取公众号的授权信息
     * @param authorizationCode  授权code
     */
    void getQueryAuth(String authorizationCode);


    /**
     * 微信开放平台处理授权事件的推送
     *
     * @param request
     */
    public void processAuthorizeEvent(HttpServletRequest request, String nonce, String timestamp, String msgSignature) throws Exception;

    /**
     * 全网发布接入检测消息
     * @param request
     *
     */
    public void checkWechatAllNetwork(HttpServletRequest request, HttpServletResponse response, String nonce, String timestamp, String msgSignature) throws Exception;
}
