/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Android-3
 */
@Entity
@Table(name = "store")
public class Store extends BaseDomain{

    private static final long serialVersionUID = 1L;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_id")
    private long productId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pro_size_color_id")
    private long proSizeColorId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quentity")
    private long quentity;


    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getProSizeColorId() {
        return proSizeColorId;
    }

    public void setProSizeColorId(long proSizeColorId) {
        this.proSizeColorId = proSizeColorId;
    }

    public long getQuentity() {
        return quentity;
    }

    public void setQuentity(long quentity) {
        this.quentity = quentity;
    }

    
}
