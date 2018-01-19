/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;
import com.weavers.duqhan.dao.ProductDao;
import com.weavers.duqhan.domain.Category;
import com.weavers.duqhan.domain.Product;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Android-3
 */
@Transactional
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
    public List<Product> getAllRecentViewProduct(Long userid, int start, int limit, Integer lowPrice, Integer highPrice, String orderByPrice) {
        //SELECT p FROM Product AS p WHERE p.id IN (SELECT DISTINCT rv.productId FROM RecentView AS rv WHERE rv.userId=:userId ORDER BY rv.viewDate DESC)
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p, RecentView AS rv WHERE p.id = rv.productId AND rv.userId=:userId ORDER BY rv.viewDate DESC").setFirstResult(start).setMaxResults(limit);
        query.setParameter("userId", userid);
        return query.getResultList();
    }
    
    @Override
    public List<Product> getAllRecentViewProductByPrice(Long userid, int start, int limit, Integer lowPrice, Integer highPrice, String orderByPrice,Double lp,Double hp) {
        //SELECT p FROM Product AS p WHERE p.id IN (SELECT DISTINCT rv.productId FROM RecentView AS rv WHERE rv.userId=:userId ORDER BY rv.viewDate DESC)
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p, RecentView AS rv INNER JOIN p.ProductPropertiesMaps AS map on p=map.productId.id WHERE p.id = rv.productId AND rv.userId=:userId AND map.discount >:lowPrice AND map.discount <:highPrice ORDER BY rv.viewDate DESC").setFirstResult(start).setMaxResults(limit);
        query.setParameter("userId", userid);
        query.setParameter("lowPrice", lp);
        query.setParameter("highPrice", hp);
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
    
    /*@Override
    public List<Product> getProductsByCategoryIncludeChildDiscount(Long categoryId, int start, int limit,Double PRICE_FILTER_BAG,Double PRICE_FILTER ) {SELECT p FROM Product p INNER JOIN ProductPropertiesMap map ON p.id = map.productId WHERE map.discount < 300 AND p.categoryId=:categoryId OR p.parentPath LIKE :parentPath GROUP BY p.id ORDER BY p.lastUpdate DESC
        Query query = getEntityManager().createQuery("SELECT p FROM Product AS p INNER JOIN p.ProductPropertiesMaps AS map WHERE p.categoryId=:categoryId OR ((p.parentPath LIKE :parentPath AND p.parentPath NOT LIKE :parentPathwith and map.discount < 300) OR (p.parentPath LIKE :parentPathwith and map.discount < 600)) GROUP BY p.id ORDER BY p.lastUpdate DESC").setFirstResult(start).setMaxResults(limit);
        query.setParameter("categoryId", categoryId);
        query.setParameter("parentPath", "%=" + categoryId + "=%");
        query.setParameter("parentPathwith", "%=25%=" + categoryId + "=%");
        return query.getResultList();
    }*/
    
    @Override
    public List<Product> getProductsByCategoryIncludeChildDiscount(Long categoryId, int start, int limit,Double PRICE_FILTER_BAG,Double PRICE_FILTER , long startTime) {
    	System.out.println("Start Of Query for category==========================="+(startTime-System.currentTimeMillis()));
    	Query q = getEntityManager().createQuery("SELECT c FROM Category AS c WHERE c.id=:categoryId");
    	q.setParameter("categoryId", categoryId);
    	List<Category> categoryList=q.getResultList();
    	Category category=categoryList.get(0);
    	Long parent=category.getParentId();
    	
    	if(parent.equals(0l)) {
    		Double highPrice = Double.parseDouble(category.getPriceLimit()); 
    		Double lowPrice = 1d;
        	String loadCat=category.getLoadCategory();
        	String[] loadCatArr=loadCat.split(",");
        	int lt=loadCatArr.length;
        	List<Product> ProductList = new ArrayList<Product>();
        	for(String cat:loadCatArr) {
        		categoryId=new Long(cat);
        		
        		Query query = getEntityManager().createQuery("SELECT p FROM Product p INNER JOIN p.ProductPropertiesMaps map "
            			+ " on p=map.productId.id WHERE map.discount >:lowPrice AND map.discount <:highPrice AND "
            			+ "p.categoryId=:categoryId GROUP BY p.id ORDER BY p.linkId").setFirstResult(start/lt).setMaxResults(limit/lt);
            	query.setParameter("categoryId", categoryId);
                query.setParameter("lowPrice", lowPrice);
                query.setParameter("highPrice", highPrice);
                ProductList.addAll(query.getResultList());
                
        	}
        	return ProductList;
    	}else {
    		Query query = getEntityManager().createQuery("SELECT p FROM Product p WHERE "
        			+ "p.categoryId IN(SELECT c.id FROM Category AS c WHERE c.parentPath like :parentPath OR c.id=:categoryId) "
        			+ "GROUP BY p.id ORDER BY p.linkId").setFirstResult(start).setMaxResults(limit);
        	query.setParameter("categoryId", categoryId);
            query.setParameter("parentPath", "%=" + categoryId + "=%");
            /*query.setParameter("discountPrice", PRICE_FILTER);*/
            return query.getResultList();
    	}
    	
    }
    
    @Override
    public List<Product> getProductsByPrice(Long categoryId, int start, int limit,Double PRICE_FILTER_BAG,Double PRICE_FILTER , long startTime,Double lowPrice,Double highPrice) {
    	
    	Query query = getEntityManager().createQuery("SELECT p FROM Product p INNER JOIN p.ProductPropertiesMaps map "
    			+ " on p=map.productId.id WHERE map.discount >:lowPrice AND map.discount <:highPrice "
    			+ "AND p.categoryId IN(SELECT c.id FROM Category AS c WHERE c.parentPath like :parentPath OR c.id=:categoryId) "
    			+ "GROUP BY p.id ORDER BY p.linkId").setFirstResult(start).setMaxResults(limit);
    	query.setParameter("categoryId", categoryId);
        query.setParameter("parentPath", "%=" + categoryId + "=%");
        query.setParameter("lowPrice", lowPrice);
        query.setParameter("highPrice", highPrice);
        return query.getResultList();
    	
    }
    
    @Override
    public List<Product> getAllAvailableProduct(int start, int limit) {/*"SELECT p FROM Product AS p ORDER BY p.lastUpdate DESC"*/
        Query query = getEntityManager().createQuery("SELECT p FROM Product p INNER JOIN p.ProductPropertiesMaps map"
        		+ " on p=map.productId.id WHERE map.discount < 300 AND p.parentPath NOT LIKE'0=42%' GROUP BY p.id ORDER BY p.lastUpdate DESC").setFirstResult(start).setMaxResults(limit);
        return query.getResultList();
    }
    @Override
    public List<Product> getAllAvailableProductByCategories(int start, int limit, Integer lowPrice, Integer highPrice, String orderbyPrice) {/*"SELECT p FROM Product AS p ORDER BY p.lastUpdate DESC"*/
    	List<Product> ProductList = new ArrayList<Product>();
    	String pattern = "14:8,8:3,21:3,1535:2,26:4";
		String[] list2 = pattern.split(",");
		Double priceLimit = 1000d;
		
    	Query q = getEntityManager().createNativeQuery("select p.latest_category,p.price_limit from product_config p");
    	List<Object[]> authors = q.getResultList();
    	
    	if(Objects.nonNull(authors)&&!authors.isEmpty()&&authors.get(0).length>1) {
    	pattern  =authors.get(0)[0].toString();
    	list2 = pattern.split(",");
    	priceLimit = new Double(new Double(authors.get(0)[1].toString()));
    	}
    	for (String string : list2) {
			String[] list3=string.split(":");
			Long cat = new Long(list3[0]);
			int catLimit= new Integer(list3[1]);
			
			Query query = getEntityManager().createQuery("SELECT p FROM Product p INNER JOIN p.ProductPropertiesMaps map "
        			+ "on p=map.productId.id WHERE map.discount <:highPrice "
        			+ "AND p.categoryId IN(SELECT c.id FROM Category AS c WHERE c.parentPath like :parentPath OR c.id=:categoryId) "
        			+ "GROUP BY p.id ORDER BY p.linkId")
					.setFirstResult((start/20)*catLimit)
					.setMaxResults(catLimit*15);
        	query.setParameter("categoryId", cat);
        	query.setParameter("parentPath", "%=" + cat + "=%");
            query.setParameter("highPrice", priceLimit);
            ProductList.addAll(query.getResultList());
    	}
    	return ProductList;
    }
    

    @Override
    @Transactional
    public List<Product> SearchProductByNameAndDescription(String searchName, int start, int limit) {
    	SessionFactory sessionFactory = getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        
        QueryBuilder qb = fullTextSession.getSearchFactory()
                .buildQueryBuilder().forEntity(Product.class).get();
       
        
        
           org.apache.lucene.search.Query query =qb.keyword().onFields("name","description").matching(searchName).createQuery();
            	
            	
        
           org.hibernate.Query hibQuery = 
                fullTextSession.createFullTextQuery(query, Product.class).setFirstResult(start).setMaxResults(limit);
        	
           List<Product> result = hibQuery.list();
           
           return result;
           /*EntityManager em = getEntityManager();

           FullTextEntityManager fullTextEntityManager = 
               org.hibernate.search.jpa.Search.getFullTextEntityManager(em);

           final QueryBuilder b = fullTextEntityManager.getSearchFactory()
               .buildQueryBuilder().forEntity( Product.class ).get();

           org.apache.lucene.search.Query luceneQuery =
               b.keyword()
                   .onFields("name","description")
                   .matching(searchName)
                   .createQuery();
           javax.persistence.Query fullTextQuery = 
               fullTextEntityManager.createFullTextQuery( luceneQuery );

           List<Product> result = fullTextQuery.getResultList(); //return a list of managed objects 
           
           
           
           return result;*/
           
        // Query query = getEntityManager().createQuery("SELECT p FROM Product AS p WHERE MATCH (p.name,p.description) AGAINST (:searchName IN NATURAL LANGUAGE MODE) ORDER BY p.lastUpdate DESC").setFirstResult(start).setMaxResults(limit);
           // query.setParameter("searchName",searchName);
        	/*Query query = getEntityManager().createNativeQuery("SELECT * FROM product WHERE MATCH (product.name,product.description) AGAINST (:searchName IN NATURAL LANGUAGE MODE) ORDER BY product.link_id").setFirstResult(start).setMaxResults(limit);
        	query.setParameter("searchName",searchName);
        	
        	List<Product> searchList = new ArrayList<Product>();
        	List<Object[]> objectArray = query.getResultList();
        	for(Object[] obj: objectArray) {
        		Product product = new Product();
            	product.setId(Long.parseLong(obj[0].toString()));
            	product.setName(obj[1].toString());
            	product.setCategoryId(Long.parseLong(obj[2].toString()));
            	product.setImgurl(obj[4].toString());
            	product.setThumbImg(obj[5].toString());
            	searchList.add(product);
        	}
            return searchList;*/
        
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
