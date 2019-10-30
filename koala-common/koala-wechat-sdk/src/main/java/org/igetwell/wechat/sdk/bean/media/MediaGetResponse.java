package org.igetwell.wechat.sdk.bean.media;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

@Getter
@Setter
public class MediaGetResponse extends BaseResponse {

    private String filename;

    private String contentType;

    private byte[] bytes;

    private String video_url;	//如果返回的是视频消息素材
}
