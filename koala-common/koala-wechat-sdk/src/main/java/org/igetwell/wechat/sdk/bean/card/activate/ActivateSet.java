package org.igetwell.wechat.sdk.bean.card.activate;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券激活设置字段
 */
@Getter
@Setter
public class ActivateSet {

    /**
     * 卡券ID
     */
    private String cardId;

    /**
     * 会员卡激活时的必填选项
     */
    private RequiredForm requiredForm;

    /**
     * 服务声明
     */
    private ServiceStatement serviceStatement;
}
