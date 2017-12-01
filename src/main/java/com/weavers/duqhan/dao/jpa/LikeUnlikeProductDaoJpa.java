package com.weavers.duqhan.dao.jpa;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.weavers.duqhan.dao.LikeUnlikeProductDao;
import com.weavers.duqhan.domain.LikeUnlikeProduct;

public class LikeUnlikeProductDaoJpa extends BaseDaoJpa<LikeUnlikeProduct> implements LikeUnlikeProductDao{

	public LikeUnlikeProductDaoJpa(){
		super(LikeUnlikeProduct.class, "LikeUnlikeProduct");
	}
	
	@Autowired
	public LikeUnlikeProductDao likeUnlikeProductDao;

	@Override
	public LikeUnlikeProduct getProductLikeUnlike(long productId, long userId) {
		Query query = getEntityManager().createQuery("SELECT l FROM LikeUnlikeProduct l WHERE userId=:userId AND productId=:productId");
		query.setParameter("userId", userId);
		query.setParameter("productId", productId);
		try{
			LikeUnlikeProduct likeUnlikeproduct = (LikeUnlikeProduct)query.getSingleResult();
			return likeUnlikeproduct;
		}catch(Exception e){
			return null;
		}
		
	}

}
