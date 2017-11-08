/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.weavers.duqhan.business.MailService;
import com.weavers.duqhan.dao.CategoryDao;
import com.weavers.duqhan.dao.ColorDao;
import com.weavers.duqhan.dao.OrderDetailsDao;
import com.weavers.duqhan.dao.ProductDao;
import com.weavers.duqhan.dao.ProductSizeColorMapDao;
import com.weavers.duqhan.dao.UserAddressDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.domain.Category;
import com.weavers.duqhan.domain.Color;
import com.weavers.duqhan.domain.OfferProductUsed;
import com.weavers.duqhan.domain.OrderDetails;
import com.weavers.duqhan.domain.Product;
import com.weavers.duqhan.domain.ProductSizeColorMap;
import com.weavers.duqhan.domain.UserAddress;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.UserBean;
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
    @Autowired
    CategoryDao categoryDao;
    private static final String ADMIN_MAIL = "mamidi.laxman.lnu@gmail.com";//duqhanapp@gmail.com
    private static final String BCC = "krisanu.nandi@pkweb.in";

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
        String status = MailSender.sendEmail(ADMIN_MAIL, "Return Request", body, BCC);// send mail to Admin if user return a order.
        return status;
    }

    @Override
    public String sendMailToAdminByUser(UserBean contactBean, Users users) {
        String body = "<div><div>Hi Admin,<br><br><span>Subject : " + contactBean.getStatusCode() + ".</span><br><br>" + contactBean.getStatus() + "<br><br>Name: " + users.getName() + " [" + users.getId() + "]<br>Email: " + contactBean.getEmail() + "<br>Phone:" + contactBean.getMobile() + "</div></div>";
        String status = MailSender.sendEmail(ADMIN_MAIL, "Customer support", body, BCC);// send mail to Admin from user.
        return status;
    }

    @Override
    public String sendMailToAdminByUser(UserBean contactBean) {
        String body = "<div><div>Hi Admin,<br><br><span>Subject : " + contactBean.getStatusCode() + ".</span><br><br>" + contactBean.getStatus() + "<br><br>Name: " + contactBean.getName() + " [Visitor]<br>Email: " + contactBean.getEmail() + "<br>Phone:" + contactBean.getMobile() + "</div></div>";
        String status = MailSender.sendEmail(ADMIN_MAIL, "Customer support", body, BCC);// send mail to Admin from user(visitor).
        return status;
    }

    @Override
    public String sendNewRegistrationToAdmin(Users users) {
        String body = "A new user is registered.<br>Name: " + users.getName() + "<br>Email: " + users.getEmail() + "<br>Phone: " + users.getMobile();
        String status = MailSender.sendEmail(ADMIN_MAIL, "New user registration", body, BCC);// send mail to Admin at new registration.
        return status;
    }

    @Override
    public String sendWelcomeMailToUser(Users users) {
        String body = "<table style=\"width:100%;border-collapse:collapse; border:3px solid rgb(250,144,5);\">"
                + "    <tr>"
                + "        <td style=\"padding:0 20px 20px 20px;vertical-align:top;font-size:13px;line-height:18px;font-family:Arial,sans-serif\">"
                + "            <table style=\"width:100%;border-collapse:collapse\">"
                + "                <thead>"
                + "                    <tr>"
                + "                        <th><h1 style=\"font-size:28px;color:rgb(204,102,0);margin:15px 0 0 0;font-weight:normal\">DUQHAN</h1><hr></th>"
                + "                    </tr>"
                + "                </thead>"
                + "                <tbody>"
                + "                    <tr>"
                + "                        <th><h3 style=\"font-size:22px;color:rgb(204,102,0);margin:15px 0 0 0;font-weight:normal\">WELCOME TO DUQHAN</h3></th>"
                + "                    </tr>"
                + "                    <tr>"
                + "                        <th><span>Thank you " + users.getName() + "for joining us.</span></th>"
                + "                    </tr>"
                + "                    <tr>"
                + "                        <th><img src=\"https://storage.googleapis.com/duqhan-users/logo.png\"  alt=\"Duqhan\" src=\"\" style=\"border:0;width:115px\" /></th>"
                + "                    </tr>"
                + "                </tbody>"
                + "            </table>"
                + "        </td>"
                + "    </tr>"
                + "</table>";
        String status = MailSender.sendEmail(users.getEmail(), "New user registration", body, BCC);// send mail to user at new registration.
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
            String status = MailSender.sendEmail(ADMIN_MAIL, "Product purchase", body, BCC);// send mail to Admin at purchase.
            return status;
        } else {
            return "fail";
        }
    }

    @Override
    public String sendPurchaseMailToUser(List<OrderDetails> orderDetails) {
        if (!orderDetails.isEmpty()) {
            String body = "";
            Users user = usersDao.loadById(orderDetails.get(0).getUserId());
            long addressId = orderDetails.get(0).getAddressId();
            UserAddress address = userAddressDao.loadById(addressId);
            String addressPath = "                             <tr> "
                    + "                                        <td style=\"font-size:14px;padding:11px 18px 18px 18px;background-color:rgb(250,244,237);width:50%;vertical-align:top;line-height:18px;font-family:Arial,sans-serif\"> "
                    + "                                            <p style=\"margin:2px 0 9px 0;font:14px Arial,sans-serif\"> <span style=\"font-size:14px;color:rgb(89,145,57)\">Your order will be sent to:</span><br> "
                    + "                                                <b> " + user.getName() + " <br>"
                    + address.getStreetOne() + "," + (address.getStreetTwo() != null ? address.getStreetTwo() : "") + "<br>"
                    + address.getCity() + " " + address.getState() + " " + address.getZipCode() + "<br>"
                    + "                                                    India <br>" + address.getPhone()
                    + "                                                </b>"
                    + "                                            </p> "
                    + "                                        </td> "
                    + "                                    </tr> ";
            Product product;
            ProductSizeColorMap sizeColorMap;
            String order = "";
            Category category;
            for (OrderDetails orderDetail : orderDetails) {
                sizeColorMap = productSizeColorMapDao.loadById(orderDetail.getMapId());
                String property = "";
                if (sizeColorMap.getColorId() != null) {
                    Color color = colorDao.loadById(sizeColorMap.getColorId());
                    property = " (" + color.getName() + ")";
                }
                product = productDao.loadById(sizeColorMap.getProductId());
                category = categoryDao.loadById(product.getCategoryId());
                order = order + "                               <tr>"
                        + "                                        <td><img src=\"" + product.getImgurl() + "\"  alt=\"Duqhan\" style=\"border:0;width:115px\"/></td>"
                        + "                                        <td>"
                        + "                                            <table style=\"width:100%;border-collapse:collapse\">"
                        + "                                                <tr>"
                        + "                                                    <td>" + product.getName() + "</td>"
                        + "                                                </tr>"
                        + "                                                <tr>"
                        + "                                                    <td>" + category.getName() + property + "</td>"
                        + "                                                </tr>"
                        + "                                                <tr>"
                        + "                                                    <td style=\"color:rgb(0,102,153);font:Arial,sans-serif\">#" + orderDetail.getOrderId() + "</td>"
                        + "                                                </tr>"
                        + "                                            </table>"
                        + "                                        </td>"
                        + "                                        <td>Rs." + orderDetail.getPaymentAmount() + "</td>"
                        + "                                    </tr>";
            }
            body = "<table style=\"width:100%;border-collapse:collapse\">"
                    + "    <tr>"
                    + "        <td style=\"padding:0 20px 20px 20px;vertical-align:top;font-size:13px;line-height:18px;font-family:Arial,sans-serif\">"
                    + "            <table style=\"width:100%;border-collapse:collapse\">"
                    + "                <thead>"
                    + "                    <tr>"
                    + "                        <td><img src=\"https://storage.googleapis.com/duqhan-users/logo.png\"  alt=\"Duqhan\" src=\"\" style=\"border:0;width:100px\" /><hr></td>"
                    + "                    </tr>"
                    + "                </thead>"
                    + "                <tbody>"
                    + "                    <tr>"
                    + "                        <td><h3 style=\"font-size:18px;color:rgb(204,102,0);margin:15px 0 0 0;font-weight:normal\">Hello " + user.getName() + ",</h3></td>"
                    + "                    </tr>"
                    + "                    <tr>"
                    + "                        <td><span>Thank you for your order.</span></td>"
                    + "                    </tr>"
                    + "                    <tr>"
                    + "                        <td>"
                    + "                            <table style=\"border-top:3px solid rgb(250,144,5);width:95%;border-collapse:collapse\">"
                    + "                                <tbody>"
                    + addressPath
                    + "                                </tbody>"
                    + "                            </table>"
                    + "                        </td>"
                    + "                    </tr>"
                    + "                    <tr>"
                    + "                        <td><h3 style=\"font-size:18px;color:rgb(204,102,0);margin:15px 0 0 0;font-weight:normal\">Order Details</h3></td>"
                    + "                    </tr>"
                    + "                    <tr>"
                    + "                        <td>"
                    + "                            <table style=\"width:100%;border-collapse:collapse\">"
                    + "                                <tbody>"
                    + order
                    + "                                </tbody>"
                    + "                            </table>"
                    + "                        </td>"
                    + "                    </tr>"
                    + "                </tbody>"
                    + "            </table>"
                    + "        </td>"
                    + "    </tr>"
                    + "</table>";
            String status = MailSender.sendEmail(user.getEmail(), "Product purchase", body, ADMIN_MAIL + "," + BCC);// send mail to User at purchase.
            return status;
        } else {
            return "fail";
        }
    }

    @Override
    public String sendPurchaseMailToAdmin(OfferProductUsed offerProductUsed) {
        if (null != offerProductUsed) {
            String body = "";
            Users user = usersDao.loadById(offerProductUsed.getUserId());
            body += "Name: " + user.getName() + " (" + user.getId() + ")<br>Contact: " + user.getMobile() + "<br>Email: " + user.getEmail() + "<br><br>";
            long addressId = offerProductUsed.getAddressId();
            UserAddress address = userAddressDao.loadById(addressId);
            String addressPath = address.getStreetOne() + "<br>" + address.getStreetTwo() + "<br>" + address.getCity() + " " + address.getZipCode() + "<br>" + address.getState() + "<br>" + address.getPhone() + "<br><br>";
            Product product;
            ProductSizeColorMap sizeColorMap;
            String order;
            sizeColorMap = productSizeColorMapDao.loadById(offerProductUsed.getMapId());
            String property = "";
            if (sizeColorMap.getColorId() != null) {
                Color color = colorDao.loadById(sizeColorMap.getColorId());
                property = color.getName();
            }
            product = productDao.loadById(sizeColorMap.getProductId());
            order = "<b>Order Id: Free product</b><br>Product: " + product.getName() + " (" + product.getId() + ")<br>Specification: " + property + "<br>Quantity: 1<br>Price: 0.00<br><br>Delivery address :<br>";
            order += addressPath;
            body += order;
            String status = MailSender.sendEmail(ADMIN_MAIL, "Product purchase(Free)", body, BCC);// send mail to Admin at purchase.
            return status;
        } else {
            return "fail";
        }
    }

    @Override
    public String sendPurchaseMailToUser(OfferProductUsed offerProductUsed) {
        if (null != offerProductUsed) {
            String body = "";
            Users user = usersDao.loadById(offerProductUsed.getUserId());
            long addressId = offerProductUsed.getAddressId();
            UserAddress address = userAddressDao.loadById(addressId);
            String addressPath = "                             <tr> "
                    + "                                        <td style=\"font-size:14px;padding:11px 18px 18px 18px;background-color:rgb(250,244,237);width:50%;vertical-align:top;line-height:18px;font-family:Arial,sans-serif\"> "
                    + "                                            <p style=\"margin:2px 0 9px 0;font:14px Arial,sans-serif\"> <span style=\"font-size:14px;color:rgb(89,145,57)\">Your order will be sent to:</span><br> "
                    + "                                                <b> " + user.getName() + " <br>"
                    + address.getStreetOne() + "," + (address.getStreetTwo() != null ? address.getStreetTwo() : "") + "<br>"
                    + address.getCity() + " " + address.getState() + " " + address.getZipCode() + "<br>"
                    + "                                                    India <br>" + address.getPhone()
                    + "                                                </b>"
                    + "                                            </p> "
                    + "                                        </td> "
                    + "                                    </tr> ";
            Product product;
            ProductSizeColorMap sizeColorMap;
            String order = "";
            Category category;
            sizeColorMap = productSizeColorMapDao.loadById(offerProductUsed.getMapId());
            String property = "";
            if (sizeColorMap.getColorId() != null) {
                Color color = colorDao.loadById(sizeColorMap.getColorId());
                property = " (" + color.getName() + ")";
            }
            product = productDao.loadById(sizeColorMap.getProductId());
            category = categoryDao.loadById(product.getCategoryId());
            order = order + "                               <tr>"
                    + "                                        <td><img src=\"" + product.getImgurl() + "\"  alt=\"Duqhan\" style=\"border:0;width:115px\"/></td>"
                    + "                                        <td>"
                    + "                                            <table style=\"width:100%;border-collapse:collapse\">"
                    + "                                                <tr>"
                    + "                                                    <td>" + product.getName() + "</td>"
                    + "                                                </tr>"
                    + "                                                <tr>"
                    + "                                                    <td>" + category.getName() + property + "</td>"
                    + "                                                </tr>"
                    + "                                                <tr>"
                    + "                                                    <td style=\"color:rgb(0,102,153);font:Arial,sans-serif\"># FREE </td>"
                    + "                                                </tr>"
                    + "                                            </table>"
                    + "                                        </td>"
                    + "                                        <td>Rs. 0.00</td>"
                    + "                                    </tr>";

            body = "<table style=\"width:100%;border-collapse:collapse\">"
                    + "    <tr>"
                    + "        <td style=\"padding:0 20px 20px 20px;vertical-align:top;font-size:13px;line-height:18px;font-family:Arial,sans-serif\">"
                    + "            <table style=\"width:100%;border-collapse:collapse\">"
                    + "                <thead>"
                    + "                    <tr>"
                    + "                        <td><img src=\"https://storage.googleapis.com/duqhan-users/logo.png\"  alt=\"Duqhan\" src=\"\" style=\"border:0;width:100px\" /><hr></td>"
                    + "                    </tr>"
                    + "                </thead>"
                    + "                <tbody>"
                    + "                    <tr>"
                    + "                        <td><h3 style=\"font-size:18px;color:rgb(204,102,0);margin:15px 0 0 0;font-weight:normal\">Hello " + user.getName() + ",</h3></td>"
                    + "                    </tr>"
                    + "                    <tr>"
                    + "                        <td><span>Thank you for your order.</span></td>"
                    + "                    </tr>"
                    + "                    <tr>"
                    + "                        <td>"
                    + "                            <table style=\"border-top:3px solid rgb(250,144,5);width:95%;border-collapse:collapse\">"
                    + "                                <tbody>"
                    + addressPath
                    + "                                </tbody>"
                    + "                            </table>"
                    + "                        </td>"
                    + "                    </tr>"
                    + "                    <tr>"
                    + "                        <td><h3 style=\"font-size:18px;color:rgb(204,102,0);margin:15px 0 0 0;font-weight:normal\">Order Details</h3></td>"
                    + "                    </tr>"
                    + "                    <tr>"
                    + "                        <td>"
                    + "                            <table style=\"width:100%;border-collapse:collapse\">"
                    + "                                <tbody>"
                    + order
                    + "                                </tbody>"
                    + "                            </table>"
                    + "                        </td>"
                    + "                    </tr>"
                    + "                </tbody>"
                    + "            </table>"
                    + "        </td>"
                    + "    </tr>"
                    + "</table>";
            String status = MailSender.sendEmail(user.getEmail(), "Product purchase", body, ADMIN_MAIL + "," + BCC);// send mail to User at purchase.
            return status;
        } else {
            return "fail";
        }
    }

}
