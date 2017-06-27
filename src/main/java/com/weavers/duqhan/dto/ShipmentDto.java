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
public class ShipmentDto {
    private String shipmentId;
    private String payKey;
    private Long userId;
    private String userName;

    /**
     * @return the shipmentId
     */
    public String getShipmentId() {
        return shipmentId;
    }

    /**
     * @param shipmentId the shipmentId to set
     */
    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    /**
     * @return the payKey
     */
    public String getPayKey() {
        return payKey;
    }

    /**
     * @param payKey the payKey to set
     */
    public void setPayKey(String payKey) {
        this.payKey = payKey;
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
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
