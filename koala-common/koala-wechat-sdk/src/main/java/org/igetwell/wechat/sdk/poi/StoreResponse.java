package org.igetwell.wechat.sdk.poi;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

/**
 * 创建门店-返回体
 */
@Getter
@Setter
public class StoreResponse extends BaseResponse {
    private String poiId;
}
