package org.igetwell.system.bean;

import lombok.Data;

@Data
public class SystemOauthClientDetailsBean {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 授权范围
     */
    private String scope;

    /**
     * 授权类型
     */
    private String authorizedGrantTypes;

    /**
     * 回调地址
     */
    private String webServerRedirectUri;

    /**
     * 令牌过期秒数
     */
    private Integer accessTokenValidity;

    /**
     * 刷新令牌过期秒数
     */
    private Integer refreshTokenValidity;

    /**
     * 自动授权
     */
    private String autoapprove;

    /**
     * 所属租户
     */
    private String tenantId;

    /**
     * 租户名
     */
    private String tenantName;

}
