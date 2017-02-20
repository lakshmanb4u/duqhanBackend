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
import com.weavers.duqhan.dto.ShipmentDto;
import com.weavers.duqhan.dto.SizeDto;
import com.weavers.duqhan.dto.StatusBean;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
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
    ProductService productService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    VendorService vendorService;
    @Autowired
    ShippingService shippingService;
    @Autowired
    NotificationService notificationService;
    
    private final Logger logger = Logger.getLogger(WebController.class);

    @RequestMapping(value = "/add-product", method = RequestMethod.GET) // open main view page
    public String addProduct(ModelMap modelMap) {
        ColorAndSizeDto colorAndSizeDto = productService.getColorSizeList();
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
    public String canceled(ModelMap modelMap, @RequestParam String token) {
        modelMap.addAttribute("msg", "Payment canceled!");
        modelMap.addAttribute("altclass", "text-danger");
        notificationService.sendPaymentNotification(token);
        return "paymentStatus";
    }

    @RequestMapping(value = "/get-pending-shipment", method = RequestMethod.GET)
    public String getPendingShipment(ModelMap modelMap) {
        List<ShipmentDto> shipmentDtos = shippingService.getPendingShipmentButPaymentApproved();
        modelMap.addAttribute("shipmentDtos", shipmentDtos);
        return "shipmentDetails";
    }

    @RequestMapping(value = "/buy-pending-shipment", method = RequestMethod.POST)
    @ResponseBody
    public StatusBean buyPendingShipment(@RequestBody ShipmentDto shipmentDto) {
        StatusBean statusBean = new StatusBean();
        statusBean.setStatus(paymentService.getPaymentStatus(shipmentDto.getUserId(), shipmentDto.getPayKey()));
        return statusBean;
    }
}
