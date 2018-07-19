/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.easypost.exception.EasyPostException;
import com.easypost.model.Shipment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;
import com.paytm.merchant.CheckSumServiceHelper;
import com.weavers.duqhan.business.MailService;
import com.weavers.duqhan.business.NotificationService;
import com.weavers.duqhan.business.PaymentService;
import com.weavers.duqhan.business.ShippingService;
import com.weavers.duqhan.business.UsersService;
import com.weavers.duqhan.dao.CartDao;
import com.weavers.duqhan.dao.CategoryDao;
import com.weavers.duqhan.dao.CurrencyCodeDao;
import com.weavers.duqhan.dao.OrderDetailsDao;
import com.weavers.duqhan.dao.PaymentDetailDao;
import com.weavers.duqhan.dao.ProductDao;
import com.weavers.duqhan.dao.ProductPropertiesMapDao;
import com.weavers.duqhan.dao.ShipmentTableDao;
import com.weavers.duqhan.dao.UserAddressDao;
import com.weavers.duqhan.dao.UserAouthDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.domain.Cart;
import com.weavers.duqhan.domain.Category;
import com.weavers.duqhan.domain.CurrencyCode;
import com.weavers.duqhan.domain.OrderDetails;
import com.weavers.duqhan.domain.PaymentDetail;
import com.weavers.duqhan.domain.Product;
import com.weavers.duqhan.domain.ProductPropertiesMap;
import com.weavers.duqhan.domain.ShipmentTable;
import com.weavers.duqhan.domain.UserAddress;
import com.weavers.duqhan.domain.UserAouth;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.CheckoutPaymentBean;
import com.weavers.duqhan.dto.PaytmStatusJSONReader;
import com.weavers.duqhan.util.GenerateAccessToken;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.util.CurrencyConverter;
import com.weavers.duqhan.util.PayPalConstants;
import com.weavers.duqhan.util.PaytmConstants;
import com.weavers.duqhan.util.StatusConstants;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONObject;
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
    @Autowired
    MailService mailService;
    @Autowired
    ProductDao productDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ProductPropertiesMapDao productPropertiesMapDao;
    @Autowired
    CurrencyCodeDao currencyCodeDao;
    @Autowired
    UserAouthDao userAouthDao;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final Logger logger = Logger.getLogger(PaymentServiceImpl.class);

    private void diductProductQuentityFromCategory(Long productId, Long quantity) {
        Product product = productDao.loadById(productId);
        Category category = categoryDao.loadById(product.getCategoryId());
        category.setQuantity(category.getQuantity() - quantity);
        categoryDao.save(category);
        String[] categoryArray = product.getParentPath().split("=");
        for (String string : categoryArray) {
            try {
                category = categoryDao.loadById(Long.valueOf(string));
                category.setQuantity(category.getQuantity() - quantity);
                categoryDao.save(category);

            } catch (NumberFormatException | NullPointerException e) {
            }
        }
    }

    @Override
    public CheckoutPaymentBean transactionRequest(HttpServletRequest request, HttpServletResponse response, CartBean cartBean, Double shippingCost, List<Shipment> shipments,Users user) {
        CheckoutPaymentBean paymentBean = new CheckoutPaymentBean();
        CurrencyCode currencyCode = new CurrencyCode();
        String symbol = new String();
        if(Objects.nonNull(user.getCurrencyCode())&&!user.getCurrencyCode().isEmpty()){
        	currencyCode = currencyCodeDao.getCurrencyConversionCode(user.getCurrencyCode());
        	symbol=currencyCode.getCode();
        }else{
        	List<UserAouth> aouthUserL = userAouthDao.loadByUserId(user.getId());
        	if(Objects.nonNull(aouthUserL)&&!aouthUserL.isEmpty()) {
            	currencyCode = currencyCodeDao.getCurrencyConversionCode(aouthUserL.get(0).getCodeName());
            	symbol=currencyCode.getCode();
            } else {
            	currencyCode.setValue(1d);
            	symbol="INR";
            }
        }
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
            logger.error("(==E==)PayPalRESTException for PayPalResource.initConfig ", ex);
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
            String itemName = " ";
            Double inrRate = CurrencyConverter.convert("INR", "USD");//Return USD For 1 INR
            for (ProductBean productBean : productBeans) {
                itemCost = "0";
                Item item = new Item();
                itemName = productBean.getName();
                if (itemName.length() > 90) {
                    itemName = itemName.substring(0, 90).concat("...");
                }
//                HashMap<String, String> propertyMap = productBean.getPropertyMap();
//                for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
//                    String key = entry.getKey();
//                    String value = entry.getValue();
//                    itemName = itemName.concat(" " + value + ",");
//                }
                item.setName(itemName);//***********************
                item.setCurrency(PayPalConstants.CURRENCY);
                item.setQuantity(productBean.getQty());
                itemCost = df2.format(inrRate * (productBean.getDiscountedPrice()/currencyCode.getValue()));
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
            transaction.setDescription("Your Items");
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
                logger.error("(==E==)PayPal Payment Create Exception :", ex);
                return null;
            }

            returnUrl = createdPayment.getLinks().get(1).getHref();
            String[] parts = returnUrl.split("&token=");
            paypalToken = parts[1];
        } catch (Exception e) {
            logger.error("(==E==)Multi Exceptions at the time of payment ", e);
            return null;
        }
        if (createdPayment != null && createdPayment.getState().equalsIgnoreCase("created")) {
            PaymentDetail paymentDetail = new PaymentDetail();
            paymentDetail.setId(null);
            paymentDetail.setPayAmount(cartBean.getOrderTotal()*currencyCode.getInrValue());
            paymentDetail.setPaymentType("CREDITED");
            paymentDetail.setUserId(cartBean.getUserId());
            paymentDetail.setPaymentKey(createdPayment.getId());
            paymentDetail.setPaymentStatus(createdPayment.getState());
            paymentDetail.setAccessToken(accessToken);
            paymentDetail.setPaypalToken(paypalToken);
            paymentDetail.setPaytmTxnId("NA");
            paymentDetail.setGatewayType(StatusConstants.PAYPAL_GATEWAY);
            if (cartBean.getAppType() == 2) {
                paymentDetail.setAppType(StatusConstants.WEB_APP);
            } else {
                paymentDetail.setAppType(StatusConstants.MOBILE_APP);
            }
            try {
                paymentDetail.setPaymentDate(sdf.parse(createdPayment.getCreateTime()));
            } catch (ParseException ex) {
                logger.error("(==E==)Date ParseException :", ex);
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
                newAddress.setHouseNo(address.getHouseNo());
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
                    logger.error("(==E==)EasyPostException :shipment.lowestRate().getRate() = null", ex);
                }
            }
            Date date = new Date();
            for (ProductBean productBean : productBeans) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setId(null);
                orderDetails.setOrderId("OD" + Calendar.getInstance().getTimeInMillis());
                orderDetails.setMapId(productBean.getProductPropertiesMapId());//required
                orderDetails.setOrderDate(date);
                orderDetails.setPaymentAmount(productBean.getDiscountedPrice()/currencyCode.getValue());//required
                orderDetails.setPaymentKey(createdPayment.getId());
                orderDetails.setStatus(createdPayment.getState());
                orderDetails.setUserId(cartBean.getUserId());
                orderDetails.setQuentity(Long.valueOf(productBean.getQty()));//required
                orderDetails.setAddressId(addressId);//*******************not null
                orderDetails.setShipmentId(orderShipmentMap.get(productBean.getProductPropertiesMapId()));
                orderDetailsDao.save(orderDetails);
            }
            payKey = createdPayment.getId();
        }
        paymentBean.setPaymentUrl(returnUrl);
        paymentBean.setGateway(StatusConstants.PAYPAL_GATEWAY);
        paymentBean.setStatusCode(payKey);
        return paymentBean;
    }

    @Override
    public String getPayerInformation(String payerId, String paymentId, String accessToken) {
        String status = null;
        Payment createdPayment = new Payment();
        PaymentDetail paymentDetail = paymentDetailDao.getDetailBypaymentId(paymentId);
        List<OrderDetails> orderDetails = orderDetailsDao.getDetailBypaymentIdAndUserId(paymentId, paymentDetail.getUserId());
        //=============================buy shipment==========================//
//        List<ShipmentTable> shipmentTables = shipmentTableDao.getShipmentByPayKeyAndUserId(paymentId, paymentDetail.getUserId());
//        for (ShipmentTable shipmentTable : shipmentTables) {
//            Shipment shipment = shippingService.getShipmentByShipmentId(shipmentTable.getShipmentId());
//            try {
//                Shipment shipment1 = shippingService.BuyShipment(shipment);
//                shipmentTable.setRateId(shipment1.lowestRate().getId());
//                shipmentTable.setCreatedAt(new Date());
//                shipmentTable.setCustomsInfoId(shipment1.getCustomsInfo().getId());
//                shipmentTable.setIsReturn(false);
//                shipmentTable.setParcelId(shipment1.getParcel().getId());
//                shipmentTable.setPostageLabelId(shipment1.getPostageLabel().getId());
//                shipmentTable.setShipmentId(shipment1.getId());
//                shipmentTable.setStatus(shipment1.getStatus());
//                shipmentTable.setTrackerId(shipment1.getTracker().getId());
//                shipmentTableDao.save(shipmentTable);
//            } catch (EasyPostException | NullPointerException ex) {
//                shipmentTable.setStatus("can_not_buy");
//
//                shipmentTableDao.save(shipmentTable);
//                Logger.getLogger(PaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
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
            logger.error("(==E==)PayPalRESTException :whene get payer information by success url", ex);
        }
        if (status != null) {
            if (status.equals("approved")) {
                HashMap<Long, Long> orderDetailsMap = new HashMap<>();
                paymentDetail.setPaymentStatus(status);
                paymentDetail.setPayerId(payerId);
                paymentDetailDao.save(paymentDetail);
                for (OrderDetails orderDetail : orderDetails) {
                    orderDetailsMap.put(orderDetail.getMapId(), null);
                    orderDetail.setStatus(status);
                    ProductPropertiesMap propertyMap = productPropertiesMapDao.loadById(orderDetail.getMapId());
                    try {
                        this.diductProductQuentityFromCategory(propertyMap.getProductId().getId(), orderDetail.getQuentity());
                    } catch (Exception e) {
                    }
                    propertyMap.setQuantity(propertyMap.getQuantity() - orderDetail.getQuentity());
                    productPropertiesMapDao.save(propertyMap);
                    orderDetailsDao.save(orderDetail);
                }
                List<Cart> carts = cartDao.getCartByUserId(paymentDetail.getUserId());
                for (Cart cart : carts) {
                    if (orderDetailsMap.containsKey(cart.getProductPropertyMapId())) {
                        cartDao.delete(cart);
                    }
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
        /*notificationService.sendPaymentNotification(paymentDetail.getUserId(), status);*///fcm token not useed
        return status;
    }

    @Override
    public Object[] storePaytmFeedBack(HttpServletRequest request) {
        Object[] feedback = new Object[2];
        String status = "";
        Integer appType = 5;
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, String[]> mapData = request.getParameterMap();
        TreeMap<String, String> parameters = new TreeMap<>();
        String paytmChecksum = "";
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if (paramName.equals("CHECKSUMHASH")) {
                paytmChecksum = mapData.get(paramName)[0];
            } else {
                parameters.put(paramName, mapData.get(paramName)[0]);
            }
        }
        boolean isValideChecksum = false;
        try {
            isValideChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(PaytmConstants.MERCHANT_KEY, parameters, paytmChecksum);
            if (isValideChecksum && parameters.containsKey("RESPCODE")) {
                PaymentDetail paymentDetail = paymentDetailDao.getDetailBypaymentId(parameters.get("ORDERID"));
                List<OrderDetails> orderDetails = orderDetailsDao.getDetailBypaymentIdAndUserId(parameters.get("ORDERID"), paymentDetail.getUserId());
                appType = paymentDetail.getAppType();
                if (parameters.get("RESPCODE").equals("01")) {
                    status = StatusConstants.PPS_APPROVED;
                    HashMap<Long, Long> orderDetailsMap = new HashMap<>();
                    paymentDetail.setPaymentStatus(status);
                    paymentDetail.setPayerId("NA");
//                    paymentDetails.setBankName(parameters.get("BANKNAME"));
//                    paymentDetails.setBanktxnId(parameters.get("BANKTXNID"));
//                    paymentDetails.setPaymentMode(parameters.get("PAYMENTMODE"));
//                    paymentDetails.setPaytmTxnId(parameters.get("TXNID"));
//                    paymentDetails.setCurrency(parameters.get("CURRENCY"));
//                    paymentDetails.setGatewayName(parameters.get("GATEWAYNAME"));
                    paymentDetailDao.save(paymentDetail);
                    for (OrderDetails orderDetail : orderDetails) {
                        orderDetailsMap.put(orderDetail.getMapId(), null);
                        orderDetail.setStatus(status);
                        ProductPropertiesMap propertyMap = productPropertiesMapDao.loadById(orderDetail.getMapId());
                        try {
                            this.diductProductQuentityFromCategory(propertyMap.getProductId().getId(), orderDetail.getQuentity());
                        } catch (Exception e) {
                        }
                        propertyMap.setQuantity(propertyMap.getQuantity() - orderDetail.getQuentity());
                        productPropertiesMapDao.save(propertyMap);
                        orderDetailsDao.save(orderDetail);
                    }
                    List<Cart> carts = cartDao.getCartByUserId(paymentDetail.getUserId());
                    for (Cart cart : carts) {
                        if (orderDetailsMap.containsKey(cart.getProductPropertyMapId())) {
                            cartDao.delete(cart);
                        }
                    }

                } else {
                    paymentDetail.setRemarks(parameters.get("RESPCODE") + ": " + parameters.get("RESPMSG"));
                    status = StatusConstants.PPS_FAILED;
                    paymentDetail.setPaymentStatus(status);
                    paymentDetailDao.save(paymentDetail);
                    for (OrderDetails orderDetail : orderDetails) {
                        orderDetail.setStatus(status);
                        orderDetailsDao.save(orderDetail);
                    }
                    System.out.println("(=====)Paytm: Payment not aproved. " + parameters.get("RESPMSG"));
                    logger.error("(==E==)Paytm: Payment not aproved. " + parameters.get("RESPMSG"));
                }

            } else {
                System.out.println("(=====)Paytm: Checksum mismatch.");
                logger.error("(==E==)Paytm: Checksum mismatch.");
            }
        } catch (Exception e) {
        }
        feedback[0] = status;
        feedback[1] = appType;
        return feedback;
    }

    @Override
    public String[] getPaymentStatus(Long userId, String payKey) {
        PaymentDetail paymentDetail = paymentDetailDao.getDetailBypaymentId(payKey);
        DecimalFormat df2 = new DecimalFormat(".##");
        String[] responseArray = new String[2];
        responseArray[1] = df2.format(paymentDetail.getPayAmount());
        int gateway = paymentDetail.getGatewayType();
        String status = StatusConstants.ARS_RETRY;
        List<OrderDetails> orderDetails = orderDetailsDao.getDetailBypaymentIdAndUserId(payKey, userId);
        List<ShipmentTable> shipmentTables = shipmentTableDao.getShipmentByPayKeyAndUserId(payKey, userId);
        boolean flag = false;
        //=============================check payment status===========================//
        if (gateway == StatusConstants.PAYPAL_GATEWAY) {
            Payment payment = new Payment();
            try {
                APIContext apiContext = new APIContext(paymentDetail.getAccessToken());
                payment = Payment.get(apiContext, payKey);
                status = payment.getState();
            } catch (PayPalRESTException e) {
                logger.error("(==E==)PayPalRESTException :getPaymentStatus by pay key", e);
            }
            if (payment.getState() != null && !status.equals(StatusConstants.ARS_RETRY)) {
                if (payment.getState().equals(StatusConstants.PPS_APPROVED)) {
                    flag = true;
                } else if (payment.getState().equals(StatusConstants.PPS_CREATED)) {
                    paymentDetail.setRemarks("Payment process not completed");
                    status = StatusConstants.PPS_FAILED;
                }
            }
        } else if (gateway == StatusConstants.PAYTM_GATEWAY) {
            PaytmStatusJSONReader jSONReader = null;
            TreeMap<String, String> parameters = new TreeMap<>();
            parameters.put("MID", PaytmConstants.MID);
            parameters.put("ORDER_ID", payKey);
            String checkSum = this.genrateCheckSum(parameters);
            parameters.put("CHECKSUMHASH", checkSum);
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                JSONObject obj = new JSONObject(parameters);
                String urlParameters = obj.toString();
                urlParameters = URLEncoder.encode(urlParameters);

                URL url = new URL(PaytmConstants.PAYTM_BASE_URL + "/oltp/HANDLER_INTERNAL/getTxnStatus?");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("contentType", "application/json");

                connection.setUseCaches(false);
                connection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes("JsonData=");
                wr.writeBytes(urlParameters);
                wr.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                String jsonData = response.toString();
                ObjectMapper mapper = new ObjectMapper();
                jSONReader = mapper.readValue(jsonData, PaytmStatusJSONReader.class);
                System.out.println("getPaymentStatus response...........................................................");
                System.out.println(jsonData);
            } catch (Exception e) {
                status = StatusConstants.PPS_FAILED;
            }
            if (jSONReader != null && jSONReader.getRESPCODE() != null) {
                if (status != null && jSONReader.getRESPCODE().equals("01")) {
                    status = StatusConstants.PPS_APPROVED;
                    flag = true;
                } else {
                    paymentDetail.setRemarks("Payment process not completed");
                    status = StatusConstants.PPS_FAILED;
                }
            } else {
                paymentDetail.setRemarks("Payment process not completed");
                status = StatusConstants.PPS_FAILED;
            }
        }

        if (flag) {
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
                    logger.error("(==E==)PayPalRESTException : At buy shipment", ex);
                }
            }
