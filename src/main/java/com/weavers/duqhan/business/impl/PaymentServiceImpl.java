/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.easypost.exception.EasyPostException;
import com.easypost.model.Shipment;
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
import com.weavers.duqhan.business.NotificationService;
import com.weavers.duqhan.business.PaymentService;
import com.weavers.duqhan.business.ShippingService;
import com.weavers.duqhan.business.UsersService;
import com.weavers.duqhan.dao.CartDao;
import com.weavers.duqhan.dao.OrderDetailsDao;
import com.weavers.duqhan.dao.PaymentDetailDao;
import com.weavers.duqhan.dao.ProductSizeColorMapDao;
import com.weavers.duqhan.dao.ShipmentTableDao;
import com.weavers.duqhan.dao.UserAddressDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.domain.Cart;
import com.weavers.duqhan.domain.OrderDetails;
import com.weavers.duqhan.domain.PaymentDetail;
import com.weavers.duqhan.domain.ProductSizeColorMap;
import com.weavers.duqhan.domain.ShipmentTable;
import com.weavers.duqhan.domain.UserAddress;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.util.GenerateAccessToken;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.util.CurrencyConverter;
import com.weavers.duqhan.util.PayPalConstants;
import com.weavers.duqhan.util.StatusConstants;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
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
    @Autowired
    ShippingService shippingService;
    @Autowired
    ShipmentTableDao shipmentTableDao;
    @Autowired
    UsersService usersService;
    @Autowired
    NotificationService notificationService;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final Logger logger = Logger.getLogger(PaymentServiceImpl.class);

    @Override
    public String[] transactionRequest(HttpServletRequest request, HttpServletResponse response, CartBean cartBean, Double shippingCost, List<Shipment> shipments) {
        String[] strArray = new String[2];
        String returnUrl = null;
        String payKey = null;
        InputStream is;
        String paypalToken = null;
        DecimalFormat df2 = new DecimalFormat(".##");
        List<ProductBean> productBeans = cartBean.getProducts();
        is = this.getClass().getResourceAsStream("/sdk_config.properties");
        try {
            PayPalResource.initConfig(is);
        } catch (PayPalRESTException ex) {
            logger.error("(==E==)PayPalRESTException for PayPalResource.initConfig " , ex);
        }

        String accessToken = null;
        Payment createdPayment = new Payment();
        try {
            accessToken = GenerateAccessToken.getAccessToken();
            Map<String, String> sdkConfig = new HashMap<>();
            sdkConfig.put("mode", PayPalConstants.MODE);

            APIContext apiContext = new APIContext(accessToken);
            apiContext.setConfigurationMap(sdkConfig);
            List<Item> items = new ArrayList<>();
            Double totalItemCost = 0.0;
            String itemCost = "0";
            Double inrRate = CurrencyConverter.convert("INR", "USD");//Return USD For 1 INR
            for (ProductBean productBean : productBeans) {
                itemCost = "0";
                Item item = new Item();
                item.setName(productBean.getName() + " " + productBean.getSize() != null ? productBean.getSize() : "" + " " + productBean.getColor() != null ? productBean.getColor() : "");//***********************
                item.setCurrency(PayPalConstants.CURRENCY);
                item.setQuantity(productBean.getQty());
                itemCost = df2.format(inrRate * productBean.getDiscountedPrice());
                item.setPrice(itemCost);
                items.add(item);
                itemCost = df2.format(Double.parseDouble(itemCost) * Double.parseDouble(productBean.getQty()));
                totalItemCost = totalItemCost + Double.parseDouble(itemCost);
            }
            itemCost = "0";
            Item item = new Item();
            item.setName("Shipping Cost");
            item.setCurrency(PayPalConstants.CURRENCY);
            item.setQuantity("1");
            itemCost = df2.format(shippingCost);
            item.setPrice(itemCost);
            totalItemCost = totalItemCost + Double.parseDouble(itemCost);
            items.add(item);
            ItemList itemList = new ItemList();
            itemList.setItems(items);

            Amount amount = new Amount();
            amount.setCurrency(PayPalConstants.CURRENCY);
            amount.setTotal(df2.format(totalItemCost));
            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");
//            PayerInfo payerInfo = new PayerInfo();
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
                logger.error("(==E==)PayPal Payment Create Exception :" , ex);
                return null;
            }

            returnUrl = createdPayment.getLinks().get(1).getHref();
            String[] parts = returnUrl.split("&token=");
            paypalToken = parts[1];
        } catch (Exception e) {
            logger.error("(==E==)Multi Exceptions at the time of payment " , e);
            return null;
        }
        if (createdPayment != null && createdPayment.getState().equalsIgnoreCase("created")) {
            PaymentDetail paymentDetail = new PaymentDetail();
            paymentDetail.setId(null);
            paymentDetail.setPayAmount(cartBean.getOrderTotal());
            paymentDetail.setPaymentType("CREDITED");
            paymentDetail.setUserId(cartBean.getUserId());
            paymentDetail.setPaymentKey(createdPayment.getId());
            paymentDetail.setPaymentStatus(createdPayment.getState());
            paymentDetail.setAccessToken(accessToken);
            paymentDetail.setPaypalToken(paypalToken);
            try {
                paymentDetail.setPaymentDate(sdf.parse(createdPayment.getCreateTime()));
            } catch (ParseException ex) {
                logger.error("(==E==)Date ParseException :" , ex);
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
            //========================store shipment======================//
            HashMap<Long, String> orderShipmentMap = new HashMap<>();
            for (Shipment shipment : shipments) {
                try {
                    if (null != shipment.lowestRate().getRate()) {
                        orderShipmentMap.put(Long.valueOf(shipment.getCustomsInfo().getCustomsItems().get(0).getHsTariffNumber()), shipment.getId());
                        ShipmentTable shipmentTable = new ShipmentTable();
                        shipmentTable.setId(null);
                        shipmentTable.setCreatedAt(new Date());
                        shipmentTable.setCustomsInfoId("0");
                        shipmentTable.setIsReturn(false);
                        shipmentTable.setParcelId("0");
                        shipmentTable.setPostageLabelId("0");
                        shipmentTable.setRateId("0");
                        shipmentTable.setShipmentId(shipment.getId());
                        shipmentTable.setStatus(StatusConstants.ESS_CREATED);
                        shipmentTable.setTrackerId("0");
                        shipmentTable.setUserId(cartBean.getUserId());
                        shipmentTable.setPayKey(createdPayment.getId());
                        shipmentTableDao.save(shipmentTable);
                    } else {
                        logger.error("(==E==)shipment.lowestRate().getRate() = null");
                        return null;
                    }
                } catch (EasyPostException ex) {
                    logger.error("(==E==)EasyPostException :shipment.lowestRate().getRate() = null" , ex);
                }
            }
            Date date = new Date();
            for (ProductBean productBean : productBeans) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setId(null);
                orderDetails.setOrderId("OD" + Calendar.getInstance().getTimeInMillis());
                orderDetails.setMapId(productBean.getSizeColorMapId());//required
                orderDetails.setOrderDate(date);
                orderDetails.setPaymentAmount(productBean.getDiscountedPrice());//required
                orderDetails.setPaymentKey(createdPayment.getId());
                orderDetails.setStatus(createdPayment.getState());
                orderDetails.setUserId(cartBean.getUserId());
                orderDetails.setQuentity(Long.valueOf(productBean.getQty()));//required
                orderDetails.setAddressId(addressId);//*******************not null
                orderDetails.setShipmentId(orderShipmentMap.get(productBean.getSizeColorMapId()));
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
        /*//=============================buy shipment==========================//
        List<ShipmentTable> shipmentTables = shipmentTableDao.getShipmentByPayKeyAndUserId(paymentId, paymentDetail.getUserId());
        for (ShipmentTable shipmentTable : shipmentTables) {
            Shipment shipment = shippingService.getShipmentByShipmentId(shipmentTable.getShipmentId());
            try {
                Shipment shipment1 = shippingService.BuyShipment(shipment);
                shipmentTable.setRateId(shipment1.lowestRate().getId());
                shipmentTable.setCreatedAt(new Date());
                shipmentTable.setCustomsInfoId(shipment1.getCustomsInfo().getId());
                shipmentTable.setIsReturn(false);
                shipmentTable.setParcelId(shipment1.getParcel().getId());
                shipmentTable.setPostageLabelId(shipment1.getPostageLabel().getId());
                shipmentTable.setShipmentId(shipment1.getId());
                shipmentTable.setStatus(shipment1.getStatus());
                shipmentTable.setTrackerId(shipment1.getTracker().getId());
                shipmentTableDao.save(shipmentTable);
            } catch (EasyPostException | NullPointerException ex) {
                shipmentTable.setStatus("can_not_buy");

                shipmentTableDao.save(shipmentTable);
                Logger.getLogger(PaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
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
            logger.error("(==E==)PayPalRESTException :whene get payer information by success url" , ex);
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
        //========================send notification to user=====================//
        notificationService.sendPaymentNotification(paymentDetail.getUserId(), status);
        return status;
    }

    @Override
    public String getPaymentStatus(Long userId, String payKey) {
        PaymentDetail paymentDetail = paymentDetailDao.getDetailBypaymentId(payKey);
        String status = StatusConstants.ARS_RETRY;
        List<OrderDetails> orderDetails = orderDetailsDao.getDetailBypaymentIdAndUserId(payKey, userId);
        List<ShipmentTable> shipmentTables = shipmentTableDao.getShipmentByPayKeyAndUserId(payKey, userId);
        //=============================check payment status===========================//
        Payment payment = new Payment();
        try {
            APIContext apiContext = new APIContext(paymentDetail.getAccessToken());
            payment = Payment.get(apiContext, payKey);
            status = payment.getState();
        } catch (PayPalRESTException e) {
            logger.error("(==E==)PayPalRESTException :getPaymentStatus by pay key" , e);
        }

        if (payment.getState() != null && !status.equals(StatusConstants.ARS_RETRY)) {
            if (payment.getState().equals(StatusConstants.PPS_APPROVED)) {
                //=============================buy shipment==========================//
                for (ShipmentTable shipmentTable : shipmentTables) {
                    Shipment shipment = shippingService.getShipmentByShipmentId(shipmentTable.getShipmentId());
                    try {
                        Shipment shipment1 = shippingService.BuyShipment(shipment);
                        shipmentTable.setRateId(shipment1.lowestRate().getId());
                        shipmentTable.setCreatedAt(new Date());
                        shipmentTable.setCustomsInfoId(shipment1.getCustomsInfo().getId());
                        shipmentTable.setIsReturn(false);
                        shipmentTable.setParcelId(shipment1.getParcel().getId());
                        shipmentTable.setPostageLabelId(shipment1.getPostageLabel().getId());
                        shipmentTable.setShipmentId(shipment1.getId());
                        shipmentTable.setStatus(shipment1.getStatus());
                        shipmentTable.setTrackerId(shipment1.getTracker().getId());
                        shipmentTableDao.save(shipmentTable);
                    } catch (EasyPostException | NullPointerException ex) {
                        shipmentTable.setStatus(StatusConstants.ESS_FAILED);
                        shipmentTableDao.save(shipmentTable);
                        logger.error("(==E==)PayPalRESTException : At buy shipment" , ex);
                    }
                }
//                status = paymentDetailDao.getPamentStatusBypaymentIdAndUserId(payKey, userId);
            } else {
                status = payment.getState();
                if (payment.getState().equals(StatusConstants.PPS_CREATED)) {
                    status = StatusConstants.PPS_FAILED;
                }
                paymentDetail.setPaymentStatus(status);
                paymentDetailDao.save(paymentDetail);
                for (OrderDetails orderDetail : orderDetails) {
                    orderDetail.setStatus(status);
                    orderDetailsDao.save(orderDetail);
                }
                for (ShipmentTable shipmentTable : shipmentTables) {
                    shipmentTable.setStatus(StatusConstants.ESS_FAILED);
                    shipmentTableDao.save(shipmentTable);
                }
            }
        }
        return status;
    }

}
