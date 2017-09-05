/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business;

import com.easypost.model.Shipment;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.CheckoutPaymentBean;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Android-3
 */
public interface PaymentService {

//<editor-fold defaultstate="collapsed" desc="Checkout">
    CheckoutPaymentBean transactionRequest(HttpServletRequest request, HttpServletResponse response, CartBean cartBean, Double shippingCost, List<Shipment> shipments); // Paypal

    CheckoutPaymentBean transactionRequest(Users user, CartBean cartBean, Double shippingCost, List<Shipment> shipments, String baseUrl);   // Paytm

    String genrateCheckSum(TreeMap<String, String> parameters);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Payment Gateway Call back url">
    String getPayerInformation(String payer, String payment, String accessToken); // Paypal

    Object[] storePaytmFeedBack(HttpServletRequest request);    // Paytm
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Check payment status">
    String[] getPaymentStatus(Long userId, String payKey);    // Paypal and Paytm both
//</editor-fold>

    int getApplicationType(String token);
}
