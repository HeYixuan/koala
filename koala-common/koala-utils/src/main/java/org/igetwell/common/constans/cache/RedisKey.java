package org.igetwell.common.constans.cache;

public interface RedisKey {

    /**
     * Access_TOKEN
     */
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    /**
     * ACCESS_TOKEN 失效时间(2小时)
     */
    public static final long ACCESS_TOKEN_EXPIRE = 60 * 60 * 60 * 60;


    /**
     * COMPONENT_VERIFY_TICKET
     */
    public static final String COMPONENT_VERIFY_TICKET = "COMPONENT_VERIFY_TICKET";

    /**
     * COMPONENT_VERIFY_TICKET 失效时间(10分钟)
     */
    public static final long COMPONENT_VERIFY_TICKET_EXPIRE = 60 * 10;

    /**
     * 第三方平台access_token
     */
    public static final String COMPONENT_ACCESS_TOKEN = "COMPONENT_ACCESS_TOKEN";

    /**
     * 第三方平台使用授权码换取公众号(授权方)的授权信息
     */
    public static final String COMPONENT_AUTHORIZED_ACCESS_TOKEN = "COMPONENT_AUTHORIZED_ACCESS_TOKEN:%s";

    /**
     * 第三方平台刷新公众号(授权方)的ACCESS_TOKEN refresh
     */
    public static final String COMPONENT_AUTHORIZED_REFRESH_TOKEN = "COMPONENT_AUTHORIZED_REFRESH_TOKEN:%s";

    /**
     * 第三方平台代公众号授权ACCESS_TOKEN
     */
    public static final String COMPONENT_APP_ACCESS_TOKEN = "COMPONENT_APP_ACCESS_TOKEN:%s:%s";

    /**
     * 第三方平台代公众号授权ACCESS_TOKEN
     */
    public static final String COMPONENT_APP_REFRESH_TOKEN = "COMPONENT_APP_REFRESH_TOKEN:%s:%s";

    /**
     * 第三方平台代公众号授权令牌STATE标志
     */
    public static final String COMPONENT_APP_STATE = "COMPONENT_%s_STATE";

    /**
     * 第三方平台预授权码
     */
    public static final String COMPONENT_PRE_AUTH_CODE = "COMPONENT_PRE_AUTH_CODE";

    /**
     * 第三方平台授权码
     */
    public static final String COMPONENT_AUTHORIZATION_CODE = "COMPONENT_AUTHORIZATION_CODE";

    /**
     * 微信公众号令牌
     */
    public static final String WX_APP_ACCESS_TOKEN = "WX_APP_ACCESS_TOKEN:%s";

    /**
     * 网站应用获取用户信息
     */
    public static final String WECHAT_WEB_USER = "WECHAT_WEB_USER_%s";

}
