/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.business.impl;

import com.weavers.duqhun.business.ProductService;
import com.weavers.duqhun.dao.CartDao;
import com.weavers.duqhun.dao.CategoryDao;
import com.weavers.duqhun.dao.ColorDao;
import com.weavers.duqhun.dao.ProductDao;
import com.weavers.duqhun.dao.ProductImgDao;
import com.weavers.duqhun.dao.ProductSizeColorMapDao;
import com.weavers.duqhun.dao.RecentViewDao;
import com.weavers.duqhun.dao.SizeeDao;
import com.weavers.duqhun.domain.Cart;
import com.weavers.duqhun.domain.Category;
import com.weavers.duqhun.domain.Color;
import com.weavers.duqhun.domain.Product;
import com.weavers.duqhun.domain.ProductImg;
import com.weavers.duqhun.domain.ProductSizeColorMap;
import com.weavers.duqhun.domain.Sizee;
import com.weavers.duqhun.dto.CartBean;
import com.weavers.duqhun.dto.ColorDto;
import com.weavers.duqhun.dto.ImageDto;
import com.weavers.duqhun.dto.ProductBean;
import com.weavers.duqhun.dto.ProductBeans;
import com.weavers.duqhun.dto.ProductDetailBean;
import com.weavers.duqhun.dto.ProductRequistBean;
import com.weavers.duqhun.dto.SizeColorMapDto;
import com.weavers.duqhun.dto.SizeDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ProductImgDao productImgDao;
    @Autowired
    SizeeDao sizeeDao;
    @Autowired
    ColorDao colorDao;
    @Autowired
    CartDao cartDao;

    private ProductBeans setProductBeans(List<Product> products, HashMap<Long, ProductSizeColorMap> mapSizeColorMap) {
        ProductBeans productBeans = new ProductBeans();
        List<ProductBean> beans = new ArrayList<>();
        for (Product product : products) {
            ProductBean bean = new ProductBean();
            bean.setProductId(product.getCategoryId());
            bean.setName(product.getName());
            bean.setImgurl(product.getImgurl());
            bean.setPrice(mapSizeColorMap.get(product.getId()).getPrice());
            beans.add(bean);
        }
        productBeans.setProducts(beans);
        return productBeans;
    }

    private Double getPercentage(Double original, Double discounted) {
        Double discountPct = 0.00;
        Double less = original - discounted;
        if (original != null && discounted != null && less > 0.00) {
            discountPct = original / less;
        }
        return discountPct;
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

    @Override
    public ProductDetailBean getProductDetailsById(Long productId) {
        ProductDetailBean productDetailBean = new ProductDetailBean();
        Product product = productDao.loadById(productId);
        if (product != null) {
            Category category = categoryDao.loadById(product.getCategoryId());
            List<ProductImg> imgs = productImgDao.getProductImgsByProductId(productId);
            List<ProductSizeColorMap> sizeColorMaps = productSizeColorMapDao.getSizeColorMapByProductId(productId);
            List<Sizee> sizees = sizeeDao.loadAll();
            HashMap<Long, Sizee> mapSize = new HashMap<>();
            for (Sizee sizee : sizees) {
                mapSize.put(sizee.getId(), sizee);
            }
            List<Color> colors = colorDao.loadAll();
            HashMap<Long, Color> mapColor = new HashMap<>();
            for (Color color : colors) {
                mapColor.put(color.getId(), color);
            }
            productDetailBean.setProductId(product.getCategoryId());
            productDetailBean.setName(product.getName());
            productDetailBean.setDescription(product.getDescription());
            productDetailBean.setCategoryId(category.getId());
            productDetailBean.setCategoryName(category.getName());
            //===============================add imgDto==============================
            List<ImageDto> imgDtos = new ArrayList<>();
            for (ProductImg productImg : imgs) {
                ImageDto imgDto = new ImageDto();
                imgDto.setImgUrl(productImg.getImgUrl());
                imgDtos.add(imgDto);
            }
            productDetailBean.setImages(imgDtos);
            //====================================add SizeDto========================
            Set<SizeDto> sizeDtos = new HashSet<>();
            Set<SizeDto> sizeDtos2 = new HashSet<>();
            Set<ColorDto> colorDtos = new HashSet<>();
            HashMap<Long, List<SizeColorMapDto>> mapSizeColorMapDto = new HashMap<>();
            for (ProductSizeColorMap sizeColorMap : sizeColorMaps) {
                SizeDto sizeDto = new SizeDto();

                sizeDto.setSizeId(sizeColorMap.getSizeId());
                sizeDto.setSizeText(mapSize.get(sizeColorMap.getSizeId()).getValu());
                sizeDtos.add(sizeDto);

                if (mapSizeColorMapDto.containsKey(sizeColorMap.getSizeId())) {
                    SizeColorMapDto sizeColorMapDto = new SizeColorMapDto();
                    sizeColorMapDto.setColorId(sizeColorMap.getColorId());
                    sizeColorMapDto.setColorText(mapColor.get(sizeColorMap.getColorId()).getName());
                    sizeColorMapDto.setMapId(sizeColorMap.getId());
                    sizeColorMapDto.setOrginalPrice(sizeColorMap.getPrice());
                    sizeColorMapDto.setSalesPrice(sizeColorMap.getDiscount());
                    sizeColorMapDto.setDiscount(this.getPercentage(sizeColorMap.getPrice(), sizeColorMap.getDiscount()));
                    sizeColorMapDto.setCount(sizeColorMap.getQuentity());
                    mapSizeColorMapDto.get(sizeColorMap.getSizeId()).add(sizeColorMapDto);
                } else {
                    List<SizeColorMapDto> sizeColorMapDtos = new ArrayList<>();
                    SizeColorMapDto sizeColorMapDto = new SizeColorMapDto();
                    sizeColorMapDto.setColorId(sizeColorMap.getColorId());
                    sizeColorMapDto.setColorText(mapColor.get(sizeColorMap.getColorId()).getName());
                    sizeColorMapDto.setMapId(sizeColorMap.getId());
                    sizeColorMapDto.setOrginalPrice(sizeColorMap.getPrice());
                    sizeColorMapDto.setSalesPrice(sizeColorMap.getDiscount());
                    sizeColorMapDto.setDiscount(this.getPercentage(sizeColorMap.getPrice(), sizeColorMap.getDiscount()));
                    sizeColorMapDto.setCount(sizeColorMap.getQuentity());
                    sizeColorMapDtos.add(sizeColorMapDto);
                    mapSizeColorMapDto.put(sizeColorMap.getSizeId(), sizeColorMapDtos);
                }

                ColorDto colorDto = new ColorDto();
                colorDto.setColorId(sizeColorMap.getColorId());
                colorDto.setColorText(mapColor.get(sizeColorMap.getColorId()).getName());
                colorDtos.add(colorDto);
            }
            for (SizeDto sizeDto1 : sizeDtos) {
                SizeDto sizeDto2 = new SizeDto();
                sizeDto2.setSizeId(sizeDto1.getSizeId());
                sizeDto2.setSizeText(sizeDto1.getSizeText());
                sizeDto2.setSizeColorMap(mapSizeColorMapDto.get(sizeDto1.getSizeId()));
                sizeDtos2.add(sizeDto2);
            }
            productDetailBean.setSizes(sizeDtos2);
            productDetailBean.setColors(colorDtos);

            productDetailBean.setArrival("Not set yet..");
            productDetailBean.setShippingCost(null);
            productDetailBean.setRelatedProducts(new ProductBeans());
        } else {
            productDetailBean.setStatus("No Product Found");
        }
        return productDetailBean;
    }

    @Override
    public String addProductToCart(ProductRequistBean requistBean) {
        String status = "failure";
        Cart cart = new Cart();
        cart.setId(null);
        cart.setLoadDate(new Date());
        cart.setSizecolormapId(requistBean.getMapId());
        cart.setUserId(requistBean.getUserId());
        Cart cart1 = cartDao.save(cart);
        if (cart1 != null) {
            status = "success";
        }
        return status;
    }

    @Override
    public CartBean getCartFoAUser(Long userId) {
        CartBean cartBean = new CartBean();
        List<ProductSizeColorMap> sizeColorMaps = cartDao.getProductSizeColorMapByUserId(userId);
        if (!sizeColorMaps.isEmpty()) {
            List<Long> priductIds = new ArrayList<>();
            List<Long> colorIds = new ArrayList<>();
            List<Long> sizeIds = new ArrayList<>();
            List<Long> imgIds = new ArrayList<>();
            for (ProductSizeColorMap sizeColorMap : sizeColorMaps) {
                priductIds.add(sizeColorMap.getProductId());
                colorIds.add(sizeColorMap.getColorId());
                sizeIds.add(sizeColorMap.getSizeId());
                imgIds.add(sizeColorMap.getProductImgId());
            }
            List<Product> products = productDao.loadByIds(priductIds);
            HashMap<Long, Product> MapProduct = new HashMap<>();
            for (Product product : products) {
                MapProduct.put(product.getId(), product);
            }
            List<Color> colors = colorDao.loadByIds(colorIds);
            HashMap<Long, Color> MapColor = new HashMap<>();
            for (Color color : colors) {
                MapColor.put(color.getId(), color);
            }
            List<Sizee> sizees = sizeeDao.loadByIds(sizeIds);
            HashMap<Long, Sizee> MapSizee = new HashMap<>();
            for (Sizee sizee : sizees) {
                MapSizee.put(sizee.getId(), sizee);
            }
            List<ProductImg> productImgs = productImgDao.loadByIds(imgIds);
            HashMap<Long, ProductImg> MapProductImg = new HashMap<>();
            for (ProductImg productImg : productImgs) {
                MapProductImg.put(productImg.getId(), productImg);
            }
            //============================================================//
            List<ProductBean> productBeans = new ArrayList<>();
            double itemTotal = 0.0;         //2000
            double orderTotal = 0.0;        //1500
            double discountTotal = 0.0;     //500
            double discountPctTotal = 0.0;  //25

            for (ProductSizeColorMap sizeColorMap : sizeColorMaps) {
                ProductBean productBean = new ProductBean();
                productBean.setProductId(sizeColorMap.getProductId());
                productBean.setName(MapProduct.get(sizeColorMap.getProductId()).getName());
                productBean.setSize(MapSizee.get(sizeColorMap.getSizeId()).getValu());
                productBean.setColor(MapColor.get(sizeColorMap.getColorId()).getName());
                productBean.setImgurl(MapProductImg.get(sizeColorMap.getProductImgId()).getImgUrl());
                productBean.setPrice(sizeColorMap.getPrice());
                itemTotal = itemTotal + sizeColorMap.getPrice();
                productBean.setDiscountedPrice(sizeColorMap.getDiscount());
                orderTotal = orderTotal + sizeColorMap.getDiscount();
                productBean.setDiscountPCT(this.getPercentage(sizeColorMap.getPrice(), sizeColorMap.getDiscount()));
                productBeans.add(productBean);
            }
            discountTotal = itemTotal - orderTotal;
            discountPctTotal = this.getPercentage(itemTotal, orderTotal);
            cartBean.setProducts(productBeans);
            cartBean.setItemTotal(itemTotal);
            cartBean.setOrderTotal(orderTotal);
            cartBean.setDiscountTotal(discountTotal);
            cartBean.setDiscountPctTotal(discountPctTotal);
        } else {
            cartBean.setStatus("Cart is empty");
        }
        return cartBean;
    }

}
