package com.weavers.duqhan.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "currency_code")
public class CurrencyCode extends BaseDomain{

	private static final long serialVersionUID = 1L;
	@Column(name = "code_name")
    private String code;
	@Column(name = "code_value")
    private Double value;
	@Column(name = "symbol")
    private String symbol;
	@Column(name = "inr_value")
    private Double inrValue;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Double getInrValue() {
		return inrValue;
	}
	public void setInrValue(Double inrValue) {
		this.inrValue = inrValue;
	}
	
	
}
