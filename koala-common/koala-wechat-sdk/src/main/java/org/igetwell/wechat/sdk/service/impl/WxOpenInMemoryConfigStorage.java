package org.igetwell.wechat.sdk.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.WxOpenComponentAccessToken;
import org.igetwell.wechat.sdk.service.IWxOpenConfigStorage;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class WxOpenInMemoryConfigStorage implements IWxOpenConfigStorage {

    private String componentAppId;
    private String componentAppSecret;
    private String componentToken;
    private String componentAesKey;
    private String componentVerifyTicket;
    private String componentAccessToken;
    private long componentExpiresTime;

    @Override
    public void updateComponentAccessToken(WxOpenComponentAccessToken componentAccessToken) {
        updateComponentAccessToken(componentAccessToken.getComponentAccessToken(), componentAccessToken.getExpiresIn());
    }

    @Override
    public void updateComponentAccessToken(String componentAccessToken, long expiresInSeconds) {
        this.componentAccessToken = componentAccessToken;
        this.componentExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
    }
}
