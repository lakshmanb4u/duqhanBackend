/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.business.impl;

import com.weavers.duqhun.business.WebService;
import com.weavers.duqhun.dao.ColorDao;
import com.weavers.duqhun.dao.SizeeDao;
import com.weavers.duqhun.domain.Color;
import com.weavers.duqhun.domain.Sizee;
import com.weavers.duqhun.dto.ColorAndSizeDto;
import com.weavers.duqhun.dto.ColorDto;
import com.weavers.duqhun.dto.SizeDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Android-3
 */
public class WebServiceImpl implements WebService {

    @Autowired
    SizeeDao sizeeDao;
    @Autowired
    ColorDao colorDao;

    @Override
    public ColorAndSizeDto getColorSizeList() {
        List<Sizee> sizees = sizeeDao.loadAll();
        List<Color> colors = colorDao.loadAll();
        ColorAndSizeDto colorAndSizeDto = new ColorAndSizeDto();
        List<SizeDto> sizeDtos = new ArrayList<>();
        List<ColorDto> colorDtos = new ArrayList<>();
        for (Sizee sizee : sizees) {
            SizeDto SizeDto = new SizeDto();
            SizeDto.setSizeId(sizee.getId());
            SizeDto.setSizeText(sizee.getValu());
            sizeDtos.add(SizeDto);
        }
        for (Color color : colors) {
            ColorDto ColorDto = new ColorDto();
            ColorDto.setColorId(color.getId());
            ColorDto.setColorText(color.getName());
            colorDtos.add(ColorDto);
        }
        colorAndSizeDto.setSizeDtos(sizeDtos);
        colorAndSizeDto.setColorDtos(colorDtos);
        return colorAndSizeDto;
    }

}
