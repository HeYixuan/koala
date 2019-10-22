package org.igetwell.merchant.card.entity;

import java.io.Serializable;
import java.util.Date;

public class MerchantCard implements Serializable {
    /**
     * 商户卡ID
     */
    private Long id;

    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * 商户编号
     */
    private String merchantNo;

    /**
     * 会员卡名称
     */
    private String cardName;

    /**
     * 会员卡副名称
     */
    private String cardSubName;

    /**
     * 会员卡LOGO
     */
    private String cardLogo;

    /**
     * 会员卡背景图
     */
    private String cardBackUrl;

    /**
     * 会员卡颜色
     */
    private String cardBackColor;

    /**
     * 会员卡状态：-1删除 0正常 1失效
     */
    private Integer cardStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardSubName() {
        return cardSubName;
    }

    public void setCardSubName(String cardSubName) {
        this.cardSubName = cardSubName;
    }

    public String getCardLogo() {
        return cardLogo;
    }

    public void setCardLogo(String cardLogo) {
        this.cardLogo = cardLogo;
    }

    public String getCardBackUrl() {
        return cardBackUrl;
    }

    public void setCardBackUrl(String cardBackUrl) {
        this.cardBackUrl = cardBackUrl;
    }

    public String getCardBackColor() {
        return cardBackColor;
    }

    public void setCardBackColor(String cardBackColor) {
        this.cardBackColor = cardBackColor;
    }

    public Integer getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}