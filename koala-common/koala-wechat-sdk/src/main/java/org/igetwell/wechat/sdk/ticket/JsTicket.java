package org.igetwell.wechat.sdk.ticket;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

@Getter
@Setter
public class JsTicket {

    private String ticket;

    private Long expiresIn;
}
