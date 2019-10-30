package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 团购券
 */
@Getter
@Setter
public class Group extends AbstractInfo {
    /**
     * 团购券专用，团购详情
     * 添加必填，不支持修改
     */
    private String dealDetail;
}
