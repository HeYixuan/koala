package org.igetwell.wechat.sdk.card.code.consume;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券核销－核销Code接口－请求参数
 */
@Getter
@Setter
public class CodeConsume {

    /**
     * 卡券ID。创建卡券时use_custom_code填写true时必填。非自定义Code不必填写。
     */
    private String cardId;

    /**
     * 需核销的Code码。<br>
     * 必填：是
     */
    private String code;

}
