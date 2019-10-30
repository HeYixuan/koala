package org.igetwell.wechat.sdk.bean.qrcode;

import lombok.Getter;
import lombok.Setter;

/**
 * 二维码 ticket
 */
@Getter
@Setter
public class QrcodeTicket {

    private String ticket;
    private Integer expireSeconds;
    private String url;
}
