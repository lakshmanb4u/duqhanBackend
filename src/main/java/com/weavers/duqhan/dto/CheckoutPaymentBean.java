/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;

import java.util.TreeMap;

/**
 *
 * @author weaversAndroid
 */
public class CheckoutPaymentBean {

    private TreeMap<String, String> parameters;
    private String paymentUrl;
    private String status;
    private String statusCode;
    private int gateway;
    /**
     * @return the parameters
     */
    public TreeMap<String, String> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(TreeMap<String, String> parameters) {
        this.parameters = parameters;
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
     * @return the gateway
     */
    public int getGateway() {
        return gateway;
    }

    /**
     * @param gateway the gateway to set
     */
    public void setGateway(int gateway) {
        this.gateway = gateway;
    }

    /**
     * @return the paymentUrl
     */
    public String getPaymentUrl() {
        return paymentUrl;
    }

    /**
     * @param paymentUrl the paymentUrl to set
     */
    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

}
