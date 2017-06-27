/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business;

/**
 *
 * @author weaversAndroid
 */
public interface NotificationService {

    void sendPaymentNotification(Long userId, String status);
    
    void sendPaymentNotification(String paypalToken);
}
