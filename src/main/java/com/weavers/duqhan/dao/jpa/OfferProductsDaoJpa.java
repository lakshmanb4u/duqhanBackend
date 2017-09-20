/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.OfferProductsDao;
import com.weavers.duqhan.domain.OfferProducts;
import com.weavers.duqhan.domain.Product;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author weaversAndroid
 */
public class OfferProductsDaoJpa extends BaseDaoJpa<OfferProducts> implements OfferProductsDao {

    public OfferProductsDaoJpa() {
        super(OfferProducts.class, "OfferProducts");
    }

    @Override
    public List<Product> getAllOfferProduct() {
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p, OfferProducts AS op WHERE p.id=op.productId");
        return query.getResultList();
    }

}