//                status = paymentDetailDao.getPamentStatusBypaymentIdAndUserId(payKey, userId);
            mailService.sendPurchaseMailToAdmin(orderDetails);
            mailService.sendPurchaseMailToUser(orderDetails);
        } else {
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
        responseArray[0] = status;
        return responseArray;
    }

    @Override
    public int getApplicationType(String token) {
        PaymentDetail paymentDetail = paymentDetailDao.getPaymentDetailByPaypalToken(token);
        return paymentDetail.getAppType();
    }

    @Override
    public String genrateCheckSum(TreeMap<String, String> parameters) {
        String checkSum = "";
        try {
            checkSum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(PaytmConstants.MERCHANT_KEY, parameters);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PaytmConstants.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checkSum;
    }

    @Override
    public CheckoutPaymentBean transactionRequest(Users user, CartBean cartBean, Double shippingCost, List<Shipment> shipments) {// String orderId, String amount, 
    	String baseUrl = "https://duqhan.com";
    	CurrencyCode currencyCode = new CurrencyCode();
        String symbol = new String();
        if(Objects.nonNull(user.getCurrencyCode())&&!user.getCurrencyCode().isEmpty()){
        	currencyCode = currencyCodeDao.getCurrencyConversionCode(user.getCurrencyCode());
        	symbol=currencyCode.getCode();
        }else{
        	List<UserAouth> aouthUserL = userAouthDao.loadByUserId(user.getId());
        	if(Objects.nonNull(aouthUserL)&&!aouthUserL.isEmpty()) {
            	currencyCode = currencyCodeDao.getCurrencyConversionCode(aouthUserL.get(0).getCodeName());
            	symbol=currencyCode.getCode();
            } else {
            	currencyCode.setValue(1d);
            	symbol="INR";
            }
        }
    	String payKey = "PAYKEY" + Calendar.getInstance().getTimeInMillis();
        DecimalFormat df2 = new DecimalFormat(".##");
        String amount = df2.format(cartBean.getOrderTotal()*currencyCode.getInrValue());
        List<ProductBean> productBeans = cartBean.getProducts();
        CheckoutPaymentBean paymentBean = new CheckoutPaymentBean();
        TreeMap<String, String> parameters = new TreeMap<String, String>();

        parameters.put("ORDER_ID", payKey);
        parameters.put("CUST_ID", user.getId().toString());
        parameters.put("TXN_AMOUNT", amount);

        parameters.put("MID", PaytmConstants.MID);
        parameters.put("CHANNEL_ID", PaytmConstants.CHANNEL_ID);
        parameters.put("INDUSTRY_TYPE_ID", PaytmConstants.INDUSTRY_TYPE_ID);
        parameters.put("WEBSITE", PaytmConstants.WEBSITE);
        parameters.put("MOBILE_NO", user.getMobile());
        parameters.put("EMAIL", user.getEmail());

        parameters.put("CALLBACK_URL", baseUrl + PaytmConstants.CALLBACK_URL);   // TODO: have to chack.

        String checkSum = this.genrateCheckSum(parameters);

        parameters.put("CHECKSUMHASH", checkSum);

        paymentBean.setParameters(parameters);
//        paymentBean.setPaymentUrl(PaytmConstants.PAYTM_TRANSACTION_REQUEST_URL);
        paymentBean.setPaymentUrl(baseUrl + "/web/paytmpayment");
        paymentBean.setGateway(StatusConstants.PAYTM_GATEWAY);
        paymentBean.setStatusCode(payKey);
        if (checkSum == null || checkSum.equals("")) {
            return null;
        }
        /////////////////start
        Date date = new Date();
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setId(null);
        try {
            paymentDetail.setPayAmount(Double.valueOf(amount));
        } catch (NumberFormatException | NullPointerException e) {
            paymentDetail.setPayAmount(0.0);
        }
        paymentDetail.setPaymentType("CREDITED");
        paymentDetail.setUserId(cartBean.getUserId());
        paymentDetail.setPaymentKey(payKey);    // reuse
        paymentDetail.setPaymentStatus(StatusConstants.PPS_CREATED);
        paymentDetail.setAccessToken(parameters.get("CHECKSUMHASH"));  // reuse
        paymentDetail.setPaypalToken("NA");
        if (cartBean.getAppType() == 2) {
            paymentDetail.setAppType(StatusConstants.WEB_APP);
        } else {
            paymentDetail.setAppType(StatusConstants.MOBILE_APP);
        }
        paymentDetail.setPaymentDate(date);
        paymentDetail.setPaytmTxnId("0");
        paymentDetail.setGatewayType(StatusConstants.PAYTM_GATEWAY);
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
            newAddress.setHouseNo(address.getHouseNo());
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
                    shipmentTable.setPayKey(payKey);
                    shipmentTableDao.save(shipmentTable);
                } else {
                    logger.error("(==E==)shipment.lowestRate().getRate() = null");
                    return null;
                }
            } catch (EasyPostException ex) {
                logger.error("(==E==)EasyPostException :shipment.lowestRate().getRate() = null", ex);
            }
        }
        for (ProductBean productBean : productBeans) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setId(null);
            orderDetails.setOrderId("OD" + Calendar.getInstance().getTimeInMillis());
            orderDetails.setMapId(productBean.getProductPropertiesMapId());//required
            orderDetails.setOrderDate(date);
            orderDetails.setPaymentAmount(productBean.getDiscountedPrice()/currencyCode.getValue());//required
            orderDetails.setPaymentKey(payKey);
            orderDetails.setStatus(StatusConstants.PPS_CREATED);
            orderDetails.setUserId(cartBean.getUserId());
            orderDetails.setQuentity(Long.valueOf(productBean.getQty()));//required
            orderDetails.setAddressId(addressId);//*******************not null
            orderDetails.setShipmentId(orderShipmentMap.get(productBean.getProductPropertiesMapId()));
            orderDetailsDao.save(orderDetails);
        }
        ////////////////////end
        return paymentBean;
    }

}
