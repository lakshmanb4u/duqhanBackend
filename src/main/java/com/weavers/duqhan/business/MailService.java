/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business;

import com.weavers.duqhan.domain.OrderDetails;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.UserBean;
import java.util.List;

/**
 *
 * @author weaversAndroid
 */
public interface MailService {

    String sendOTPforPasswordReset(String email, String otp);

    String returnRequestToAdmin(OrderDetails orderDetails);

    String sendMailToAdminByUser(UserBean contactBean, Users users);

    String sendMailToAdminByUser(UserBean contactBean);

    String sendNewRegistrationToAdmin(Users users);

    String sendWelcomeMailToUser(Users users);

    String sendPurchaseMailToAdmin(List<OrderDetails> orderDetails);

    String sendPurchaseMailToUser(List<OrderDetails> orderDetails);
}
