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
public class ProductRequistBean {

    private Long categoryId;
    private Long productId;
    private Boolean isRecent;

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
     * @return the isRecent
     */
    public Boolean getIsRecent() {
        return isRecent;
    }

    /**
     * @param isRecent the isRecent to set
     */
    public void setIsRecent(Boolean isRecent) {
        this.isRecent = isRecent;
    }

}
