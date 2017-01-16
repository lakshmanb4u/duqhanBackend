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
public class ColorAndSizeDto {

    private List<SizeDto> sizeDtos;
    private List<SizeDto> sizeGroupDtos;
    private List<ColorDto> colorDtos;
    private List<CategoryDto> categoryDtos;

    /**
     * @return the sizeDtos
     */
    public List<SizeDto> getSizeDtos() {
        return sizeDtos;
    }

    /**
     * @param sizeDtos the sizeDtos to set
     */
    public void setSizeDtos(List<SizeDto> sizeDtos) {
        this.sizeDtos = sizeDtos;
    }

    /**
     * @return the colorDtos
     */
    public List<ColorDto> getColorDtos() {
        return colorDtos;
    }

    /**
     * @param colorDtos the colorDtos to set
     */
    public void setColorDtos(List<ColorDto> colorDtos) {
        this.colorDtos = colorDtos;
    }

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
     * @return the sizeGroupDtos
     */
    public List<SizeDto> getSizeGroupDtos() {
        return sizeGroupDtos;
    }

    /**
     * @param sizeGroupDtos the sizeGroupDtos to set
     */
    public void setSizeGroupDtos(List<SizeDto> sizeGroupDtos) {
        this.sizeGroupDtos = sizeGroupDtos;
    }
}
