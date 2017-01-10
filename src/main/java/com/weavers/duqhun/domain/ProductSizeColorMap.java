/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.domain;

import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Android-3
 */
@Entity
@Table(name = "product_size_color_map")
public class ProductSizeColorMap extends BaseDomain {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_id")
    private long productId;
    @Column(name = "size_id")
    private BigInteger sizeId;
    @Column(name = "color_id")
    private BigInteger colorId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private double price;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "discount")
    private Double discount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quentity")
    private long quentity;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public BigInteger getSizeId() {
        return sizeId;
    }

    public void setSizeId(BigInteger sizeId) {
        this.sizeId = sizeId;
    }

    public BigInteger getColorId() {
        return colorId;
    }

    public void setColorId(BigInteger colorId) {
        this.colorId = colorId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public long getQuentity() {
        return quentity;
    }

    public void setQuentity(long quentity) {
        this.quentity = quentity;
    }
}
