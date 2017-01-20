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
public class CategorysBean {
    private List<CategoryDto> categoryDtos;
    private int childCount;
    private int productCount;
    private String statusCode;
    private String status;

    /**
     * @return the categoryDtos
     */
    public List<CategoryDto> getCategoryDtos() {
        return categoryDtos;
    }

    /**
     * @param categoryDtos the categoryDtos to set
     */
    public void setCategoryDtos(List<CategoryDto> categoryDtos) {
        this.categoryDtos = categoryDtos;
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
     * @return the childCount
     */
    public int getChildCount() {
        return childCount;
    }

    /**
     * @param childCount the childCount to set
     */
    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    /**
     * @return the productCount
     */
    public int getProductCount() {
        return productCount;
    }

    /**
     * @param productCount the productCount to set
     */
    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
