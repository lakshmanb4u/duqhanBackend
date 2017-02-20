/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.ProductDao;
import com.weavers.duqhan.domain.Product;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class ProductDaoJpa extends BaseDaoJpa<Product> implements ProductDao {

    public ProductDaoJpa() {
        super(Product.class, "Product");
    }

    @Override
    public List<Product> loadByIds(List<Long> productIds) {
        if (productIds.isEmpty()) {
            return new ArrayList<>();
        }
        String q = "SELECT p FROM Product AS p WHERE p.id IN (";
        int i = 0;
        String s = "";
        for (Long productId : productIds) {
            s = s + (i == 0 ? "" : ",") + ":id" + i++;
        }
        Query query = getEntityManager().createQuery(q + s + ")");
        i = 0;
        for (Long productId : productIds) {
            query.setParameter("id" + i++, productId);
        }
        return query.getResultList();
    }

    @Override
    public List<Product> getAllAvailableProductByProductIds(List<Long> productIds) {    //get All Available Product from ProductIds.
        if (productIds.isEmpty()) {
            return new ArrayList<>();
        }
        String q = "SELECT p FROM Product AS p WHERE p.id IN (";
        int i = 0;
        String s = null;
        for (Long productId : productIds) {
            s = s + (i == 0 ? "" : ",") + "id" + i++;
        }
        Query query = getEntityManager().createQuery(q + s + ")");
        i = 0;
        for (Long productId : productIds) {
            query.setParameter("id" + i++, productId);
        }
        return query.getResultList();
    }

    @Override
    public List<Product> getAllRecentViewProduct(Long userid) {
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p WHERE p.id IN (SELECT rv.productId FROM RecentView AS rv WHERE rv.userId=:userId ORDER BY rv.viewDate)");
        query.setParameter("userId", userid);
        return query.getResultList();
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p WHERE p.categoryId=:categoryId ORDER BY p.lastUpdate");
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }

    @Override
    public List<Product> getProductsByCategoryIncludeChild(Long categoryId) {
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p WHERE p.categoryId=:categoryId OR p.parentPath LIKE :parentPath ORDER BY p.lastUpdate");
        query.setParameter("categoryId", categoryId);
        query.setParameter("parentPath", "%=" + categoryId + "=%");
        return query.getResultList();
    }

    @Override
    public List<Product> getAllAvailableProduct() {
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p ORDER BY p.lastUpdate");
        return query.getResultList();
    }

    @Override
    public List<Product> SearchProductByNameAndDescription(String searchName) {
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p WHERE p.name LIKE :searchName OR p.description LIKE :searchName ORDER BY p.lastUpdate");
        query.setParameter("searchName", "%" + searchName + "%");
        return query.getResultList();
    }

}
