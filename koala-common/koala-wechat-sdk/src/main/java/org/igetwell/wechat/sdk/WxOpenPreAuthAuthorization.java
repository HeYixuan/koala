package org.igetwell.wechat.sdk;

import lombok.Getter;
import lombok.Setter;

/**
 * 获取第三方平台预授权码
 */
@Getter
@Setter
public class WxOpenPreAuthAuthorization {

    /**
     * 第三方平台预授权码
     */
    private String preAuthCode;

    /**
     * 有效期
     */
    private long expiresIn = -1;
}
