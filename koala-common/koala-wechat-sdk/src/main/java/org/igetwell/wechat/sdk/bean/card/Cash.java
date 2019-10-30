package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 代金券
 */
@Getter
@Setter
public class Cash extends AbstractInfo {

    /**
     * 代金券专用，表示起用金额（单位为分）,如果无起用门槛则填0
     * 添加必填，不支持修改
     */
    private Integer leastCost;

    /**
     * 代金券专用，表示减免金额。（单位为分）
     * 添加必填，不支持修改
     */
    private Integer reduceCost;
}
