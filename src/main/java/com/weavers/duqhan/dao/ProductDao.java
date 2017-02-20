/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao;

import com.weavers.duqhan.domain.Product;
import java.util.List;

/**
 *
 * @author Android-3
 */
public interface ProductDao extends BaseDao<Product> {

    List<Product> loadByIds(List<Long> productIds);

    List<Product> getAllAvailableProductByProductIds(List<Long> productIds);

    List<Product> getAllAvailableProduct();
    
    List<Product> SearchProductByNameAndDescription(String searchName);

    List<Product> getAllRecentViewProduct(Long userid);

    List<Product> getProductsByCategory(Long categoryId);
    
    List<Product> getProductsByCategoryIncludeChild(Long categoryId);
}
