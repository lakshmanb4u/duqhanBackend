/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao;

import com.weavers.duqhan.domain.PaymentDetail;

/**
 *
 * @author Android-3
 */
public interface PaymentDetailDao extends BaseDao<PaymentDetail> {

    PaymentDetail getDetailBypaymentId(String paymentId);

    String getPamentStatusBypaymentIdAndUserId(String payKey, Long userId);

    PaymentDetail getDetailByPayKey(String paymentKey);
}
