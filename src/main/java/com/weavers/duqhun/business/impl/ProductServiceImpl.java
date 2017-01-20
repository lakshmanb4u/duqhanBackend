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
import com.weavers.duqhun.dao.SizeGroupDao;
import com.weavers.duqhun.dao.SizeeDao;
import com.weavers.duqhun.domain.Cart;
import com.weavers.duqhun.domain.Category;
import com.weavers.duqhun.domain.Color;
import com.weavers.duqhun.domain.Product;
import com.weavers.duqhun.domain.ProductImg;
import com.weavers.duqhun.domain.ProductSizeColorMap;
import com.weavers.duqhun.domain.RecentView;
import com.weavers.duqhun.domain.SizeGroup;
import com.weavers.duqhun.domain.Sizee;
import com.weavers.duqhun.dto.CartBean;
import com.weavers.duqhun.dto.CategoryDto;
import com.weavers.duqhun.dto.CategorysBean;
import com.weavers.duqhun.dto.ColorDto;
import com.weavers.duqhun.dto.ImageDto;
import com.weavers.duqhun.dto.ProductBean;
import com.weavers.duqhun.dto.ProductBeans;
import com.weavers.duqhun.dto.ProductDetailBean;
import com.weavers.duqhun.dto.ProductRequistBean;
import com.weavers.duqhun.dto.SizeColorMapDto;
import com.weavers.duqhun.dto.SizeDto;
import com.weavers.duqhun.util.FileUploader;
import java.text.DecimalFormat;
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
    @Autowired
    SizeGroupDao sizeGroupDao;

    private ProductBeans setProductBeans(List<Product> products, HashMap<Long, ProductSizeColorMap> mapSizeColorMap) {
        ProductBeans productBeans = new ProductBeans();
        List<String> allImages = new ArrayList<>();
        List<ProductBean> beans = new ArrayList<>();
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
        int i = 0;
        for (Product product : products) {
            if (mapSizeColorMap.containsKey(product.getId())) {
                ProductBean bean = new ProductBean();
                bean.setProductId(product.getId());
                bean.setName(product.getName());
                bean.setImgurl(product.getImgurl());
                allImages.add(product.getImgurl());
                bean.setDescription(product.getDescription());
                bean.setCategoryId(product.getCategoryId());
                bean.setSizeId(mapSizeColorMap.get(product.getId()).getSizeId());
                bean.setColorId(mapSizeColorMap.get(product.getId()).getColorId());
                if (mapSizeColorMap.get(product.getId()).getSizeId() != null) {     // if this product have any size
                    bean.setSize(mapSize.get(mapSizeColorMap.get(product.getId()).getSizeId()).getValu());
                }
                if (mapSizeColorMap.get(product.getId()).getColorId() != null) {    // if this product have any color
                    bean.setColor(mapColor.get(mapSizeColorMap.get(product.getId()).getColorId()).getName());
                }
                bean.setPrice(mapSizeColorMap.get(product.getId()).getPrice());
                bean.setDiscountedPrice(mapSizeColorMap.get(product.getId()).getDiscount());
                bean.setDiscountPCT(this.getPercentage(mapSizeColorMap.get(product.getId()).getPrice(), mapSizeColorMap.get(product.getId()).getDiscount()));
                Long qunty = mapSizeColorMap.get(product.getId()).getQuentity();
                bean.setAvailable(qunty.intValue());
                i = i + qunty.intValue();   // count total product
                beans.add(bean);
            }
        }
        productBeans.setTotalProducts(i);
        productBeans.setProducts(beans);
        productBeans.setAllImages(allImages);
        return productBeans;
    }

    private Double getPercentage(Double original, Double discounted) {
        Double discountPct = 0.00;
        Double less = original - discounted;
        if (original != null && discounted != null && less > 0.00) {
            discountPct = original / less;  // calculate discount percentage
        }
        DecimalFormat df = new DecimalFormat("#.00");
        discountPct = Double.parseDouble(df.format(discountPct));
        return discountPct;
    }

    @Override
    public ProductBeans getProductsByCategory(Long categoryId) {
        List<Product> products = productDao.getProductsByCategory(categoryId);  // Find category wise product 
        List<Long> productIds = new ArrayList<>();
        for (Product product : products) {
            productIds.add(product.getId());
        }
        HashMap<Long, ProductSizeColorMap> mapSizeColorMaps = productSizeColorMapDao.getSizeColorMapbyMinPriceIfAvailable(productIds);
        return this.setProductBeans(products, mapSizeColorMaps);
    }

    @Override
    public ProductBeans getProductsByRecentView(Long userId) {
        List<Product> products = productDao.getAllRecentViewProduct(userId);    // Find recent view product 
        List<Long> productIds = new ArrayList<>();
        for (Product product : products) {
            productIds.add(product.getId());
        }
        HashMap<Long, ProductSizeColorMap> mapSizeColorMaps = productSizeColorMapDao.getSizeColorMapbyMinPriceRecentView(productIds);
        return this.setProductBeans(products, mapSizeColorMaps);
    }

    @Override
    public ProductBeans getAllProducts() {
        List<Product> products = productDao.getAllAvailableProduct();   // Find all products 
        List<Long> productIds = new ArrayList<>();
        for (Product product : products) {
            productIds.add(product.getId());
        }
        HashMap<Long, ProductSizeColorMap> mapSizeColorMaps = productSizeColorMapDao.getSizeColorMapbyMinPriceIfAvailable(productIds);
        return this.setProductBeans(products, mapSizeColorMaps);
    }

    @Override
    public ProductDetailBean getProductDetailsById(Long productId, Long userId) {
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
            productDetailBean.setProductId(product.getId());
            productDetailBean.setName(product.getName());
            productDetailBean.setDescription(product.getDescription());
            productDetailBean.setCategoryId(category.getId());
            productDetailBean.setCategoryName(category.getName());
            productDetailBean.setProductImg(product.getImgurl());
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
            HashMap<Long, Object> check = new HashMap<>();
            HashMap<Long, Object> check1 = new HashMap<>();
            HashMap<Long, List<SizeColorMapDto>> mapSizeColorMapDto = new HashMap<>();
            Double orginalPrice = 0.0;
            Double salesPrice = 0.0;
            int flg = 0;
            for (ProductSizeColorMap sizeColorMap : sizeColorMaps) {
                SizeDto sizeDto = new SizeDto();
                if (mapSize.get(sizeColorMap.getSizeId()) != null) {
                    sizeDto.setSizeId(sizeColorMap.getSizeId());
                    sizeDto.setSizeText(mapSize.get(sizeColorMap.getSizeId()).getValu());
                }
                if (!check.containsKey(sizeColorMap.getSizeId())) {
                    sizeDtos.add(sizeDto);
                    check.put(sizeColorMap.getSizeId(), null);
                }

                if (mapSizeColorMapDto.containsKey(sizeColorMap.getSizeId())) {
                    SizeColorMapDto sizeColorMapDto = new SizeColorMapDto();
                    if (mapColor.get(sizeColorMap.getColorId()) != null) {
                        sizeColorMapDto.setColorId(sizeColorMap.getColorId());
                        sizeColorMapDto.setColorText(mapColor.get(sizeColorMap.getColorId()).getName());
                    }
                    sizeColorMapDto.setMapId(sizeColorMap.getId());
                    sizeColorMapDto.setOrginalPrice(sizeColorMap.getPrice());
                    if (sizeColorMap.getPrice() < orginalPrice) {
                        orginalPrice = sizeColorMap.getPrice();
                        salesPrice = sizeColorMap.getDiscount();
                    }
                    sizeColorMapDto.setSalesPrice(sizeColorMap.getDiscount());
                    sizeColorMapDto.setDiscount(this.getPercentage(sizeColorMap.getPrice(), sizeColorMap.getDiscount()));
                    sizeColorMapDto.setCount(sizeColorMap.getQuentity());
                    mapSizeColorMapDto.get(sizeColorMap.getSizeId()).add(sizeColorMapDto);
                } else {
                    if (flg == 0) {
                        orginalPrice = sizeColorMap.getPrice();
                        salesPrice = sizeColorMap.getDiscount();
                    }
                    flg++;
                    List<SizeColorMapDto> sizeColorMapDtos = new ArrayList<>();
                    SizeColorMapDto sizeColorMapDto = new SizeColorMapDto();
                    if (mapColor.get(sizeColorMap.getColorId()) != null) {
                        sizeColorMapDto.setColorId(sizeColorMap.getColorId());
                        sizeColorMapDto.setColorText(mapColor.get(sizeColorMap.getColorId()).getName());
                    }
                    sizeColorMapDto.setMapId(sizeColorMap.getId());
                    sizeColorMapDto.setOrginalPrice(sizeColorMap.getPrice());
                    if (sizeColorMap.getPrice() < orginalPrice) {
                        orginalPrice = sizeColorMap.getPrice();
                        salesPrice = sizeColorMap.getDiscount();
                    }
                    sizeColorMapDto.setSalesPrice(sizeColorMap.getDiscount());
                    sizeColorMapDto.setDiscount(this.getPercentage(sizeColorMap.getPrice(), sizeColorMap.getDiscount()));
                    sizeColorMapDto.setCount(sizeColorMap.getQuentity());
                    sizeColorMapDtos.add(sizeColorMapDto);
                    mapSizeColorMapDto.put(sizeColorMap.getSizeId(), sizeColorMapDtos);
                }

                ColorDto colorDto = new ColorDto();
                if (mapColor.get(sizeColorMap.getColorId()) != null) {
                    colorDto.setColorId(sizeColorMap.getColorId());
                    colorDto.setColorText(mapColor.get(sizeColorMap.getColorId()).getName());
                }
                if (!check1.containsKey(sizeColorMap.getColorId())) {
                    colorDtos.add(colorDto);
                    check1.put(sizeColorMap.getColorId(), null);
                }
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
            productDetailBean.setOrginalPrice(orginalPrice);
            productDetailBean.setSalesPrice(salesPrice);
            productDetailBean.setDiscount(this.getPercentage(orginalPrice, salesPrice));
            productDetailBean.setArrival("Not set yet..");
            productDetailBean.setShippingCost(null);
            productDetailBean.setRelatedProducts(new ProductBeans());

            RecentView recentView = new RecentView();
            recentView.setId(null);
            recentView.setProductId(product.getId());
            recentView.setUserId(userId);
            recentView.setViewDate(new Date());
            recentViewDao.save(recentView);
        } else {
            productDetailBean.setStatus("No Product Found");
        }
        return productDetailBean;
    }

    @Override
    public String addProductToCart(ProductRequistBean requistBean) {
        String status = "failure";
        Cart cart2 = cartDao.loadByUserIdAndMapId(requistBean.getUserId(), requistBean.getMapId());
        if (cart2 != null) {
            status = "Product already added";
        } else {
            Cart cart = new Cart();
            cart.setId(null);
            cart.setLoadDate(new Date());
            cart.setSizecolormapId(requistBean.getMapId());
            cart.setUserId(requistBean.getUserId());
            Cart cart1 = cartDao.save(cart);    // add product to cart.
            if (cart1 != null) {
                status = "success";
            }
        }
        return status;
    }

    @Override
    public String removeProductFromCart(ProductRequistBean requistBean) {
        String status = "failure";
        Cart cart = cartDao.getCartByIdAndMapId(requistBean.getCartId(), requistBean.getMapId());
        if (cart != null) {
            cartDao.delete(cart);   //remove product from cart.
            status = "success";
        }
        return status;
    }

    @Override
    public CartBean getCartFoAUser(Long userId) {
        CartBean cartBean = new CartBean();
        HashMap<Long, Cart> MapCart = new HashMap<>();
        List<ProductSizeColorMap> sizeColorMaps = cartDao.getProductSizeColorMapByUserId(userId);   // Load user cart
        if (!sizeColorMaps.isEmpty()) {
            List<Cart> carts = cartDao.getCartByUserId(userId);
            for (Cart cart : carts) {
                MapCart.put(cart.getSizecolormapId(), cart);
            }
            List<Long> productIds = new ArrayList<>();
            List<Long> colorIds = new ArrayList<>();
            List<Long> sizeIds = new ArrayList<>();
//            List<Long> imgIds = new ArrayList<>();
            for (ProductSizeColorMap sizeColorMap : sizeColorMaps) {
                productIds.add(sizeColorMap.getProductId());
                colorIds.add(sizeColorMap.getColorId());
                sizeIds.add(sizeColorMap.getSizeId());
//                imgIds.add(sizeColorMap.getProductImgId());
            }
            List<Product> products = productDao.loadByIds(productIds);
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
//            List<ProductImg> productImgs = productImgDao.loadByIds(imgIds);
//            HashMap<Long, ProductImg> MapProductImg = new HashMap<>();
//            for (ProductImg productImg : productImgs) {
//                MapProductImg.put(productImg.getId(), productImg);
//            }
            //============================================================//
            List<ProductBean> productBeans = new ArrayList<>();
            double itemTotal = 0.0;         //2000
            double orderTotal = 0.0;        //1500
            double discountTotal = 0.0;     //500
            double discountPctTotal = 0.0;  //25
            //=====================ready product bean====================//
            for (ProductSizeColorMap sizeColorMap : sizeColorMaps) {
                ProductBean productBean = new ProductBean();
                productBean.setQty("1");
                productBean.setSizeColorMapId(sizeColorMap.getId());
                productBean.setProductId(sizeColorMap.getProductId());
                productBean.setName(MapProduct.get(sizeColorMap.getProductId()).getName());
                productBean.setDescription(MapProduct.get(sizeColorMap.getProductId()).getDescription());
                if (MapSizee.get(sizeColorMap.getSizeId()) != null) {
                    productBean.setSize(MapSizee.get(sizeColorMap.getSizeId()).getValu());
                }
                if (MapColor.get(sizeColorMap.getColorId()) != null) {
                    productBean.setColor(MapColor.get(sizeColorMap.getColorId()).getName());
                }
//                productBean.setImgurl(MapProductImg.get(sizeColorMap.getProductImgId()).getImgUrl());
                productBean.setImgurl(MapProduct.get(sizeColorMap.getProductId()).getImgurl());
                productBean.setPrice(sizeColorMap.getPrice());
                itemTotal = itemTotal + sizeColorMap.getPrice();
                productBean.setDiscountedPrice(sizeColorMap.getDiscount());
                orderTotal = orderTotal + sizeColorMap.getDiscount();
                productBean.setDiscountPCT(this.getPercentage(sizeColorMap.getPrice(), sizeColorMap.getDiscount()));
                productBean.setAvailable(Long.valueOf(sizeColorMap.getQuentity()).intValue());
                productBean.setCartId(MapCart.get(sizeColorMap.getId()).getId());
                productBeans.add(productBean);
            }
            discountTotal = itemTotal - orderTotal;
            discountPctTotal = this.getPercentage(itemTotal, orderTotal);   // calcute discount percentage.
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

    @Override
    public String saveProduct(ProductBean productBean) {
        String status = "ERROR: Product can not be saved!!";
        if (productBean != null) {
            Product product = new Product();
            product.setId(null);
            product.setName(productBean.getName());
            product.setImgurl(productBean.getImgurl());
            product.setCategoryId(productBean.getCategoryId());
            product.setDescription(productBean.getDescription());
            product.setLastUpdate(new Date());
            Product product1 = productDao.save(product);
            if (product1 != null) {
                //====multiple ProductSizeColorMap added=======//
                List<SizeColorMapDto> sizeColorMapDtos = productBean.getSizeColorMaps();
                if (!sizeColorMapDtos.isEmpty()) {
                    for (SizeColorMapDto sizeColorMapDto : sizeColorMapDtos) {
                        ProductSizeColorMap sizeColorMap = new ProductSizeColorMap();
                        sizeColorMap.setId(null);
                        sizeColorMap.setColorId(sizeColorMapDto.getColorId());
                        sizeColorMap.setSizeId(sizeColorMapDto.getSizeId());
                        sizeColorMap.setDiscount(sizeColorMapDto.getSalesPrice());
                        sizeColorMap.setPrice(sizeColorMapDto.getOrginalPrice());
                        sizeColorMap.setQuentity(sizeColorMapDto.getCount());
                        sizeColorMap.setProductId(product1.getId());
                        ProductSizeColorMap sizeColorMap1 = productSizeColorMapDao.save(sizeColorMap);
                    }
                }

                //===========multiple image add==========//
                List<ImageDto> imageDtos = productBean.getImageDtos();
                if (!imageDtos.isEmpty()) {
                    for (ImageDto imageDto : imageDtos) {
                        ProductImg productImg = new ProductImg();
                        productImg.setId(null);
                        productImg.setImgUrl(imageDto.getImgUrl());
                        productImg.setProductId(product1.getId());
                        productImgDao.save(productImg);
                    }
                    status = "Product saved.";
                }
            }
        }
        return status;
    }

    @Override
    public String saveCategory(CategoryDto categoryDto) {
        String status = "ERROR: Category can not be saved!!";
        if (categoryDto.getCategoryName() != null) {
            Category category = new Category();
            category.setId(null);
            category.setName(categoryDto.getCategoryName());
            category.setParentId(categoryDto.getPatentId());
            Category category2 = categoryDao.save(category);    //save new category
            if (category2 != null) {
                status = "Category saved.";
            }
        }
        return status;
    }

    @Override
    public String saveSize(SizeDto sizeDto) {
        Sizee sizee = new Sizee();
        sizee.setId(null);
        sizee.setGroupId(sizeDto.getSizeGroupId());
        sizee.setValu(sizeDto.getSizeText());
        sizee.setUnit(sizeDto.getSizeText());
        Sizee sizee1 = sizeeDao.save(sizee);    //save new size
        if (sizee1 != null) {
            return "ERROR: Size can not be saved!!";
        } else {
            return "Size saved..";
        }
    }

    @Override
    public String saveSizeGroup(String sizeGroupName) {
        SizeGroup sizeGroup = new SizeGroup();
        sizeGroup.setId(null);
        sizeGroup.setName(sizeGroupName);
        SizeGroup sizeGroup1 = sizeGroupDao.save(sizeGroup);    //save new size group
        if (sizeGroup1 != null) {
            return "ERROR: SizeGroup can not be saved!!";
        } else {
            return "SizeGroup saved..";
        }
    }

    @Override
    public String saveColor(String color) {
        Color color1 = new Color();
        color1.setId(null);
        color1.setName(color);
        color1.setCode(color);
        Color color2 = colorDao.save(color1);   //save new color
        if (color2 != null) {
            return "ERROR: Color can not be saved!!";
        } else {
            return "Color saved..";
        }
    }

    @Override
    public Long getCartCountFoAUser(Long userId) {
        return cartDao.getCartCountByUserId(userId);    //number of item in cart fo a user.
    }

    @Override
    public String saveProductImage(ProductBean productBean) {
        return FileUploader.uploadImage(productBean.getFrontImage()).getUrl();
    }

    @Override
    public CategorysBean getChildById(Long parentId) {
        CategorysBean categorysBean = new CategorysBean();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        List<Category> categorys = categoryDao.getChildByParentId(parentId);
        if (categorys.isEmpty()) {
            categorysBean.setStatus("No child category found");
        } else {
            for (Category category : categorys) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setCategoryId(category.getId());
                categoryDto.setCategoryName(category.getName());
                categoryDtos.add(categoryDto);
            }
            categorysBean.setCategoryDtos(categoryDtos);
            categorysBean.setChildCount(categoryDtos.size());
            List<Product> products = productDao.getProductsByCategory(parentId);
            categorysBean.setProductCount(0);
            if (!products.isEmpty()) {
                categorysBean.setProductCount(products.size());
            }
            categorysBean.setStatus("success");
            categorysBean.setStatusCode("200");
        }
        return categorysBean;
    }

}
