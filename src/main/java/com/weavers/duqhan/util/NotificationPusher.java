/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.util;

import com.weavers.duqhan.dto.PushNotificationRequestDto;
import org.codehaus.jackson.map.ObjectMapper;
import com.weavers.duqhan.dto.PushNotificationResponseDto;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author weaversAndroid
 */
public class NotificationPusher {

    private final static Logger logger = Logger.getLogger(NotificationPusher.class);
    private final static String FCM_AUTHORIZATION_KEY = "AAAAWhGtBIE:APA91bH3TCeGyuVuSsqptAj_s-xupGUywdHjBVYDHcf0nMMRNUXtHY8zuKQF5fV884spW7srLIzcV2E--lLky8iPNHwuVlFpahC_tPE2bHWELZ_rhNbdEeE6yra9l1G4UX8weeolaQyC";
    private final static String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    public static PushNotificationResponseDto pushOnSingleDevice(PushNotificationRequestDto requestDto) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(FCM_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + FCM_AUTHORIZATION_KEY);
            String input = requestDto.getNotificationMessages();

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                logger.error("Send Notification Error. Response code : " + conn.getResponseCode());
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = br.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();

//            System.out.println("jsonText======== " + jsonText);
            PushNotificationResponseDto dto;
            dto = new ObjectMapper().readValue(jsonText, PushNotificationResponseDto.class);

//            System.out.println("=========== " + dto.getMulticastId());
            return dto;

        } catch (Exception e) {
//            logger.error("Send Notification Error :", e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

    }

//    public static void main(String[] args) {
//        PushNotificationRequestDto requestDto = new PushNotificationRequestDto();
//        Map<String, String> data = new HashMap<>();
//        data.put("payment", "Done");
//        requestDto.setData(data);
//        requestDto.setSound(Boolean.TRUE);
//        requestDto.setTitle("Payment Status");
//        requestDto.setBody("body_new");
//        requestDto.setTo("dTFjHWOSFaQ:APA91bH80v9cMkvI66PkA6rhQ42WSvrOjOpH0PWs126uZRDFJHDD_ic1AM50RGSH2qu2AhCmvfCoPXvTwexOcuaMrk2JtpWnYXhXqNKr1nqAU38rvHO5WLPGTXOl1ze1BVYbqyJVNGel");
//        System.out.println("requestDto=== " + requestDto.getNotificationMessages());
//        NotificationPusher.pushOnSingleDevice(requestDto);
//    }
}
