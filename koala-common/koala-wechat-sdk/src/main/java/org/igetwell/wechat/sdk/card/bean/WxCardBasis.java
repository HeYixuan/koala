package org.igetwell.wechat.sdk.card.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券基础信息
 *
 */

@Getter
@Setter
public class WxCardBasis {

    /**
     * 卡券的商户logo，建议像素为300*300。<br>
     * 添加必填
     */
    private String logoUrl;

    /**
     * 商户名字,字数上限为12个汉字。<br>
     * 添加必填，不支持修改
     */
    private String brandName;

    /**
     *
     * "CODE_TYPE_TEXT"，文本；<br>
     * "CODE_TYPE_BARCODE"，一维码； <br>
     * "CODE_TYPE_QRCODE"，二维码；<br>
     * "CODE_TYPE_ONLY_QRCODE"，二维码无code显示；<br>
     * "CODE_TYPE_ONLY_BARCODE"，一维码无code显示；<br>
     * "CODE_TYPE_NONE"，不显示code和条形码类型<br>
     * 添加必填
     */
    private String codeType;

    /**
     * 卡券名，字数上限为9个汉字。(建议涵盖卡券属性、服务及金额)。<br>
     * 添加必填
     */
    private String title;

    /**
     * 券名，字数上限为18个汉字。
     */
    private String subTitle;

    /**
     * 券颜色。按色彩规范标注填写Color010-Color100<br>
     * https://developers.weixin.qq.com/doc/offiaccount/Cards_and_Offer/Create_a_Coupon_Voucher_or_Card.html
     * 添加必填
     */
    private String color;

    /**
     * 卡券使用提醒，字数上限为16个汉字。<br>
     * 添加必填
     */
    private String notice;

    /**
     * 客服电话。
     */
    private String servicePhone;

    /**
     * 卡券使用说明，字数上限为1024个汉字。<br>
     * 添加必填
     */
    private String description;

    /**
     * 使用日期，有效期的信息。<br>
     * 添加必填
     */
    private WxCardDate dateInfo;

    /**
     * 商品信息。<br>
     * 添加必填，不支持修改
     */
    private WxCardSku sku;

    /**
     * 每人可领券的数量限制,不填写默认为50。
     */
    private Integer getLimit;

    /**
     * 每人可核销的数量限制,不填写默认为50。
     */
    private Integer useLimit;

    /**
     * 是否自定义Code码。 填写true或false，默认为false。 <br>
     * 通常自有优惠码系统的开发者选择自定义Code码，并在卡券投放时带入 <br>
     * 不支持修改
     */
    private Boolean useCustomCode;

    /**
     * 填入 GET_CUSTOM_CODE_MODE_DEPOSIT 表示该卡券为预存code模式卡券， 须导入超过库存数目的自定义code后方可投放， 填入该字段后，quantity字段须为0,须导入code 后再增加库存
     */
    private String getCustomCodeMode;

    /**
     * 是否指定用户领取，填写true或false。默认为false。 <br>
     * 通常指定特殊用户群体投放卡券或防止刷券时选择指定用户领取。<br>
     * 不支持修改
     */
    private Boolean bindOpenid;

    /**
     * 卡券领取页面是否可分享。
     */
    private Boolean canShare;

    /**
     * 卡券是否可转赠。
     */
    private Boolean canGiveFriend;

    /**
     * 门店位置poiid。<br>
     * 调用POI门店管理接口获取门店位置poiid。<br>
     * 具备线下门店的商户为必填。<br>
     */
    private Integer[] locationIdList;

    /**
     * 卡券顶部居中的按钮，如“立即使用”，仅在卡券状态正常(可以核销)时显示
     */
    private String centerTitle;

    /**
     * 显示在入口下方的提示语，如“立即享受优惠”，仅在卡券状态正常(可以核销)时显示。
     */
    private String centerSubTitle;

    /**
     * 顶部居中的url，仅在卡券状态正常(可以核销)时显示。
     */
    private String centerUrl;

    /**
     * 自定义跳转外链的入口名字， 如“立即使用”。详情见活用自定义入口
     */
    private String customUrlName;

    /**
     * 自定义跳转的URL。
     */
    private String customUrl;

    /**
     * 显示在入口右侧的提示语。如“更多惊喜”。
     */
    private String customUrlSubTitle;

    /**
     * 营销场景的自定义入口名称。如，“产品介绍”。
     */
    private String promotionUrlName;

    /**
     * 入口跳转外链的地址链接。
     */
    private String promotionUrl;

    /**
     * 显示在营销入口右侧的提示语。如，“卖场大优惠。”。
     */
    private String promotionUrlSubTitle;

    /**
     * 第三方来源名，例如同程旅游、大众点评。<br>
     * 不支持修改
     */
    private String source;

    /**
     * 会员卡是否支持全部门店，填写后商户门店更新时会自动同步至卡券
     */
    private Boolean useAllLocations;

    /**
     * 填写true为用户点击进入会员卡时推送事件，默认为false。详情见 进入会员卡事件推送
     */
    private Boolean needPushOnView;

    /**
     * 是否设置该会员卡中部的按钮同时支持微信支付刷卡和会员卡二维码
     */
    private Boolean isPayAndQrcode;


}
