package org.igetwell.wechat.sdk.poi;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

/**
 * 查询门店信息-返回提
 */
@Getter
@Setter
public class StoreInfoResponse extends BaseResponse {
    private Business business;
}
