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

    List<Product> getAllAvailableProduct(int start, int limit);

    List<Product> SearchProductByNameAndDescription(String searchName, int start, int limit);

    List<Product> getAllRecentViewProduct(Long userid, int start, int limit, Integer lowPrice, Integer highPrice, String orderByPrice);
    
    List<Product> getAllRecentViewProductByPrice(Long userid, int start, int limit, Integer lowPrice, Integer highPrice, String orderByPrice,Double lp,Double hp);

    List<Product> getProductsByCategory(Long categoryId);

    List<Product> getProductsByCategoryIncludeChild(Long categoryId, int start, int limit);

    Product getProductByExternelLink(String link);

    boolean isAnyProductInCategoryId(Long categoryId);

    List<Product> loadAllByLimit(int start, int limit);

	List<Product> getProductsByCategoryIncludeChildDiscount(Long categoryId, int start, int limit,Double PRICE_FILTER_BAG, Double PRICE_FILTER,  long startTime);
	
	List<Product> getProductsByPrice(Long categoryId, int start, int limit,Double PRICE_FILTER_BAG, Double PRICE_FILTER,  long startTime,Double lp,Double hp);

	List<Product> getAllAvailableProductByCategories(int start, int limit, Integer lowPrice, Integer highPrice, String orderByPrice);
}
