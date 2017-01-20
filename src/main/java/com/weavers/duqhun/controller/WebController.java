/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.controller;

import com.weavers.duqhun.business.ProductService;
import com.weavers.duqhun.business.WebService;
import com.weavers.duqhun.dto.CategoryDto;
import com.weavers.duqhun.dto.ColorAndSizeDto;
import com.weavers.duqhun.dto.ProductBean;
import com.weavers.duqhun.dto.SizeDto;
import com.weavers.duqhun.dto.StatusBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Weavers-web
 */
@Controller
@RequestMapping("/web/**")  // for web view only 
public class WebController {

    @Autowired
    WebService webService;
    @Autowired
    ProductService productService;

    @RequestMapping(value = "/add-product", method = RequestMethod.GET) // open main view page
    public String addProduct(ModelMap modelMap) {
        ColorAndSizeDto colorAndSizeDto = webService.getColorSizeList();
        modelMap.addAttribute("sizeAndColor", colorAndSizeDto);
        return "addProduct";
    }

    @RequestMapping(value = "/save-product", method = RequestMethod.POST)   // save a new product.
    @ResponseBody
    public StatusBean addingProduct(@RequestBody ProductBean productBean) {
        StatusBean response = new StatusBean();
        response.setStatus(productService.saveProduct(productBean));
        return response;
    }

    @RequestMapping(value = "/save-category", method = RequestMethod.POST)  // save a new category
    @ResponseBody
    public StatusBean saveCategory(@RequestBody CategoryDto categoryDto) {
        StatusBean response = new StatusBean();
        response.setStatus(productService.saveCategory(categoryDto));
        return response;
    }

    @RequestMapping(value = "/save-size", method = RequestMethod.POST)  // save new size
    @ResponseBody
    public StatusBean saveSize(@RequestBody SizeDto sizeDto) {
        StatusBean response = new StatusBean();
        response.setStatus(productService.saveSize(sizeDto));
        return response;
    }

    @RequestMapping(value = "/save-sizegroup", method = RequestMethod.POST) // save new sizegroup
    @ResponseBody
    public StatusBean saveSizeGroup(@RequestBody String sizeGroop) {
        StatusBean response = new StatusBean();
        response.setStatus(productService.saveSizeGroup(sizeGroop));
        return response;
    }

    @RequestMapping(value = "/save-color", method = RequestMethod.POST) // save new color
    @ResponseBody
    public StatusBean saveColor(@RequestBody String color) {
        StatusBean response = new StatusBean();
        response.setStatus(productService.saveColor(color));
        return response;
    }

    @RequestMapping(value = "/save-product-image", method = RequestMethod.POST)   // save product image.
    @ResponseBody
    public StatusBean addingProductImage(@ModelAttribute("productBean") ProductBean productBean) {
        StatusBean response = new StatusBean();
        response.setStatus("failure");
        response.setStatus(productService.saveProductImage(productBean));
        return response;
    }
}
