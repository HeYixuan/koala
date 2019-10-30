package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 折扣券
 */
@Getter
@Setter
public class Discount extends AbstractInfo {

    /**
     * 折扣券专用，表示打折额度（百分比）。填30就是七折
     * 添加必填，不支持修改
     */
    private Integer discount;
}
