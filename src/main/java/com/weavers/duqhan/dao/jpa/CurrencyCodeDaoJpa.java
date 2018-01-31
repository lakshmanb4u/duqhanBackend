package com.weavers.duqhan.dao.jpa;

import javax.persistence.Query;

import com.weavers.duqhan.dao.CurrencyCodeDao;
import com.weavers.duqhan.domain.CurrencyCode;

public class CurrencyCodeDaoJpa extends BaseDaoJpa<CurrencyCode> implements CurrencyCodeDao {

	CurrencyCodeDaoJpa() {
		super(CurrencyCode.class,"CurrencyCode");
	}
	@Override
    public CurrencyCode getCurrencyConversionCode(String code) {
		 try {
	            Query query = getEntityManager().createQuery("SELECT c FROM CurrencyCode As c WHERE c.code = :code");
	            query.setParameter("code", code);
	            return (CurrencyCode) query.getSingleResult();
	        } catch (Exception e) {
	            return null;
	        }
	}
}
