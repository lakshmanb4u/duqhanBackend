/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.weavers.duqhan.business.MailService;
import com.weavers.duqhan.dao.ProductDao;
import com.weavers.duqhan.dao.ProductSizeColorMapDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.domain.OrderDetails;
import com.weavers.duqhan.domain.ProductSizeColorMap;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.util.DateFormater;
import com.weavers.duqhan.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author weaversAndroid
 */
public class MailServiceImpl implements MailService {

    @Autowired
    UsersDao usersDao;
    @Autowired
    ProductSizeColorMapDao productSizeColorMapDao;
    @Autowired
    ProductDao productDao;

    @Override
    public String sendOTPforPasswordReset(String email, String otp) {
        String status = MailSender.sendEmail(email, "OTP For Password Resset", "Your OTP is:  " + otp, "");// send mail to user with otp.
        return status;
    }

    @Override
    public String returnRequestToAdmin(OrderDetails orderDetails) {
        String body = "";
        ProductSizeColorMap sizeColorMap = productSizeColorMapDao.loadById(orderDetails.getMapId());
        body = "<table><tr><td>User ID </td><td> : " + orderDetails.getUserId() + "</td></tr><tr><td>User Name</td><td> : " + usersDao.loadById(orderDetails.getUserId()).getName() + "</td></tr><tr><td>Order ID</td><td> : " + orderDetails.getOrderId() + "</td></tr><tr><td>Product</td><td> : " + productDao.loadById(sizeColorMap.getProductId()).getName() +"</td></tr><tr><td>Product Size Color Map ID</td><td> : " + sizeColorMap.getId() + "</td></tr><tr><td>Quantity</td><td> : " + orderDetails.getQuentity() + "</td></tr><tr><td>Order Date</td><td> : " + DateFormater.formate(orderDetails.getOrderDate()) + "</td></tr></table>";
        String status = MailSender.sendEmail("duqhanapp@gmail.com", "Return Request", body, "");// send mail to Admin if user return a order.
        return status;
    }

    @Override
    public String sendMailToAdminByUser(StatusBean contactBean, Users users) {
        String body = "<div><div>Hi Admin,<br><br><span>Subject : "+contactBean.getStatusCode()+".</span><br><br>"+contactBean.getStatus()+"<br><br>Name: "+users.getName()+" ["+users.getId()+"]<br>Email: "+users.getEmail()+"<br>Phone:"+users.getMobile()+"</div></div>";
        String status = MailSender.sendEmail("duqhanapp@gmail.com", "Customer support", body, "");// send mail to Admin from user.
        return status;
    }

}
