/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 *
 * @author Android-3
 */
public class RestApiHitter {

    //------------------JSON Read By Using Java Dependency (org.json) (net.sf.json-lib)----------------------//http://www.jsonschema2pojo.org/
    public static void readJsonFromUrl() throws IOException, JSONException {
        JSONObject JsonData = new JSONObject();

        JsonData.put("MID", "DIY12386817555501617");
        JsonData.put("TOKEN", "bKMfNxPPf_QdZppa");

        InputStream is = new URL("https://pguat.paytm.com/oltp/HANDLER_INTERNAL/checkBalance?JsonData=:" + JsonData).openStream();
        Object obj;
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();
//            obj = new ObjectMapper().readValue(jsonText, valueType);
//            return (T) obj;
            System.out.println("jsonText === " + jsonText);
        } finally {
            is.close();
        }
    }

    public static void main(String[] args) throws IOException {
        RestApiHitter.readJsonFromUrl();
    }
}
