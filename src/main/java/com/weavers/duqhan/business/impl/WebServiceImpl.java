/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.weavers.duqhan.business.WebService;
import com.weavers.duqhan.dao.CategoryDao;
import com.weavers.duqhan.dao.ColorDao;
import com.weavers.duqhan.dao.SizeGroupDao;
import com.weavers.duqhan.dao.SizeeDao;
import com.weavers.duqhan.domain.Category;
import com.weavers.duqhan.domain.Color;
import com.weavers.duqhan.domain.SizeGroup;
import com.weavers.duqhan.domain.Sizee;
import com.weavers.duqhan.dto.CategoryDto;
import com.weavers.duqhan.dto.ColorAndSizeDto;
import com.weavers.duqhan.dto.ColorDto;
import com.weavers.duqhan.dto.SizeDto;
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
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    SizeGroupDao sizeGroupDao;

    @Override
    public ColorAndSizeDto getColorSizeList() { // get color size category from database on add produc page load.
        List<Sizee> sizees = sizeeDao.loadAll();
        List<Color> colors = colorDao.loadAll();
        List<Category> categorys = categoryDao.loadAll();
        List<SizeGroup> sizeGroups = sizeGroupDao.loadAll();
        ColorAndSizeDto colorAndSizeDto = new ColorAndSizeDto();
        List<SizeDto> sizeDtos = new ArrayList<>();
        List<ColorDto> colorDtos = new ArrayList<>();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        List<SizeDto> sizeGroupDtos = new ArrayList<>();
        for (Sizee sizee : sizees) {    //=============get size.
            SizeDto SizeDto = new SizeDto();
            SizeDto.setSizeId(sizee.getId());
            SizeDto.setSizeText(sizee.getValu());
            sizeDtos.add(SizeDto);
        }
        for (Color color : colors) {    //=============get colors.
            ColorDto ColorDto = new ColorDto();
            ColorDto.setColorId(color.getId());
            ColorDto.setColorText(color.getName());
            colorDtos.add(ColorDto);
        }
        for (Category category : categorys) {   //=============get category.
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryId(category.getId());
            categoryDto.setCategoryName(category.getName());
            categoryDtos.add(categoryDto);
        }
        for (SizeGroup sizeGroup : sizeGroups) {    //=============get size groups.
            SizeDto sizeGroupDto = new SizeDto();
            sizeGroupDto.setSizeGroupId(sizeGroup.getId());
            sizeGroupDto.setSizeText(sizeGroup.getName());
            sizeGroupDtos.add(sizeGroupDto);
        }
        colorAndSizeDto.setSizeGroupDtos(sizeGroupDtos);
        colorAndSizeDto.setSizeDtos(sizeDtos);
        colorAndSizeDto.setColorDtos(colorDtos);
        colorAndSizeDto.setCategoryDtos(categoryDtos);
        return colorAndSizeDto;
    }

}
