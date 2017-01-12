/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dto;

import java.util.Date;

/**
 *
 * @author Android-3
 */
public class ProductBean {
    private Long productId;
    private String name;
    private Long categoryId;
    private String categoryName;
    private String description;
    private String imgurl;
    private Date lastUpdate;
    private Double price;
    private Double discountedPrice;
    private Double discountPCT;
    private Long sizeId;
    private String size;
    private String sizeUnit;
    private Long colorId;
    private String color;
    private int available;

    /**
     * @return the productId
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the categoryId
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the imgurl
     */
    public String getImgurl() {
        return imgurl;
    }

    /**
     * @param imgurl the imgurl to set
     */
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    /**
     * @return the lastUpdate
     */
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the discountedPrice
     */
    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    /**
     * @param discountedPrice the discountedPrice to set
     */
    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    /**
     * @return the discountPCT
     */
    public Double getDiscountPCT() {
        return discountPCT;
    }

    /**
     * @param discountPCT the discountPCT to set
     */
    public void setDiscountPCT(Double discountPCT) {
        this.discountPCT = discountPCT;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the sizeUnit
     */
    public String getSizeUnit() {
        return sizeUnit;
    }

    /**
     * @param sizeUnit the sizeUnit to set
     */
    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the available
     */
    public int getAvailable() {
        return available;
    }

    /**
     * @param available the available to set
     */
    public void setAvailable(int available) {
        this.available = available;
    }

    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName the categoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the sizeId
     */
    public Long getSizeId() {
        return sizeId;
    }

    /**
     * @param sizeId the sizeId to set
     */
    public void setSizeId(Long sizeId) {
        this.sizeId = sizeId;
    }

    /**
     * @return the colorId
     */
    public Long getColorId() {
        return colorId;
    }

    /**
     * @param colorId the colorId to set
     */
    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }
    
}
