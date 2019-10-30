package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义会员信息类目
 */

@Getter
@Setter
public class WxCardCustomerCell {

    /**
     * 入口名称
     */
    private String name;

    /**
     * 入口右侧提示语，6个汉字内
     */
    private String tips;

    /**
     * 入口跳转链接
     */
    private String url;
}
