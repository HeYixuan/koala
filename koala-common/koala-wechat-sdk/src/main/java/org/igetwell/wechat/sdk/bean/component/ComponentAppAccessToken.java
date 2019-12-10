package org.igetwell.wechat.sdk.bean.component;

import java.io.Serializable;

/**
 * 第三方开放平台代公众号授权令牌
 */
public class ComponentAppAccessToken implements Serializable {

    /**
     * 接口调用凭证
     */
    private String accessToken;

    /**
     * 接口调用凭证超时时间，单位（秒）
     */
    private Long expiresIn;

    /**
     * 用户刷新access_token
     */
    private String refreshToken;

    /**
     * 授权用户唯一标识
     */
    private String openid;

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
