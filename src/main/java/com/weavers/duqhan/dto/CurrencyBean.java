/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;

import java.util.List;

import com.weavers.duqhan.domain.CurrencyCode;

/**
 *
 * @author Android-3
 */
public class CurrencyBean {

    private List<CurrencyCode> CurrencyCodes;
    private String status;
    private String statusCode;
    
    public List<CurrencyCode> getCurrencyCodes() {
		return CurrencyCodes;
	}
	public void setCurrencyCodes(List<CurrencyCode> currencyCodes) {
		CurrencyCodes = currencyCodes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	
    
}
