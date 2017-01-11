/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.ProductImgDao;
import com.weavers.duqhun.domain.ProductImg;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class ProductImgDaoJpa extends BaseDaoJpa<ProductImg> implements ProductImgDao {

    public ProductImgDaoJpa() {
        super(ProductImg.class, "ProductImg");
    }

    @Override
    public List<ProductImg> getProductImgsByProductId(Long productId) {
        Query query = getEntityManager().createQuery("SELECT i FROM ProductImg AS i WHERE i.productId=:productId");
        query.setParameter("productId", productId);
        return query.getResultList();
    }

}
