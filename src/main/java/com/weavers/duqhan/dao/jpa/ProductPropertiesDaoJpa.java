/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.ProductPropertiesDao;
import com.weavers.duqhan.domain.ProductProperties;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author weaversAndroid
 */
public class ProductPropertiesDaoJpa extends BaseDaoJpa<ProductProperties> implements ProductPropertiesDao {

    public ProductPropertiesDaoJpa() {
        super(ProductProperties.class, "ProductProperties");
    }

    @Override
    public ProductProperties loadByName(String propertyName) {
        try {
            Query query = getEntityManager().createQuery("SELECT prop FROM ProductProperties AS prop WHERE prop.propertyName=:propertyName");
            query.setParameter("propertyName", propertyName);
            return (ProductProperties) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<ProductProperties> loadByIds(List<Long> properyIds) {
        if (properyIds.isEmpty()) {
            return new ArrayList<>();
        }
        String q = "SELECT prop FROM ProductProperties AS prop WHERE prop.id IN (";
        int i = 0;
        String s = "";
        for (Long properyId : properyIds) {
            s = s + (i == 0 ? "" : ",") + ":id" + i++;
        }
        Query query = getEntityManager().createQuery(q + s + ")");
        i = 0;
        for (Long properyId : properyIds) {
            query.setParameter("id" + i++, properyId);
        }
        return query.getResultList();
    }

}
