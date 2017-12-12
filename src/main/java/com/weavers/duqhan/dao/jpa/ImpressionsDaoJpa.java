/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;
import com.weavers.duqhan.dao.ImpressionsDao;
import com.weavers.duqhan.domain.Impressions;

/**
 *
 * @author Android-3
 */
public class ImpressionsDaoJpa extends BaseDaoJpa<Impressions> implements ImpressionsDao {
	
	public ImpressionsDaoJpa() {
        super(Impressions.class, "Cart");
    }
}