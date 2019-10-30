package org.igetwell.wechat.sdk.bean.card.qrcode;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.response.BaseResponse;

/**
 * 卡券投放－创建二维码（ticket）－响应参数
 */
@Getter
@Setter
public class QrCodeCreateResponse extends BaseResponse {

    /**
     * 获取的二维码ticket，凭借此ticket调用通过ticket换取二维码接口可以在有效时间内换取二维码。
     */
    private String ticket;

    /**
     * 二维码的有效时间，范围是60 ~ 1800秒。不填默认为365天有效
     */
    private Integer expireSeconds;

    /**
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    private String url;

    /**
     * 二维码显示地址，点击后跳转二维码页面
     */
    private String showQrcodeUrl;
}
