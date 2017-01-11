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
    private List<ColorDto> colorDtos;

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
}
