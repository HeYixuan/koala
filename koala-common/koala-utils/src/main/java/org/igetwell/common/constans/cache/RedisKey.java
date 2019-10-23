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
     * 第三方平台使用授权码换取公众号的授权信息
     */
    public static final String COMPONENT_AUTHORIZATION = "COMPONENT_AUTHORIZATION";


    /**
     * 第三方平台授权码
     */
    public static final String COMPONENT_AUTHORIZATION_CODE = "COMPONENT_AUTHORIZATION_CODE";

    /**
     * 第三方平台预授权码
     */
    public static final String COMPONENT_PRE_AUTH_CODE = "COMPONENT_PRE_AUTH_CODE";

}
