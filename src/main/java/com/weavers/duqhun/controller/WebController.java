/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.controller;

import com.weavers.duqhun.business.ProductService;
import com.weavers.duqhun.business.WebService;
import com.weavers.duqhun.dto.ColorAndSizeDto;
import com.weavers.duqhun.dto.ProductBean;
import com.weavers.duqhun.dto.StatusBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Weavers-web
 */
@Controller
@RequestMapping("/web/**")
public class WebController {

    @Autowired
    WebService webService;
    @Autowired
    ProductService productService;

    @RequestMapping(value = "/add-product", method = RequestMethod.GET)
    public String addProduct(ModelMap modelMap) {
        ColorAndSizeDto colorAndSizeDto = webService.getColorSizeList();
        modelMap.addAttribute("sizeAndColor", colorAndSizeDto);
        return "addProduct";
    }
    @RequestMapping(value = "/save-product", method = RequestMethod.POST)
    @ResponseBody
    public StatusBean addingContest(@RequestBody ProductBean productBean) {
        StatusBean response = new StatusBean();
        response.setStatus(productService.saveProduct(productBean));
        return response;
    }

}
