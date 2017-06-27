/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;

import java.util.List;

/**
 *
 * @author Android-3
 */
public class OrderDetailsBean {

    private String status;
    private String statusCode;
    private List<OrderDetailsDto> orderDetailsDtos;

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
     * @return the orderDetailsDtos
     */
    public List<OrderDetailsDto> getOrderDetailsDtos() {
        return orderDetailsDtos;
    }

    /**
     * @param orderDetailsDtos the orderDetailsDtos to set
     */
    public void setOrderDetailsDtos(List<OrderDetailsDto> orderDetailsDtos) {
        this.orderDetailsDtos = orderDetailsDtos;
    }

}
