/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author weaversAndroid
 */
@Entity
@Table(name = "guest_token")
public class GuestFcmToken extends BaseDomain {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Size(min = 1, max = 500)
    @Column(name = "token")
    private String token;
    @Basic(optional = false)
    @Size(min = 1, max = 200)
    @Column(name = "uuid")
    private String uuid;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
    
    
    
    
}
