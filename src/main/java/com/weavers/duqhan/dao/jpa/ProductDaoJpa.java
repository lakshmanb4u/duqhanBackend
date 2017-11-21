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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
    public List<Product> getAllRecentViewProduct(Long userid, int start, int limit) {
        //SELECT p FROM Product AS p WHERE p.id IN (SELECT DISTINCT rv.productId FROM RecentView AS rv WHERE rv.userId=:userId ORDER BY rv.viewDate DESC)
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p, RecentView AS rv WHERE p.id = rv.productId AND rv.userId=:userId ORDER BY rv.viewDate DESC").setFirstResult(start).setMaxResults(limit);
        query.setParameter("userId", userid);
        return query.getResultList();
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p WHERE p.categoryId=:categoryId ORDER BY p.lastUpdate DESC");
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }

    @Override
    public List<Product> getProductsByCategoryIncludeChild(Long categoryId, int start, int limit) {/*SELECT p FROM Product p INNER JOIN ProductPropertiesMap map ON p.id = map.productId WHERE map.discount < 300 AND p.categoryId=:categoryId OR p.parentPath LIKE :parentPath GROUP BY p.id ORDER BY p.lastUpdate DESC*/
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p WHERE p.categoryId=:categoryId OR p.parentPath LIKE :parentPath ORDER BY p.lastUpdate DESC").setFirstResult(start).setMaxResults(limit);
        query.setParameter("categoryId", categoryId);
        query.setParameter("parentPath", "%=" + categoryId + "=%");
        return query.getResultList();
    }
    
    @Override
    public List<Product> getProductsByCategoryIncludeChildDiscount(Long categoryId, int start, int limit,Double PRICE_FILTER_BAG,Double PRICE_FILTER ) {/*SELECT p FROM Product p INNER JOIN ProductPropertiesMap map ON p.id = map.productId WHERE map.discount < 300 AND p.categoryId=:categoryId OR p.parentPath LIKE :parentPath GROUP BY p.id ORDER BY p.lastUpdate DESC*/
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p INNER JOIN p.ProductPropertiesMaps AS map WHERE p.categoryId=:categoryId OR ((p.parentPath LIKE :parentPath AND p.parentPath NOT LIKE :parentPathwith and map.discount < 300) OR (p.parentPath LIKE :parentPathwith and map.discount < 600)) GROUP BY p.id ORDER BY p.lastUpdate DESC").setFirstResult(start).setMaxResults(limit);
        query.setParameter("categoryId", categoryId);
        query.setParameter("parentPath", "%=" + categoryId + "=%");
        query.setParameter("parentPathwith", "%=25%=" + categoryId + "=%");
        return query.getResultList();
    }
    
    @Override
    public List<Product> getProductsByCategory25(Long categoryId, int start, int limit,Double PRICE_FILTER_BAG,Double PRICE_FILTER ) {/*SELECT p FROM Product p INNER JOIN ProductPropertiesMap map ON p.id = map.productId WHERE map.discount < 300 AND p.categoryId=:categoryId OR p.parentPath LIKE :parentPath GROUP BY p.id ORDER BY p.lastUpdate DESC*/
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p INNER JOIN p.ProductPropertiesMaps AS map WHERE p.categoryId=:categoryId OR ((p.parentPath LIKE :parentPath AND map.discount < 600 )) GROUP BY p.id ORDER BY p.lastUpdate DESC").setFirstResult(start).setMaxResults(limit);
        query.setParameter("categoryId", categoryId);
        query.setParameter("parentPath", "%=" + categoryId + "=%");
        return query.getResultList();
    }
    
    @Override
    public List<Product> getAllAvailableProduct(int start, int limit) {/*"SELECT p FROM Product AS p ORDER BY p.lastUpdate DESC"*/
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p INNER JOIN p.ProductPropertiesMaps AS map WHERE map.discount < 300 AND p.parentPath NOT LIKE'0=42%' GROUP BY p.id ORDER BY p.lastUpdate DESC").setFirstResult(start).setMaxResults(limit);
        return query.getResultList();
    }
    
    @Override
    public List<Product> getAllAvailableProductByCategories(int start, int limit) {/*"SELECT p FROM Product AS p ORDER BY p.lastUpdate DESC"*/
    	List<Product> allTypesList = new ArrayList<>();
    	
    	Query q = getEntityManager().createNativeQuery("select p.latest_category from product_config p");
    	List<String> authors = q.getResultList();
    	String[] list2 = authors.get(0).split(",");
    	for (String string : list2) {
			String[] list3=string.split(":");
			Long cat = new Long(list3[0]);
			int catLimit = new Integer(list3[1]);
			Query query = getEntityManager().createQuery("SELECT p FROM Product AS p INNER JOIN p.ProductPropertiesMaps AS map WHERE map.discount < 300 AND p.parentPath like :key  GROUP BY p.id ORDER BY p.lastUpdate DESC").setFirstResult((start/20)*catLimit).setMaxResults(catLimit);
			query.setParameter("key", "%="+cat+"=%");
	        allTypesList.addAll(query.getResultList());
	        if(allTypesList.size() >= 20)
	        	break;
    	}
    	if(allTypesList.size() >= 20) {
    	return (new ArrayList<Product>(allTypesList.subList(0, 19)));
    	}else {
    	return allTypesList;
    	}
    	
    }

    @Override
    public List<Product> SearchProductByNameAndDescription(String searchName, int start, int limit) {
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p WHERE p.name LIKE :searchName OR p.description LIKE :searchName ORDER BY p.lastUpdate DESC").setFirstResult(start).setMaxResults(limit);
        query.setParameter("searchName", "%" + searchName + "%");
        return query.getResultList();
    }

    @Override
    public Product getProductByExternelLink(String externalLink) {
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM Product AS p WHERE p.externalLink=:externalLink");
            query.setParameter("externalLink", externalLink);
            return (Product) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nre) {
            return new Product();
        }
    }

    @Override
    public boolean isAnyProductInCategoryId(Long categoryId) {
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM Product AS p WHERE p.categoryId=:categoryId").setMaxResults(1);
            query.setParameter("categoryId", categoryId);
            return !query.getResultList().isEmpty();
        } catch (NoResultException nre) {
            return false;
        }
    }

    @Override
    public List<Product> loadAllByLimit(int start, int limit) {
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p").setFirstResult(start).setMaxResults(limit);
        return query.getResultList();
    }
}
