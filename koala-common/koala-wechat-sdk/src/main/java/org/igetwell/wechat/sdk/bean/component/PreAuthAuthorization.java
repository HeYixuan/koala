package org.igetwell.wechat.sdk.bean.component;

import java.io.Serializable;

/**
 * 获取第三方平台预授权码
 */
public class PreAuthAuthorization implements Serializable {

    /**
     * 第三方平台预授权码
     */
    private String preAuthCode;

    /**
     * 有效期
     */
    private Long expiresIn;

    public String getPreAuthCode() {
        return preAuthCode;
    }

    public void setPreAuthCode(String preAuthCode) {
        this.preAuthCode = preAuthCode;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
