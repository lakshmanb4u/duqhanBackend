/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.util;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Android-3
 */
public class Crypting {

    public static String encrypt(String original) {
        if (original != null) {
            byte[] bytesEncoded = Base64.encodeBase64(original.getBytes());
            return new String(bytesEncoded);
        } else {
            return null;
        }
    }

    public static String decrypt(String base64String) {
        if (base64String != null) {
            byte[] valueDecoded = Base64.decodeBase64(base64String);
            return new String(valueDecoded);
        } else {
            return null;
        }
    }
}
