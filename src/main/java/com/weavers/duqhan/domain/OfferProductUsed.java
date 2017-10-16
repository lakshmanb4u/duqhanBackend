/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.domain;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author weaversAndroid
 */
@Entity
@Table(name = "offer_product_used")
public class OfferProductUsed extends BaseDomain {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private long userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_id")
    private long productId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "map_id")
    private long mapId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "address_id")
    private long addressId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actual_price")
    private double actualPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "offer_accepted_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date offerAcceptedOn;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Date getOfferAcceptedOn() {
        return offerAcceptedOn;
    }

    public void setOfferAcceptedOn(Date offerAcceptedOn) {
        this.offerAcceptedOn = offerAcceptedOn;
    }
}
