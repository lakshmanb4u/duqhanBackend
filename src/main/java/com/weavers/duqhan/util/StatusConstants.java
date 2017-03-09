/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.util;

/**
 *
 * @author weaversAndroid
 */
public class StatusConstants {
//********************************Paypal Payment Status*********************************//

    public static final String PPS_CREATED = "created";     // when a transaction is create but buyer not accepted.
    public static final String PPS_APPROVED = "approved";   // when buyer accept the transaction.
    public static final String PPS_FAILED = "failed";       // when buyer pay but request cannot success.
    public static final String PPS_CANCELLED = "cancelled"; // when buyer not accepted to pay.

//*******************************Easypost Shipment Status*******************************//
    public static final String ESS_CREATED = "created";     // when a shipment create but not buy.
    public static final String ESS_APPROVED = "approved";   // when created shipment successfully buy.
    public static final String ESS_FAILED = "failed";       // when created shipment can not buy successfully due to some exception.

    public static final String ESS_UNKNOWN = "unknown";       //
    public static final String ESS_PRE_TRANSIT = "pre_transit";       //
    public static final String ESS_IN_TRANSIT = "in_transit";       //
    public static final String ESS_OUT_FOR_DELIVERY = "out_for_delivery";       //
    public static final String ESS_DELIVERED = "delivered";       //

//******************************Application Response Status*****************************//
    public static final String ARS_RETRY = "retry";         // Requist not success.
    public static final Boolean IS_SHIPMENT = false;
    public static final String NEW_RAGISTRATION = "registration";   //For new registration
    public static final String LOGIN = "login";     //For login
    public static final String REQUEST_FOR_RETURN = "requested";    // When user cancel a order
}
