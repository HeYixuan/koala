package org.igetwell.wechat.sdk.bean.card.mpNews;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

@Getter
@Setter
public class MpNewsResponse extends BaseResponse {

    /**
     * 一段html代码，可以直接嵌入到图文消息的正文里。<br>
     * 即可以把这段代码嵌入到上传图文消息素材接口中的content字段里。
     */
    private String content;
}
