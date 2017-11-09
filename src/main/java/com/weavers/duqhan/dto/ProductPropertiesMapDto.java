/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;

/**
 *
 * @author weaversAndroid
 */
public class ProductPropertiesMapDto {
    private Long mapId;
    private long productId;
    private String propertyvalueComposition;
    private double discount;
    private double orginalPrice;
    private double salesPrice;
    private long quantity;

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
     * @return the productId
     */
    public long getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(long productId) {
        this.productId = productId;
    }

    /**
     * @return the propertyvalueComposition
     */
    public String getPropertyvalueComposition() {
        return propertyvalueComposition;
    }

    /**
     * @param propertyvalueComposition the propertyvalueComposition to set
     */
    public void setPropertyvalueComposition(String propertyvalueComposition) {
        this.propertyvalueComposition = propertyvalueComposition;
    }

    /**
     * @return the discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * @return the quantity
     */
    public long getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the orginalPrice
     */
    public double getOrginalPrice() {
        return orginalPrice;
    }

    /**
     * @param orginalPrice the orginalPrice to set
     */
    public void setOrginalPrice(double orginalPrice) {
        this.orginalPrice = orginalPrice;
    }

    /**
     * @return the salesPrice
     */
    public double getSalesPrice() {
        return salesPrice;
    }

    /**
     * @param salesPrice the salesPrice to set
     */
    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }
}
