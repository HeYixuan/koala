package org.igetwell.wechat.sdk.bean.card.code.get;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

/**
 * 卡券核销－查询Code接口－响应参数
 */
@Getter
@Setter
public class ConsumeCodeResponse extends BaseResponse {

    /**
     * 卡券信息
     */
    private ConsumeCodeCard card;

    /**
     * 用户openid
     */
    private String openid;

    /**
     * 是否可以核销，true为可以核销，false为不可核销
     */
    private Boolean canConsume;

    /**
     * 当前code对应卡券的状态: <br>
     * NORMAL 正常 <br>
     * CONSUMED 已核销 <br>
     * EXPIRE 已过期 <br>
     * GIFTING 转赠中<br>
     * GIFT_TIMEOUT 转赠超时<br>
     * DELETE 已删除<br>
     * UNAVAILABLE 已失效 <br>
     * code未被添加或被转赠领取的情况则统一报错：invalid serial code
     */
    private String userCardStatus;

}
