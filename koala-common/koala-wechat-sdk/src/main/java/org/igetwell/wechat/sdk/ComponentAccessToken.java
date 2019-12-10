package org.igetwell.wechat.sdk;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 获取第三方平台access_token
 */
@Getter
@Setter
public class ComponentAccessToken implements Serializable {

    /**
     * 第三方平台access_token
     */
    private String componentAccessToken;


    /**
     * 有效期
     */
    private Long expiresIn;
}
