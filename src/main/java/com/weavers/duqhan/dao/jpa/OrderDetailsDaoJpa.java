/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.OrderDetailsDao;
import com.weavers.duqhan.domain.OrderDetails;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class OrderDetailsDaoJpa extends BaseDaoJpa<OrderDetails> implements OrderDetailsDao {

    public OrderDetailsDaoJpa() {
        super(OrderDetails.class, "OrderDetails");
    }

    @Override
    public List<OrderDetails> getDetailBypaymentIdAndUserId(String paymentKey, Long userId) {
        Query query = getEntityManager().createQuery("SELECT od FROM OrderDetails AS od WHERE od.paymentKey = :paymentKey AND od.userId=:userId");
        query.setParameter("paymentKey", paymentKey);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Object[]> getDetailByUserId(Long userId) {
        Query query = getEntityManager().createQuery("SELECT od, map FROM OrderDetails AS od, ProductSizeColorMap AS map WHERE od.userId=:userId AND od.mapId=map.id ORDER BY od.orderDate DESC");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

}
