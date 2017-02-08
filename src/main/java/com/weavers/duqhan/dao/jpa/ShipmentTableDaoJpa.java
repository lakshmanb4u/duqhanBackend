/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.domain.ShipmentTable;
import com.weavers.duqhan.dao.ShipmentTableDao;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author weaversAndroid
 */
public class ShipmentTableDaoJpa extends BaseDaoJpa<ShipmentTable> implements ShipmentTableDao {

    public ShipmentTableDaoJpa() {
        super(ShipmentTable.class, "Shipment");
    }

    @Override
    public List<ShipmentTable> getShipmentByPayKeyAndUserId(String payKey, Long userId) {
        Query query = getEntityManager().createQuery("SELECT st FROM ShipmentTable AS st WHERE st.payKey = :payKey AND st.userId=:userId");
        query.setParameter("payKey", payKey);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public ShipmentTable getShipmentByShipmentId(String shipmentId) {
        try {
            Query query = getEntityManager().createQuery("SELECT st FROM ShipmentTable AS st WHERE st.shipmentId = :shipmentId");
            query.setParameter("shipmentId", shipmentId);
            return (ShipmentTable) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
