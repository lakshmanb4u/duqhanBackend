/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.business.impl;

import com.weavers.duqhun.business.ProductService;
import com.weavers.duqhun.dao.ProductDao;
import com.weavers.duqhun.dao.ProductSizeColorMapDao;
import com.weavers.duqhun.dao.RecentViewDao;
import com.weavers.duqhun.domain.Product;
import com.weavers.duqhun.domain.ProductSizeColorMap;
import com.weavers.duqhun.dto.ProductBean;
import com.weavers.duqhun.dto.ProductBeans;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Android-3
 */
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;
    @Autowired
    ProductSizeColorMapDao productSizeColorMapDao;
    @Autowired
    RecentViewDao recentViewDao;

    private ProductBeans setProductBeans(List<Product> products, HashMap<Long, ProductSizeColorMap> mapSizeColorMap) {
        ProductBeans productBeans = new ProductBeans();
        List<ProductBean> beans = new ArrayList<>();
        for (Product product : products) {
            ProductBean bean = new ProductBean();
            bean.setProductId(product.getCategoryId());
            bean.setName(product.getName());
            bean.setImgurl(product.getImgUrl());
            bean.setPrice(mapSizeColorMap.get(product.getId()).getPrice());
            beans.add(bean);
        }
        productBeans.setProducts(beans);
        return productBeans;
    }

    @Override
    public ProductBeans getProductsByCategory(Long categoryId) {
        List<Product> products = productDao.getProductsByCategory(categoryId);
        List<Long> productIds = new ArrayList<>();
        for (Product product : products) {
            productIds.add(product.getId());
        }
        HashMap<Long, ProductSizeColorMap> mapSizeColorMaps = productSizeColorMapDao.getSizeColorMapbyMinPriceIfAvailable(productIds);
        return this.setProductBeans(products, mapSizeColorMaps);
    }

    @Override
    public ProductBeans getProductsByRecentView(Long userId) {
        List<Product> products = productDao.getAllRecentViewProduct(userId);
        List<Long> productIds = new ArrayList<>();
        for (Product product : products) {
            productIds.add(product.getId());
        }
        HashMap<Long, ProductSizeColorMap> mapSizeColorMaps = productSizeColorMapDao.getSizeColorMapbyMinPriceIfAvailable(productIds);
        return this.setProductBeans(products, mapSizeColorMaps);
    }

    @Override
    public ProductBeans getAllProducts() {
        List<Product> products = productDao.getAllAvailableProduct();
        List<Long> productIds = new ArrayList<>();
        for (Product product : products) {
            productIds.add(product.getId());
        }
        HashMap<Long, ProductSizeColorMap> mapSizeColorMaps = productSizeColorMapDao.getSizeColorMapbyMinPriceIfAvailable(productIds);
        return this.setProductBeans(products, mapSizeColorMaps);
    }

}
