package org.igetwell.wechat.sdk.bean.component;

import java.io.Serializable;

public class ComponentRefreshAccessToken implements Serializable {

    /**
     * 授权方令牌
     */
    private String authorizerAccessToken;
    /**
     * 有效期
     */
    private Long expiresIn;

    /**
     * 刷新令牌
     */
    private String authorizerRefreshToken;

    public String getAuthorizerAccessToken() {
        return authorizerAccessToken;
    }

    public void setAuthorizerAccessToken(String authorizerAccessToken) {
        this.authorizerAccessToken = authorizerAccessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAuthorizerRefreshToken() {
        return authorizerRefreshToken;
    }

    public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
        this.authorizerRefreshToken = authorizerRefreshToken;
    }
}
