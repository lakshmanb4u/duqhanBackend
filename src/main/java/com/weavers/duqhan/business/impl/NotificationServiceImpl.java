/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.weavers.duqhan.business.NotificationService;
import com.weavers.duqhan.dao.PaymentDetailDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.domain.PaymentDetail;
import com.weavers.duqhan.dto.PushNotificationRequestDto;
import com.weavers.duqhan.util.NotificationPusher;
import com.weavers.duqhan.util.StatusConstants;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author weaversAndroid
 */
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    UsersDao usersDao;
    @Autowired
    PaymentDetailDao paymentDetailDao;

    private final Logger logger = Logger.getLogger(NotificationServiceImpl.class);

    @Override
    public void sendPaymentNotification(Long userId, String paymentStatus) {
        String fcmToken = usersDao.GetFcmTokenByuserId(userId);
        if (fcmToken != null) {
            if (paymentStatus.equals(StatusConstants.PPS_APPROVED)) {
                paymentStatus = "Payment made successfully";
            } else {
                paymentStatus = "Payment cancel";
            }
            PushNotificationRequestDto requestDto = new PushNotificationRequestDto();
            Map<String,String> data = new HashMap<>();
            data.put("payment", "Done");
            requestDto.setData(data);
            requestDto.setTitle("Payment Status");
            requestDto.setBody(paymentStatus);
            requestDto.setTo(fcmToken);
            NotificationPusher.pushOnSingleDevice(requestDto);
        } else {
            logger.info("(==I==)FcmToken not found for this User id :" + userId);
        }
    }

    @Override
    public void sendPaymentNotification(String paypalToken) {
        String paymentStatus = "";
        PaymentDetail paymentDetail = paymentDetailDao.getPaymentDetailByPaypalToken(paypalToken);
        String fcmToken = usersDao.GetFcmTokenByuserId(paymentDetail.getUserId());
        if (fcmToken != null) {
            if (paymentDetail.getPaymentStatus().equals(StatusConstants.PPS_APPROVED)) {
                paymentStatus = "Payment made successfully";
            } else {
                paymentStatus = "Payment cancel";
            }
            PushNotificationRequestDto requestDto = new PushNotificationRequestDto();
            Map<String,String> data = new HashMap<>();
            data.put("payment", "Done");
            requestDto.setData(data);
            requestDto.setTitle("Payment Status");
            requestDto.setBody(paymentStatus);
            requestDto.setTo(fcmToken);
            NotificationPusher.pushOnSingleDevice(requestDto);
        } else {
            logger.info("(==I==)FcmToken not found for this User id :" + paymentDetail.getUserId());
        }
    }
}
