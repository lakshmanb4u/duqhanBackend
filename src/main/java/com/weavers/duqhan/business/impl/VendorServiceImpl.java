/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.weavers.duqhan.business.VendorService;
import com.weavers.duqhan.dao.VendorDao;
import com.weavers.duqhan.domain.Vendor;
import com.weavers.duqhan.dto.AddressDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Android-3
 */
public class VendorServiceImpl implements VendorService {

    @Autowired
    VendorDao vendorDao;
    private final Logger logger = Logger.getLogger(VendorServiceImpl.class);

    @Override
    public String saveVendor(AddressDto addressDto) {
        Vendor vendor = new Vendor();
        vendor.setId(null);
        vendor.setCity(addressDto.getCity());
        vendor.setCountry(addressDto.getCountry());
        vendor.setPhone(addressDto.getPhone());
        vendor.setState(addressDto.getState());
        vendor.setStreetOne(addressDto.getStreetOne());
        vendor.setStreetTwo(addressDto.getStreetTwo());
        vendor.setVendorName(addressDto.getContactName());
        vendor.setZip(addressDto.getZipCode());
        Vendor vendor2 = vendorDao.save(vendor);
        if (vendor2 == null) {
            return "ERROR: Vendor can not be saved!!";
        } else {
            return "Vendor saved..";
        }
    }

}
