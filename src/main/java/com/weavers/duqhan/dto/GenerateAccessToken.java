/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;

import com.paypal.core.ConfigManager;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;
import com.weavers.duqhan.util.PayPalConstants;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Android-3
 */
public class GenerateAccessToken {

    public static String getAccessToken() throws PayPalRESTException {

        // ###AccessToken
        // Retrieve the access token from
        // OAuthTokenCredential by passing in
        // ClientID and ClientSecret
        String clientID = ConfigManager.getInstance().getValue("clientID");
        String clientSecret = ConfigManager.getInstance().getValue(
                "clientSecret");
        Map<String, String> sdkConfig = new HashMap<>();
                sdkConfig.put("mode", PayPalConstants.MODE);
//        sdkConfig.put("mode", "live");

        String accessToken = new OAuthTokenCredential(clientID, clientSecret, sdkConfig).getAccessToken();

        return accessToken;
    }
}
