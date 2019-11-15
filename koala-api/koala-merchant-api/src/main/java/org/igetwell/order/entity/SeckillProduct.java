package org.igetwell.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SeckillProduct implements Serializable {
    private Integer id;

    /**
     * 商品id
     */
    private String prodId;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 商品状态,0-上架，1-下架
     */
    private Integer prodStatus;

    /**
     * 商品库存
     */
    private Integer prodStock;

    /**
     * 商品售价
     */
    private BigDecimal prodPrice;

    private Date createTime;

    private Date updateTime;

    /**
     * 更新版本号
     */
    private Integer version;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Integer getProdStatus() {
        return prodStatus;
    }

    public void setProdStatus(Integer prodStatus) {
        this.prodStatus = prodStatus;
    }

    public Integer getProdStock() {
        return prodStock;
    }

    public void setProdStock(Integer prodStock) {
        this.prodStock = prodStock;
    }

    public BigDecimal getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(BigDecimal prodPrice) {
        this.prodPrice = prodPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}