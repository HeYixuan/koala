package org.igetwell.wechat.sdk.bean.component;

import java.io.Serializable;

/**
 * 获取第三方平台access_token
 */
public class ComponentAccessToken implements Serializable {

    /**
     * 第三方平台access_token
     */
    private String componentAccessToken;


    /**
     * 有效期
     */
    private Long expiresIn;

    public String getComponentAccessToken() {
        return componentAccessToken;
    }

    public void setComponentAccessToken(String componentAccessToken) {
        this.componentAccessToken = componentAccessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
