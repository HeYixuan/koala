package org.igetwell.wechat.sdk.bean.card.code;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券核销－查询Code接口－响应参数－卡券信息
 */
@Getter
@Setter
public class CodeGetResponseCard {

    /**
     * 卡券ID
     */
    private String cardId;

    /**
     * 起始使用时间
     */
    private Integer beginTime;

    /**
     * 结束时间
     */
    private Integer endTime;
}
