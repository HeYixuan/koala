package org.igetwell.wechat.sdk.card.code.decrypt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeDecrypt {

    /**
     * 经过加密的Code码。<br>
     * 必填：是
     */
    private String encryptCode;
}
