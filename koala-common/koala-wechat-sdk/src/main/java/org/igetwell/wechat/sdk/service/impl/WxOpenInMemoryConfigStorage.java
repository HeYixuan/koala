package org.igetwell.wechat.sdk.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.ComponentAccessToken;
import org.igetwell.wechat.sdk.ComponentAuthorization;
import org.igetwell.wechat.sdk.service.IWxOpenConfigStorage;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class WxOpenInMemoryConfigStorage implements IWxOpenConfigStorage {
    /**
     * 开放平台token
     */
    private String componentAccessToken;
    private long componentExpiresTime;

    /**
     * 授权信息
     */
    private String authorizerAppid;
    private String authorizerAccessToken;
    private long expiresIn;
    private String authorizerRefreshToken;

    @Override
    public void updateComponentAccessToken(ComponentAccessToken componentAccessToken) {
        updateComponentAccessToken(componentAccessToken.getComponentAccessToken(), componentAccessToken.getExpiresIn());
    }

    @Override
    public void updateComponentAccessToken(String componentAccessToken, long expiresInSeconds) {
        this.componentAccessToken = componentAccessToken;
        this.componentExpiresTime = (expiresInSeconds - 10);
        //this.componentExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
    }

    @Override
    public void updateComponentAuthorization(ComponentAuthorization authorization) {
        this.authorizerAppid = authorization.getAuthorizerAppid();
        this.authorizerAccessToken = authorization.getAuthorizerAccessToken();
        this.expiresIn = (authorization.getExpiresIn() - 10);
        this.authorizerRefreshToken = authorization.getAuthorizerRefreshToken();
    }
}
