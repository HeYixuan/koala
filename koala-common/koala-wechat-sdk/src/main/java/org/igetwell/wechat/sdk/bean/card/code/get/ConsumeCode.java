package org.igetwell.wechat.sdk.bean.card.code.get;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券核销－查询Code接口－请求参数
 */
@Getter
@Setter
public class ConsumeCode {

    /**
     * 卡券ID代表一类卡券。自定义code卡券必填。
     */
    private String cardId;

    /**
     * 单张卡券的唯一标准。<br>
     * 必填：是
     */
    private String code;

    /**
     * 是否校验code核销状态，填入true和false时的code异常状态返回数据不同。<br>
     * 必填：否
     */
    private Boolean checkConsume;
}
