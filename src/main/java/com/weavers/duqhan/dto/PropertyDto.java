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
public class PropertyDto {
    private Long id;
    private String property;
    private List<PropertyValueDto> propertyValues;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * @return the propertyValues
     */
    public List<PropertyValueDto> getPropertyValues() {
        return propertyValues;
    }

    /**
     * @param propertyValues the propertyValues to set
     */
    public void setPropertyValues(List<PropertyValueDto> propertyValues) {
        this.propertyValues = propertyValues;
    }
}
