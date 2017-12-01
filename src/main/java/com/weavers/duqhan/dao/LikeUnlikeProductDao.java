package com.weavers.duqhan.dao;

import java.util.List;

import com.weavers.duqhan.domain.LikeUnlikeProduct;
import com.weavers.duqhan.dto.LikeUnlikeProductDto;

public interface LikeUnlikeProductDao extends BaseDao<LikeUnlikeProduct> {
	
	LikeUnlikeProduct getProductLikeUnlike(long productId, long userId);
}
