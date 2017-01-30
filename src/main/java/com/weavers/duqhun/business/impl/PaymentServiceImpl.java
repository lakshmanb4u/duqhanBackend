/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.business.impl;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;
import com.weavers.duqhun.business.PaymentService;
import com.weavers.duqhun.dao.CartDao;
import com.weavers.duqhun.dao.OrderDetailsDao;
import com.weavers.duqhun.dao.PaymentDetailDao;
import com.weavers.duqhun.dao.ProductSizeColorMapDao;
import com.weavers.duqhun.dao.UserAddressDao;
import com.weavers.duqhun.dao.UsersDao;
import com.weavers.duqhun.domain.Cart;
import com.weavers.duqhun.domain.OrderDetails;
import com.weavers.duqhun.domain.PaymentDetail;
import com.weavers.duqhun.domain.ProductSizeColorMap;
import com.weavers.duqhun.domain.UserAddress;
import com.weavers.duqhun.dto.AddressDto;
import com.weavers.duqhun.dto.CartBean;
import com.weavers.duqhun.dto.GenerateAccessToken;
import com.weavers.duqhun.dto.ProductBean;
import com.weavers.duqhun.util.PayPalConstants;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Android-3
 */
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    UsersDao usersDao;
    @Autowired
    PaymentDetailDao paymentDetailDao;
    @Autowired
    OrderDetailsDao orderDetailsDao;
    @Autowired
    ProductSizeColorMapDao productSizeColorMapDao;
    @Autowired
    UserAddressDao userAddressDao;
    @Autowired
    CartDao cartDao;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String[] transactionRequest(HttpServletRequest request, HttpServletResponse response, CartBean cartBean) {
        String[] strArray = new String[2];
        String returnUrl = null;
        String payKey = null;
        InputStream is;
        List<ProductBean> productBeans = cartBean.getProducts();
        is = this.getClass().getResourceAsStream("/sdk_config.properties");
        try {
            PayPalResource.initConfig(is);
        } catch (PayPalRESTException ex) {
            System.out.println("PayPalRESTException=" + ex);
        }

        String accessToken = null;
        Payment createdPayment = new Payment();
        try {
            accessToken = GenerateAccessToken.getAccessToken();
            System.out.println("accessToken111111st " + accessToken);

            Map<String, String> sdkConfig = new HashMap<>();
            sdkConfig.put("mode", PayPalConstants.MODE);

            APIContext apiContext = new APIContext(accessToken);
            apiContext.setConfigurationMap(sdkConfig);
            List<Item> items = new ArrayList<>();
            for (ProductBean productBean : productBeans) {
                Item item = new Item();
                item.setName(productBean.getName() + " " + productBean.getSize() != null ? productBean.getSize() : "" + " " + productBean.getColor() != null ? productBean.getColor() : "");//***********************
                item.setCurrency(PayPalConstants.CURRENCY);
                item.setQuantity(productBean.getQty());//**********************
                item.setPrice(productBean.getDiscountedPrice().toString());//****************************
                items.add(item);
            }
            ItemList itemList = new ItemList();
            itemList.setItems(items);

            Amount amount = new Amount();
            amount.setCurrency(PayPalConstants.CURRENCY);
            amount.setTotal(cartBean.getOrderTotal().toString());

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");
            PayerInfo payerInfo = new PayerInfo();

            Transaction transaction = new Transaction();
            transaction.setDescription("creating a payment");
            transaction.setAmount(amount);
            transaction.setItemList(itemList);

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);

            String url = request.getRequestURL().toString();
            String uri = request.getRequestURI();
            String ctx = request.getContextPath();
            String base = url.substring(0, url.length() - uri.length()) + ctx;

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl(base + PayPalConstants.CANCEL_URL);
            redirectUrls.setReturnUrl(base + PayPalConstants.SUCCESS_URL);
            payment.setRedirectUrls(redirectUrls);

            try {
                createdPayment = payment.create(apiContext);
            } catch (PayPalRESTException ex) {
                System.out.println("");
                Logger.getLogger(PaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            returnUrl = createdPayment.getLinks().get(1).getHref();
        } catch (PayPalRESTException e) {
            System.out.println("PayPalRESTExceptionsdsa=" + e);
        }
        if (createdPayment.getState().equalsIgnoreCase("created")) {
            PaymentDetail paymentDetail = new PaymentDetail();
            paymentDetail.setId(null);
            paymentDetail.setPayAmount(cartBean.getOrderTotal());
            paymentDetail.setPaymentType("CREDITED");
            paymentDetail.setUserId(cartBean.getUserId());
            paymentDetail.setPaymentKey(createdPayment.getId());
            paymentDetail.setPaymentStatus(createdPayment.getState());
            paymentDetail.setAccessToken(accessToken);
            try {
                paymentDetail.setPaymentDate(sdf.parse(createdPayment.getCreateTime()));
            } catch (ParseException ex) {
                Logger.getLogger(PaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            paymentDetailDao.save(paymentDetail);

            //====================================//
            Long addressId = null;
            if (cartBean.getDeliveryAddressId() != null) {
                addressId = cartBean.getDeliveryAddressId();
            } else {
                AddressDto address = cartBean.getAddressDto();
                UserAddress newAddress = new UserAddress();
                newAddress.setId(null);
                newAddress.setCity(address.getCity());
                newAddress.setCompanyName(address.getCompanyName());
                newAddress.setContactName(address.getContactName());
                newAddress.setCountry(address.getCountry());
                newAddress.setEmail(address.getEmail());
                newAddress.setPhone(address.getPhone());
                newAddress.setResidential(address.getIsResidential());
                newAddress.setState(address.getState());
                newAddress.setStatus(2l);
                newAddress.setStreetOne(address.getStreetOne());
                newAddress.setStreetTwo(address.getStreetTwo());
                newAddress.setUserId(address.getUserId());
                newAddress.setZipCode(address.getZipCode());
                UserAddress newAddress1 = userAddressDao.save(newAddress);
                addressId = newAddress1.getId();
            }
            Date date = new Date();
            for (ProductBean productBean : productBeans) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setId(null);
                orderDetails.setOrderId("OD" + Calendar.getInstance().getTimeInMillis());
                orderDetails.setMapId(productBean.getSizeColorMapId());//*********************
                orderDetails.setOrderDate(date);
                orderDetails.setPaymentAmount(productBean.getDiscountedPrice());//*************
                orderDetails.setPaymentKey(createdPayment.getId());
                orderDetails.setStatus(createdPayment.getState());
                orderDetails.setUserId(cartBean.getUserId());
                orderDetails.setQuentity(Long.valueOf(productBean.getQty()));//***********************
                orderDetails.setAddressId(addressId);//*******************not null
                orderDetailsDao.save(orderDetails);
            }
            payKey = createdPayment.getId();
        }
        strArray[0] = returnUrl;
        strArray[1] = payKey;
        return strArray;
    }

    @Override
    public String getPayerInformation(String payerId, String paymentId, String accessToken) {
        String status = null;
        Payment createdPayment = new Payment();
        PaymentDetail paymentDetail = paymentDetailDao.getDetailBypaymentId(paymentId);
        List<OrderDetails> orderDetails = orderDetailsDao.getDetailBypaymentIdAndUserId(paymentId, paymentDetail.getUserId());
        try {

            Map<String, String> sdkConfig = new HashMap<>();
            sdkConfig.put("mode", PayPalConstants.MODE);

            APIContext apiContext = new APIContext(paymentDetail.getAccessToken());
            apiContext.setConfigurationMap(sdkConfig);

            Payment payment1 = new Payment();
            payment1.setId(paymentId);
            PaymentExecution paymentExecute = new PaymentExecution();
            paymentExecute.setPayerId(payerId);
            createdPayment = payment1.execute(apiContext, paymentExecute);

            status = createdPayment.getState();
        } catch (PayPalRESTException ex) {
            Logger.getLogger(PaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (status != null) {
            if (status.equals("approved")) {
                List<Cart> carts = cartDao.getCartByUserId(paymentDetail.getUserId());
                for (Cart cart : carts) {
                    cartDao.delete(cart);
                }
                paymentDetail.setPaymentStatus(status);
                paymentDetail.setPayerId(payerId);
                paymentDetailDao.save(paymentDetail);
                for (OrderDetails orderDetail : orderDetails) {
                    orderDetail.setStatus(status);
                    ProductSizeColorMap sizeColorMap = productSizeColorMapDao.loadById(orderDetail.getMapId());
                    sizeColorMap.setQuentity(sizeColorMap.getQuentity() - orderDetail.getQuentity());
                    productSizeColorMapDao.save(sizeColorMap);
                    orderDetailsDao.save(orderDetail);
                }

            } else {
                paymentDetail.setPaymentStatus(status);
                paymentDetailDao.save(paymentDetail);
                for (OrderDetails orderDetail : orderDetails) {
                    orderDetail.setStatus(status);
                    orderDetailsDao.save(orderDetail);
                }
            }
        } else {
            paymentDetail.setPaymentStatus("Return null");
            paymentDetailDao.save(paymentDetail);
            for (OrderDetails orderDetail : orderDetails) {
                orderDetail.setStatus("Return null");
                orderDetailsDao.save(orderDetail);
            }
        }
        return status;
    }

    @Override
    public String getPaymentStatus(Long userId, String payKey) {
        return paymentDetailDao.getPamentStatusBypaymentIdAndUserId(payKey, userId);
    }

}
