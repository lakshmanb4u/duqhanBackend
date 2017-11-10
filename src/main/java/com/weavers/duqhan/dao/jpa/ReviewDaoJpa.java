/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;

import com.weavers.duqhan.dao.ReviewDao;
import com.weavers.duqhan.domain.Review;

/**
 *
 * @author Android-3
 */
public class ReviewDaoJpa extends BaseDaoJpa<Review> implements ReviewDao{
    
    public ReviewDaoJpa() {
        super(Review.class, "Review");
    }

	@Override
	public List<Review> getAllByproductId(Long productId) {
		 Query query = getEntityManager().createQuery("SELECT rv FROM Review AS rv WHERE rv.productId=:productId ORDER BY rv.date");
		 query.setParameter("productId", productId);   
		 return query.getResultList();
	}
	
	@Override
	public List<Object[]> getAllRatingCount(Long productId){
		Query query = getEntityManager().createNativeQuery("select count(CASE WHEN r.rating = 1 THEN 1 ELSE null END) as ret1 ,count(CASE WHEN r.rating = 2 THEN 1 ELSE null END) as ret2 ,count(CASE WHEN r.rating = 3 THEN 1 ELSE null END) as ret3 ,count(CASE WHEN r.rating = 4 THEN 1 ELSE null END) as ret4 ,count(CASE WHEN r.rating = 5 THEN 1 ELSE null END) as ret5,count(*) as ret  from review r where r.product_id=:productId");
		query.setParameter("productId", productId);
		return query.getResultList();
		
	}
    
}
