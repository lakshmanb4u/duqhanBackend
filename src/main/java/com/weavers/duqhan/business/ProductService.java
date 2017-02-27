/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business;

import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.CategoryDto;
import com.weavers.duqhan.dto.CategorysBean;
import com.weavers.duqhan.dto.ColorAndSizeDto;
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

    ColorAndSizeDto getColorSizeList();

    ProductBeans getProductsByCategory(Long categoryId, int start, int limit);

    ProductBeans getProductsByRecentView(Long userId, int start, int limit);

    ProductBeans getAllProducts(int start, int limit);

    ProductBeans searchProducts(ProductRequistBean requistBean);

    ProductDetailBean getProductDetailsById(Long productId, Long userId);

    Long getCartCountFoAUser(Long userId);

    String addProductToCart(ProductRequistBean requistBean);

    String removeProductFromCart(ProductRequistBean requistBean);

    CartBean getCartForUser(Long userId);

    String saveProduct(ProductBean productBean);

    String saveProductImage(ProductBean productBean);

    String saveCategory(CategoryDto categoryDto);

    String saveSize(SizeDto sizeDto);

    String saveSizeGroup(String sizeGroup);

    String saveColor(String color);

    CategorysBean getChildById(Long parentId);

    OrderDetailsBean getOrderDetails(Long userId, int start, int limit);

    void cancelOrder(String orderId, Long userId);
}
