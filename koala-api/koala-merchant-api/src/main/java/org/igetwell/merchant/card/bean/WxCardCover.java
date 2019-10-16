package org.igetwell.merchant.card.bean;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * 封面摘要
 */
@Getter
@Setter
public class WxCardCover {

    /**
     * 封面摘要简介。<br>
     * 添加必填，长度：24
     */
    @SerializedName("abstract")
    private String abstractText;

    /**
     * 封面图片列表，仅支持填入一个封面图片链接，上传图片接口上传获取图片获得链接，<br>
     * 填写非CDN链接会报错，并在此填入。建议图片尺寸像素850*350 <br>
     * 添加必填，长度：128
     */
    private String iconUrlList;

}
