/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.util;

import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paytm.merchant.CheckSumServiceHelper;


/**
 *
 * @author weaversAndroid
 */
public class PaytmConstants {

    /*public final static String MID = "DUQHAN23756765142856";
    public final static String MERCHANT_KEY = "%Bhyu4K9NMv8FAdJ";
    public final static String INDUSTRY_TYPE_ID = "Retail";
    public final static String CHANNEL_ID = "WEB";
    public final static String WEBSITE = "WEB_STAGING";
    public final static String PAYTM_BASE_URL = "https://pguat.paytm.com";  //  Staging   */  

    public final static String MID = "DUQHAN71559260066171";
    public final static String MERCHANT_KEY = "FzAhP41GLlI3sWEM";
    public final static String INDUSTRY_TYPE_ID = "Retail109";
    public final static String CHANNEL_ID = "WEB";
    public final static String WEBSITE = "DUQHANWEB";
    public final static String PAYTM_BASE_URL = "https://secure.paytm.in";  //  Production
    
    public final static String PAYTM_TRANSACTION_REQUEST_URL = PAYTM_BASE_URL + "/oltp-web/processTransaction";
    public final static String CALLBACK_URL = "/web/paytm-call-back";

    //******************Payment Status***************//
    /**
     * 01 = Transaction Successful 141 = Transaction canceled by customer after
     * landing on Payment Gateway Page. 227 = Payment Failed due to a Bank
     * Failure. Please try after some time. 325 = Duplicate order id. 810 = Page
     * closed by customer after landing on Payment Gateway Page. 8102 =
     * Transaction canceled by customer post login. Customer had sufficient
     * Wallet balance for completing transaction. 8103 = Transaction canceled by
     * customer post login. Customer had in-sufficient Wallet balance for
     * completing transaction.
     */
    public static final int CREATED = 0;
    public static final int APPROVED = 1;
    public static final int FAILED = 3;

    public static void main(String[] args) {
        TreeMap<String, String> parameters = new TreeMap<String, String>();

        parameters.put("ORDER_ID", "9876559999");
        parameters.put("CUST_ID", "CUST0011");
        parameters.put("TXN_AMOUNT", "21");

        parameters.put("MID", PaytmConstants.MID);
        parameters.put("CHANNEL_ID", PaytmConstants.CHANNEL_ID);
        parameters.put("INDUSTRY_TYPE_ID", PaytmConstants.INDUSTRY_TYPE_ID);
        parameters.put("WEBSITE", PaytmConstants.WEBSITE);
        parameters.put("MOBILE_NO", "9876543210");
        parameters.put("EMAIL", "test1@gmail.com");
        parameters.put("CALLBACK_URL", "http://localhost:8084/test");

        String checkSum = "";
        try {
            checkSum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(PaytmConstants.MERCHANT_KEY, parameters);
        } catch (Exception ex) {
            Logger.getLogger(PaytmConstants.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("checkSum == " + checkSum);
    }
}
