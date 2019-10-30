package org.igetwell.wechat.sdk.bean.media;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

@Getter
@Setter
public class Media extends BaseResponse {

    private String type;
    private String mediaId;
    private Integer createdAt;
    private String url;
}
