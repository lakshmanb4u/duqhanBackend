/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao;

import com.weavers.duqhun.domain.ProductImg;
import java.util.List;

/**
 *
 * @author Android-3
 */
public interface ProductImgDao extends BaseDao<ProductImg> {

    List<ProductImg> getProductImgsByProductId(Long productId);
}
