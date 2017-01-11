/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dto;

/**
 *
 * @author Android-3
 */
public class SizeColorMapDto {

    private Long colorId;
    private String colorText;
    private Long mapId;
    private int count;
    private Double orginalPrice;
    private Double salesPrice;
    private Double discount;

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

    /**
     * @return the colorText
     */
    public String getColorText() {
        return colorText;
    }

    /**
     * @param colorText the colorText to set
     */
    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    /**
     * @return the mapId
     */
    public Long getMapId() {
        return mapId;
    }

    /**
     * @param mapId the mapId to set
     */
    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the orginalPrice
     */
    public Double getOrginalPrice() {
        return orginalPrice;
    }

    /**
     * @param orginalPrice the orginalPrice to set
     */
    public void setOrginalPrice(Double orginalPrice) {
        this.orginalPrice = orginalPrice;
    }

    /**
     * @return the salesPrice
     */
    public Double getSalesPrice() {
        return salesPrice;
    }

    /**
     * @param salesPrice the salesPrice to set
     */
    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    /**
     * @return the discount
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
