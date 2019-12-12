package org.igetwell.wechat.sdk.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Card implements Serializable {

    /**
     * 卡券ID
     */
    private String cardId;
}
