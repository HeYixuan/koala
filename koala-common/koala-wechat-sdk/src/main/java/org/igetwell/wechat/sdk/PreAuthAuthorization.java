package org.igetwell.wechat.sdk;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 获取第三方平台预授权码
 */
@Getter
@Setter
public class PreAuthAuthorization implements Serializable {

    /**
     * 第三方平台预授权码
     */
    private String preAuthCode;

    /**
     * 有效期
     */
    private Long expiresIn;
}
