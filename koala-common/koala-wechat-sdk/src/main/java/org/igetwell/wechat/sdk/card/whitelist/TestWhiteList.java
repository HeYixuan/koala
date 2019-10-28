package org.igetwell.wechat.sdk.card.whitelist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestWhiteList {

    /**
     * 测试的openid列表
     */
    private String[] openid;

    /**
     * 测试的微信号列表
     */
    private String[] username;
}
