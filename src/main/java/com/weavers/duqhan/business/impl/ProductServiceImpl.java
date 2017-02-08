/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.easypost.model.Shipment;
import com.weavers.duqhan.business.ProductService;
import com.weavers.duqhan.business.ShippingService;
import com.weavers.duqhan.dao.CartDao;
import com.weavers.duqhan.dao.CategoryDao;
import com.weavers.duqhan.dao.ColorDao;
import com.weavers.duqhan.dao.OrderDetailsDao;
import com.weavers.duqhan.dao.ProductDao;
import com.weavers.duqhan.dao.ProductImgDao;
import com.weavers.duqhan.dao.ProductSizeColorMapDao;
import com.weavers.duqhan.dao.RecentViewDao;
import com.weavers.duqhan.dao.SizeGroupDao;
import com.weavers.duqhan.dao.SizeeDao;
import com.weavers.duqhan.dao.UserAddressDao;
import com.weavers.duqhan.dao.VendorDao;
import com.weavers.duqhan.domain.Cart;
import com.weavers.duqhan.domain.Category;
import com.weavers.duqhan.domain.Color;
import com.weavers.duqhan.domain.OrderDetails;
import com.weavers.duqhan.domain.Product;
import com.weavers.duqhan.domain.ProductImg;
import com.weavers.duqhan.domain.ProductSizeColorMap;
import com.weavers.duqhan.domain.RecentView;
import com.weavers.duqhan.domain.ShipmentTable;
import com.weavers.duqhan.domain.SizeGroup;
import com.weavers.duqhan.domain.Sizee;
import com.weavers.duqhan.domain.UserAddress;
import com.weavers.duqhan.domain.Vendor;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.CategoryDto;
import com.weavers.duqhan.dto.CategorysBean;
import com.weavers.duqhan.dto.ColorAndSizeDto;
import com.weavers.duqhan.dto.ColorDto;
import com.weavers.duqhan.dto.ImageDto;
import com.weavers.duqhan.dto.OrderDetailsBean;
import com.weavers.duqhan.dto.OrderDetailsDto;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.dto.ProductBeans;
import com.weavers.duqhan.dto.ProductDetailBean;
import com.weavers.duqhan.dto.ProductRequistBean;
import com.weavers.duqhan.dto.SizeColorMapDto;
import com.weavers.duqhan.dto.SizeDto;
import com.weavers.duqhan.util.DateFormater;
import com.weavers.duqhan.util.FileUploader;
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
    @Autowired
    OrderDetailsDao orderDetailsDao;
    @Autowired
    UserAddressDao userAddressDao;
    @Autowired
    VendorDao vendorDao;
    @Autowired
    ShippingService shippingService;

    // <editor-fold defaultstate="collapsed" desc="setAddressDto">
    private AddressDto setAddressDto(UserAddress userAddress) {
        AddressDto addressDto = new AddressDto();
        addressDto.setAddressId(userAddress.getId());
        addressDto.setCity(userAddress.getCity());
        addressDto.setCompanyName(userAddress.getCompanyName());
        addressDto.setContactName(userAddress.getContactName());
        addressDto.setCountry(userAddress.getCountry());
        addressDto.setEmail(userAddress.getEmail());
        addressDto.setIsResidential(userAddress.getResidential());
        addressDto.setPhone(userAddress.getPhone());
        addressDto.setState(userAddress.getState());
        addressDto.setStatus(userAddress.getStatus());
        addressDto.setStreetOne(userAddress.getStreetOne());
        addressDto.setStreetTwo(userAddress.getStreetTwo());
        addressDto.setUserId(userAddress.getUserId());
        addressDto.setZipCode(userAddress.getZipCode());
        return addressDto;
    }
