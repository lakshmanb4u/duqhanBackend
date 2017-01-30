/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dto;

import java.util.List;

/**
 *
 * @author Android-3
 */
public class CartBean {

    private Double itemTotal;
    private Double discountTotal;
    private Double discountPctTotal;
    private Double orderTotal;
    private int totalProducts;
    private String statusCode;
    private String status;
    private Long deliveryAddressId;
    private List<ProductBean> products;
    private Long userId;
    private AddressDto addressDto;

    /**
     * @return the itemTotal
     */
    public Double getItemTotal() {
        return itemTotal;
    }

    /**
     * @param itemTotal the itemTotal to set
     */
    public void setItemTotal(Double itemTotal) {
        this.itemTotal = itemTotal;
    }

    /**
     * @return the discountTotal
     */
    public Double getDiscountTotal() {
        return discountTotal;
    }

    /**
     * @param discountTotal the discountTotal to set
     */
    public void setDiscountTotal(Double discountTotal) {
        this.discountTotal = discountTotal;
    }

    /**
     * @return the discountPctTotal
     */
    public Double getDiscountPctTotal() {
        return discountPctTotal;
    }

    /**
     * @param discountPctTotal the discountPctTotal to set
     */
    public void setDiscountPctTotal(Double discountPctTotal) {
        this.discountPctTotal = discountPctTotal;
    }

    /**
     * @return the orderTotal
     */
    public Double getOrderTotal() {
        return orderTotal;
    }

    /**
     * @param orderTotal the orderTotal to set
     */
    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    /**
     * @return the totalProducts
     */
    public int getTotalProducts() {
        return totalProducts;
    }

    /**
     * @param totalProducts the totalProducts to set
     */
    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the products
     */
    public List<ProductBean> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the deliveryAddressId
     */
    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    /**
     * @param deliveryAddressId the deliveryAddressId to set
     */
    public void setDeliveryAddressId(Long deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    /**
     * @return the addressDto
     */
    public AddressDto getAddressDto() {
        return addressDto;
    }

    /**
     * @param addressDto the addressDto to set
     */
    public void setAddressDto(AddressDto addressDto) {
        this.addressDto = addressDto;
    }
}
