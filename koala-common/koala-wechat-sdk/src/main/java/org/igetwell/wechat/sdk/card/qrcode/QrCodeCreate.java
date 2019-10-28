package org.igetwell.wechat.sdk.card.qrcode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QrCodeCreate {

    private String actionName;
    /**
     * 指定二维码的有效时间，范围是60 ~ 1800秒。不填默认为365天有效
     */
    private int expireSeconds;

    /**
     * 卡券领取类型：QR_CARD
     * @return 卡券领取类型
     */
    private ActionInfo actionInfo;

}
