package org.igetwell.system.order.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 统一下单支付接口
 */
public class ComponentPayRequest implements Serializable {

    /**
     * 商户ID
     */
    private Long mchId;

    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 支付渠道ID(微信或支付宝)
     */
    private Long channelId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 支付金额,单位为分
     */
    private BigDecimal fee;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getMchNo() {
        return mchNo;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
