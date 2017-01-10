/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.ProductSizeColorMapDao;
import com.weavers.duqhun.domain.ProductSizeColorMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class ProductSizeColorMapDaoJpa extends BaseDaoJpa<ProductSizeColorMap> implements ProductSizeColorMapDao {

    public ProductSizeColorMapDaoJpa() {
        super(ProductSizeColorMap.class, "ProductSizeColorMap");
    }

    @Override
    public HashMap<Long, ProductSizeColorMap> getSizeColorMapbyMinPriceIfAvailable(List<Long> productIds) {
        HashMap<Long, ProductSizeColorMap> mapSizeColorMap = new HashMap<>();
        if (!productIds.isEmpty()) {
            String q = "SELECT pmap.productId, MIN(pmap.discount), price FROM ProductSizeColorMap AS pmap WHERE pmap.quentity>0 AND pmap.productId IN (";
            int i = 0;
            String s = null;
            for (Long productId : productIds) {
                s = s + (i == 0 ? "" : ",") + "productId" + i++;
            }
            Query query = getEntityManager().createQuery(q + s + ") GROUP BY pmap.productId, pmap.price");
            i = 0;
            for (Long productId : productIds) {
                query.setParameter("productId" + i++, productId);
            }
            List<Object[]> objects = query.getResultList();
            if (!objects.isEmpty()) {
                for (Object[] object : objects) {
                    if (mapSizeColorMap.containsKey((Long) object[0])) {
                        if (mapSizeColorMap.get((Long) object[0]).getDiscount() > (Double) object[1]) {
                            mapSizeColorMap.get((Long) object[0]).setDiscount((Double) object[1]);
                            mapSizeColorMap.get((Long) object[0]).setPrice((Double) object[2]);
                        }
                    } else {
                        ProductSizeColorMap productSizeColorMap = new ProductSizeColorMap();
                        productSizeColorMap.setProductId((Long) object[0]);
                        productSizeColorMap.setDiscount((Double) object[1]);
                        productSizeColorMap.setPrice((Double) object[2]);
                        mapSizeColorMap.put((Long) object[0], productSizeColorMap);
                    }
                }
            }
        }
//        Query query = getEntityManager().createQuery("SELECT pmap.productId, MIN(pmap.discount), price FROM ProductSizeColorMap AS pmap WHERE pmap.quentity>0 AND pmap.productId IN (1) GROUP BY pmap.productId, pmap.price");
//        query.getResultList();
        return mapSizeColorMap;
    }

    @Override
    public HashMap<Long, ProductSizeColorMap> getSizeColorMapbyMinPriceRecentView(List<Long> productIds) {
        HashMap<Long, ProductSizeColorMap> mapSizeColorMap = new HashMap<>();
        if (!productIds.isEmpty()) {
            String q = "SELECT pmap.productId, MIN(pmap.discount), price FROM ProductSizeColorMap AS pmap WHERE pmap.productId IN (";
            int i = 0;
            String s = null;
            for (Long productId : productIds) {
                s = s + (i == 0 ? "" : ",") + "productId" + i++;
            }
            Query query = getEntityManager().createQuery(q + s + ") GROUP BY pmap.productId, pmap.price");
            i = 0;
            for (Long productId : productIds) {
                query.setParameter("productId" + i++, productId);
            }
            List<Object[]> objects = query.getResultList();
            if (!objects.isEmpty()) {
                for (Object[] object : objects) {
                    if (mapSizeColorMap.containsKey((Long) object[0])) {
                        if (mapSizeColorMap.get((Long) object[0]).getDiscount() > (Double) object[1]) {
                            mapSizeColorMap.get((Long) object[0]).setDiscount((Double) object[1]);
                            mapSizeColorMap.get((Long) object[0]).setPrice((Double) object[2]);
                        }
                    } else {
                        ProductSizeColorMap productSizeColorMap = new ProductSizeColorMap();
                        productSizeColorMap.setProductId((Long) object[0]);
                        productSizeColorMap.setDiscount((Double) object[1]);
                        productSizeColorMap.setPrice((Double) object[2]);
                        mapSizeColorMap.put((Long) object[0], productSizeColorMap);
                    }
                }
            }
        }
//        Query query = getEntityManager().createQuery("SELECT pmap.productId, MIN(pmap.discount), price FROM ProductSizeColorMap AS pmap WHERE pmap.quentity>0 AND pmap.productId IN (1) GROUP BY pmap.productId, pmap.price");
//        query.getResultList();
        return mapSizeColorMap;
    }
}
