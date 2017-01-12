/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.business;

import com.weavers.duqhun.dto.CartBean;
import com.weavers.duqhun.dto.ProductBean;
import com.weavers.duqhun.dto.ProductBeans;
import com.weavers.duqhun.dto.ProductDetailBean;
import com.weavers.duqhun.dto.ProductRequistBean;

/**
 *
 * @author Android-3
 */
public interface ProductService {

    ProductBeans getProductsByCategory(Long categoryId);

    ProductBeans getProductsByRecentView(Long userId);

    ProductBeans getAllProducts();
    
    ProductDetailBean getProductDetailsById(Long productId);
    
    String addProductToCart(ProductRequistBean requistBean);
    
    CartBean getCartFoAUser(Long userId);
    
    String saveProduct(ProductBean productBean);
}
