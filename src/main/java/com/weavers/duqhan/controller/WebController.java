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
import com.weavers.duqhan.dto.AouthBean;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.dto.ShipmentDto;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.util.PaytmConstants;
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
        int appType = paymentService.getApplicationType(token);
        modelMap.addAttribute("appType", appType);
        String msg = null;
        String altclass = null;
        if (status.equals("approved")) {
            msg = "success!";
            altclass = "text-success";
        } else {
            msg = "failure!";
            altclass = "text-danger";
        }
        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("altclass", altclass);
        return "paymentStatus";
    }

    @RequestMapping(value = "/to-be-canceled", method = RequestMethod.GET)
    public String canceled(ModelMap modelMap, @RequestParam String token) {
        int appType = paymentService.getApplicationType(token);
        modelMap.addAttribute("appType", appType);
        modelMap.addAttribute("msg", "Payment canceled!");
        modelMap.addAttribute("altclass", "text-danger");
        /*notificationService.sendPaymentNotification(token);*///fcm token not useed
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

    //<editor-fold defaultstate="collapsed" desc="Paytm">
    @RequestMapping(value = "/paytmpayment", method = RequestMethod.GET)
    public String paytmpayment(ModelMap modelMap, @RequestParam(required = false) String CALLBACK_URL, @RequestParam(required = false) String CHANNEL_ID, @RequestParam(required = false) String CHECKSUMHASH, @RequestParam(required = false) String CUST_ID, @RequestParam(required = false) String EMAIL, @RequestParam(required = false) String INDUSTRY_TYPE_ID, @RequestParam(required = false) String MID, @RequestParam(required = false) String MOBILE_NO, @RequestParam(required = false) String ORDER_ID, @RequestParam(required = false) String TXN_AMOUNT, @RequestParam(required = false) String WEBSITE, @RequestParam(required = false) Integer APPTYPE) {
        modelMap.addAttribute("paymenturl", PaytmConstants.PAYTM_TRANSACTION_REQUEST_URL);
        modelMap.addAttribute("CALLBACK_URL", CALLBACK_URL);
        modelMap.addAttribute("CHANNEL_ID", CHANNEL_ID);
        modelMap.addAttribute("CHECKSUMHASH", CHECKSUMHASH);
        modelMap.addAttribute("CUST_ID", CUST_ID);
        modelMap.addAttribute("EMAIL", EMAIL);
        modelMap.addAttribute("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
        modelMap.addAttribute("MID", MID);
        modelMap.addAttribute("MOBILE_NO", MOBILE_NO);
        modelMap.addAttribute("ORDER_ID", ORDER_ID);
        modelMap.addAttribute("TXN_AMOUNT", TXN_AMOUNT);
        modelMap.addAttribute("WEBSITE", WEBSITE);
        modelMap.addAttribute("appType", APPTYPE);
        System.out.println("CHECKSUMHASH1 -- " + CHECKSUMHASH + " --");
        return "paytmpayment";
    }

    @RequestMapping(value = "/paytm-call-back", method = RequestMethod.POST)
    public String paytmCallBack(ModelMap modelMap, HttpServletRequest request) {
        Object[] feedback = paymentService.storePaytmFeedBack(request);
        String status = (String) feedback[0];
        int appType = (int) feedback[1];
        modelMap.addAttribute("appType", appType);
        String msg = null;
        String altclass = null;
        if (status.equals("approved")) {
            msg = "success!";
            altclass = "text-success";
        } else {
            msg = "failure!";
            altclass = "text-danger";
        }
        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("altclass", altclass);

        return "paymentStatus";
    }
//</editor-fold>
}
