package org.igetwell.wechat.sdk.card.activate;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券激活设置字段-服务声明
 */
@Getter
@Setter
public class ServiceStatement {

    /**
     * 会员声明字段名称
     */
    private String name;

    /**
     * 自定义url 请填写http:// 或者https://开头的链接
     */
    private String url;
}
