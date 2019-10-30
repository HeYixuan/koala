package org.igetwell.wechat.sdk.bean.token;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

@Getter
@Setter
public class Token extends BaseResponse {

    private String accessToken;
    private int expiresIn;
}
