package org.igetwell.wechat.sdk.card.storage;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

/**
 * 卡券发放－创建货架接口－响应参数
 */
@Getter
@Setter
public class StorageCreateResponse extends BaseResponse {

    /**
     * 货架链接。
     */
    private String url;

    /**
     * 货架ID。货架的唯一标识。
     */
    private Integer pageId;
}
