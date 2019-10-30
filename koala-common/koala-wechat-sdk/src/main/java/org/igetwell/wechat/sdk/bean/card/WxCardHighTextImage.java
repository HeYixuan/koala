package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券高级信息，图文列表
 */
@Getter
@Setter
public class WxCardHighTextImage {

    /**
     * 图片链接，必须调用 上传图片接口
     * 上传图片获得链接，并在此填入， 否则报错
     */
    private String imageUrl;

    /**
     * 图文描述
     */
    private String text;
}
