/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.controller;

import com.weavers.duqhun.business.PaymentService;
import com.weavers.duqhun.business.ProductService;
import com.weavers.duqhun.business.WebService;
import com.weavers.duqhun.dto.CategoryDto;
import com.weavers.duqhun.dto.ColorAndSizeDto;
import com.weavers.duqhun.dto.ProductBean;
import com.weavers.duqhun.dto.ProductRequistBean;
import com.weavers.duqhun.dto.SizeDto;
import com.weavers.duqhun.dto.StatusBean;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    PaymentService paymentService;

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
    public StatusBean saveSizeGroup(@RequestBody ProductRequistBean requistBean) {
        StatusBean response = new StatusBean();
        response.setStatus(productService.saveSizeGroup(requistBean.getName()));
        return response;
    }

    @RequestMapping(value = "/save-color", method = RequestMethod.POST) // save new color
    @ResponseBody
    public StatusBean saveColor(@RequestBody ProductRequistBean requistBean) {
        StatusBean response = new StatusBean();
        response.setStatus(productService.saveColor(requistBean.getName()));
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

    @RequestMapping(value = "/to-be-redirected", method = RequestMethod.GET)
    public String redirected(@RequestParam String paymentId, @RequestParam String token, @RequestParam String PayerID, HttpServletRequest request, ModelMap modelMap) {
        String status = paymentService.getPayerInformation(PayerID, paymentId, token);
        String msg = null;
        String altclass = null;
        if (status.equals("approved")) {
            msg = "Payment success!";
            altclass = "alert alert-success";
        } else {
            msg = "Payment failure!";
            altclass = "alert alert-danger";
        }
        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("altclass", altclass);
        return "paymentStatus";
    }

    @RequestMapping(value = "/to-be-canceled", method = RequestMethod.GET)
    public String canceled(ModelMap modelMap) {
        modelMap.addAttribute("msg", "Payment canceled!");
        modelMap.addAttribute("altclass", "alert alert-warning");
        return "paymentStatus";
    }
}
