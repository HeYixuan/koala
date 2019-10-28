package org.igetwell.wechat.sdk.card.code.decrypt;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

@Getter
@Setter
public class CodeDecryptResponse extends BaseResponse {

    /**
     * 解密后获取的真实Code码
     */
    private String code;
}
