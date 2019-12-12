package org.igetwell.wechat.sdk.bean.card.shelves;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 卡券发放－创建货架接口－请求参数
 */
@Getter
@Setter
public class Shelves{

    /**
     * 页面的banner图片链接，须调用，建议尺寸为640*300。<br>
     * 必填：是
     */
    private String banner;

    /**
     * 页面的title。<br>
     * 必填：是
     */
    private String pageTitle;

    /**
     * 页面是否可以分享,填入true/false<br>
     * 必填：是
     */
    private Boolean canShare;

    /**
     * 投放页面的场景值：<br>
     * SCENE_NEAR_BY 附近 <br>
     * SCENE_MENU 自定义菜单 <br>
     * SCENE_QRCODE 二维码 <br>
     * SCENE_ARTICLE 公众号文章 <br>
     * SCENE_H5 h5页面 <br>
     * SCENE_IVR 自动回复 <br>
     * SCENE_CARD_CUSTOM_CELL 卡券自定义cell<br>
     * 必填：是
     */
    private String scene;

    /**
     * 卡券列表<br>
     * 必填：是
     */
    private List<ShelvesCard> cardList;

}
