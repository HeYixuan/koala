package org.igetwell.wechat.sdk.bean.card.code.consume;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

/**
 * 卡券核销－核销Code接口－响应参数
 */
@Getter
@Setter
public class CodeConsumeResponse extends BaseResponse {

    private CodeConsumeCard card;

    /**
     * 用户在该公众号内的唯一身份标识。
     */
    private String openid;
}
