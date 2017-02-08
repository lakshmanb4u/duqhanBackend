/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao;

import com.weavers.duqhan.domain.ShipmentTable;
import java.util.List;

/**
 *
 * @author weaversAndroid
 */
public interface ShipmentTableDao extends BaseDao<ShipmentTable> {

    List<ShipmentTable> getShipmentByPayKeyAndUserId(String paymentId, Long userId);

    ShipmentTable getShipmentByShipmentId(String shipmentId);
}
