package com.weavers.duqhan.dao;

import com.weavers.duqhan.domain.CurrencyCode;

public interface CurrencyCodeDao extends BaseDao<CurrencyCode> {

	CurrencyCode getCurrencyConversionCode(String code);
}
