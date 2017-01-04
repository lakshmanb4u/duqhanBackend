/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.util;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Android-3
 */
public class Crypting {

    public static String encrypt(String original) {
        byte[] bytesEncoded = Base64.encodeBase64(original.getBytes());
        return new String(bytesEncoded);
    }

    public static String decrypt(String base64String) {
        byte[] valueDecoded = Base64.decodeBase64(base64String);
        return new String(valueDecoded);
    }
}
