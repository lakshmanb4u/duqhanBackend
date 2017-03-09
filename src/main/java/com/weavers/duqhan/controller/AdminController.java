/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.controller;

import com.weavers.duqhan.business.NotificationService;
import com.weavers.duqhan.business.PaymentService;
import com.weavers.duqhan.business.ProductService;
import com.weavers.duqhan.business.ShippingService;
import com.weavers.duqhan.business.VendorService;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.CategoryDto;
import com.weavers.duqhan.dto.ColorAndSizeDto;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.dto.ProductRequistBean;
import com.weavers.duqhan.dto.SizeDto;
import com.weavers.duqhan.dto.SpecificationDto;
import com.weavers.duqhan.dto.StatusBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
 * @author weaversAndroid
 */
@Controller
@RequestMapping("/admin/**")
public class AdminController {

    @Autowired
    ProductService productService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    VendorService vendorService;
    @Autowired
    ShippingService shippingService;
    @Autowired
    NotificationService notificationService;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut(HttpServletRequest request, HttpSession session) { //admin logout
//        request.logout(); 
        session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/web/home";
    }

    @RequestMapping(value = "/add-product", method = RequestMethod.GET) // open main view page
    public String addProduct() {
        return "addProduct";
    }

//    @RequestMapping(value = "/add-product", method = RequestMethod.GET) // have to remove when admin palen is ready.
//    public String addProduct(ModelMap modelMap) {
//        ColorAndSizeDto colorAndSizeDto = productService.getColorSizeList();
//        modelMap.addAttribute("sizeAndColor", colorAndSizeDto);
//        return "addProduct";
//    }

    @RequestMapping(value = "/get-category", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getCategory() {
        ColorAndSizeDto colorAndSizeDto = productService.getCategories();
        return colorAndSizeDto;
    }

    @RequestMapping(value = "/get-size", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getSize() {
        ColorAndSizeDto colorAndSizeDto = productService.getSizes();
        return colorAndSizeDto;
    }

    @RequestMapping(value = "/get-sizegroup", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getSizegroup() {
        ColorAndSizeDto colorAndSizeDto = productService.getSizeGroupe();
        return colorAndSizeDto;
    }

    @RequestMapping(value = "/get-color", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getColor() {
        ColorAndSizeDto colorAndSizeDto = productService.getColors();
        return colorAndSizeDto;
    }

    @RequestMapping(value = "/get-vendor", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getVendor() {
        ColorAndSizeDto colorAndSizeDto = productService.getVendors();
        return colorAndSizeDto;
    }
    
    @RequestMapping(value = "/get-specifications", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getSpecifications(@RequestParam Long categoryId) {
        ColorAndSizeDto colorAndSizeDto = productService.getSpecificationsByCategoryId(categoryId);
        return colorAndSizeDto;
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

    @RequestMapping(value = "/save-vendor", method = RequestMethod.POST)  // save a new category
    @ResponseBody
    public StatusBean saveVendor(@RequestBody AddressDto addressDto) {
        StatusBean response = new StatusBean();
        StatusBean statusBean = shippingService.verifyAddress(addressDto);
        if (statusBean.getStatusCode().equals("200")) {
            response.setStatus(vendorService.saveVendor(addressDto));
        } else {
            response.setStatusCode(statusBean.getStatusCode());
            response.setStatus(statusBean.getStatus());
        }
        return response;
    }
    
    @RequestMapping(value = "/save-specification", method = RequestMethod.POST)  // save a new Specification
    @ResponseBody
    public StatusBean saveSpecification(@RequestBody SpecificationDto specificationDto) {
        StatusBean response = new StatusBean();
        response.setStatus(productService.saveSpecification(specificationDto));
        return response;
    }
    
    @RequestMapping(value = "/save-specification-value", method = RequestMethod.POST)  // save a new Specification
    @ResponseBody
    public StatusBean saveSpecificationValue(@RequestBody SpecificationDto specificationDto) {
        StatusBean response = new StatusBean();
        response.setStatus(productService.saveSpecificationValue(specificationDto));
        return response;
    }
}
