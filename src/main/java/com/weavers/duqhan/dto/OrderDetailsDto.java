/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;

import java.util.HashMap;

/**
 *
 * @author Android-3
 */
public class OrderDetailsDto {

    private String orderId;
    private Long userId;
    private String paymentKey;
    private Long mapId;
    private Double paymentAmount;
    private Double price;
    private Double discount;
    private String orderDate;
    private String status;
    private String productName;
    private String deliveryDate;
    private String phone;
    private String email;
    private String prodImg;
    private Long quty;
    private AddressDto addressDto;
    private TrackerBean trackerBean;
    private String returnStatus;
    private HashMap<String, String> propertyMap;

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
     * @return the paymentKey
     */
    public String getPaymentKey() {
        return paymentKey;
    }

    /**
     * @param paymentKey the paymentKey to set
     */
    public void setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
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
     * @return the paymentAmount
     */
    public Double getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * @param paymentAmount the paymentAmount to set
     */
    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * @return the orderDate
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
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
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the deliveryDate
     */
    public String getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
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

    /**
     * @return the prodImg
     */
    public String getProdImg() {
        return prodImg;
    }

    /**
     * @param prodImg the prodImg to set
     */
    public void setProdImg(String prodImg) {
        this.prodImg = prodImg;
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

    /**
     * @return the quty
     */
    public Long getQuty() {
        return quty;
    }

    /**
     * @param quty the quty to set
     */
    public void setQuty(Long quty) {
        this.quty = quty;
    }

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the trackerBean
     */
    public TrackerBean getTrackerBean() {
        return trackerBean;
    }

    /**
     * @param trackerBean the trackerBean to set
     */
    public void setTrackerBean(TrackerBean trackerBean) {
        this.trackerBean = trackerBean;
    }

    /**
     * @return the returnStatus
     */
    public String getReturnStatus() {
        return returnStatus;
    }

    /**
     * @param returnStatus the returnStatus to set
     */
    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    /**
     * @return the propertyMap
     */
    public HashMap<String, String> getPropertyMap() {
        return propertyMap;
    }

    /**
     * @param propertyMap the propertyMap to set
     */
    public void setPropertyMap(HashMap<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }

}
