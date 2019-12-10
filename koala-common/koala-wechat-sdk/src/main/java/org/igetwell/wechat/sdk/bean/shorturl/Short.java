package org.igetwell.wechat.sdk.bean.shorturl;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

@Getter
@Setter
public class Short extends BaseResponse {
    private String shortUrl;
}
