/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;

import java.util.List;

/**
 *
 * @author weaversAndroid
 */
public class TrackerBean {

    private String trackerId;
    private String mode;
    private String trackingCode;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String signedBy;
    private String weight;
    private String estDeliveryDate;
    private String shipmentId;
    private String carrier;
    private String publicUrl;
    private List<TrackerDto> trackingDetails;
    private String statusCode;

    /**
     * @return the trackerId
     */
    public String getTrackerId() {
        return trackerId;
    }

    /**
     * @param trackerId the trackerId to set
     */
    public void setTrackerId(String trackerId) {
        this.trackerId = trackerId;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the trackingCode
     */
    public String getTrackingCode() {
        return trackingCode;
    }

    /**
     * @param trackingCode the trackingCode to set
     */
    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
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
     * @return the createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return the signedBy
     */
    public String getSignedBy() {
        return signedBy;
    }

    /**
     * @param signedBy the signedBy to set
     */
    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    /**
     * @return the weight
     */
    public String getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(String weight) {
        this.weight = weight;
    }

    /**
     * @return the estDeliveryDate
     */
    public String getEstDeliveryDate() {
        return estDeliveryDate;
    }

    /**
     * @param estDeliveryDate the estDeliveryDate to set
     */
    public void setEstDeliveryDate(String estDeliveryDate) {
        this.estDeliveryDate = estDeliveryDate;
    }

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
     * @return the carrier
     */
    public String getCarrier() {
        return carrier;
    }

    /**
     * @param carrier the carrier to set
     */
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    /**
     * @return the publicUrl
     */
    public String getPublicUrl() {
        return publicUrl;
    }

    /**
     * @param publicUrl the publicUrl to set
     */
    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    /**
     * @return the trackingDetails
     */
    public List<TrackerDto> getTrackingDetails() {
        return trackingDetails;
    }

    /**
     * @param trackingDetails the trackingDetails to set
     */
    public void setTrackingDetails(List<TrackerDto> trackingDetails) {
        this.trackingDetails = trackingDetails;
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
}
