/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.domain;

import java.lang.Long;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author weaversAndroid
 */
@Entity
@Table(name = "temp_product_size_color_map")
public class TempProductSizeColorMap extends BaseDomain {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "size_id")
    private Long sizeId;
    @Column(name = "color_id")
    private Long colorId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private double price;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "discount")
    private Double discount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private Long quantity;
    @Column(name = "product_length")
    private Double productLength;
    @Column(name = "product_width")
    private Double productWidth;
    @Column(name = "product_height")
    private Double productHeight;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_weight")
    private double productWeight;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSizeId() {
        return sizeId;
    }

    public void setSizeId(Long sizeId) {
        this.sizeId = sizeId;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getProductLength() {
        return productLength;
    }

    public void setProductLength(Double productLength) {
        this.productLength = productLength;
    }

    public Double getProductWidth() {
        return productWidth;
    }

    public void setProductWidth(Double productWidth) {
        this.productWidth = productWidth;
    }

    public Double getProductHeight() {
        return productHeight;
    }

    public void setProductHeight(Double productHeight) {
        this.productHeight = productHeight;
    }

    public double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(double productWeight) {
        this.productWeight = productWeight;
    }

}
