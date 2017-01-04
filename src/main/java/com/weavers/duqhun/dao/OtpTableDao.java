/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao;

import com.weavers.duqhun.domain.OtpTable;

/**
 *
 * @author Android-3
 */
public interface OtpTableDao extends BaseDao<OtpTable>{
    boolean isValidOtp(Long userId, String email);
}
