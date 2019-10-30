package org.igetwell.wechat.sdk.ticket;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

@Getter
@Setter
public class JsTicket extends BaseResponse {

    private String ticket;

    private int expiresIn = -1;
}
