/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao;

import com.weavers.duqhun.domain.Cart;
import com.weavers.duqhun.domain.ProductSizeColorMap;
import java.util.List;

/**
 *
 * @author Android-3
 */
public interface CartDao extends BaseDao<Cart> {

    List<ProductSizeColorMap> getProductSizeColorMapByUserId(Long userId);

    Cart loadByUserIdAndMapId(Long userId, Long mapId);
    
    Long getCartCountByUserId(Long userId);
}
