/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao;

import com.weavers.duqhan.domain.OtpTable;

/**
 *
 * @author Android-3
 */
public interface OtpTableDao extends BaseDao<OtpTable> {

    boolean isValidOtp(Long userId, String email, String otp);

    OtpTable getValidOtp(Long userId, String email, String otp);
    
    OtpTable getOtpTableByUserId(Long userId);
}
