package org.igetwell.merchant.card.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class MerchantCardExpand implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 商户会员卡ID
     */
    private String merchantCardId;

    /**
     * 商户编号
     */
    private String merchantNo;

    /**
     * 开卡方式 1直接开卡 2预存开卡 3付费开卡
     */
    private Integer openMethod;

    /**
     * 开卡金额：默认0.00  开放方式是直接开卡，此字段无效
     */
    private BigDecimal openMoney;

    /**
     * 开卡属性 1姓名 2手机号 3兴趣 4生日 5性别 6邮箱
     */
    private String openAttr;

    /**
     * 会员卡提示：结账时出示会员卡
     */
    private String cardNotice;

    /**
     * 会员卡使用说明：会员卡须先领卡再使用
     */
    private String cardDescription;

    /**
     * 会员卡特权说明
     */
    private String cardPrivilege;

    /**
     * 有效期：1永久有效 DATE_TYPE_PERMANENT  2固定范围有效DATE_TYPE_FIX_TIME_RANGE 3自领取日 DATE_TYPE_FIX_TERM (单位为天当天有效输入0)
     */
    private Integer cardValidType;

    /**
     * 有效日期开始时间  永久有效不填 2018-01-01
     */
    private Integer cardBegin;

    /**
     * 有效日期结束时间  永久有效不填 2018-12-31
     */
    private Integer cardEnd;

    /**
     * 可用时段 例：周一至周五 全天 限制类型枚举值：支持填入 MONDAY 周一 TUESDAY 周二 WEDNESDAY 周三 THURSDAY 周四 FRIDAY 周五 SATURDAY 周六 SUNDAY 周日
     */
    private Integer cardLimitTime;

    /**
     * 可用时段：开始时间10：00
     */
    private String cardLimitBegin;

    /**
     * 可用时段：结束时间23：59
     */
    private String cardLimitEnd;

    /**
     * 激活类型： 1自动激活 AUTO_ACTIVATE 2一键激活 WX_ACTIVATE
     */
    private Integer activateType;

    /**
     * 激活地址
     */
    private String activateUrl;

    /**
     * 是否显示积分：true或false
     */
    private Integer displayPoint;

    /**
     * 设置跳转外链查看积分详情地址
     */
    private String cardPointUrl;

    /**
     * 是否支持储值：true或false
     */
    private Integer cardSupportBalance;

    /**
     * 设置跳转外链查看余额详情地址
     */
    private String cardBalanceUrl;

    /**
     * 栏位展示  1积分 POINT  2余额 BALANCE  3优惠券 COUPON 4等级 LEVEL (只能有三项)
     */
    private String cardDisplayField;

    /**
     * 条码展示：1条形码(CODE_TYPE_BARCODE) 2二维码(CODE_TYPE_QRCODE) 3文本(CODE_TYPE_TEXT)
     */
    private Integer cardDisplayBar;

    /**
     * 中心按钮类型：1会员支付 2微信支付 3自定义 4不展示
     */
    private Integer cardCenterType;

    /**
     * 中心按钮文案：会员支付 微信支付 自定义支付
     */
    private String cardCenterButton;

    /**
     * 中心按钮提示： 点击生成付款码
     */
    private String cardCenterTips;

    /**
     * 每人可领券的数量限制，建议会员卡每人限领一张
     */
    private Integer cardLimit;

    /**
     * 是否支持所有门店：0否 1是
     */
    private boolean isSupportStore;

    /**
     * 适用门店ID
     */
    private String supportStores;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantCardId() {
        return merchantCardId;
    }

    public void setMerchantCardId(String merchantCardId) {
        this.merchantCardId = merchantCardId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Integer getOpenMethod() {
        return openMethod;
    }

    public void setOpenMethod(Integer openMethod) {
        this.openMethod = openMethod;
    }

    public BigDecimal getOpenMoney() {
        return openMoney;
    }

    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }

    public String getOpenAttr() {
        return openAttr;
    }

    public void setOpenAttr(String openAttr) {
        this.openAttr = openAttr;
    }

    public String getCardNotice() {
        return cardNotice;
    }

    public void setCardNotice(String cardNotice) {
        this.cardNotice = cardNotice;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getCardPrivilege() {
        return cardPrivilege;
    }

    public void setCardPrivilege(String cardPrivilege) {
        this.cardPrivilege = cardPrivilege;
    }

    public Integer getCardValidType() {
        return cardValidType;
    }

    public void setCardValidType(Integer cardValidType) {
        this.cardValidType = cardValidType;
    }

    public Integer getCardBegin() {
        return cardBegin;
    }

    public void setCardBegin(Integer cardBegin) {
        this.cardBegin = cardBegin;
    }

    public Integer getCardEnd() {
        return cardEnd;
    }

    public void setCardEnd(Integer cardEnd) {
        this.cardEnd = cardEnd;
    }

    public Integer getCardLimitTime() {
        return cardLimitTime;
    }

    public void setCardLimitTime(Integer cardLimitTime) {
        this.cardLimitTime = cardLimitTime;
    }

    public String getCardLimitBegin() {
        return cardLimitBegin;
    }

    public void setCardLimitBegin(String cardLimitBegin) {
        this.cardLimitBegin = cardLimitBegin;
    }

    public String getCardLimitEnd() {
        return cardLimitEnd;
    }

    public void setCardLimitEnd(String cardLimitEnd) {
        this.cardLimitEnd = cardLimitEnd;
    }

    public Integer getActivateType() {
        return activateType;
    }

    public void setActivateType(Integer activateType) {
        this.activateType = activateType;
    }

    public String getActivateUrl() {
        return activateUrl;
    }

    public void setActivateUrl(String activateUrl) {
        this.activateUrl = activateUrl;
    }

    public Integer getDisplayPoint() {
        return displayPoint;
    }

    public void setDisplayPoint(Integer displayPoint) {
        this.displayPoint = displayPoint;
    }

    public String getCardPointUrl() {
        return cardPointUrl;
    }

    public void setCardPointUrl(String cardPointUrl) {
        this.cardPointUrl = cardPointUrl;
    }

    public Integer getCardSupportBalance() {
        return cardSupportBalance;
    }

    public void setCardSupportBalance(Integer cardSupportBalance) {
        this.cardSupportBalance = cardSupportBalance;
    }

    public String getCardBalanceUrl() {
        return cardBalanceUrl;
    }

    public void setCardBalanceUrl(String cardBalanceUrl) {
        this.cardBalanceUrl = cardBalanceUrl;
    }

    public String getCardDisplayField() {
        return cardDisplayField;
    }

    public void setCardDisplayField(String cardDisplayField) {
        this.cardDisplayField = cardDisplayField;
    }

    public Integer getCardDisplayBar() {
        return cardDisplayBar;
    }

    public void setCardDisplayBar(Integer cardDisplayBar) {
        this.cardDisplayBar = cardDisplayBar;
    }

    public Integer getCardCenterType() {
        return cardCenterType;
    }

    public void setCardCenterType(Integer cardCenterType) {
        this.cardCenterType = cardCenterType;
    }

    public String getCardCenterButton() {
        return cardCenterButton;
    }

    public void setCardCenterButton(String cardCenterButton) {
        this.cardCenterButton = cardCenterButton;
    }

    public String getCardCenterTips() {
        return cardCenterTips;
    }

    public void setCardCenterTips(String cardCenterTips) {
        this.cardCenterTips = cardCenterTips;
    }

    public Integer getCardLimit() {
        return cardLimit;
    }

    public void setCardLimit(Integer cardLimit) {
        this.cardLimit = cardLimit;
    }

    public String  getSupportStores() {
        return supportStores;
    }

    public void setSupportStores(String supportStores) {
        this.supportStores = supportStores;
    }

    public boolean isSupportStore() {
        return isSupportStore;
    }

    public void setSupportStore(boolean supportStore) {
        isSupportStore = supportStore;
    }
}