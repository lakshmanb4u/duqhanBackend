/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.controller;

import com.weavers.duqhan.business.AdminService;
import com.weavers.duqhan.business.NotificationService;
import com.weavers.duqhan.business.PaymentService;
import com.weavers.duqhan.business.ProductService;
import com.weavers.duqhan.business.ShippingService;
import com.weavers.duqhan.business.VendorService;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.AouthBean;
import com.weavers.duqhan.dto.CategoryDto;
import com.weavers.duqhan.dto.ColorAndSizeDto;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.dto.ProductRequistBean;
import com.weavers.duqhan.dto.ShipmentDto;
import com.weavers.duqhan.dto.SizeDto;
import com.weavers.duqhan.dto.StatusBean;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Weavers-web
 */
@CrossOrigin
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
    @Autowired
    AdminService adminService;

    private final Logger logger = Logger.getLogger(WebController.class);

    @RequestMapping(value = "/home", method = RequestMethod.GET) // open home page
    public String home() {
        return "index";
    }

//    @RequestMapping(value = "/adminlogin", method = RequestMethod.GET) // open admin login page
//    public String adminLogin() {
//        return "adminlogin";
//    }
    
    @RequestMapping(value = "/admin-login", method = RequestMethod.POST)  // Log in by email only register user. Auth-Token generate.
    @ResponseBody
    public AouthBean trainerLogin(HttpServletResponse response, @RequestBody LoginBean loginBean) {
        AouthBean userBean = adminService.generatAccessToken(loginBean);
//        response.setStatus(Integer.valueOf(userBean.getStatusCode()));
        return userBean;
    }

    @RequestMapping(value = "/adminlogin-action", method = RequestMethod.POST) // admin login action
    public ModelAndView adminLoginAction(LoginBean loginBean, HttpSession session, HttpServletResponse response) throws IOException {
        String redirect = adminService.adminLogin(loginBean, session);
        return new ModelAndView("redirect:/" + redirect);
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
