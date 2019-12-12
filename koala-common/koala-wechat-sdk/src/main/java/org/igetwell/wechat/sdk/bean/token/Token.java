package org.igetwell.wechat.sdk.bean.token;

import java.io.Serializable;

public class Token implements Serializable {

    /**
     * 接口调用凭证
     */
    private String accessToken;

    /**
     * 接口调用凭证超时时间，单位（秒）
     */
    private Long expiresIn;

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
}
