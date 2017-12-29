/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.easypost.model.User;
import com.weavers.duqhan.business.MailService;
import com.weavers.duqhan.business.ProductService;
import com.weavers.duqhan.business.ShippingService;
import com.weavers.duqhan.dao.CartDao;
import com.weavers.duqhan.dao.CategoryDao;
import com.weavers.duqhan.dao.ImpressionsDao;
import com.weavers.duqhan.dao.LikeUnlikeProductDao;
import com.weavers.duqhan.dao.OfferProductsDao;
import com.weavers.duqhan.dao.OrderDetailsDao;
import com.weavers.duqhan.dao.PaymentDetailDao;
import com.weavers.duqhan.dao.ProductDao;
import com.weavers.duqhan.dao.ProductImgDao;
import com.weavers.duqhan.dao.ProductPropertiesDao;
import com.weavers.duqhan.dao.ProductPropertiesMapDao;
import com.weavers.duqhan.dao.ProductPropertyvaluesDao;
import com.weavers.duqhan.dao.RecentViewDao;
import com.weavers.duqhan.dao.RecordedActionsDao;
import com.weavers.duqhan.dao.RequestReturnDao;
import com.weavers.duqhan.dao.ReviewDao;
import com.weavers.duqhan.dao.UserAddressDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.dao.VendorDao;
import com.weavers.duqhan.domain.Cart;
import com.weavers.duqhan.domain.Category;
import com.weavers.duqhan.domain.Impressions;
import com.weavers.duqhan.domain.LikeUnlikeProduct;
import com.weavers.duqhan.domain.OrderDetails;
import com.weavers.duqhan.domain.PaymentDetail;
import com.weavers.duqhan.domain.Product;
import com.weavers.duqhan.domain.ProductImg;
import com.weavers.duqhan.domain.ProductProperties;
import com.weavers.duqhan.domain.ProductPropertiesMap;
import com.weavers.duqhan.domain.ProductPropertyvalues;
import com.weavers.duqhan.domain.RecentView;
import com.weavers.duqhan.domain.RecordedActions;
import com.weavers.duqhan.domain.RequestReturn;
import com.weavers.duqhan.domain.Review;
import com.weavers.duqhan.domain.ShipmentTable;
import com.weavers.duqhan.domain.UserAddress;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.CategoryDto;
import com.weavers.duqhan.dto.CategorysBean;
import com.weavers.duqhan.dto.ImageDto;
import com.weavers.duqhan.dto.LikeDto;
import com.weavers.duqhan.dto.LikeUnlikeProductBean;
import com.weavers.duqhan.dto.LikeUnlikeProductDto;
import com.weavers.duqhan.dto.OrderDetailsBean;
import com.weavers.duqhan.dto.OrderDetailsDto;
import com.weavers.duqhan.dto.OrderReturnDto;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.dto.ProductBeans;
import com.weavers.duqhan.dto.ProductDetailBean;
import com.weavers.duqhan.dto.ProductNewBean;
import com.weavers.duqhan.dto.ProductNewBeans;
import com.weavers.duqhan.dto.ProductPropertiesMapDto;
import com.weavers.duqhan.dto.ProductRequistBean;
import com.weavers.duqhan.dto.PropertyDto;
import com.weavers.duqhan.dto.PropertyValueDto;
import com.weavers.duqhan.dto.ReviewBean;
import com.weavers.duqhan.dto.ReviewDto;
import com.weavers.duqhan.util.DateFormater;
import com.weavers.duqhan.util.GoogleBucketFileUploader;
import com.weavers.duqhan.util.StatusConstants;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Android-3
 */
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;
    @Autowired
    ProductPropertiesMapDao productPropertiesMapDao;
    @Autowired
    ProductPropertiesDao productPropertiesDao;
    @Autowired
    ProductPropertyvaluesDao productPropertyvaluesDao;
    @Autowired
    RecentViewDao recentViewDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ProductImgDao productImgDao;
    @Autowired
    CartDao cartDao;
    @Autowired
    OrderDetailsDao orderDetailsDao;
    @Autowired
    UserAddressDao userAddressDao;
    @Autowired
    VendorDao vendorDao;
    @Autowired
    ShippingService shippingService;
    @Autowired
    PaymentDetailDao paymentDetailDao;
    @Autowired
    MailService mailService;
    @Autowired
    OfferProductsDao offerProductsDao;
    @Autowired
    UsersDao usersDao;
    @Autowired
    ReviewDao reviewDao;
    @Autowired
    LikeUnlikeProductDao likeUnlikeProductDao;
	@Autowired
    RequestReturnDao requestReturnDao;
	@Autowired
    ImpressionsDao impressionsDao;
	@Autowired
    RecordedActionsDao recordedActionsDao;

    private final Logger logger = Logger.getLogger(ProductServiceImpl.class);

    private Double getTwoDecimalFormat(Double unformatedValue) {
        Double formatedValue = 0.0;
        if (unformatedValue != null && unformatedValue > 0) {
//            formatedValue = Math.round(unformatedValue * 100.0) / 100.0;
            DecimalFormat df = new DecimalFormat("#.00");
            try {
                formatedValue = Double.parseDouble(df.format(unformatedValue));
            } catch (IllegalArgumentException e) {
            }
        }
        return formatedValue;
    }

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
    
    private ProductNewBeans setNewProductBeans(List<Product> products, HashMap<Long, ProductPropertiesMap> mapProductPropertiesMaps, long startTime,Users users) {
        ProductNewBeans productNewBeans = new ProductNewBeans();
        List<ProductNewBean> beans = new ArrayList<>();
        for (Product product : products) {
            if (mapProductPropertiesMaps.containsKey(product.getId())) {
                ProductNewBean bean = new ProductNewBean();
                double price = getTwoDecimalFormat(mapProductPropertiesMaps.get(product.getId()).getPrice()) + StatusConstants.PRICE_GREASE;
                bean.setProductId(product.getId());
                bean.setName(product.getName());
                bean.setPrice(price);
                bean.setDiscountedPrice(getTwoDecimalFormat(mapProductPropertiesMaps.get(product.getId()).getDiscount()));
                bean.setDiscountPCT(this.getPercentage(price, mapProductPropertiesMaps.get(product.getId()).getDiscount()));
                if(product.getThumbImg()==null || (product.getThumbImg().equals("-")) || (product.getThumbImg().equals("failure"))){
                	if(!(product.getImgurl() ==null || product.getImgurl().equals("-") || product.getImgurl().equals("failure"))) {
                    bean.setImgurl(product.getImgurl());
                	beans.add(bean);
                	}
                  }else{
                
                		bean.setImgurl(product.getThumbImg());
                		beans.add(bean);
                }
                
            }
        }
        productNewBeans.setProducts(beans);
        return productNewBeans;
    
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setProductBeans">
    private ProductBeans setProductBeans(List<Product> products, HashMap<Long, ProductPropertiesMap> mapProductPropertiesMaps, long startTime,Users users) {
        ProductBeans productBeans = new ProductBeans();
        List<String> allImages = new ArrayList<>();
        List<ProductBean> beans = new ArrayList<>();
        List<Long> ids;

        //==================Load Category start====================//
        ids = new ArrayList<>();
        for (Product product : products) {
            ids.add(product.getCategoryId());
        }
        System.out.println("Start Of Query for category load by Id==========================="+(startTime-System.currentTimeMillis()));
        List<Category> categorys = categoryDao.loadByIds(ids);
        System.out.println("End Of Query for category load by Id==========================="+(startTime-System.currentTimeMillis()));
        HashMap<Long, String> mapCategory = new HashMap<>();
        for (Category category : categorys) {
            mapCategory.put(category.getId(), category.getName());
        }
        //==================Load Category end====================//
        int i = 0;
        for (Product product : products) {
            if (mapProductPropertiesMaps.containsKey(product.getId())) {
                ProductBean bean = new ProductBean();
                double price = getTwoDecimalFormat(mapProductPropertiesMaps.get(product.getId()).getPrice()) + StatusConstants.PRICE_GREASE;
                bean.setProductId(product.getId());
                bean.setName(product.getName());
                if(product.getThumbImg()==null){
                	bean.setImgurl(product.getImgurl());
                  }else if (product.getThumbImg().equals("-") || product.getThumbImg().equals("failure")) {
                	  bean.setImgurl(null);
                  }else{
                	bean.setImgurl(product.getThumbImg());
					}
                /*LikeUnlikeProduct likeUnlikeProduct=likeUnlikeProductDao.getProductLikeUnlike(product.getId(),users.getId() );
                if(Objects.nonNull(likeUnlikeProduct)){
                bean.setLikeUnlike(likeUnlikeProduct.isLikeUnlike());}
                else{
                	bean.setLikeUnlike(false);}*/
                bean.setDescription(product.getDescription());
                bean.setCategoryId(product.getCategoryId());
                bean.setCategoryName(mapCategory.get(product.getCategoryId()));
                bean.setPrice(price);
                bean.setDiscountedPrice(getTwoDecimalFormat(mapProductPropertiesMaps.get(product.getId()).getDiscount()));
                bean.setDiscountPCT(this.getPercentage(price, mapProductPropertiesMaps.get(product.getId()).getDiscount()));
                Long qunty = mapProductPropertiesMaps.get(product.getId()).getQuantity();
                bean.setAvailable(qunty.intValue());
                bean.setVendorId(product.getVendorId());
                bean.setShippingRate(product.getShippingRate());
                bean.setShippingTime(product.getShippingTime());
                bean.setExternalLink(product.getExternalLink());
                boolean isAdd = false;
                double filter = StatusConstants.PRICE_FILTER;
                if (product.getParentPath().contains("25")) {
                    filter = StatusConstants.PRICE_FILTER_BAG;
                }
                if (bean.getDiscountedPrice() < filter) {
                    isAdd = true;
                    allImages.add(product.getImgurl());
                }

                //==========================load specification start==============================//
                String specifications = product.getSpecifications();
                bean.setSpecifications(specifications);
                if (specifications != null && !specifications.equals("")) {
                    String[] fiturs = specifications.split(",");
                    HashMap<String, String> map = new HashMap<>();
                    for (String fitur : fiturs) {
                        map.put(fitur.split(":")[0], fitur.split(":")[1]);
                    }
                    bean.setSpecificationsMap(map);
                } else {
                    bean.setSpecificationsMap(new HashMap<String, String>());
                }
                //==========================load specification end==============================//

                //==========================Price filter start==============================//
                if (isAdd) {
                    i = i + qunty.intValue();   // count total product
                    beans.add(bean);
                }
                //==========================Price filter end==============================//
            }
        }
        productBeans.setTotalProducts(i);
        productBeans.setProducts(beans);
        productBeans.setAllImages(allImages);
        System.out.println("End Of Older Logic==========================="+(startTime-System.currentTimeMillis()));
        return productBeans;
    }
    
    private ProductNewBeans setNewCacheProductBeans(List<Product> products, HashMap<Long, ProductPropertiesMap> mapProductPropertiesMaps, long startTime) {
        ProductNewBeans productNewBeans = new ProductNewBeans();
        List<ProductNewBean> beans = new ArrayList<>();
        for (Product product : products) {
            if (mapProductPropertiesMaps.containsKey(product.getId())) {
                ProductNewBean bean = new ProductNewBean();
                double price = getTwoDecimalFormat(mapProductPropertiesMaps.get(product.getId()).getPrice()) + StatusConstants.PRICE_GREASE;
                bean.setProductId(product.getId());
                bean.setName(product.getName());
                if(product.getThumbImg()==null){
                	bean.setImgurl(product.getImgurl());
                  }else if (product.getThumbImg().equals("-") || product.getThumbImg().equals("failure")) {
                	  bean.setImgurl(null);
                  }else{
                	bean.setImgurl(product.getThumbImg());
					}
                bean.setPrice(price);
                bean.setDiscountedPrice(getTwoDecimalFormat(mapProductPropertiesMaps.get(product.getId()).getDiscount()));
                bean.setDiscountPCT(this.getPercentage(price, mapProductPropertiesMaps.get(product.getId()).getDiscount()));
                beans.add(bean);
            }
        }
        productNewBeans.setProducts(beans);
        return productNewBeans;
    
    }
    
    private ProductBeans setCacheProductBeans(List<Product> products, HashMap<Long, ProductPropertiesMap> mapProductPropertiesMaps, long startTime) {
        ProductBeans productBeans = new ProductBeans();
        List<String> allImages = new ArrayList<>();
        List<ProductBean> beans = new ArrayList<>();
        List<Long> ids;

        //==================Load Category start====================//
        ids = new ArrayList<>();
        for (Product product : products) {
            ids.add(product.getCategoryId());
        }
        System.out.println("Start Of Query for category load by Id==========================="+(startTime-System.currentTimeMillis()));
        List<Category> categorys = categoryDao.loadByIds(ids);
        System.out.println("End Of Query for category load by Id==========================="+(startTime-System.currentTimeMillis()));
        HashMap<Long, String> mapCategory = new HashMap<>();
        for (Category category : categorys) {
            mapCategory.put(category.getId(), category.getName());
        }
        //==================Load Category end====================//
        int i = 0;
        for (Product product : products) {
            if (mapProductPropertiesMaps.containsKey(product.getId())) {
                ProductBean bean = new ProductBean();
                double price = getTwoDecimalFormat(mapProductPropertiesMaps.get(product.getId()).getPrice()) + StatusConstants.PRICE_GREASE;
                bean.setProductId(product.getId());
                bean.setName(product.getName());
                if(product.getThumbImg()==null){
                	bean.setImgurl(product.getImgurl());
                  }else if (product.getThumbImg().equals("-") || product.getThumbImg().equals("failure")) {
                	  bean.setImgurl(null);
                  }else{
                	bean.setImgurl(product.getThumbImg());
					}
                bean.setDescription(product.getDescription());
                bean.setCategoryId(product.getCategoryId());
                bean.setCategoryName(mapCategory.get(product.getCategoryId()));
                bean.setPrice(price);
                bean.setDiscountedPrice(getTwoDecimalFormat(mapProductPropertiesMaps.get(product.getId()).getDiscount()));
                bean.setDiscountPCT(this.getPercentage(price, mapProductPropertiesMaps.get(product.getId()).getDiscount()));
                Long qunty = mapProductPropertiesMaps.get(product.getId()).getQuantity();
                bean.setAvailable(qunty.intValue());
                bean.setVendorId(product.getVendorId());
                bean.setShippingRate(product.getShippingRate());
                bean.setShippingTime(product.getShippingTime());
                bean.setExternalLink(product.getExternalLink());
                boolean isAdd = false;
                double filter = StatusConstants.PRICE_FILTER;
                if (product.getParentPath().contains("25")) {
                    filter = StatusConstants.PRICE_FILTER_BAG;
                }
                if (bean.getDiscountedPrice() < filter) {
                    isAdd = true;
                    allImages.add(product.getImgurl());
                }

                //==========================load specification start==============================//
                String specifications = product.getSpecifications();
                bean.setSpecifications(specifications);
                if (specifications != null && !specifications.equals("")) {
                    String[] fiturs = specifications.split(",");
                    HashMap<String, String> map = new HashMap<>();
                    for (String fitur : fiturs) {
                        map.put(fitur.split(":")[0], fitur.split(":")[1]);
                    }
                    bean.setSpecificationsMap(map);
                } else {
                    bean.setSpecificationsMap(new HashMap<String, String>());
                }
                //==========================load specification end==============================//

                //==========================Price filter start==============================//
                if (isAdd) {
                    i = i + qunty.intValue();   // count total product
                    beans.add(bean);
                }
                //==========================Price filter end==============================//
            }
        }
        productBeans.setTotalProducts(i);
        productBeans.setProducts(beans);
        productBeans.setAllImages(allImages);
        System.out.println("End Of Older Logic==========================="+(startTime-System.currentTimeMillis()));
        return productBeans;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getPercentage">
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
    // </editor-fold>

    /*@Override
    public ColorAndSizeDto getColorSizeList() { // get color size category from database for add produc page on load.
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

    @Override
    public ColorAndSizeDto getCategories() {
        List<Category> categorys = categoryDao.loadAll();

        ColorAndSizeDto colorAndSizeDto = new ColorAndSizeDto();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categorys) {   //=============get category.
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryId(category.getId());
            categoryDto.setCategoryName(category.getName());
            categoryDtos.add(categoryDto);
        }
        colorAndSizeDto.setCategoryDtos(categoryDtos);
        return colorAndSizeDto;
    }

    @Override
    public ColorAndSizeDto getSizes() {
        List<Sizee> sizees = sizeeDao.loadAll();

        ColorAndSizeDto colorAndSizeDto = new ColorAndSizeDto();
        List<SizeDto> sizeDtos = new ArrayList<>();
        for (Sizee sizee : sizees) {    //=============get size.
            SizeDto SizeDto = new SizeDto();
            SizeDto.setSizeId(sizee.getId());
            SizeDto.setSizeText(sizee.getValu());
            sizeDtos.add(SizeDto);
        }
        colorAndSizeDto.setSizeDtos(sizeDtos);
        return colorAndSizeDto;
    }

    @Override
    public ColorAndSizeDto getSizeGroupe() {
        List<SizeGroup> sizeGroups = sizeGroupDao.loadAll();

        ColorAndSizeDto colorAndSizeDto = new ColorAndSizeDto();
        List<SizeDto> sizeGroupDtos = new ArrayList<>();
        for (SizeGroup sizeGroup : sizeGroups) {    //=============get size groups.
            SizeDto sizeGroupDto = new SizeDto();
            sizeGroupDto.setSizeGroupId(sizeGroup.getId());
            sizeGroupDto.setSizeText(sizeGroup.getName());
            sizeGroupDtos.add(sizeGroupDto);
        }
        colorAndSizeDto.setSizeGroupDtos(sizeGroupDtos);
        return colorAndSizeDto;
    }

    @Override
    public ColorAndSizeDto getColors() {
        List<Color> colors = colorDao.loadAll();

        ColorAndSizeDto colorAndSizeDto = new ColorAndSizeDto();
        List<ColorDto> colorDtos = new ArrayList<>();
        for (Color color : colors) {    //=============get colors.
            ColorDto ColorDto = new ColorDto();
            ColorDto.setColorId(color.getId());
            ColorDto.setColorText(color.getName());
            colorDtos.add(ColorDto);
        }
        colorAndSizeDto.setColorDtos(colorDtos);
        return colorAndSizeDto;
    }

    @Override
    public ColorAndSizeDto getVendors() {
        List<Vendor> vendors = vendorDao.loadAll();

        ColorAndSizeDto colorAndSizeDto = new ColorAndSizeDto();
        List<AddressDto> vendorsDtos = new ArrayList<>();
        for (Vendor vendor : vendors) {    //=============get Vendor.
            AddressDto vendorDto = new AddressDto();
            vendorDto.setUserId(vendor.getId());
            vendorDto.setContactName(vendor.getVendorName());
            vendorsDtos.add(vendorDto);
        }
        colorAndSizeDto.setVendorDtos(vendorsDtos);
        return colorAndSizeDto;
    }

    @Override
    public ColorAndSizeDto getSpecificationsByCategoryId(Long categoryId) {
        List<Specification> specifications = specificationDao.getSpecificationsByCategoryId(categoryId);
        List<SpecificationDto> specificationDtos = new ArrayList<>();
        for (Specification specification : specifications) {
            SpecificationDto specificationDto = new SpecificationDto();
            String[] values = specification.getFeaturesValue().split(",");
            specificationDto.setId(specification.getId());
            specificationDto.setName(specification.getFeatures());
            specificationDto.setValues(Arrays.asList(values));
            specificationDtos.add(specificationDto);
        }
        ColorAndSizeDto colorAndSizeDto = new ColorAndSizeDto();
        colorAndSizeDto.setSpecifications(specificationDtos);
        return colorAndSizeDto;
    }*/
    @Override
    public ProductNewBeans getProductsByCategory(Long categoryId, int start, int limit, ProductRequistBean requestBean,long startTime,Users users) {
    	List<Product> products = new ArrayList<Product>();
    	products = productDao.getProductsByCategoryIncludeChildDiscount(categoryId, start, limit,StatusConstants.PRICE_FILTER_BAG,StatusConstants.PRICE_FILTER,startTime);  // Find category wise product 
    	System.out.println("End Of Query for product==========================="+(startTime-System.currentTimeMillis()));
    	HashMap<Long, ProductPropertiesMap> mapProductPropertiesMap = new HashMap<>();
        for (Product product : products) {
            List<ProductPropertiesMap> productPropertiesMaps = product.getProductPropertiesMaps();
            for (ProductPropertiesMap productPropertiesMap : productPropertiesMaps) {
                Long productId = productPropertiesMap.getProductId().getId();
                if (mapProductPropertiesMap.containsKey(productId)) {    // if map contains then update
                    if (mapProductPropertiesMap.get(productId).getDiscount() > productPropertiesMap.getDiscount()) {
                        mapProductPropertiesMap.get(productId).setDiscount(productPropertiesMap.getDiscount());
                        mapProductPropertiesMap.get(productId).setPrice(productPropertiesMap.getPrice());
                        mapProductPropertiesMap.get(productId).setQuantity(mapProductPropertiesMap.get(productId).getQuantity() + productPropertiesMap.getQuantity());
                        
                    }
                } else {    // else add new
                    mapProductPropertiesMap.put(productId, productPropertiesMap);
                }
            }
            /*Impressions impressions = new Impressions();
            impressions.setDate(new Date());
            impressions.setProductId(product.getId());
            impressions.setUserId(users.getId());
            impressionsDao.save(impressions);*/
        }
        System.out.println("logicccc end==========================="+(startTime-System.currentTimeMillis()));
//        HashMap<Long, ProductPropertiesMap> mapProductPropertiesMaps = productPropertiesMapDao.getProductPropertiesMapByMinPriceIfAvailable(productIds);
        //ProductBeans productBeans = this.setProductBeans(products, mapProductPropertiesMap,startTime,users);
        ProductNewBeans productBeans = this.setNewProductBeans(products, mapProductPropertiesMap,startTime,users);
        System.out.println("Start Of categoryDao load by id==========================="+(startTime-System.currentTimeMillis()));
        System.out.println("End Of categoryDao load by id==========================="+(startTime-System.currentTimeMillis()));
        return productBeans;
    }
    
    @Override
    public ProductNewBeans getProductsByPrice(Long categoryId, int start, int limit, ProductRequistBean requestBean,long startTime,Users users,String lp,String hp) {
    	List<Product> products = new ArrayList<Product>();
    	Double lowPrice = Double.parseDouble(lp);
    	Double highPrice = Double.parseDouble(hp);
    	products = productDao.getProductsByPrice(categoryId, start, limit,StatusConstants.PRICE_FILTER_BAG,StatusConstants.PRICE_FILTER,startTime,lowPrice,highPrice);  // Find category wise product 
    	System.out.println("End Of Query for product==========================="+(startTime-System.currentTimeMillis()));
    	HashMap<Long, ProductPropertiesMap> mapProductPropertiesMap = new HashMap<>();
        for (Product product : products) {
            List<ProductPropertiesMap> productPropertiesMaps = product.getProductPropertiesMaps();
            for (ProductPropertiesMap productPropertiesMap : productPropertiesMaps) {
                Long productId = productPropertiesMap.getProductId().getId();
                if (mapProductPropertiesMap.containsKey(productId)) {    // if map contains then update
                    if (mapProductPropertiesMap.get(productId).getDiscount() > productPropertiesMap.getDiscount()) {
                        mapProductPropertiesMap.get(productId).setDiscount(productPropertiesMap.getDiscount());
                        mapProductPropertiesMap.get(productId).setPrice(productPropertiesMap.getPrice());
                        mapProductPropertiesMap.get(productId).setQuantity(mapProductPropertiesMap.get(productId).getQuantity() + productPropertiesMap.getQuantity());
                        
                    }
                } else {    // else add new
                    mapProductPropertiesMap.put(productId, productPropertiesMap);
                }
            }
            /*Impressions impressions = new Impressions();
            impressions.setDate(new Date());
            impressions.setProductId(product.getId());
            impressions.setUserId(users.getId());
            impressionsDao.save(impressions);*/
        }
        System.out.println("logicccc end==========================="+(startTime-System.currentTimeMillis()));
//        HashMap<Long, ProductPropertiesMap> mapProductPropertiesMaps = productPropertiesMapDao.getProductPropertiesMapByMinPriceIfAvailable(productIds);
        //ProductBeans productBeans = this.setProductBeans(products, mapProductPropertiesMap,startTime,users);
        ProductNewBeans productBeans = this.setNewProductBeans(products, mapProductPropertiesMap,startTime,users);
        System.out.println("Start Of categoryDao load by id==========================="+(startTime-System.currentTimeMillis()));
        System.out.println("End Of categoryDao load by id==========================="+(startTime-System.currentTimeMillis()));
        return productBeans;
    }

    @Override
    public ProductNewBeans getProductsByRecentView(Long userId, int start, int limit, ProductRequistBean requestBean,Users users) {
        List<Product> products = productDao.getAllRecentViewProduct(userId, start, limit, requestBean.getPriceLt(), requestBean.getPriceGt(), requestBean.getPriceOrderBy());    // Find recent view product 
        HashMap<Long, ProductPropertiesMap> mapProductPropertiesMap = new HashMap<>();
        for (Product product : products) {
            List<ProductPropertiesMap> productPropertiesMaps = product.getProductPropertiesMaps();
            for (ProductPropertiesMap productPropertiesMap : productPropertiesMaps) {
                Long productId = productPropertiesMap.getProductId().getId();
                if (mapProductPropertiesMap.containsKey(productId)) {    // if map contains then update
                    if (mapProductPropertiesMap.get(productId).getDiscount() > productPropertiesMap.getDiscount()) {
                        mapProductPropertiesMap.get(productId).setDiscount(productPropertiesMap.getDiscount());
                        mapProductPropertiesMap.get(productId).setPrice(productPropertiesMap.getPrice());
                        mapProductPropertiesMap.get(productId).setQuantity(mapProductPropertiesMap.get(productId).getQuantity() + productPropertiesMap.getQuantity());
                    }
                } else {    // else add new
                    mapProductPropertiesMap.put(productId, productPropertiesMap);
                }
            }
            /*Impressions impressions = new Impressions();
            impressions.setDate(new Date());
            impressions.setProductId(product.getId());
            impressions.setUserId(users.getId());
            impressionsDao.save(impressions);*/
        }
        //HashMap<Long, ProductPropertiesMap> mapProductPropertiesMaps = productPropertiesMapDao.getProductPropertiesMapByMinPriceRecentView(productIds);
        return this.setNewProductBeans(products, mapProductPropertiesMap,25L,users);
    }
    
    @Override
    public ProductNewBeans getProductsByRecentViewPrice(Long userId, int start, int limit, ProductRequistBean requestBean,Users users,String lp,String hp) {
        Double lowPrice = Double.parseDouble(lp);
        Double highPrice = Double.parseDouble(hp);
    	List<Product> products = productDao.getAllRecentViewProductByPrice(userId, start, limit, requestBean.getPriceLt(), requestBean.getPriceGt(), requestBean.getPriceOrderBy(),lowPrice,highPrice);    // Find recent view product 
        HashMap<Long, ProductPropertiesMap> mapProductPropertiesMap = new HashMap<>();
        for (Product product : products) {
            List<ProductPropertiesMap> productPropertiesMaps = product.getProductPropertiesMaps();
            for (ProductPropertiesMap productPropertiesMap : productPropertiesMaps) {
                Long productId = productPropertiesMap.getProductId().getId();
                if (mapProductPropertiesMap.containsKey(productId)) {    // if map contains then update
                    if (mapProductPropertiesMap.get(productId).getDiscount() > productPropertiesMap.getDiscount()) {
                        mapProductPropertiesMap.get(productId).setDiscount(productPropertiesMap.getDiscount());
                        mapProductPropertiesMap.get(productId).setPrice(productPropertiesMap.getPrice());
                        mapProductPropertiesMap.get(productId).setQuantity(mapProductPropertiesMap.get(productId).getQuantity() + productPropertiesMap.getQuantity());
                    }
                } else {    // else add new
                    mapProductPropertiesMap.put(productId, productPropertiesMap);
                }
            }
            /*Impressions impressions = new Impressions();
            impressions.setDate(new Date());
            impressions.setProductId(product.getId());
            impressions.setUserId(users.getId());
            impressionsDao.save(impressions);*/
        }
        //HashMap<Long, ProductPropertiesMap> mapProductPropertiesMaps = productPropertiesMapDao.getProductPropertiesMapByMinPriceRecentView(productIds);
        return this.setNewProductBeans(products, mapProductPropertiesMap,25L,users);
    }

    @Override
    public ProductNewBeans getAllCacheProducts(int start, int limit, ProductRequistBean requestBean) {
    	if(requestBean.getPriceLt() == null){
    		requestBean.setPriceLt(0);
    	}
    	if(requestBean.getPriceOrderBy() == null){
    		requestBean.setPriceOrderBy("ASC");
        }
        
        
        
        List<Product> products = productDao.getAllAvailableProductByCategories(start, limit, requestBean.getPriceLt(), requestBean.getPriceGt(), requestBean.getPriceOrderBy());   // Find all products 
        HashMap<Long, ProductPropertiesMap> mapProductPropertiesMap = new HashMap<>();
        for (Product product : products) {
            List<ProductPropertiesMap> productPropertiesMaps = product.getProductPropertiesMaps();
            for (ProductPropertiesMap productPropertiesMap : productPropertiesMaps) {
                Long productId = productPropertiesMap.getProductId().getId();
                if (mapProductPropertiesMap.containsKey(productId)) {    // if map contains then update
                    if (mapProductPropertiesMap.get(productId).getDiscount() > productPropertiesMap.getDiscount()) {
                        mapProductPropertiesMap.get(productId).setDiscount(productPropertiesMap.getDiscount());
                        mapProductPropertiesMap.get(productId).setPrice(productPropertiesMap.getPrice());
                        mapProductPropertiesMap.get(productId).setQuantity(mapProductPropertiesMap.get(productId).getQuantity() + productPropertiesMap.getQuantity());
                    }
                } else {    // else add new
                    mapProductPropertiesMap.put(productId, productPropertiesMap);
                }
            }
        }
//        HashMap<Long, ProductPropertiesMap> mapProductPropertiesMaps = productPropertiesMapDao.getProductPropertiesMapByMinPriceIfAvailable(productIds);
        return this.setNewCacheProductBeans(products, mapProductPropertiesMap,25L);
    }
    
    

//    @Override
//    public ProductBeans getAllProductsIncloudeZeroAvailable(int start, int limit) {
//        List<Product> products = productDao.getAllAvailableProduct(start, limit);   // Find all products 
//        List<Long> productIds = new ArrayList<>();
//        for (Product product : products) {
//            productIds.add(product.getId());
//        }
//        HashMap<Long, ProductSizeColorMap> mapSizeColorMaps = productSizeColorMapDao.getSizeColorMapByProductIds(productIds);
//        return this.setProductBeans(products, mapSizeColorMaps);
//    }
    @Override
    public ProductBeans searchProducts(ProductRequistBean requistBean,Users users) {
        List<Product> products = productDao.SearchProductByNameAndDescription(requistBean.getName(), requistBean.getStart(), requistBean.getLimit());   // Search products by name and Description
        HashMap<Long, ProductPropertiesMap> mapProductPropertiesMap = new HashMap<>();
        for (Product product : products) {
            List<ProductPropertiesMap> productPropertiesMaps = product.getProductPropertiesMaps();
            for (ProductPropertiesMap productPropertiesMap : productPropertiesMaps) {
                Long productId = productPropertiesMap.getProductId().getId();
                if (mapProductPropertiesMap.containsKey(productId)) {    // if map contains then update
                    if (mapProductPropertiesMap.get(productId).getDiscount() > productPropertiesMap.getDiscount()) {
                        mapProductPropertiesMap.get(productId).setDiscount(productPropertiesMap.getDiscount());
                        mapProductPropertiesMap.get(productId).setPrice(productPropertiesMap.getPrice());
                        mapProductPropertiesMap.get(productId).setQuantity(mapProductPropertiesMap.get(productId).getQuantity() + productPropertiesMap.getQuantity());
                    }
                } else {    // else add new
                    mapProductPropertiesMap.put(productId, productPropertiesMap);
                }
            }
        }
//        HashMap<Long, ProductPropertiesMap> mapProductPropertiesMaps = productPropertiesMapDao.getProductPropertiesMapByMinPriceIfAvailable(productIds);
        ProductBeans productBeans = this.setProductBeans(products, mapProductPropertiesMap,25L,users);
        productBeans.setSearchString(requistBean.getName());
        return productBeans;
    }
    
    @Override
    public LikeDto getLikeUnlike(Long productId, Long userId) {/*http://duqhan.com/#/store/product/55918/overview*/
    	LikeDto likeDto = new LikeDto();
    	if (userId != null) {
    		likeDto.setLikeUnlikeDetails(likeUnlikeProductDao.getProductLikeUnlike(productId, userId));	
        }
    	return likeDto;
    }
    @Override
    public ProductDetailBean saveRecentRecord(Long productId, Long userId) {/*http://duqhan.com/#/store/product/55918/overview*/
        ProductDetailBean productDetailBean = new ProductDetailBean();
        Product product = productDao.loadById(productId);
        if (product != null) {
        	if (userId != null) {	
                RecentView recentView = recentViewDao.getRecentViewProductByUserIdProductId(userId, productId);
                if (null != recentView) {
                    long count = recentView.getVisitCount();
                    recentView.setVisitCount(++count);
                } else {
                    recentView = new RecentView();
                    recentView.setId(null);
                    recentView.setVisitCount(1L);
                }
                recentView.setProductId(product.getId());
                recentView.setUserId(userId);
                recentView.setViewDate(new Date());
                recentViewDao.save(recentView);
            }
            //===================Save in recent view table end==================//
            RecordedActions actions = new RecordedActions();
            actions.setDate(new Date());
            actions.setProductId(productId);
            actions.setUserId(userId);
            actions.setAction("overview");
            recordedActionsDao.save(actions);
        } else {
            productDetailBean.setStatus("No Product Found");
        }
        return productDetailBean;
    }
    @Override
    public ProductDetailBean getProductReviewsById(Long productId, Long userId) {/*http://duqhan.com/#/store/product/55918/overview*/
        ProductDetailBean productDetailBean = new ProductDetailBean();
        Product product = productDao.loadById(productId);
        if (product != null) {
        	List<Review> reviews = new ArrayList<>();
            reviews = reviewDao.getAllByproductId(productId);
            
           productDetailBean.setReviews(reviews);
           /*List<Object[]> ratingCount=new ArrayList<Object[]>();
           ratingCount=reviewDao.getAllRatingCount(productId);
           if(Objects.nonNull(ratingCount)){
           	Map<String,BigInteger> ratingMap = new HashMap<String,BigInteger>();
           	for (Object[] objects : ratingCount) {
           		ratingMap.put("ret1", (BigInteger)objects[0]);
           		ratingMap.put("ret2", (BigInteger)objects[1]);
           		ratingMap.put("ret3", (BigInteger)objects[2]);
           		ratingMap.put("ret4", (BigInteger)objects[3]);
           		ratingMap.put("ret5", (BigInteger)objects[4]);
           		ratingMap.put("ret", (BigInteger)objects[5]);
           		Long divident =((BigInteger)objects[5]).longValue();
           		Long count = new Long(0);
           		if(divident !=0){
           		count=(((BigInteger)objects[0]).longValue() * 1 + ((BigInteger)objects[1]).longValue()*2 + ((BigInteger)objects[2]).longValue() * 3 + ((BigInteger)objects[3]).longValue() * 4+((BigInteger)objects[4]).longValue() * 5) / ((BigInteger)objects[5]).longValue();
           		}
           		ratingMap.put("retAvg",new BigInteger(count.toString()));
				}
           	productDetailBean.setRatingCount(ratingMap);
           }*/
        }else{
        	productDetailBean.setStatus("No Product Found");
        }
        return productDetailBean;
    }

    @Override
    public ProductDetailBean getProductDetailsById(Long productId, Long userId) {/*http://duqhan.com/#/store/product/55918/overview*/
        ProductDetailBean productDetailBean = new ProductDetailBean();
        Product product = productDao.loadById(productId);
        if (product != null) {
            List<Long> ids;
           // Category category = categoryDao.loadById(product.getCategoryId());
            List<ProductImg> imgs = productImgDao.getProductImgsByProductId(productId);
            double orginalPrice = 0.0;
            double salesPrice = 0.0;
            int c = 0;
            //==================Load productPropertiesMaps start====================//
            List<ProductPropertiesMapDto> productPropertiesMapDtos = new ArrayList();
            List<ProductPropertiesMap> productPropertiesMaps = product.getProductPropertiesMaps()/*productPropertiesMapDao.getProductPropertiesMapsByProductId(productId)*/;
            for (ProductPropertiesMap productPropertiesMap : productPropertiesMaps) {
                ProductPropertiesMapDto propertiesMapDto = new ProductPropertiesMapDto();
                propertiesMapDto.setMapId(productPropertiesMap.getId());
                propertiesMapDto.setProductId(productPropertiesMap.getProductId().getId());
                propertiesMapDto.setPropertyvalueComposition(productPropertiesMap.getPropertyvalueComposition());
                propertiesMapDto.setOrginalPrice(productPropertiesMap.getPrice());
                propertiesMapDto.setSalesPrice(productPropertiesMap.getDiscount());
                propertiesMapDto.setDiscount(this.getPercentage(productPropertiesMap.getPrice(), productPropertiesMap.getDiscount()));
                propertiesMapDto.setQuantity(productPropertiesMap.getQuantity());
                productPropertiesMapDtos.add(propertiesMapDto);
                if (c == 0) {
                    orginalPrice = productPropertiesMap.getPrice();
                    salesPrice = productPropertiesMap.getDiscount();
                    c++;
                } else {
                    if (salesPrice > productPropertiesMap.getDiscount()) {
                        orginalPrice = productPropertiesMap.getPrice();
                        salesPrice = productPropertiesMap.getDiscount();
                    }
                }
            }
            //==================Load productPropertiesMaps end====================//

            //==================Load Propertieses start====================//
            List<PropertyDto> properties = new ArrayList<>();
            if (product.getProperties() != null) {
                ids = new ArrayList<>();
                Long id;
                String[] propertyIds = new String[0];
                propertyIds = product.getProperties().split("-");
                System.out.println("propertyIds ===== " + propertyIds);
                if (propertyIds.length > 0) {
                    //------------------Load Property value start----------------//
                    HashMap<Long, List<PropertyValueDto>> hashMap = new HashMap<>();
                    List<ProductPropertyvalues> propertyvalueses = productPropertyvaluesDao.loadByProductId(product.getId());
                    for (ProductPropertyvalues propertyvaluese : propertyvalueses) {
                        PropertyValueDto propertyValueDto = new PropertyValueDto();
                        propertyValueDto.setId(propertyvaluese.getId());
                        propertyValueDto.setValue(propertyvaluese.getValueName());
                        if (!hashMap.containsKey(propertyvaluese.getPropertyId())) {
                            List<PropertyValueDto> list = new ArrayList<>();
                            list.add(propertyValueDto);
                            hashMap.put(propertyvaluese.getPropertyId(), list);
                        } else {
                            hashMap.get(propertyvaluese.getPropertyId()).add(propertyValueDto);
                        }
                    }
                    //------------------Load Property value end------------------//
                    for (String propertyId : propertyIds) {
                        try {
                            id = Long.valueOf(propertyId);
                        } catch (NumberFormatException e) {
                            id = 0l;
                            ids.clear();
                            break;
                        }
                        ids.add(id);
                    }
                    List<ProductProperties> productPropertieses = productPropertiesDao.loadByIds(ids);
                    for (ProductProperties productProperties : productPropertieses) {
                        PropertyDto propertyDto = new PropertyDto();
                        propertyDto.setId(productProperties.getId());
                        propertyDto.setProperty(productProperties.getPropertyName());
                        propertyDto.setPropertyValues(hashMap.get(productProperties.getId()));
                        properties.add(propertyDto);
                    }
                }
            }
            //==================Load Propertieses end====================//
            productDetailBean.setProductId(product.getId());
            productDetailBean.setName(product.getName());
            productDetailBean.setDescription(product.getDescription());
           /* productDetailBean.setCategoryId(category.getId());
            productDetailBean.setCategoryName(category.getName());*/
            productDetailBean.setProductImg(product.getImgurl());
            productDetailBean.setVendorId(product.getVendorId());
            productDetailBean.setShippingCost(product.getShippingRate());
            productDetailBean.setShippingTime(product.getShippingTime());
            productDetailBean.setPropertiesMapDto(productPropertiesMapDtos);
            productDetailBean.setProperties(properties);
            //======================add imgDto start=========================//
            List<ImageDto> imgDtos = new ArrayList<>();
            ImageDto imgDto;
            imgDto = new ImageDto();
            imgDto.setImgUrl(product.getImgurl());
            imgDtos.add(imgDto);    //including front image
            for (ProductImg productImg : imgs) {
                imgDto = new ImageDto();
                imgDto.setImgUrl(productImg.getImgUrl());
                imgDtos.add(imgDto);
            }
            productDetailBean.setImages(imgDtos);
            //======================add imgDto end=========================//
            //====================load specification start=================//
            String specifications = product.getSpecifications();
            if (specifications != null && !specifications.equals("")) {
                String[] fiturs = specifications.split(",");
                HashMap<String, String> map = new HashMap<String, String>();
                for (String fitur : fiturs) {
                    map.put(fitur.split(":")[0], fitur.split(":")[1]);
                }
                productDetailBean.setSpecifications(map);

            } else {
                productDetailBean.setSpecifications(new HashMap<String, String>());
            }
            //====================load specification end=================//
            orginalPrice += StatusConstants.PRICE_GREASE;
            productDetailBean.setOrginalPrice(orginalPrice);
            productDetailBean.setSalesPrice(salesPrice);
            productDetailBean.setDiscount(this.getPercentage(orginalPrice, salesPrice));
            productDetailBean.setLikeUnlikeCount(product.getLikeUnlikeCount());
//            productDetailBean.setArrival("Not set yet..");
//            productDetailBean.setShippingCost(null);
//            productDetailBean.setRelatedProducts(new ProductBeans());
            //===================Save in recent view table start==================//
        } else {
            productDetailBean.setStatus("No Product Found");
        }
        return productDetailBean;
    }

    @Override
    public String addProductToCart(ProductRequistBean requistBean) {
        String status = "failure";
//        boolean flag = true;
//        ProductSizeColorMap productSizeColorMap = productSizeColorMapDao.loadById(requistBean.getMapId());
//        if (productSizeColorMap != null) {
//            if (productSizeColorMap.getQuentity() != 0L) {
//                Product product = productDao.loadById(productSizeColorMap.getProductId());
//                try {
//                    Document doc = Jsoup.connect(product.getExternalLink()).get();
//                    Elements detailMain = doc.select("#j-detail-page");
//                    if (detailMain.isEmpty()) {
//                        flag = false;
//                    }
//                } catch (IOException ex) {
//                    flag = false;
//                }
//                if (!flag) {
//                    productSizeColorMap.setQuentity(0L);
//                    productSizeColorMapDao.save(productSizeColorMap);
//                }
//            } else {
//                flag = false;
//            }
//        } else {
//            flag = false;
//        }
//        if (flag) {
        Cart cart2 = cartDao.loadByUserIdAndMapId(requistBean.getUserId(), requistBean.getMapId());
        if (cart2 != null) {
            status = "Product already added";
        } else {
            Cart cart = new Cart();
            cart.setId(null);
            cart.setLoadDate(new Date());
            cart.setProductPropertyMapId(requistBean.getMapId());
            cart.setUserId(requistBean.getUserId());
            cart.setDiscountOfferPct(requistBean.getDiscountOfferPct());
            Cart cart1 = cartDao.save(cart);    // add product to cart.
            if (cart1 != null) {
                status = "success";
                RecordedActions actions = new RecordedActions();
                actions.setDate(new Date());
                actions.setProductId(requistBean.getProductId());
                actions.setUserId(requistBean.getUserId());
                actions.setAction("addToCart");
                recordedActionsDao.save(actions);
            }
        }
        //} else {
        //    status = "Product not exsist.";
        //}
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
    public CartBean getCartForUser(Long userId) {
        CartBean cartBean = new CartBean();
        HashMap<Long, Cart> MapCart = new HashMap<>();
        List<ProductPropertiesMap> propertiesMaps = cartDao.getProductPropertiesMapByUserId(userId);   // Load user cart
        if (!propertiesMaps.isEmpty()) {
            List<Cart> carts = cartDao.getCartByUserId(userId);
            List<Long> productIds = new ArrayList<>();
            for (Cart cart : carts) {
                MapCart.put(cart.getProductPropertyMapId(), cart);
                Long productId = productPropertiesMapDao.loadById(cart.getProductPropertyMapId()).getProductId().getId();
                productIds.add(productId);
            }
            List<Product> products = productDao.loadByIds(productIds);
            HashMap<Long, Product> MapProduct = new HashMap<>();
            for (Product product : products) {
                MapProduct.put(product.getId(), product);
            }

            //============================================================//
            List<ProductBean> productBeans = new ArrayList<>();
            double itemTotal = 0.0;         //2000
            double orderTotal = 0.0;        //1500
            double discountTotal = 0.0;     //500
            double discountPctTotal = 0.0;  //25
            //=====================ready product bean====================//
            for (ProductPropertiesMap propertiesMap : propertiesMaps) {
                HashMap<String, String> propertyMap = new HashMap<>();
                boolean flag = true;
                Product product = MapProduct.get(propertiesMap.getProductId().getId());
                ProductBean productBean = new ProductBean();
                productBean.setQty(propertiesMap.getQuantity() == 0L ? "0" : "1");
                productBean.setProductPropertiesMapId(propertiesMap.getId());
                productBean.setProductId(propertiesMap.getProductId().getId());
                productBean.setName(product.getName());
                productBean.setDescription(product.getDescription());
                String[] propertyValueIds = new String[0];
                propertyValueIds = propertiesMap.getPropertyvalueComposition().split("_");
                if (propertyValueIds.length > 0) {
                    for (String propertyValueId : propertyValueIds) {
                        try {
                            ProductPropertyvalues productPropertyvalues = productPropertyvaluesDao.loadById(Long.valueOf(propertyValueId));
                            ProductProperties properties = productPropertiesDao.loadById(productPropertyvalues.getPropertyId());
                            propertyMap.put(properties.getPropertyName(), productPropertyvalues.getValueName());
                        } catch (Exception nfe) {
                        }
                    }
                }
                productBean.setPropertyMap(propertyMap);
//                productBean.setImgurl(MapProductImg.get(propertiesMap.getProductImgId()).getImgUrl());
                productBean.setImgurl(product.getImgurl());
                productBean.setPrice(propertiesMap.getPrice() + StatusConstants.PRICE_GREASE);
                itemTotal = itemTotal + propertiesMap.getPrice() + StatusConstants.PRICE_GREASE;
                double reDiscountPct = MapCart.get(propertiesMap.getId()).getDiscountOfferPct();
                if (reDiscountPct > 0) {
                    double reDiscount = propertiesMap.getDiscount();
                    reDiscount = reDiscount - ((reDiscount * reDiscountPct) / 100);
                    productBean.setDiscountedPrice(reDiscount);
                    orderTotal = orderTotal + reDiscount;
                    productBean.setDiscountPCT(this.getPercentage(propertiesMap.getPrice() + StatusConstants.PRICE_GREASE, reDiscount));
                } else {
                    productBean.setDiscountedPrice(propertiesMap.getDiscount());
                    orderTotal = orderTotal + propertiesMap.getDiscount();
                    productBean.setDiscountPCT(this.getPercentage(propertiesMap.getPrice() + StatusConstants.PRICE_GREASE, propertiesMap.getDiscount()));
                }
                productBean.setAvailable(Long.valueOf(propertiesMap.getQuantity()).intValue());
                productBean.setCartId(MapCart.get(propertiesMap.getId()).getId());
                if (StatusConstants.IS_SHIPMENT) {
                    productBean.setShippingRate(product.getShippingRate());
                    productBean.setShippingTime(product.getShippingTime());
                } else {
                    productBean.setShippingRate(0.0);
                    productBean.setShippingTime("21");
                }
                productBean.setVendorId(product.getVendorId());
                productBean.setProductHeight(product.getProductHeight());
                productBean.setProductLength(product.getProductLength());
                productBean.setProductWeight(product.getProductWeight());
                productBean.setProductWidth(product.getProductWidth());
                //<editor-fold defaultstate="collapsed" desc="Check product availability in aliexpress.com">
//                if (Long.valueOf(propertiesMap.getQuentity()).intValue() != 0) {
//                    try {
//                        Document doc = Jsoup.connect(product.getExternalLink()).get();
//                        Elements detailMain = doc.select("#j-detail-page");
//                        if (detailMain.isEmpty()) {
//                            flag = false;
//                        }
//                    } catch (IOException ex) {
//                        flag = false;
//                    }
//                    if (!flag) {
//                        propertiesMap.setQuentity(0L);
//                        productBean.setAvailable(0);
//                        productSizeColorMapDao.save(propertiesMap);
//                    }
//                }
//</editor-fold>
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

    /*@Override
    public String saveProduct(ProductBean productBean) {
        String status = "ERROR: Product can not be saved!!";
        if (productBean != null) {
            Category parentCategory = categoryDao.loadById(productBean.getCategoryId());//>>>>>>>>>>>>>>
            Product product = new Product();
            product.setId(null);
            product.setName(productBean.getName());//>>>>>>>>>>>>>>>>>
            product.setImgurl(productBean.getImgurl());//>>>>>>>>>>>>>>>>
            product.setCategoryId(productBean.getCategoryId());//>>>>>>>>>>>
            product.setDescription(productBean.getDescription());//>>>>>>>>>>>>>>>>
            product.setLastUpdate(new Date());
            product.setVendorId(productBean.getVendorId());     //>>>>>>>>>>>>>>>>>
            product.setParentPath(parentCategory.getParentPath());
            product.setExternalLink(productBean.getExternalLink()); //>>>>>>>>>>>>>>
            product.setSpecifications(productBean.getSpecifications());//>>>>>>>>>>>>
            if (StatusConstants.IS_SHIPMENT) {
                Shipment shipment = shippingService.createDefaultShipmentDomestic();
                if (shipment != null && shipment.getRates() != null && !shipment.getRates().isEmpty()) {
                    product.setShippingTime(shipment.getRates().get(0).getDeliveryDays() != null ? shipment.getRates().get(0).getDeliveryDays().toString() : null);
                    product.setShippingRate(shipment.getRates().get(0).getRate() != null ? shipment.getRates().get(0).getRate().doubleValue() : null);
                } else {
                    product.setShippingTime(productBean.getShippingTime());//>>>>>>>>>>>>>>
                    product.setShippingRate(productBean.getShippingRate());//>>>>>>>>>>>>>>
                }
            } else {
                product.setShippingTime(productBean.getShippingTime());
                product.setShippingRate(productBean.getShippingRate());
            }
            Product product1 = productDao.save(product);
            if (product1 != null) {
                //====multiple ProductSizeColorMap added=======//
                List<SizeColorMapDto> sizeColorMapDtos = productBean.getSizeColorMaps();//>>>>>>>>>>>>>>
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
                List<ImageDto> imageDtos = productBean.getImageDtos();//>>>>>>>>>>>>
                if (!imageDtos.isEmpty()) {
                    for (ImageDto imageDto : imageDtos) {
                        ProductImg productImg = new ProductImg();
                        productImg.setId(null);
                        productImg.setImgUrl(imageDto.getImgUrl());
                        productImg.setProductId(product1.getId());
                        productImgDao.save(productImg);
                    }
                }
                status = "Product saved.";
            }
        }
        return status;
    }

    @Override
    public String saveCategory(CategoryDto categoryDto) {
        String status = "ERROR: Category can not be saved!!";
        Category parentCategory = categoryDao.loadById(categoryDto.getPatentId());
        if (categoryDto.getCategoryName() != null && null != parentCategory) {
            Category category = new Category();
            category.setId(null);
            category.setName(categoryDto.getCategoryName());
            category.setParentId(categoryDto.getPatentId());
            category.setParentPath(parentCategory.getParentPath() + categoryDto.getPatentId() + "=");
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
    public String saveSpecification(SpecificationDto specificationDto) {
        Specification specification = new Specification();
        specification.setId(null);
        specification.setCategoryId(specificationDto.getId());
        specification.setFeatures(specificationDto.getName());
        specification.setFeaturesValue(specificationDto.getValue() + ",");
        Specification specification2 = specificationDao.save(specification);   //save new specification
        if (specification2 == null) {
            return "ERROR: Specification can not be saved!!";
        } else {
            return "Color saved..";
        }
    }

    @Override
    public String saveSpecificationValue(SpecificationDto specificationDto) {
        Specification specification = specificationDao.loadById(specificationDto.getId());
        String featureValue = specification.getFeaturesValue();
        if (featureValue != null) {
            featureValue = featureValue + specificationDto.getValue() + ",";
        } else {
            featureValue = specificationDto.getValue() + ",";
        }
        specification.setFeaturesValue(featureValue);
        Specification specification2 = specificationDao.save(specification);   //save new specification value
        if (specification2 == null) {
            return "ERROR: Specification value can not be saved!!";
        } else {
            return "Color saved..";
        }
    }
     */
    @Override
    public Long getCartCountFoAUser(Long userId) {
        return cartDao.getCartCountByUserId(userId);    //number of item in cart fo a user.
    }

    /*
    @Override
    public String saveProductImage(ProductBean productBean) {
        return FileUploader.uploadImage(productBean.getFrontImage()).getUrl();
    }
     */
    @Override
    public CategorysBean getChildById(Long parentId) {
        CategorysBean categorysBean = new CategorysBean();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        List<Category> categorys = categoryDao.getChildByParentId(parentId);
        categorysBean.setCategoryName(categoryDao.loadById(parentId).getName());
        if (categorys.isEmpty()) {
        	categorysBean.setStatus("No child category found");
            categorysBean.setStatusCode("403");
        	List<Category> categorysParent=categoryDao.getById(parentId);
        	String parentPath=categorysParent.get(0).getParentPath();
        	String[] categoryList = parentPath.split("=");
        	List<Long> categoryId = new ArrayList<>();
        	for(int i=1;i<categoryList.length;i++){
        	categoryId.add(new Long(categoryList[i]));
        	}
        	categoryId.add(parentId);
         categorysBean.setBreadcrums(categoryDao.getCategoryNameAndIds(categoryId));
        } else {
        	
        	String parentPath = categorys.get(0).getParentPath();
        	String[] categoryList = parentPath.split("=");
        	List<Long> categoryId = new ArrayList<>();
        	for(int i=1;i<categoryList.length;i++){
        		categoryId.add(new Long(categoryList[i]));
        	}
        	
        	categorysBean.setBreadcrums(categoryDao.getCategoryNameAndIds(categoryId));
        	
            for (Category category : categorys) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setIsLeaf(false);
                categoryDto.setCategoryId(category.getId());
                categoryDto.setCategoryName(category.getName());
                categoryDto.setImgUrl(category.getImgUrl());
                categoryDto.setDisplayText(category.getDisplayText());
                categoryDto.setMenuIcon(category.getMenuIcon());
                List<Category> categorys2 = categoryDao.getChildByParentId(category.getId());
                if (categorys2.isEmpty()) {
                    categoryDto.setIsLeaf(true);
                }
//                if (productDao.isAnyProductInCategoryId(category.getId())) {
//                categoryDtos.add(categoryDto);
//                }
                /*if (!(category.getImgUrl().equals("-"))) {
                    categoryDtos.add(categoryDto);
                }*/
                categoryDtos.add(categoryDto);
            }

            categorysBean.setCategoryDtos(categoryDtos);
            categorysBean.setChildCount(categoryDtos.size());
            
            /*List<Product> products = productDao.getProductsByCategory(parentId);
            categorysBean.setProductCount(0);
            if (!products.isEmpty()) {
                categorysBean.setProductCount(products.size());
            }*/
            categorysBean.setStatus("success");
            categorysBean.setStatusCode("200");
        }
        return categorysBean;
    }
    
    @Override
    public CategorysBean getChildByIdAndActive(Long parentId) {
        CategorysBean categorysBean = new CategorysBean();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        List<Category> categorys = categoryDao.getChildByParentIdAndActive(parentId);
        if (categorys.isEmpty()) {
            categorysBean.setStatus("No child category found");
            categorysBean.setStatusCode("403");
        } else {
            for (Category category : categorys) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setIsLeaf(false);
                categoryDto.setCategoryId(category.getId());
                categoryDto.setCategoryName(category.getName());
                categoryDto.setImgUrl(category.getImgUrl());
                categoryDto.setDisplayText(category.getDisplayText());
                categoryDto.setMenuIcon(category.getMenuIcon());
                List<Category> categorys2 = categoryDao.getChildByParentId(category.getId());
                if (categorys2.isEmpty()) {
                    categoryDto.setIsLeaf(true);
                }
//                if (productDao.isAnyProductInCategoryId(category.getId())) {
//                categoryDtos.add(categoryDto);
//                }
                /*if (!(category.getImgUrl().equals("-"))) {
                    categoryDtos.add(categoryDto);
                }*/
                categoryDtos.add(categoryDto);
            }

            categorysBean.setCategoryDtos(categoryDtos);
            categorysBean.setChildCount(categoryDtos.size());
            /*List<Product> products = productDao.getProductsByCategory(parentId);
            categorysBean.setProductCount(0);
            if (!products.isEmpty()) {
                categorysBean.setProductCount(products.size());
            }*/
            categorysBean.setStatus("success");
            categorysBean.setStatusCode("200");
        }
        return categorysBean;
    }

    @Override
    public OrderDetailsBean getOrderDetails(Long userId, int start, int limit) {
        OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
        List<OrderDetailsDto> orderdetailsDtos = new ArrayList<>();
        List<Object[]> objects = orderDetailsDao.getDetailByUserId(userId, start, limit);
        if (!objects.isEmpty()) {
            Object[] od = objects.get(0);
            OrderDetails details = (OrderDetails) od[0];
            PaymentDetail paymentDetail = paymentDetailDao.getDetailByPayKey(details.getPaymentKey());
            List<UserAddress> userAddresses = userAddressDao.getAddressByUserId(userId);
            HashMap<Long, UserAddress> mapUserAddress = new HashMap<>();
            for (UserAddress userAddress : userAddresses) {
                mapUserAddress.put(userAddress.getId(), userAddress);
            }
            List<Long> productIds = new ArrayList();
            for (Object[] object : objects) {
                ProductPropertiesMap propertyMap = (ProductPropertiesMap) object[1];
                productIds.add(propertyMap.getProductId().getId());
            }
            List<Product> products = productDao.loadByIds(productIds);
            HashMap<Long, Product> mapProduct = new HashMap<>();
            for (Product product : products) {
                mapProduct.put(product.getId(), product);
            }
            for (Object[] object : objects) {
                OrderDetails orderDetailse = (OrderDetails) object[0];//********************
                ProductPropertiesMap propertyMap = (ProductPropertiesMap) object[1];//***********
                OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
                orderDetailsDto.setAddressDto(this.setAddressDto(mapUserAddress.get(orderDetailse.getAddressId())));
                String[] propertyValueIds = new String[0];
                propertyValueIds = propertyMap.getPropertyvalueComposition().split("_");
                HashMap<String, String> propertiesMap = new HashMap<>();
                if (propertyValueIds.length > 0) {
                    for (String propertyValueId : propertyValueIds) {
                        try {
                            ProductPropertyvalues productPropertyvalues = productPropertyvaluesDao.loadById(Long.valueOf(propertyValueId));
                            ProductProperties properties = productPropertiesDao.loadById(productPropertyvalues.getPropertyId());
                            propertiesMap.put(properties.getPropertyName(), productPropertyvalues.getValueName());
                        } catch (Exception nfe) {
                        }
                    }
                }
                orderDetailsDto.setPropertyMap(propertiesMap);
                orderDetailsDto.setOrderId(orderDetailse.getOrderId());
                orderDetailsDto.setDeliveryDate(DateFormater.formate(orderDetailse.getDeliveryDate()));
                orderDetailsDto.setEmail(mapUserAddress.get(orderDetailse.getAddressId()).getEmail());
                orderDetailsDto.setMapId(orderDetailse.getMapId());
                orderDetailsDto.setOrderDate(DateFormater.formate(orderDetailse.getOrderDate()));
                orderDetailsDto.setOrderDateTime(orderDetailse.getOrderDate());
                orderDetailsDto.setPaymentAmount(orderDetailse.getPaymentAmount());
                orderDetailsDto.setPaymentKey(orderDetailse.getPaymentKey());
                orderDetailsDto.setPhone(mapUserAddress.get(orderDetailse.getAddressId()).getPhone());
                orderDetailsDto.setProductName(mapProduct.get(propertyMap.getProductId().getId()).getName());
                if (orderDetailse.getReturnStatus() != null && orderDetailse.getReturnStatus().equals(StatusConstants.REQUEST_FOR_RETURN)) {
                    orderDetailsDto.setStatus("returned");
                } else {
                    orderDetailsDto.setStatus(orderDetailse.getStatus());
                }
                orderDetailsDto.setProdImg(mapProduct.get(propertyMap.getProductId().getId()).getImgurl());
                orderDetailsDto.setPrice(propertyMap.getPrice() + StatusConstants.PRICE_GREASE);
                orderDetailsDto.setDiscount(this.getPercentage(propertyMap.getPrice() + StatusConstants.PRICE_GREASE, orderDetailse.getPaymentAmount()));
                orderDetailsDto.setQuty(orderDetailse.getQuentity());
                orderDetailsDto.setReturnStatus(orderDetailse.getReturnStatus());
                ShipmentTable shipmentTable = orderDetailse.getShipmentId() != null ? shippingService.getShipmentTableByShipmentId(orderDetailse.getShipmentId()) : null;
                orderDetailsDto.setTrackerBean(shippingService.getTrackerByTrackerId(shipmentTable, orderDetailse.getStatus(), paymentDetail));
                orderdetailsDtos.add(orderDetailsDto);
            }
//            orderDetailsBean.setOrderDetailsDtos(orderdetailsDtos);
//            orderDetailsBean.setStatus("success");
//            orderDetailsBean.setStatusCode("200");
//            return orderDetailsBean;
        }
        if (orderdetailsDtos.isEmpty()) {
            orderDetailsBean.setStatus("No order found.");
        } else {
            orderDetailsBean.setStatus("success");
        }
        orderDetailsBean.setOrderDetailsDtos(orderdetailsDtos);
        orderDetailsBean.setStatusCode("200");
        return orderDetailsBean;

    }

    @Override
    public void cancelOrder(String orderId, Long userId) {
        OrderDetails orderDetails = orderDetailsDao.getOrderDetailsByUserIdAndOrderId(userId, orderId);       
        if (orderDetails != null) {
            //orderDetails.setReturnStatus(StatusConstants.REQUEST_FOR_RETURN);
        	orderDetails.setStatus("order cancel");
            orderDetailsDao.save(orderDetails);
            //mailService.returnRequestToAdmin(orderDetails);
        }
    }

    @Override
    public void returnOrder(OrderReturnDto orderReturnDto,MultipartFile file) {
    	RequestReturn obj = new RequestReturn();
    	obj.setOrderId(orderReturnDto.getOrderId());
    	obj.setUserId(orderReturnDto.getUserId());
    	obj.setReturnText(orderReturnDto.getReturnText());
    	obj.setRequestDate(new Date());
    	obj.setImageUrl(GoogleBucketFileUploader.uploadReturnImage(file, orderReturnDto.getUserId()));
        //System.out.println("image uploaded.......................................");
    	requestReturnDao.save(obj);
    	//System.out.println(orderReturnDto.getOrderId()+"  "+orderReturnDto.getReturnText());
    }
    /*
    @Override
    public String updateProduct(ProductBean productBean) {
        String status = "ERROR: Product can not be update!!";
        Product product = productDao.loadById(productBean.getProductId());
        if (product != null) {
            Category parentCategory = categoryDao.loadById(productBean.getCategoryId());
            product.setName(productBean.getName());
            product.setImgurl(productBean.getImgurl());
            product.setCategoryId(productBean.getCategoryId());
            product.setDescription(productBean.getDescription());
            product.setLastUpdate(new Date());
            product.setVendorId(productBean.getVendorId());     //>>>>>>>>
            product.setParentPath(parentCategory.getParentPath());
            product.setExternalLink(productBean.getExternalLink());
            product.setSpecifications(productBean.getSpecifications());

            product.setShippingTime(productBean.getShippingTime());
            product.setShippingRate(productBean.getShippingRate());

            Product product1 = productDao.save(product);
//            if (product1 != null) {
//                //===========multiple image add==========//
//                List<ImageDto> imageDtos = productBean.getImageDtos();
//                if (!imageDtos.isEmpty()) {
//                    for (ImageDto imageDto : imageDtos) {
//                        ProductImg productImg = new ProductImg();
//                        productImg.setId(null);
//                        productImg.setImgUrl(imageDto.getImgUrl());
//                        productImg.setProductId(product1.getId());
//                        productImgDao.save(productImg);
//                    }
//                    status = "Product saved.";
//                }
//            }
        }
        return status;
    }

    @Override
    public ProductBean getProductInventoryById(Long productId) {
        List<SizeColorMapDto> sizeColorMapDtos = new ArrayList<>();
        ProductBean productBean = new ProductBean();
        productBean.setProductId(productId);
        List<ProductSizeColorMap> sizeColorMaps = productSizeColorMapDao.getSizeColorMapByProductId(productId);
//        List<Sizee> sizees = sizeeDao.loadAll();
//        HashMap<Long, Sizee> mapSize = new HashMap<>();
//        for (Sizee sizee : sizees) {
//            mapSize.put(sizee.getId(), sizee);
//        }
//        List<Color> colors = colorDao.loadAll();
//        HashMap<Long, Color> mapColor = new HashMap<>();
//        for (Color color : colors) {
//            mapColor.put(color.getId(), color);
//        }
        List<Long> ids;
        //==================Load color start====================//
        ids = new ArrayList<>();
        for (ProductSizeColorMap sizeColorMap : sizeColorMaps) {
            if (sizeColorMap.getColorId() != null) {
                ids.add(sizeColorMap.getColorId());
            }
        }
        List<Color> colors = colorDao.loadByIds(ids);
        HashMap<Long, Color> mapColor = new HashMap<>();
        for (Color color : colors) {
            mapColor.put(color.getId(), color);
        }
        //==================Load Color end====================//

        //==================Load Size start====================//
        ids = new ArrayList<>();
        for (ProductSizeColorMap sizeColorMap : sizeColorMaps) {
            if (sizeColorMap.getSizeId() != null) {
                ids.add(sizeColorMap.getSizeId());
            }
        }
        List<Sizee> sizees = sizeeDao.loadByIds(ids);
        HashMap<Long, Sizee> mapSize = new HashMap<>();
        for (Sizee sizee : sizees) {
            mapSize.put(sizee.getId(), sizee);
        }
        //==================Load Size end====================//

        for (ProductSizeColorMap sizeColorMap : sizeColorMaps) {
            SizeColorMapDto sizeColorMapDto = new SizeColorMapDto();
            if (mapSize.get(sizeColorMap.getSizeId()) != null) {
                sizeColorMapDto.setSizeId(sizeColorMap.getSizeId());
                sizeColorMapDto.setSizeText(mapSize.get(sizeColorMap.getSizeId()).getValu());
            }
            if (mapColor.get(sizeColorMap.getColorId()) != null) {
                sizeColorMapDto.setColorId(sizeColorMap.getColorId());
                sizeColorMapDto.setColorText(mapColor.get(sizeColorMap.getColorId()).getName());
            }
            sizeColorMapDto.setMapId(sizeColorMap.getId());
            sizeColorMapDto.setOrginalPrice(sizeColorMap.getPrice());
            sizeColorMapDto.setOrginalPrice(sizeColorMap.getPrice());
            sizeColorMapDto.setDiscount(sizeColorMap.getDiscount());
            sizeColorMapDto.setSalesPrice(sizeColorMap.getDiscount());
            sizeColorMapDto.setDiscount(this.getPercentage(sizeColorMap.getPrice(), sizeColorMap.getDiscount()));
            sizeColorMapDto.setCount(sizeColorMap.getQuentity());
            sizeColorMapDto.setProductLength(sizeColorMap.getProductLength());
            sizeColorMapDto.setProductHeight(sizeColorMap.getProductHeight());
            sizeColorMapDto.setProductWidth(sizeColorMap.getProductWidth());
            sizeColorMapDto.setProductWeight(sizeColorMap.getProductWeight());
            sizeColorMapDtos.add(sizeColorMapDto);
        }
        productBean.setSizeColorMaps(sizeColorMapDtos);
        List<ImageDto> imageDtos = new ArrayList<>();
        List<ProductImg> productImgs = productImgDao.getProductImgsByProductId(productId);
        for (ProductImg productImg : productImgs) {
            ImageDto imageDto = new ImageDto();
            imageDto.setId(productImg.getId());
            imageDto.setImgUrl(productImg.getImgUrl());
            imageDtos.add(imageDto);
        }
        productBean.setImageDtos(imageDtos);
        return productBean;
    }

    @Override
    public String updateProductInventory(ProductBean productBean) {
        String status = "ERROR: Product Inventory can not be update!!";
        Product product = productDao.loadById(productBean.getProductId());
        if (product != null) {
            //====multiple ProductSizeColorMap added=======//
            List<SizeColorMapDto> sizeColorMapDtos = productBean.getSizeColorMaps();
            if (!sizeColorMapDtos.isEmpty()) {
                for (SizeColorMapDto sizeColorMapDto : sizeColorMapDtos) {
                    ProductSizeColorMap sizeColorMap;
                    if (sizeColorMapDto.getMapId() != null) {
                        sizeColorMap = productSizeColorMapDao.loadById(sizeColorMapDto.getMapId());
                    } else {
                        sizeColorMap = new ProductSizeColorMap();
                        sizeColorMap.setId(null);
                    }
                    sizeColorMap.setColorId(sizeColorMapDto.getColorId());
                    sizeColorMap.setSizeId(sizeColorMapDto.getSizeId());
                    sizeColorMap.setDiscount(sizeColorMapDto.getSalesPrice());
                    sizeColorMap.setPrice(sizeColorMapDto.getOrginalPrice());
                    sizeColorMap.setQuentity(sizeColorMapDto.getCount());
                    sizeColorMap.setProductId(product.getId());
                    sizeColorMap.setProductHeight(sizeColorMapDto.getProductHeight());
                    sizeColorMap.setProductLength(sizeColorMapDto.getProductLength());
                    sizeColorMap.setProductWeight(sizeColorMapDto.getProductWeight());
                    sizeColorMap.setProductWidth(sizeColorMapDto.getProductWidth());
                    ProductSizeColorMap sizeColorMap1 = productSizeColorMapDao.save(sizeColorMap);
                }
            }
            List<ImageDto> imageDtos = productBean.getImageDtos();
            if (imageDtos != null) {
                for (ImageDto imageDto : imageDtos) {
                    ProductImg productImg;
                    if (imageDto.getId() != null) {
                        productImg = productImgDao.loadById(imageDto.getId());
                    } else {
                        productImg = new ProductImg();
                        productImg.setId(null);
                    }
                    productImg.setProductId(product.getId());
                    productImg.setImgUrl(imageDto.getImgUrl());
                    productImgDao.save(productImg);
                }
            }
            status = "Product Inventory update.";
        }
        return status;
    }

    @Override
    public ProductDetailBean getProductSpecifications(Long categoryId) {
        ProductDetailBean detailBean = new ProductDetailBean();
        List<Specification> specifications = specificationDao.getSpecificationsByCategoryId(categoryId);
        HashMap<String, String> specificationMap = new HashMap();
        for (Specification specification : specifications) {
            specificationMap.put(specification.getId().toString(), specification.getFeatures());
        }
        detailBean.setSpecifications(specificationMap);
        return detailBean;
    }

    @Override
    public SpecificationDto getProductSpecificationValue(Long specificationId) {
        Specification specification = specificationDao.loadById(specificationId);
        SpecificationDto specificationDto = new SpecificationDto();
        String[] values = specification.getFeaturesValue().split(",");
        specificationDto.setId(specification.getId());
        specificationDto.setName(specification.getFeatures());
        specificationDto.setValues(Arrays.asList(values));
        return specificationDto;
    }

    @Override
    public ProductBeans getAllTempProductsIncloudeZeroAvailable(int start, int limit) {
        List<TempProduct> products = tempProductDao.getAllAvailableProduct(start, limit);   // Find all products 
        List<Long> productIds = new ArrayList<>();
        for (TempProduct product : products) {
            productIds.add(product.getId());
        }
        HashMap<Long, TempProductSizeColorMap> mapSizeColorMaps = tempProductSizeColorMapDao.getSizeColorMapByProductIds(productIds);
        return this.setTempProductBeans(products, mapSizeColorMaps);
    }
     */
    @Override
    public void test() {   // to category wise product quantity.
        List<Category> categorys = categoryDao.loadAll();
        HashMap<Long, Integer> categoryMap = new HashMap<>();
        for (Category category : categorys) {
            categoryMap.put(category.getId(), 0);
        }
        int start = 1;
        int limit = 5000;
        for (int i = 0; i < 20; i++) {
            start = i * limit;
            List<Product> products = productDao.loadAllByLimit(start, limit);
            if (products.isEmpty()) {
                break;
            }
            for (Product product : products) {
                int count = 0;
                String[] categoryArray = product.getParentPath().split("=");
                List<ProductPropertiesMap> productPropertiesMaps = product.getProductPropertiesMaps()/*productPropertiesMapDao.getPropertyMapByProductId(product.getId())*/;
                for (ProductPropertiesMap productPropertiesMap : productPropertiesMaps) {
                    double filter = StatusConstants.PRICE_FILTER;
                    if (product.getParentPath().contains("25")) {
                        filter = StatusConstants.PRICE_FILTER_BAG;
                    }
                    if (productPropertiesMap.getDiscount() < filter) {
                        count += productPropertiesMap.getQuantity();
                    }
                }
                int proCount = categoryMap.get(product.getCategoryId());
                categoryMap.put(product.getCategoryId(), proCount + count);
                for (String string : categoryArray) {
                    try {
                        proCount = categoryMap.get(Long.valueOf(string));
                        categoryMap.put(Long.valueOf(string), proCount + count);
                    } catch (NumberFormatException | NullPointerException e) {
                    }
                }
            }
        }
//        System.out.println("==============================\n" + products.size());
//        System.out.println("==============================\n" + categoryMap);
        for (Map.Entry<Long, Integer> entry : categoryMap.entrySet()) {
            Long key = entry.getKey();
            Integer value = entry.getValue();
            Category category = categoryDao.loadById(key);
            category.setQuantity(value);
            categoryDao.save(category);
        }
    }

    /*
    @Override
    public List<Map<String, Object>> getTestOrderDetails(List<String> orderids) {
        List<Map<String, Object>> detailList = new ArrayList<>();
        List<Object[]> allObjects = orderDetailsDao.getOrderDetailsByOrderIds(orderids);
        for (Object[] objectArray : allObjects) {
            OrderDetails orderDetails = (OrderDetails) objectArray[0];
            ProductSizeColorMap sizeColorMap = (ProductSizeColorMap) objectArray[1];
            Users user = (Users) objectArray[2];
            Product product = productDao.loadById(sizeColorMap.getProductId());
            Map<String, Object> map = new TreeMap<>();
            map.put("Date", DateFormater.formate(orderDetails.getOrderDate()));
            map.put("Product_id", product.getId());
            map.put("Product_name", product.getName());
            map.put("External_link", product.getExternalLink());
            map.put("Color_name", sizeColorMap.getColorId() != null ? colorDao.loadById(sizeColorMap.getColorId()).getName() : null);
            map.put("Quantity", orderDetails.getQuentity());
            map.put("User_name", user.getName());
            map.put("Email", user.getEmail());
            detailList.add(map);
        }
        return detailList;
    }
     */
    @Override
    public ProductBeans getFreeProducts() {
        List<Product> offerProducts = offerProductsDao.getAllOfferProduct();
        if (!offerProducts.isEmpty()) {
            HashMap<Long, ProductPropertiesMap> mapProductPropertiesMap = new HashMap<>();
            for (Product product : offerProducts) {
                List<ProductPropertiesMap> productPropertiesMaps = product.getProductPropertiesMaps();
                for (ProductPropertiesMap productPropertiesMap : productPropertiesMaps) {
                    Long productId = productPropertiesMap.getProductId().getId();
                    if (mapProductPropertiesMap.containsKey(productId)) {    // if map contains then update
                        if (mapProductPropertiesMap.get(productId).getDiscount() > productPropertiesMap.getDiscount()) {
                            mapProductPropertiesMap.get(productId).setDiscount(productPropertiesMap.getDiscount());
                            mapProductPropertiesMap.get(productId).setPrice(productPropertiesMap.getPrice());
                            mapProductPropertiesMap.get(productId).setQuantity(mapProductPropertiesMap.get(productId).getQuantity() + productPropertiesMap.getQuantity());
                        }
                    } else {    // else add new
                        mapProductPropertiesMap.put(productId, productPropertiesMap);
                    }
                }
            }
//            HashMap<Long, ProductPropertiesMap> mapPropertyMaps = productPropertiesMapDao.getProductPropertiesMapbyMinPriceIfAvailable(productIds);
            ProductBeans productBeans = this.setCacheProductBeans(offerProducts, mapProductPropertiesMap,25L);
            return productBeans;
        } else {
            return new ProductBeans();
        }
    }

    @Override
    public void acceptFreeProduct(Users users, CartBean cartBean) {
        ProductPropertiesMap propertyMap = productPropertiesMapDao.loadById(cartBean.getUserId());  //cartBean.getUserId() = product_size_color_map id
        if (!users.getFreeOfferAccepted()) {
            if (propertyMap != null) {
                users.setFreeOfferAccepted(true);
                usersDao.save(users);
                cartBean.setStatusCode("200");
            } else {
                cartBean.setStatusCode("500");
            }
        } else {
            cartBean.setStatusCode("208");
        }
    }
    
    @Override
    public ReviewBean saveReview(ReviewDto reviewDto) {
    	ReviewBean reviewBean = new ReviewBean();
    	Review review = new Review();
    	Review review1 = new Review();
    	List<Review> reviewList = new ArrayList<Review>();
    	review.setDate(new Date());
    	review.setProductId(reviewDto.getProductId());
    	review.setRating(reviewDto.getRating());
    	review.setSubject(reviewDto.getSubject());
    	review.setComment(reviewDto.getComment());
    	review.setUserId(reviewDto.getUserId());
    	review.setUserName(reviewDto.getUserName());
    	review.setId(null);
    	try {
    		review1=reviewDao.save(review);
    		reviewList =reviewDao.getAllByproductId(reviewDto.getProductId());
    		reviewBean.setReviews(reviewList);
    		reviewBean.setStatus("Success review save");
    		reviewBean.setStatusCode("200");
            return reviewBean;
        } catch (Exception e) {
        	reviewBean.setReviews(null);
        	reviewBean.setStatus("Review can not be added.");
        	reviewBean.setStatusCode("500");
            return reviewBean;
		}
    	
    	
    }


    public static void main(String[] args) {

    }

	@Override
	public void updateLikeUnlike(LikeUnlikeProductDto likeUnlikeProductDto) {
		LikeUnlikeProduct likeUnlikeProduct = likeUnlikeProductDao.getProductLikeUnlike(likeUnlikeProductDto.getProductId(), likeUnlikeProductDto.getUserId());
		if(likeUnlikeProduct == null){
			LikeUnlikeProduct likeunlikeproduct = new LikeUnlikeProduct();
			likeunlikeproduct.setLikeUnlike(likeUnlikeProductDto.isLikeUnlike());
			likeunlikeproduct.setProductId(likeUnlikeProductDto.getProductId());
			likeunlikeproduct.setUserId(likeUnlikeProductDto.getUserId());
			likeUnlikeProductDao.save(likeunlikeproduct);
		}else {
			likeUnlikeProduct.setLikeUnlike(likeUnlikeProductDto.isLikeUnlike());
			likeUnlikeProductDao.save(likeUnlikeProduct);
		}
		
		Product product = productDao.loadById(likeUnlikeProductDto.getProductId());
		long count = product.getLikeUnlikeCount();
		if(likeUnlikeProductDto.isLikeUnlike()){
			count++;
			product.setLikeUnlikeCount(count);
			productDao.save(product);
		}else{
			count--;
			product.setLikeUnlikeCount(count);
			productDao.save(product);
		}
	}
}
