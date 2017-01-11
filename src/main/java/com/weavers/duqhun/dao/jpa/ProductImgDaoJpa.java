/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.ProductImgDao;
import com.weavers.duqhun.domain.ProductImg;
import java.util.ArrayList;
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

    @Override
    public List<ProductImg> loadByIds(List<Long> ProductImgIds) {
        if (ProductImgIds.isEmpty()) {
            return new ArrayList<>();
        }
        String q = "SELECT i FROM ProductImg AS i WHERE i.id IN (";
        int i = 0;
        String s = null;
        for (Long productImgId : ProductImgIds) {
            s = s + (i == 0 ? "" : ",") + "id" + i++;
        }
        Query query = getEntityManager().createQuery(q + s + ")");
        i = 0;
        for (Long productImgId : ProductImgIds) {
            query.setParameter("id" + i++, productImgId);
        }
        return query.getResultList();
    }

}
