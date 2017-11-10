/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.ProductPropertyvaluesDao;
import com.weavers.duqhan.domain.ProductPropertyvalues;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author weaversAndroid
 */
public class ProductPropertyvaluesDaoJpa extends BaseDaoJpa<ProductPropertyvalues> implements ProductPropertyvaluesDao {

    public ProductPropertyvaluesDaoJpa() {
        super(ProductPropertyvalues.class, "ProductPropertyvalues");
    }

    @Override
    public List<ProductPropertyvalues> loadByProductId(Long productId) {
        Query query = getEntityManager().createQuery("SELECT v FROM ProductPropertyvalues AS v WHERE v.productId=:productId");
        query.setParameter("productId", productId);
        return query.getResultList();
    }

}
