/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.domain;

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
 * @author Android-3
 */
@Entity
@Table(name = "cart")
public class Cart extends BaseDomain {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private long userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "load_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date loadDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sizecolormap_id")
    private long sizecolormapId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }

    public long getSizecolormapId() {
        return sizecolormapId;
    }

    public void setSizecolormapId(long sizecolormapId) {
        this.sizecolormapId = sizecolormapId;
    }

}
