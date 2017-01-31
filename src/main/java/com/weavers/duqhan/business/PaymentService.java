/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business;

import com.weavers.duqhan.dto.CartBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Android-3
 */
public interface PaymentService {

    String[] transactionRequest(HttpServletRequest request, HttpServletResponse response, CartBean cartBean);

    String getPayerInformation(String payer, String payment, String accessToken);

    String getPaymentStatus(Long userId, String payKey);
}
