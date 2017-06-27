/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business;

import com.easypost.model.Shipment;
import com.weavers.duqhan.domain.PaymentDetail;
import com.weavers.duqhan.domain.ShipmentTable;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.ShipmentDto;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.dto.TrackerBean;
import java.util.List;

/**
 *
 * @author Android-3
 */
public interface ShippingService {

    StatusBean verifyAddress(AddressDto addressDto);

    Shipment createDefaultShipmentDomestic();

    CartBean getCartAfterShipment(CartBean cartBean);

    Object[] createShipments(CartBean cartBean);

    Shipment BuyShipment(Shipment confirmShipment);

    Shipment getShipmentByShipmentId(String shipmentId);

    TrackerBean getTrackerByTrackerId(ShipmentTable shipmentTable, String paymentStatus,PaymentDetail paymentDetail);

//  From Shipment Table
    ShipmentTable getShipmentTableByShipmentId(String shipmentId);
    
    List<ShipmentDto> getPendingShipmentButPaymentApproved();

}
