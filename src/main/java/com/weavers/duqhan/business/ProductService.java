/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business;

import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.CategoryDto;
import com.weavers.duqhan.dto.CategorysBean;
import com.weavers.duqhan.dto.OrderDetailsBean;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.dto.ProductBeans;
import com.weavers.duqhan.dto.ProductDetailBean;
import com.weavers.duqhan.dto.ProductRequistBean;
import com.weavers.duqhan.dto.SizeDto;

/**
 *
 * @author Android-3
 */
public interface ProductService {

    ProductBeans getProductsByCategory(Long categoryId);

    ProductBeans getProductsByRecentView(Long userId);

    ProductBeans getAllProducts();

    ProductDetailBean getProductDetailsById(Long productId, Long userId);

    Long getCartCountFoAUser(Long userId);

    String addProductToCart(ProductRequistBean requistBean);

    String removeProductFromCart(ProductRequistBean requistBean);

    CartBean getCartFoAUser(Long userId);

    String saveProduct(ProductBean productBean);

    String saveProductImage(ProductBean productBean);

    String saveCategory(CategoryDto categoryDto);

    String saveSize(SizeDto sizeDto);

    String saveSizeGroup(String sizeGroup);

    String saveColor(String color);

    CategorysBean getChildById(Long parentId);

    OrderDetailsBean getOrderDetails(Long id);
}
