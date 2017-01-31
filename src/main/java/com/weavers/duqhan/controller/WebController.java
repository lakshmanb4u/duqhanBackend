/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.controller;

import com.weavers.duqhan.business.PaymentService;
import com.weavers.duqhan.business.ProductService;
import com.weavers.duqhan.business.WebService;
import com.weavers.duqhan.dto.CategoryDto;
import com.weavers.duqhan.dto.ColorAndSizeDto;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.dto.ProductRequistBean;
import com.weavers.duqhan.dto.SizeDto;
import com.weavers.duqhan.dto.StatusBean;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
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
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(WebController.class);

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
            altclass = "text-success";
        } else {
            msg = "Payment failure!";
            altclass = "text-danger";
        }
        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("altclass", altclass);
        return "paymentStatus";
    }

    @RequestMapping(value = "/to-be-canceled", method = RequestMethod.GET)
    public String canceled(ModelMap modelMap) {
        modelMap.addAttribute("msg", "Payment canceled!");
        modelMap.addAttribute("altclass", "text-danger");
//        try {
//            Integer.parseInt("4hk");
//        } catch (Exception e) {
//            logger.error("This is Error message", new Exception("Testing"));
//        }
        return "paymentStatus";
    }
}
