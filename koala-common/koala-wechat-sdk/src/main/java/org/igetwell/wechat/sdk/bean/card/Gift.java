package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 兑换券
 */
@Getter
@Setter
public class Gift extends AbstractInfo {

    /**
     * 兑换券专用，填写兑换内容的名称
     * 添加必填，不支持修改
     */
    private String gift;
}
