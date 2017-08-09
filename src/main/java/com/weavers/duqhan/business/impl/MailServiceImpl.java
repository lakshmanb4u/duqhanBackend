/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.weavers.duqhan.business.MailService;
import com.weavers.duqhan.dao.ColorDao;
import com.weavers.duqhan.dao.OrderDetailsDao;
import com.weavers.duqhan.dao.ProductDao;
import com.weavers.duqhan.dao.ProductSizeColorMapDao;
import com.weavers.duqhan.dao.UserAddressDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.domain.Color;
import com.weavers.duqhan.domain.OrderDetails;
import com.weavers.duqhan.domain.Product;
import com.weavers.duqhan.domain.ProductSizeColorMap;
import com.weavers.duqhan.domain.UserAddress;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.util.DateFormater;
import com.weavers.duqhan.util.MailSender;
import java.util.List;
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
    @Autowired
    UserAddressDao userAddressDao;
    @Autowired
    ColorDao colorDao;
    @Autowired
    OrderDetailsDao orderDetailsDao;
    private String adminMail = "admin@duqhan.com";//duqhanapp@gmail.com

    @Override
    public String sendOTPforPasswordReset(String email, String otp) {
        String status = MailSender.sendEmail(email, "OTP For Password Resset", "Your OTP is:  " + otp, "");// send mail to user with otp.
        return status;
    }

    @Override
    public String returnRequestToAdmin(OrderDetails orderDetails) {
        String body = "";
        ProductSizeColorMap sizeColorMap = productSizeColorMapDao.loadById(orderDetails.getMapId());
        body = "<table><tr><td>User ID </td><td> : " + orderDetails.getUserId() + "</td></tr><tr><td>User Name</td><td> : " + usersDao.loadById(orderDetails.getUserId()).getName() + "</td></tr><tr><td>Order ID</td><td> : " + orderDetails.getOrderId() + "</td></tr><tr><td>Product</td><td> : " + productDao.loadById(sizeColorMap.getProductId()).getName() + "</td></tr><tr><td>Product Size Color Map ID</td><td> : " + sizeColorMap.getId() + "</td></tr><tr><td>Quantity</td><td> : " + orderDetails.getQuentity() + "</td></tr><tr><td>Order Date</td><td> : " + DateFormater.formate(orderDetails.getOrderDate()) + "</td></tr></table>";
        String status = MailSender.sendEmail(adminMail, "Return Request", body, "");// send mail to Admin if user return a order.
        return status;
    }

    @Override
    public String sendMailToAdminByUser(StatusBean contactBean, Users users) {
        String body = "<div><div>Hi Admin,<br><br><span>Subject : " + contactBean.getStatusCode() + ".</span><br><br>" + contactBean.getStatus() + "<br><br>Name: " + users.getName() + " [" + users.getId() + "]<br>Email: " + users.getEmail() + "<br>Phone:" + users.getMobile() + "</div></div>";
        String status = MailSender.sendEmail(adminMail, "Customer support", body, "");// send mail to Admin from user.
        return status;
    }

    @Override
    public String sendNewRegistrationToAdmin(Users users) {
        String body = "A new user is registered.<br>Name: " + users.getName() + "<br>Email: " + users.getEmail() + "<br>Phone: " + users.getMobile();
        String status = MailSender.sendEmail(adminMail, "New user registration", body, "");// send mail to Admin at new registration.
        return status;
    }

    @Override
    public String sendPurchaseMailToAdmin(List<OrderDetails> orderDetails) {
        if (!orderDetails.isEmpty()) {
            String body = "";
            Users user = usersDao.loadById(orderDetails.get(0).getUserId());
            body += "Name: " + user.getName() + " (" + user.getId() + ")<br>Contact: " + user.getMobile() + "<br>Email: " + user.getEmail() + "<br><br>";
            long addressId = orderDetails.get(0).getAddressId();
            UserAddress address = userAddressDao.loadById(addressId);
            String addressPath = address.getStreetOne() + "<br>" + address.getStreetTwo() + "<br>" + address.getCity() + " " + address.getZipCode() + "<br>" + address.getState() + "<br>" + address.getPhone() + "<br><br>";
            Product product;
            ProductSizeColorMap sizeColorMap;
            String order;
            for (OrderDetails orderDetail : orderDetails) {
                sizeColorMap = productSizeColorMapDao.loadById(orderDetail.getMapId());
                String property = "";
                if (sizeColorMap.getColorId() != null) {
                    Color color = colorDao.loadById(sizeColorMap.getColorId());
                    property = color.getName();
                }
                product = productDao.loadById(sizeColorMap.getProductId());
                order = "<b>Order Id: " + orderDetail.getOrderId() + "</b><br>Product: " + product.getName() + " (" + product.getId() + ")<br>Specification: " + property + "<br>Quantity: " + orderDetail.getQuentity() + "<br>Price: " + orderDetail.getPaymentAmount() + "<br><br>Delivery address :<br>";
                if (addressId != orderDetail.getAddressId()) {
                    address = userAddressDao.loadById(orderDetail.getAddressId());
                    order += address.getStreetOne() + "<br>" + address.getStreetTwo() + "<br>" + address.getCity() + " " + address.getZipCode() + "<br>" + address.getState() + "<br>" + address.getPhone() + "<br><br>";
                } else {
                    order += addressPath;
                }
                body += order;
            }
            String status = MailSender.sendEmail(adminMail, "Product purchase", body, "");// send mail to Admin at purchase.
            return status;
        } else {
            return "fail";
        }
    }
}
