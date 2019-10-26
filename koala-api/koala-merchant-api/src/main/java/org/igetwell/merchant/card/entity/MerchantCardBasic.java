package org.igetwell.merchant.card.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class MerchantCardBasic implements Serializable {
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
     * 开卡方式 1直接开卡 2预存开卡  3付费开卡
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
    private boolean supplyBonus;

    /**
     * 设置跳转外链查看积分详情地址
     */
    private String bonusUrl;

    /**
     * 是否支持储值：true或false
     */
    private boolean supplyBalance;

    /**
     * 设置跳转外链查看余额详情地址
     */
    private String balanceUrl;

    /**
     * 栏位展示  1积分 POINT  2余额 BALANCE  3优惠券 COUPON 4等级 LEVEL (只能有三项)
     */
    private String displayField;

    /**
     * 条码展示：1条形码(CODE_TYPE_BARCODE) 2二维码(CODE_TYPE_QRCODE) 3文本(CODE_TYPE_TEXT)
     */
    private Integer barType;

    /**
     * 中心按钮类型：1会员支付 2微信支付 3自定义 4不展示
     */
    private Integer centerType;

    /**
     * 中心按钮文案：会员支付 微信支付 自定义支付
     */
    private String centerText;

    /**
     * 中心按钮提示： 点击生成付款码
     */
    private String centerSubText;

    /**
     * 有效期：1永久有效 DATE_TYPE_PERMANENT  2固定范围有效DATE_TYPE_FIX_TIME_RANGE 3自领取日 DATE_TYPE_FIX_TERM (单位为天当天有效输入0)
     */
    private Integer validType;

    /**
     * 有效日期开始时间  永久有效不填 2018-01-01
     */
    private Integer beginTime;

    /**
     * 有效日期结束时间  永久有效不填 2018-12-31
     */
    private Integer endTime;

    /**
     * 可用时段 例：周一至周五 全天 限制类型枚举值：支持填入 MONDAY 周一 TUESDAY 周二 WEDNESDAY 周三 THURSDAY 周四 FRIDAY 周五 SATURDAY 周六 SUNDAY 周日
     */
    private Integer limitTime;

    /**
     * 可用时段：开始时间10：00
     */
    private String limitBegin;

    /**
     * 可用时段：结束时间23：59
     */
    private String limitEnd;

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

    private static final long serialVersionUID = 1L;

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

    public boolean getSupplyBonus() {
        return supplyBonus;
    }

    public void setSupplyBonus(boolean supplyBonus) {
        this.supplyBonus = supplyBonus;
    }

    public String getBonusUrl() {
        return bonusUrl;
    }

    public void setBonusUrl(String bonusUrl) {
        this.bonusUrl = bonusUrl;
    }

    public boolean getSupplyBalance() {
        return supplyBalance;
    }

    public void setSupplyBalance(boolean supplyBalance) {
        this.supplyBalance = supplyBalance;
    }

    public String getBalanceUrl() {
        return balanceUrl;
    }

    public void setBalanceUrl(String balanceUrl) {
        this.balanceUrl = balanceUrl;
    }

    public String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    public Integer getBarType() {
        return barType;
    }

    public void setBarType(Integer barType) {
        this.barType = barType;
    }

    public Integer getCenterType() {
        return centerType;
    }

    public void setCenterType(Integer centerType) {
        this.centerType = centerType;
    }

    public String getCenterText() {
        return centerText;
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
    }

    public String getCenterSubText() {
        return centerSubText;
    }

    public void setCenterSubText(String centerSubText) {
        this.centerSubText = centerSubText;
    }

    public Integer getValidType() {
        return validType;
    }

    public void setValidType(Integer validType) {
        this.validType = validType;
    }

    public Integer getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Integer beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(Integer limitTime) {
        this.limitTime = limitTime;
    }

    public String getLimitBegin() {
        return limitBegin;
    }

    public void setLimitBegin(String limitBegin) {
        this.limitBegin = limitBegin;
    }

    public String getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(String limitEnd) {
        this.limitEnd = limitEnd;
    }

    public Integer getCardLimit() {
        return cardLimit;
    }

    public void setCardLimit(Integer cardLimit) {
        this.cardLimit = cardLimit;
    }

    public boolean supportStore() {
        return isSupportStore;
    }

    public void setSupportStore(boolean isSupportStore) {
        this.isSupportStore = isSupportStore;
    }

    public String getSupportStores() {
        return supportStores;
    }

    public void setSupportStores(String supportStores) {
        this.supportStores = supportStores;
    }
}