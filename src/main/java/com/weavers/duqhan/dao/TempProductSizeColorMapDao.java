/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao;

import com.weavers.duqhan.domain.TempProductSizeColorMap;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author weaversAndroid
 */
public interface TempProductSizeColorMapDao extends BaseDao<TempProductSizeColorMap>{
    
    HashMap<Long, TempProductSizeColorMap> getSizeColorMapByProductIds(List<Long> productIds);
    
    List<TempProductSizeColorMap> getSizeColorMapByProductId(Long productId);
}
