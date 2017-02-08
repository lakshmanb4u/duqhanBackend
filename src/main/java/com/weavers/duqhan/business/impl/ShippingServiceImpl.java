/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.weavers.duqhan.business.ShippingService;
import com.easypost.EasyPost;
import com.easypost.exception.EasyPostException;
import com.easypost.model.Address;
import com.easypost.model.CustomsItem;
import com.easypost.model.Shipment;
import com.easypost.model.Tracker;
import com.easypost.model.TrackingDetail;
import com.weavers.duqhan.dao.UserAddressDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.dao.VendorDao;
import com.weavers.duqhan.domain.UserAddress;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.domain.Vendor;
import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.util.EasyPostConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.weavers.duqhan.dao.ShipmentTableDao;
import com.weavers.duqhan.domain.ShipmentTable;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.dto.TrackerBean;
import com.weavers.duqhan.dto.TrackerDto;
import com.weavers.duqhan.util.DateFormater;

/**
 *
 * @author Android-3
 */
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    VendorDao vendorDao;
    @Autowired
    UserAddressDao userAddressDao;
    @Autowired
    UsersDao usersDao;
    @Autowired
    ShipmentTableDao shipmentTableDao;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ShippingServiceImpl.class);

    Map<String, Object> defaultFromAddress = new HashMap<String, Object>();
    Map<String, Object> defaultToAddress = new HashMap<String, Object>();
    Map<String, Object> defaultParcel = new HashMap<String, Object>();
    Map<String, Object> defaultCustomsItem = new HashMap<String, Object>();
    Map<String, Object> defaultCustomsInfo = new HashMap<String, Object>();

    public static void main(String[] args) {
        ShippingServiceImpl impl = new ShippingServiceImpl();
        AddressDto addressDto = new AddressDto();
//        addressDto.setCity("SF");
//        addressDto.setCompanyName("EasyPost");
//        addressDto.setCountry("US");
//        addressDto.setPhone("");
//        addressDto.setState("CA");
//        addressDto.setStreetOne("");
//        addressDto.setStreetTwo("5");
//        addressDto.setZipCode("94104");

//        addressDto.setCity("Kolkata");
//        addressDto.setCompanyName("");
//        addressDto.setCountry("IN");
//        addressDto.setPhone("9874433192");
//        addressDto.setState("westbengal");
//        addressDto.setStreetOne("33 rajadinendra street");
//        addressDto.setStreetTwo("");
//        addressDto.setZipCode("700009");
//        impl.getShipmentByShipmentId("shp_496320b580ac41c0ba8ce43cf9f7ee48");
        System.out.println("statusBean.getStatus() : " + impl.getTrackerByTrackerId("trk_f7e3633b9d6f49deb63cbf2575279a90").getStatus());
    }

    @Override
    public Shipment createDefaultShipmentDomestic() {// create defaflt shipment
        try {
            setUp();
            Map<String, Object> shipmentMap = new HashMap<String, Object>();
            shipmentMap.put("to_address", defaultToAddress);
            shipmentMap.put("from_address", defaultFromAddress);
            shipmentMap.put("parcel", defaultParcel);
            Shipment shipment = Shipment.create(shipmentMap);
            return shipment;
        } catch (EasyPostException ex) {
            Logger.getLogger(ShippingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void setUp() {
        EasyPost.apiKey = EasyPostConstants.API_KEY;

        defaultFromAddress.put("name", "MR. ZHIMIN LI");
        defaultFromAddress.put("name", "EasyPost");
        defaultFromAddress.put("street1", "164 Townsend St");
        defaultFromAddress.put("street2", "Unit 1");
        defaultFromAddress.put("city", "San Francisco");
        defaultFromAddress.put("state", "CA");
        defaultFromAddress.put("zip", "94107");
        defaultFromAddress.put("phone", "415-456-7890");

        defaultToAddress.put("company", "Airport Shipping");
        defaultToAddress.put("street1", "Senate House");
        defaultToAddress.put("city", "Allahabad");
        defaultToAddress.put("state", "UP");
        defaultToAddress.put("zip", "211002");
        defaultToAddress.put("country", "IN");

        defaultParcel.put("length", 10.8);
        defaultParcel.put("width", 8.3);
        defaultParcel.put("height", 6);
        defaultParcel.put("weight", 10);

        defaultCustomsItem.put("description", "Item descrpition");
        defaultCustomsItem.put("origin_country", "US");
        defaultCustomsItem.put("quantity", 1);
        defaultCustomsItem.put("value", 10.50);
        defaultCustomsItem.put("weight", 9.9);

        List<Map<String, Object>> customsItems = new ArrayList<Map<String, Object>>();
        customsItems.add(defaultCustomsItem);
        defaultCustomsInfo.put("customs_certify", true);
        defaultCustomsInfo.put("customs_signer", "Shipping Manager");
        defaultCustomsInfo.put("contents_type", "merchandise");
        defaultCustomsInfo.put("non_delivery_option", "return");
        defaultCustomsInfo.put("restriction_type", "none");
        defaultCustomsInfo.put("customs_items", customsItems);

    }

    private Map<String, Object> getFromAddress(Long vendorId) {
        Vendor vendor = vendorDao.loadById(vendorId);
        Map<String, Object> fromAddress = new HashMap<String, Object>();
        fromAddress.put("name", vendor.getVendorName());
        fromAddress.put("street1", vendor.getStreetOne());
        fromAddress.put("street2", null != vendor.getStreetTwo() ? vendor.getStreetTwo() : "");
        fromAddress.put("city", null != vendor.getCity() ? vendor.getCity() : "");
        fromAddress.put("state", null != vendor.getState() ? vendor.getState() : "");
        fromAddress.put("zip", null != vendor.getZip() ? vendor.getZip() : "");
        fromAddress.put("country", null != vendor.getCountry() ? vendor.getCountry() : "");
        fromAddress.put("phone", vendor.getPhone());
        return fromAddress;
    }

    private Map<String, Object> getToAddress(Long userAddressId) {
        UserAddress userAddress = userAddressDao.loadById(userAddressId);
        Users users = usersDao.loadById(userAddress.getUserId());
        Map<String, Object> toAddress = new HashMap<String, Object>();
        toAddress.put("name", null != users.getName() ? users.getName() : users.getEmail());
        toAddress.put("street1", userAddress.getStreetOne());
        toAddress.put("street2", null != userAddress.getStreetTwo() ? userAddress.getStreetTwo() : "");
        toAddress.put("city", null != userAddress.getCity() ? userAddress.getCity() : "");
        toAddress.put("state", null != userAddress.getState() ? userAddress.getState() : "");
        toAddress.put("zip", null != userAddress.getZipCode() ? userAddress.getZipCode() : "");
        toAddress.put("country", null != userAddress.getCountry() ? userAddress.getCountry() : "");
        return toAddress;
    }

    private Map<String, Object> getParcel(ProductBean product) {
        Map<String, Object> parcelMap = new HashMap<String, Object>();
        parcelMap.put("length", null != product.getProductLength() ? product.getProductLength() : 0.0);
        parcelMap.put("width", null != product.getProductWidth() ? product.getProductWidth() : 0.0);
        parcelMap.put("height", null != product.getProductHeight() ? product.getProductHeight() : 0.0);
        parcelMap.put("weight", product.getProductWeight());
        return parcelMap;
    }

    private Map<String, Object> getCustomsInfo(ProductBean product) {
        Vendor vendor = vendorDao.loadById(product.getVendorId());
        Map<String, Object> customsInfoMap = new HashMap<String, Object>();
        List<CustomsItem> customsItemsList = new ArrayList<CustomsItem>();
        Map<String, Object> customsItemMap = new HashMap<String, Object>();
        customsItemMap.put("description", product.getName());
        customsItemMap.put("quantity", Integer.parseInt(product.getQty()));
        customsItemMap.put("value", product.getDiscountedPrice());
        customsItemMap.put("weight", product.getProductWeight());
        customsItemMap.put("origin_country", vendor.getCountry());
        customsItemMap.put("hs_tariff_number", product.getSizeColorMapId().toString());
        CustomsItem customsItem1;
        try {
            customsItem1 = CustomsItem.create(customsItemMap);
            customsItemsList.add(customsItem1);
        } catch (EasyPostException ex) {
            Logger.getLogger(ShippingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        customsInfoMap.put("customs_certify", true);
        customsInfoMap.put("customs_signer", "Steve Brule");
        customsInfoMap.put("contents_type", "merchandise");
        customsInfoMap.put("contents_explanation", "");
        customsInfoMap.put("eel_pfc", "NOEEI 30.37(a)");
        customsInfoMap.put("non_delivery_option", "abandon");
        customsInfoMap.put("restriction_type", "none");
        customsInfoMap.put("restriction_comments", "");
        customsInfoMap.put("customs_items", customsItemsList);
        return customsInfoMap;
    }

    @Override
    public Object[] createShipments(CartBean cartBean) {
        EasyPost.apiKey = EasyPostConstants.API_KEY;
        List<ProductBean> products = cartBean.getProducts();
        float shippingCost = 0.0f;
        Object[] objects = new Object[2];
        List<Shipment> shipments = new ArrayList<>();
        try {
            Map<String, Object> toAddressMap = this.getToAddress(cartBean.getDeliveryAddressId());
            for (ProductBean product : products) {

                Map<String, Object> defaultFromAddress = this.getFromAddress(product.getVendorId());
                Map<String, Object> parcelMap = this.getParcel(product);
                Map<String, Object> customsInfoMap = this.getCustomsInfo(product);
                Map<String, Object> shipmentMap = new HashMap<String, Object>();
                shipmentMap.put("to_address", toAddressMap);
                shipmentMap.put("from_address", defaultFromAddress);
                shipmentMap.put("parcel", parcelMap);
                shipmentMap.put("customs_info", customsInfoMap);
                shipmentMap.put("return_address", defaultFromAddress);

                Shipment shipment = Shipment.create(shipmentMap);
                shipments.add(shipment);
                if (null != shipment.lowestRate().getRate()) {
                    shippingCost = shippingCost + shipment.lowestRate().getRate();
                }
            }
            objects[0] = Double.valueOf(String.valueOf(shippingCost));
            objects[1] = shipments;
            return objects;
        } catch (EasyPostException ex) {
            logger.error("Shipment cannot create cause " + ex);
            Logger.getLogger(ShippingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Shipment BuyShipment(Shipment shipment) {
        if (shipment != null && shipment.getRates() != null && !shipment.getRates().isEmpty()) {
            try {
                EasyPost.apiKey = EasyPostConstants.API_KEY;
                List<String> buyCarriers = new ArrayList<String>();
                buyCarriers.add(EasyPostConstants.BUY_CARRIER);
                shipment = shipment.buy(shipment.lowestRate(buyCarriers));
            } catch (EasyPostException ex) {
                Logger.getLogger(ShippingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } else {
            logger.error("Shipment cannot buy. ");
            Logger.getLogger(ShippingServiceImpl.class.getName()).log(Level.SEVERE, "Shipment cannot buy");
            return null;
        }
        return shipment;
    }

    @Override
    public CartBean getCartAfterShipment(CartBean cartBean) {
        EasyPost.apiKey = EasyPostConstants.API_KEY;
        List<ProductBean> products = cartBean.getProducts();
        try {
            Map<String, Object> toAddressMap = this.getToAddress(cartBean.getDeliveryAddressId());
            for (ProductBean product : products) {

                Map<String, Object> defaultFromAddress = this.getFromAddress(product.getVendorId());
                Map<String, Object> parcelMap = this.getParcel(product);
                Map<String, Object> customsInfoMap = this.getCustomsInfo(product);
                Map<String, Object> shipmentMap = new HashMap<String, Object>();
                shipmentMap.put("to_address", toAddressMap);
                shipmentMap.put("from_address", defaultFromAddress);
                shipmentMap.put("parcel", parcelMap);
                shipmentMap.put("customs_info", customsInfoMap);

                Shipment shipment = Shipment.create(shipmentMap);
                if (shipment != null && shipment.getRates() != null && !shipment.getRates().isEmpty()) {
                    product.setShippingTime(shipment.getRates().get(0).getDeliveryDays() != null ? shipment.getRates().get(0).getDeliveryDays().toString() : null);
                    product.setShippingRate(shipment.getRates().get(0).getRate() != null ? shipment.getRates().get(0).getRate().doubleValue() : null);
                } else {
                    product.setShippingTime("10");
                    product.setShippingRate(27.00);
                }
            }
            cartBean.setProducts(products);
            return cartBean;
        } catch (EasyPostException ex) {
            Logger.getLogger(ShippingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public StatusBean verifyAddress(AddressDto addressDto) {
        EasyPost.apiKey = EasyPostConstants.API_KEY;
        StatusBean statusBean = new StatusBean();
        Map<String, Object> addressHash = new HashMap<String, Object>();
        addressHash.put("street1", addressDto.getStreetOne()); // street1 will get cleaned up
        addressHash.put("street2", addressDto.getStreetTwo());
        addressHash.put("city", addressDto.getCity());
        addressHash.put("state", addressDto.getState());
        addressHash.put("zip", addressDto.getZipCode());
        addressHash.put("country", addressDto.getCountry());
        addressHash.put("company", addressDto.getCompanyName());
        addressHash.put("phone", addressDto.getPhone());

        List<String> verificationList = new ArrayList<>();
        verificationList.add("delivery");
        addressHash.put("verify", verificationList);

// This address will be verified
        Address address;
        try {
            address = Address.create(addressHash);
            System.out.println(address.getStreet1());
            if (address != null && address.getVerifications().get("delivery") != null) {
                if (address.getVerifications().get("delivery").getSuccess()) {
                    statusBean.setStatus("Address is deliverable");
                    statusBean.setStatusCode("200");
                } else {
                    List<com.easypost.model.Error> errors = address.getVerifications().get("delivery").getErrors();
                    StringBuilder errs = new StringBuilder();
                    for (com.easypost.model.Error error : errors) {
                        errs.append(error.getMessage());
                        errs.append(", ");
                    }
                    statusBean.setStatus(errs != null ? errs.toString() : "Error");
                    statusBean.setStatusCode("402");
                }

            } else {
                statusBean.setStatus("Address is not deliverable");
                statusBean.setStatusCode("402");
            }
        } catch (EasyPostException ex) {
            Logger.getLogger(ShippingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            statusBean.setStatus("Error in verification of address");
            statusBean.setStatusCode("500");
        }
        return statusBean;
    }

    @Override
    public Shipment getShipmentByShipmentId(String shipmentId) {
        EasyPost.apiKey = EasyPostConstants.API_KEY;
        Shipment shipment = null;
        try {
            shipment = Shipment.retrieve(shipmentId);
        } catch (EasyPostException ex) {
            Logger.getLogger(ShippingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shipment;
    }

    @Override
    public TrackerBean getTrackerByTrackerId(String trackerId) {
        EasyPost.apiKey = EasyPostConstants.API_KEY;
        Tracker tracker;
        TrackerBean trackerBean = new TrackerBean();
        try {
            tracker = Tracker.retrieve(trackerId);
            if (null != tracker) {
                List<TrackerDto> trackerDtos = new ArrayList<>();
                List<TrackingDetail> trackingDetails = tracker.getTrackingDetails();
                for (TrackingDetail trackingDetail : trackingDetails) {
                    TrackerDto trackerDto = new TrackerDto();
                    trackerDto.setCity(trackingDetail.getTrackingLocation().getCity());
                    trackerDto.setCountry(trackingDetail.getTrackingLocation().getCountry());
                    trackerDto.setState(trackingDetail.getTrackingLocation().getState());
                    trackerDto.setZip(trackingDetail.getTrackingLocation().getZip());
                    trackerDto.setDatetime(DateFormater.formate(trackingDetail.getDatetime()));
                    trackerDto.setMessage(trackingDetail.getMessage());
                    trackerDto.setStatus(trackingDetail.getStatus());
//                    trackerDto.setSource(trackingDetai);
                    trackerDtos.add(trackerDto);
                }
                trackerBean.setCarrier(tracker.getCarrier());
                trackerBean.setEstDeliveryDate(DateFormater.formate(tracker.getEstDeliveryDate()));
                trackerBean.setMode(tracker.getMode());
                trackerBean.setPublicUrl(tracker.getLabelUrl());
                trackerBean.setShipmentId(tracker.getShipmentId());
                trackerBean.setSignedBy(tracker.getSignedBy());
                trackerBean.setStatus(tracker.getStatus());
                trackerBean.setStatusCode("200");
                trackerBean.setTrackingCode(tracker.getTrackingCode());
                trackerBean.setTrackingDetails(trackerDtos);
//                trackerBean.setCreatedAt(tracker.get);
//                trackerBean.setUpdatedAt(tracker.);

            } else {
                logger.error("No Tracker found.");
                trackerBean.setStatus("No Tracker found.");
                trackerBean.setStatusCode("402");
            }
        } catch (EasyPostException ex) {
            Logger.getLogger(ShippingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("Problem to get Tracker.");
            trackerBean.setStatus("Problem to get Tracker.");
            trackerBean.setStatusCode("402");
        }
        return trackerBean;
    }

    @Override
    public ShipmentTable getShipmentTableByShipmentId(String shipmentId) {
        return shipmentTableDao.getShipmentByShipmentId(shipmentId);
    }

}
