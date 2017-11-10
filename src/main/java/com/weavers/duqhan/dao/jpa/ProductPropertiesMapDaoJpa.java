/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.ProductPropertiesMapDao;
import com.weavers.duqhan.domain.ProductPropertiesMap;

/**
 *
 * @author weaversAndroid
 */
public class ProductPropertiesMapDaoJpa extends BaseDaoJpa<ProductPropertiesMap> implements ProductPropertiesMapDao {

    public ProductPropertiesMapDaoJpa() {
        super(ProductPropertiesMap.class, "ProductPropertiesMap");
    }

    /*@Override
    public HashMap<Long, ProductPropertiesMap> getProductPropertiesMapByMinPriceIfAvailable(List<Long> productIds) {
        HashMap<Long, ProductPropertiesMap> mapProductPropertiesMap = new HashMap<>();
        if (!productIds.isEmpty()) {
            String q = "SELECT pmap.productId, MIN(pmap.discount), pmap.price, SUM(pmap.quantity) FROM ProductPropertiesMap AS pmap WHERE pmap.quantity>0 AND pmap.productId IN (";
            int i = 0;
            String s = "";
            for (Long productId : productIds) {
                s = s + (i == 0 ? "" : ",") + ":productId" + i++;
            }
            Query query = getEntityManager().createQuery(q + s + ") GROUP BY pmap.productId, pmap.price");
            i = 0;
            for (Long productId : productIds) {
                query.setParameter("productId" + i++, productId);
            }
            List<Object[]> objects = query.getResultList();
            if (!objects.isEmpty()) {
                Long c = 0l;
                for (Object[] object : objects) {
                    if (mapProductPropertiesMap.containsKey((Long) object[0])) {    // if map contains then update
                        if (mapProductPropertiesMap.get((Long) object[0]).getDiscount() > (Double) object[1]) {
                            mapProductPropertiesMap.get((Long) object[0]).setDiscount((Double) object[1]);
                            mapProductPropertiesMap.get((Long) object[0]).setPrice((Double) object[2]);
                            mapProductPropertiesMap.get((Long) object[0]).setQuantity(mapProductPropertiesMap.get((Long) object[0]).getQuantity() + (long) object[3]);
                        }
                    } else {    // else add new
                        ProductPropertiesMap productPropertiesMap = new ProductPropertiesMap();
                        productPropertiesMap.setProductId((Product) object[0]);
                        productPropertiesMap.setDiscount((Double) object[1]);
                        productPropertiesMap.setPrice((Double) object[2]);
                        productPropertiesMap.setQuantity((long) object[3]);
                        mapProductPropertiesMap.put((Long) object[0], productPropertiesMap);
                    }

                }
            }
        }
        return mapProductPropertiesMap;
    }*/

 /*@Override
    public HashMap<Long, ProductPropertiesMap> getProductPropertiesMapByMinPriceRecentView(List<Long> productIds) {
        HashMap<Long, ProductPropertiesMap> mapProductPropertiesMap = new HashMap<>();
        if (!productIds.isEmpty()) {
            String q = "SELECT pmap.productId, MIN(pmap.discount), price, SUM(pmap.quantity) FROM ProductPropertiesMap AS pmap WHERE pmap.productId IN (";
            int i = 0;
            String s = "";
            for (Long productId : productIds) {
                s = s + (i == 0 ? "" : ",") + ":productId" + i++;
            }
            Query query = getEntityManager().createQuery(q + s + ") GROUP BY pmap.productId, pmap.price");
            i = 0;
            for (Long productId : productIds) {
                query.setParameter("productId" + i++, productId);
            }
            List<Object[]> objects = query.getResultList();
            if (!objects.isEmpty()) {
                Long c = 0l;
                for (Object[] object : objects) {
                    if (mapProductPropertiesMap.containsKey((Long) object[0])) {    // if map contains then update
                        if (mapProductPropertiesMap.get((Long) object[0]).getDiscount() > (Double) object[1]) {
                            mapProductPropertiesMap.get((Long) object[0]).setDiscount((Double) object[1]);
                            mapProductPropertiesMap.get((Long) object[0]).setPrice((Double) object[2]);
                            mapProductPropertiesMap.get((Long) object[0]).setQuantity(mapProductPropertiesMap.get((Long) object[0]).getQuantity() + (long) object[3]);
                        }
                    } else {    // else add new
                        ProductPropertiesMap productPropertiesMap = new ProductPropertiesMap();
                        productPropertiesMap.setProductId((Product) object[0]);
                        productPropertiesMap.setDiscount((Double) object[1]);
                        productPropertiesMap.setPrice((Double) object[2]);
                        productPropertiesMap.setQuantity((long) object[3]);
                        mapProductPropertiesMap.put((Long) object[0], productPropertiesMap);
                    }

                }
            }
        }
        return mapProductPropertiesMap;
    }*/
 /*@Override
    public List<ProductPropertiesMap> getProductPropertiesMapsByProductId(Long productId) {
        Query query = getEntityManager().createQuery("SELECT map FROM ProductPropertiesMap AS map WHERE map.productId=:productId");
        query.setParameter("productId", productId);
        return query.getResultList();
    }*/

