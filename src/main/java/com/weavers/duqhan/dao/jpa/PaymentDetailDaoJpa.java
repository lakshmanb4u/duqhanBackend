/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.PaymentDetailDao;
import com.weavers.duqhan.domain.PaymentDetail;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class PaymentDetailDaoJpa extends BaseDaoJpa<PaymentDetail> implements PaymentDetailDao {

    public PaymentDetailDaoJpa() {
        super(PaymentDetail.class, "PaymentDetail");
    }

    @Override
    public PaymentDetail getDetailBypaymentId(String paymentId) {
        try {
            Query query = getEntityManager().createQuery("SELECT pd FROM PaymentDetail AS pd WHERE pd.paymentKey = :paymentId");
            query.setParameter("paymentId", paymentId);
            return (PaymentDetail) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getPamentStatusBypaymentIdAndUserId(String paymentId, Long userId) {
        try {
            Query query = getEntityManager().createQuery("SELECT pd FROM PaymentDetail AS pd WHERE pd.paymentKey = :paymentId AND pd.userId=:userId");
            query.setParameter("paymentId", paymentId);
            query.setParameter("userId", userId);
            PaymentDetail paymentDetail = (PaymentDetail) query.getSingleResult();
            return paymentDetail.getPaymentStatus();
        } catch (Exception e) {
            return "No result found";
        }
    }

}
