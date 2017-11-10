/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao;
import java.util.List;

import com.weavers.duqhan.domain.Review;


/**
 *
 * @author Android-3
 */
public interface ReviewDao extends BaseDao<Review>{
  
	List<Review> getAllByproductId(Long productId);
	List<Object[]> getAllRatingCount(Long productId);
    
}