 /*@Override
    public HashMap<Long, ProductPropertiesMap> getProductPropertiesMapbyMinPriceIfAvailable(List<Long> productIds) {
        HashMap<Long, ProductPropertiesMap> mapProductPropertiesMap = new HashMap<>();
        if (!productIds.isEmpty()) {
            String q = "SELECT pmap.productId, MIN(pmap.discount), price, SUM(pmap.quantity) FROM ProductPropertiesMap AS pmap WHERE pmap.quantity>0 AND pmap.productId IN (";
            int i = 0;
            String s = "";
            for (Long productId : productIds) {
                s = s + (i == 0 ? "" : ",") + ":productId" + i++;
            }
            Query query = getEntityManager().createQuery(q + s + ") GROUP BY pmap.productId, pmap.price");
            i = 0;
            for (Long productId : productIds) {
                query.setParameter("productId" + i++, productId);
            }
            List<Object[]> objects = query.getResultList();
            if (!objects.isEmpty()) {
                Long c = 0l;
                for (Object[] object : objects) {
                    if (mapProductPropertiesMap.containsKey((Long) object[0])) {    // if map contains then update
                        if (mapProductPropertiesMap.get((Long) object[0]).getDiscount() > (Double) object[1]) {
                            mapProductPropertiesMap.get((Long) object[0]).setDiscount((Double) object[1]);
                            mapProductPropertiesMap.get((Long) object[0]).setPrice((Double) object[2]);
                            mapProductPropertiesMap.get((Long) object[0]).setQuantity(mapProductPropertiesMap.get((Long) object[0]).getQuantity() + (long) object[3]);
                        }
                    } else {    // else add new
                        ProductPropertiesMap productPropertiesMap = new ProductPropertiesMap();
                        productPropertiesMap.setProductId((Product) object[0]);
                        productPropertiesMap.setDiscount((Double) object[1]);
                        productPropertiesMap.setPrice((Double) object[2]);
                        productPropertiesMap.setQuantity((long) object[3]);
                        mapProductPropertiesMap.put((Long) object[0], productPropertiesMap);
                    }

                }
            }
        }
//        Query query = getEntityManager().createQuery("SELECT pmap.productId, MIN(pmap.discount), price FROM ProductSizeColorMap AS pmap WHERE pmap.quentity>0 AND pmap.productId IN (1) GROUP BY pmap.productId, pmap.price");
//        query.getResultList();
        return mapProductPropertiesMap;
    }*/
 /*@Override
    public List<ProductPropertiesMap> getPropertyMapByProductId(Long productId) {
        Query query = getEntityManager().createQuery("SELECT map FROM ProductPropertiesMap AS map WHERE map.productId =:productId");
        query.setParameter("productId", productId);
        return query.getResultList();
    }*/
}