// </editor-fold>

    @Override
    public ColorAndSizeDto getColorSizeList() { // get color size category from database on add produc page load.
        List<Sizee> sizees = sizeeDao.loadAll();
        List<Color> colors = colorDao.loadAll();
        List<Category> categorys = categoryDao.loadAll();
        List<SizeGroup> sizeGroups = sizeGroupDao.loadAll();
        List<Vendor> vendors = vendorDao.loadAll();

        ColorAndSizeDto colorAndSizeDto = new ColorAndSizeDto();
        List<SizeDto> sizeDtos = new ArrayList<>();
        List<ColorDto> colorDtos = new ArrayList<>();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        List<SizeDto> sizeGroupDtos = new ArrayList<>();
        List<AddressDto> vendorsDtos = new ArrayList<>();
        for (Sizee sizee : sizees) {    //=============get size.
            SizeDto SizeDto = new SizeDto();
            SizeDto.setSizeId(sizee.getId());
            SizeDto.setSizeText(sizee.getValu());
            sizeDtos.add(SizeDto);
        }
        for (Color color : colors) {    //=============get colors.
            ColorDto ColorDto = new ColorDto();
            ColorDto.setColorId(color.getId());
            ColorDto.setColorText(color.getName());
            colorDtos.add(ColorDto);
        }
        for (Category category : categorys) {   //=============get category.
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryId(category.getId());
            categoryDto.setCategoryName(category.getName());
            categoryDtos.add(categoryDto);
        }
        for (SizeGroup sizeGroup : sizeGroups) {    //=============get size groups.
            SizeDto sizeGroupDto = new SizeDto();
            sizeGroupDto.setSizeGroupId(sizeGroup.getId());
            sizeGroupDto.setSizeText(sizeGroup.getName());
            sizeGroupDtos.add(sizeGroupDto);
        }
        for (Vendor vendor : vendors) {    //=============get Vendor.
            AddressDto vendorDto = new AddressDto();
            vendorDto.setUserId(vendor.getId());
            vendorDto.setContactName(vendor.getVendorName());
            vendorsDtos.add(vendorDto);
        }
        colorAndSizeDto.setSizeGroupDtos(sizeGroupDtos);
        colorAndSizeDto.setSizeDtos(sizeDtos);
        colorAndSizeDto.setColorDtos(colorDtos);
        colorAndSizeDto.setCategoryDtos(categoryDtos);
        colorAndSizeDto.setVendorDtos(vendorsDtos);
        return colorAndSizeDto;
    }

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
                bean.setVendorId(product.getVendorId());
                bean.setShippingRate(product.getShippingRate());
                bean.setShippingTime(product.getShippingTime());
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
            discountPct = (less / original) * 100;  // calculate discount percentage
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
        ProductBeans productBeans = this.setProductBeans(products, mapSizeColorMaps);
        productBeans.setCategoryName(categoryDao.loadById(categoryId).getName());
        return productBeans;
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
            productDetailBean.setVendorId(product.getVendorId());
            productDetailBean.setShippingCost(product.getShippingRate());
            productDetailBean.setShippingTime(product.getShippingTime());
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
                productBean.setShippingRate(MapProduct.get(sizeColorMap.getProductId()).getShippingRate());
                productBean.setShippingTime(MapProduct.get(sizeColorMap.getProductId()).getShippingTime());
                productBean.setVendorId(MapProduct.get(sizeColorMap.getProductId()).getVendorId());
                productBean.setProductHeight(sizeColorMap.getProductHeight());
                productBean.setProductLength(sizeColorMap.getProductLength());
                productBean.setProductWeight(sizeColorMap.getProductWeight());
                productBean.setProductWidth(sizeColorMap.getProductWidth());
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
            product.setVendorId(productBean.getVendorId());     //>>>>>>>>
            Shipment shipment = shippingService.createDefaultShipmentDomestic();
            if (shipment != null && shipment.getRates() != null && !shipment.getRates().isEmpty()) {
                product.setShippingTime(shipment.getRates().get(0).getDeliveryDays() != null ? shipment.getRates().get(0).getDeliveryDays().toString() : null);
                product.setShippingRate(shipment.getRates().get(0).getRate() != null ? shipment.getRates().get(0).getRate().doubleValue() : null);
            } else {
                product.setShippingTime(null);
                product.setShippingRate(null);
            }
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
                        sizeColorMap.setProductHeight(sizeColorMapDto.getProductHeight());
                        sizeColorMap.setProductLength(sizeColorMapDto.getProductLength());
                        sizeColorMap.setProductWeight(sizeColorMapDto.getProductWeight());
                        sizeColorMap.setProductWidth(sizeColorMapDto.getProductWidth());
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
        if (sizee1 == null) {
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
        if (sizeGroup1 == null) {
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
        if (color2 == null) {
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
            categorysBean.setStatusCode("403");
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

    @Override
    public OrderDetailsBean getOrderDetails(Long userId) {
        OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
        List<OrderDetailsDto> orderdetailsDtos = new ArrayList<>();
        List<Object[]> objects = orderDetailsDao.getDetailByUserId(userId);
        List<UserAddress> userAddresses = userAddressDao.getAddressByUserId(userId);
        HashMap<Long, UserAddress> mapUserAddress = new HashMap<>();
        for (UserAddress userAddress : userAddresses) {
            mapUserAddress.put(userAddress.getId(), userAddress);
        }
        List<Product> products = productDao.loadAll();
        HashMap<Long, Product> mapProduct = new HashMap<>();
        for (Product product : products) {
            mapProduct.put(product.getId(), product);
        }
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
        for (Object[] object : objects) {
            OrderDetails orderDetailse = (OrderDetails) object[0];//********************
            ProductSizeColorMap sizeColorMap = (ProductSizeColorMap) object[1];//***********
            OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
            orderDetailsDto.setAddressDto(this.setAddressDto(mapUserAddress.get(orderDetailse.getAddressId())));
            if (mapColor.containsKey(sizeColorMap.getColorId())) {
                orderDetailsDto.setColor(mapColor.get(sizeColorMap.getColorId()).getName());
            }
            if (mapSize.containsKey(sizeColorMap.getSizeId())) {
                orderDetailsDto.setSize(mapSize.get(sizeColorMap.getSizeId()).getValu());
            }
            orderDetailsDto.setOrderId(orderDetailse.getOrderId());
            orderDetailsDto.setDeliveryDate(DateFormater.formate(orderDetailse.getDeliveryDate()));
            orderDetailsDto.setEmail(mapUserAddress.get(orderDetailse.getAddressId()).getEmail());
            orderDetailsDto.setMapId(orderDetailse.getMapId());
            orderDetailsDto.setOrderDate(DateFormater.formate(orderDetailse.getOrderDate()));
            orderDetailsDto.setPaymentAmount(orderDetailse.getPaymentAmount());
            orderDetailsDto.setPaymentKey(orderDetailse.getPaymentKey());
            orderDetailsDto.setPhone(mapUserAddress.get(orderDetailse.getAddressId()).getPhone());
            orderDetailsDto.setProductName(mapProduct.get(sizeColorMap.getProductId()).getName());
            orderDetailsDto.setStatus(orderDetailse.getStatus());
            orderDetailsDto.setProdImg(mapProduct.get(sizeColorMap.getProductId()).getImgurl());
            orderDetailsDto.setPrice(sizeColorMap.getPrice());
            orderDetailsDto.setDiscount(this.getPercentage(sizeColorMap.getPrice(), orderDetailse.getPaymentAmount()));
            orderDetailsDto.setQuty(orderDetailse.getQuentity());
            ShipmentTable shipmentTable = shippingService.getShipmentTableByShipmentId(orderDetailse.getShipmentId());
            if (null != shipmentTable && !shipmentTable.getTrackerId().equals("0")) {
                orderDetailsDto.setTrackerBean(shippingService.getTrackerByTrackerId(shipmentTable.getTrackerId()));
            }
            orderdetailsDtos.add(orderDetailsDto);
        }
        orderDetailsBean.setOrderDetailsDtos(orderdetailsDtos);
        orderDetailsBean.setStatus("success");
        orderDetailsBean.setStatusCode("200");
        return orderDetailsBean;
    }

}
