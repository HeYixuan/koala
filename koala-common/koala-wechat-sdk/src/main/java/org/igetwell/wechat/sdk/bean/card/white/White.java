package org.igetwell.wechat.sdk.bean.card.white;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class White {

    /**
     * 测试的openid列表
     */
    private List<String> openid;

    /**
     * 测试的微信号列表
     */
    private List<String> username;
}
