/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.controller;

import com.easypost.model.Shipment;
import com.easypost.model.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.weavers.duqhan.business.AouthService;
import com.weavers.duqhan.business.MailService;
import com.weavers.duqhan.business.PaymentService;
import com.weavers.duqhan.business.ProductService;
import com.weavers.duqhan.business.ShippingService;
import com.weavers.duqhan.business.UsersService;
import com.weavers.duqhan.business.impl.ProductServiceImpl;
import com.weavers.duqhan.dao.CurrencyCodeDao;
import com.weavers.duqhan.dao.ImpressionsDao;
import com.weavers.duqhan.dao.LikeUnlikeProductDao;
import com.weavers.duqhan.dao.RecordedActionsDao;
import com.weavers.duqhan.dao.UserAouthDao;
import com.weavers.duqhan.domain.CurrencyCode;
import com.weavers.duqhan.domain.Impressions;
import com.weavers.duqhan.domain.RecordedActions;
import com.weavers.duqhan.domain.UserAouth;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.AddressBean;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.AutoComplete;
import com.weavers.duqhan.dto.AwsBean;
import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.dto.OrderDetailsBean;
import com.weavers.duqhan.dto.OrderReturnDto;
import com.weavers.duqhan.dto.PriceFilterBean;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.dto.CheckoutPaymentBean;
import com.weavers.duqhan.dto.CurrencyBean;
import com.weavers.duqhan.dto.LikeDto;
import com.weavers.duqhan.dto.LikeUnlikeProductBean;
import com.weavers.duqhan.dto.LikeUnlikeProductDto;
import com.weavers.duqhan.dto.ProductBeans;
import com.weavers.duqhan.dto.ProductDetailBean;
import com.weavers.duqhan.dto.ProductNewBean;
import com.weavers.duqhan.dto.ProductNewBeans;
import com.weavers.duqhan.dto.ProductRequistBean;
import com.weavers.duqhan.dto.ReviewBean;
import com.weavers.duqhan.dto.ReviewDto;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.dto.UserBean;
//import com.weavers.duqhan.util.AwsCloudWatchHelper;
import com.weavers.duqhan.util.DateFormater;
import com.weavers.duqhan.util.StatusConstants;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.social.linkedin.api.Product;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.weavers.duqhan.util.AwsCloudWatchHelper;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author Android-3
 */
@CrossOrigin
@RestController
@RequestMapping("/user/**")
public class UserController {

    //<editor-fold defaultstate="collapsed" desc="Autowired">
    @Autowired
    UsersService usersService;
    @Autowired
    ProductService productService;
    @Autowired
    AouthService aouthService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    ShippingService shippingService;
    @Autowired
    RecordedActionsDao recordedActionsDao;
    @Autowired
    LikeUnlikeProductDao likeUnlikeProductDao;
    @Autowired
    ImpressionsDao impressionsDao;
    @Autowired
    UserAouthDao userAouthDao;
    @Autowired
    CurrencyCodeDao currencyCodeDao;
    
    @Autowired
    MailService mailService;
    
    private final Logger logger = Logger.getLogger(UserController.class);
//</editor-fold>

     private AwsCloudWatchHelper awsCloudWatchHelper = new AwsCloudWatchHelper();
    //<editor-fold defaultstate="collapsed" desc="User Profile Module">
     
     @RequestMapping(value = "/aws-cloud-watch", method = RequestMethod.POST) //logout, destroy auth token.
     public StatusBean awsCloudWatch(HttpServletRequest request, @RequestBody AwsBean awsBean) {
    	 double timeTaken = awsBean.getTimeTaken() / 1000.0;
    	 CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
         	return awsCloudWatchHelper.logCount(awsBean.getName(), awsBean.getName()+" "+"count", awsBean.getApiName()+" "+"API hit counter");
         });
         CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
         	return awsCloudWatchHelper.logTimeSecounds(awsBean.getName(), awsBean.getName()+" "+ "response", awsBean.getApiName()+" "+"API response time", timeTaken);
         });
         StatusBean statusBean = new StatusBean();
         statusBean.setStatus("success");
         return statusBean;
     }
     
    @RequestMapping(value = "/logout", method = RequestMethod.POST) //logout, destroy auth token.
     public StatusBean logOut(HttpServletRequest request, @RequestBody LoginBean loginBean) {
        StatusBean statusBean = new StatusBean();
        loginBean.setAuthtoken(request.getHeader("X-Auth-Token"));  // Check whether Auth-Token is valid, provided by user
        statusBean.setStatus(usersService.userLogout(loginBean));
       return statusBean;
    }
    
    public StatusBean logErrorOnAws(String name) {
   	 CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        	return awsCloudWatchHelper.logCount("Error"+name, "Error "+name+" count", name+" "+"API hit counter");
        });
        StatusBean statusBean = new StatusBean();
        statusBean.setStatus("success");
        mailService.errorLogToAdmin(name);
        return statusBean;
    }
    
    @RequestMapping(value = "/get-profile-details", method = RequestMethod.POST)    // viewe user's profile.
    public UserBean getProfileDetails(HttpServletResponse response, HttpServletRequest request) {
        UserBean userBean = new UserBean();
        try {
        	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
            if (users != null) {
                userBean.setId(users.getId());
                userBean.setDob(DateFormater.formate(users.getDob(), "dd-MMM-yyyy"));
                userBean.setEmail(users.getEmail());
                userBean.setGender(users.getGender());
                userBean.setMobile(users.getMobile());
                userBean.setName(users.getName());
                userBean.setProfileImg(users.getProfileImg());
                userBean.setCurrencyCode(users.getCurrencyCode());
                userBean.setStatusCode("200");
                userBean.setStatus("Success..");
            } else {
            	this.logErrorOnAws("profile detail");
                response.setStatus(401);
                userBean.setStatusCode("401");
                userBean.setStatus("Invalid Token.");
            }
		} catch (Exception e) {
			this.logErrorOnAws("get profile detail exception");
		}
        return userBean;
    }
    
    @RequestMapping(value = "/get-country-code", method = RequestMethod.POST)    // viewe user's profile.
    public CurrencyBean getCountryCode(HttpServletResponse response, HttpServletRequest request) {
       CurrencyBean currencyBean = new CurrencyBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
        	List<CurrencyCode> currencyCodes=currencyCodeDao.loadAll();
        	currencyBean.setCurrencyCodes(currencyCodes);
        } else {
            response.setStatus(401);
            currencyBean.setStatusCode("401");
            currencyBean.setStatus("Invalid Token.");
        }
        return currencyBean;
    }

    @RequestMapping(value = "/update-profile-details", method = RequestMethod.POST) // Update user profile.
    public UserBean updateProfileDetails(HttpServletResponse response, HttpServletRequest request, @RequestBody UserBean userBean) {
        UserBean userBean1 = new UserBean();
        try {
        	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
            if (users != null) {
                userBean1 = usersService.updateUserProfile(users, userBean);
                if(userBean1.getStatusCode().equals("403"))
                	this.logErrorOnAws("update profile");
            } else {
                response.setStatus(401);
                userBean1.setStatusCode("401");
                userBean1.setStatus("Invalid Token.");
                this.logErrorOnAws("update profile");
            }
		} catch (Exception e) {
			this.logErrorOnAws("update profile exception");
		}
        return userBean1;
    }

    /*@RequestMapping(value = "/update-profile-image", method = RequestMethod.POST)   // Update profile image.
    public UserBean updateProfileImage(HttpServletResponse response, HttpServletRequest request, @RequestBody UserBean userBean) {
        UserBean userBean1 = new UserBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            userBean1 = usersService.updateUserProfileImage(users, userBean);
        } else {
            response.setStatus(401);
            userBean1.setStatusCode("401");
            userBean1.setStatus("Invalid Token.");
        }
        return userBean1;
    }*/
    @RequestMapping(value = "/update-profile-image/{userId}", method = RequestMethod.POST)   // Update profile image.
    public UserBean updateProfileImage(HttpServletResponse response, @RequestParam MultipartFile file, @PathVariable("userId") Long userId) {
        UserBean userBean1 = new UserBean();
        try {
        	Users users = usersService.getUserById(userId);   // Check whether Auth-Token is valid, provided by user
            if (users != null && file != null) {
            	System.out.println(file.getOriginalFilename()+ "==" + userId);
                userBean1.setProfileImg(usersService.updateUserProfileImage(users, file));
            } else {
                response.setStatus(401);
                userBean1.setStatusCode("401");
                userBean1.setStatus("Invalid Token.");
            }
		} catch (Exception e) {
			this.logErrorOnAws("update profile image exception");
		}
        return userBean1;
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public StatusBean changePassword(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginBean loginBean) {
        StatusBean statusBean = new StatusBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            statusBean = usersService.changePassword(loginBean, users);
        } else {
            statusBean.setStatusCode("401");
            statusBean.setStatus("Invalid Token.");
        }

        return statusBean;
    }

    @RequestMapping(value = "/get-user-email-phone", method = RequestMethod.POST)    // viewe user's profile.
    public UserBean getUserEmail(HttpServletResponse response, HttpServletRequest request) {
    	UserBean userBean = new UserBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            userBean.setEmail(users.getEmail());
            userBean.setMobile(users.getMobile());
            userBean.setStatusCode("200");
            userBean.setStatus("Success.");
        } else {
            response.setStatus(401);
            userBean.setStatusCode("401");
            userBean.setStatus("Invalid Token.");
        }
        return userBean;
    }

    @RequestMapping(value = "/set-user-email-phone", method = RequestMethod.POST)    // viewe user's profile.
    public UserBean setUserEmail(HttpServletResponse response, HttpServletRequest request, @RequestBody UserBean userBean) {
    	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            if (userBean != null && userBean.getEmail() != null && userBean.getMobile() != null) {
                usersService.saveUsersEmailAndPhone(users, userBean);
            } else {
                response.setStatus(402);
                userBean.setStatusCode("402");
                userBean.setStatus("Please provide email and phone.");
            }
        } else {
            response.setStatus(401);
            userBean.setStatusCode("401");
            userBean.setStatus("Invalid Token.");
        }
        return userBean;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="User address module">
    @RequestMapping(value = "/get-addresses", method = RequestMethod.POST)   // get Addresses.
    public AddressBean getUserAddresses(HttpServletResponse response, HttpServletRequest request) {
        AddressBean addressBean = new AddressBean();
        try {
        	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
            if (users != null) {
                addressBean = usersService.getUserActiveAddresses(users.getId());
            } else {
                response.setStatus(401);
                addressBean.setStatusCode("401");
                addressBean.setStatus("Invalid Token.");
            }
		} catch (Exception e) {
			this.logErrorOnAws("get address exception");
		}
        return addressBean;
    }

    @RequestMapping(value = "/save-address", method = RequestMethod.POST)   // saveUserAddress
    public AddressBean saveOrUpdateUserAddress(HttpServletResponse response, HttpServletRequest request, @RequestBody AddressDto address) {
        AddressBean addressBean = new AddressBean();
        try {
        	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
            if (users != null) {
                address.setUserId(users.getId());
//                StatusBean statusBean = shippingService.verifyAddress(address);
//                if (statusBean.getStatusCode().equals("200")) {
                addressBean = usersService.saveUserAddress(address);
//                } else {
//                    response.setStatus(402);
//                    addressBean.setStatusCode(statusBean.getStatusCode());
//                    addressBean.setStatus(statusBean.getStatus());
//                }
            } else {
                response.setStatus(401);
                addressBean.setStatusCode("401");
                addressBean.setStatus("Invalid Token.");
                this.logErrorOnAws("save address");
            }
		} catch (Exception e) {
			this.logErrorOnAws("save adress exception");
		}
        return addressBean;
    }

    @RequestMapping(value = "/set-default-addresses", method = RequestMethod.POST)   // setUserAddressesAsDefault
    public AddressBean setDefaultAddresses(HttpServletResponse response, HttpServletRequest request, @RequestBody AddressDto address) {
        AddressBean addressBean = new AddressBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            addressBean = usersService.setUserAddressesAsDefault(users.getId(), address.getAddressId());
        } else {
            response.setStatus(401);
            addressBean.setStatusCode("401");
            addressBean.setStatus("Invalid Token.");
        }
        return addressBean;
    }

    @RequestMapping(value = "/get-default-addresses", method = RequestMethod.POST)   // get default Address
    public AddressBean getDefaultAddresses(HttpServletResponse response, HttpServletRequest request) {
        AddressBean addressBean = new AddressBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            addressBean = usersService.getUserDefaultAddress(users.getId());
        } else {
            response.setStatus(401);
            addressBean.setStatusCode("401");
            addressBean.setStatus("Invalid Token.");
        }
        return addressBean;
    }

    @RequestMapping(value = "/deactivate-address", method = RequestMethod.POST)   // deactivateAddress
    public AddressBean deactivateAddress(HttpServletResponse response, HttpServletRequest request, @RequestBody AddressDto address) {
        AddressBean addressBean = new AddressBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            addressBean = usersService.deactivateUserAddress(users.getId(), address.getAddressId());
        } else {
            response.setStatus(401);
            addressBean.setStatusCode("401");
            addressBean.setStatus("Invalid Token.");
        }
         return addressBean;
    }
    @RequestMapping(value = "/get-price-filter", method = RequestMethod.POST) 
    public PriceFilterBean getPriceFilter(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
    	String countryCode=request.getHeader("X-Country-Code");
    	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
    	PriceFilterBean priceFilterBean = new PriceFilterBean(); 
    	if (users != null) {
    		priceFilterBean=productService.getPriceFilter(users, countryCode);
    	 }else {
    		 response.setStatus(401);
    		 priceFilterBean.setStatusCode("401");
    		 priceFilterBean.setStatus("Invalid Token."); 
    	 }
    	return priceFilterBean;
    }
    //<editor-fold defaultstate="collapsed" desc="Find Product">
    @RequestMapping(value = "/get-product", method = RequestMethod.POST)    // get latest product, get recent view product by user, get product by category id
    public ProductNewBeans getProduct(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
    	 ProductNewBeans productBeans = new ProductNewBeans();
    	try {
    		long startTime = System.currentTimeMillis();
        	System.out.println("Start Of User auth==========================="+(startTime-System.currentTimeMillis()));
            Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
            System.out.println("End Of User auth==========================="+(startTime-System.currentTimeMillis()));
            String[] countryArray = {"USD","EUR","GBP","ILS","INR","JPY","KRW","NGN","PHP","PLN","PYG","THB","UAH","VND","KWD"};
            String countryCode=request.getHeader("X-Country-Code");
            Boolean flag = true;
            String currencyCode = "INR";
            if(countryCode != null) {
            	String cuntCode=Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
            	for (String code : countryArray) {
    			 if(cuntCode.equals(code)){
    			  flag=false;
    			  currencyCode=code;
    			  break;
    			 }
            	}
            }
            if(Objects.isNull(countryCode) || flag){
            	currencyCode="INR";
            }
            if (users != null) {
                Long categoryId = null;
                Boolean isRecent = null;
                categoryId = requistBean.getCategoryId();
                isRecent = requistBean.getIsRecent();
                if (isRecent == null) {
                    isRecent = Boolean.FALSE;
                }
                
                if(requistBean.getPriceOrderBy() == null){
                	requistBean.setPriceOrderBy("ASC");
                }
                
                if(requistBean.getPriceLt() == null){
                	requistBean.setPriceLt(0);
                }
                
                if (categoryId != null && !isRecent) {
                    //**********by category id***************//
                    if(Objects.nonNull(requistBean.getLowPrice()) && Objects.nonNull(requistBean.getHighPrice())) {
                    	productBeans = productService.getProductsByPrice(categoryId, requistBean.getStart(), requistBean.getLimit(), requistBean,startTime,users,requistBean.getLowPrice(),requistBean.getHighPrice(),currencyCode);	
                    }else {
                    productBeans = productService.getProductsByCategory(categoryId, requistBean.getStart(), requistBean.getLimit(), requistBean,startTime,users,currencyCode);
                    }
                } else if (categoryId == null && isRecent) {
                    //**********recent viewed****************//
                	 if(Objects.nonNull(requistBean.getLowPrice()) && Objects.nonNull(requistBean.getHighPrice())) {
                     	productBeans = productService.getProductsByRecentViewPrice(users.getId(), requistBean.getStart(), requistBean.getLimit(), requistBean,users,requistBean.getLowPrice(),requistBean.getHighPrice(),currencyCode);	
                     }else {
                    productBeans = productService.getProductsByRecentView(users.getId(), requistBean.getStart(), requistBean.getLimit(), requistBean,users,currencyCode);
                     }
                     } else if (categoryId == null && !isRecent) {
                    //******************all******************//
                   // productBeans = productService.getAllProducts(requistBean.getStart(), requistBean.getStart(), requistBean);
                    	 if(Objects.nonNull(requistBean.getLowPrice()) && Objects.nonNull(requistBean.getHighPrice())) {
                    		 productBeans = this.getProductCacheByPrice(users, requistBean,currencyCode);
                          }else {
                            productBeans = this.getProductV1(users,requistBean,currencyCode);
                          }
                }
            } else {
                response.setStatus(401);
                productBeans.setStatusCode("401");
                productBeans.setStatus("Invalid Token.");
                this.logErrorOnAws("get product");
            }
		} catch (Exception e) {
			//this.logErrorOnAws("get product exception");
		}

        return productBeans;
    }
    
    //@RequestMapping(value = "/get-product-new", method = RequestMethod.POST)    // get latest product, get recent view product by user, get product by category id
    public ProductNewBeans getProductV1(Users users,ProductRequistBean requistBean,String currencyCo) throws JsonParseException, JsonMappingException, IOException {
        ProductNewBeans productBeans = new ProductNewBeans();
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        		
        	if(!CacheController.isProductBeanListAvailableForUser(users)) {
        		CacheController.buildProductBeanList(users);
        	} 
        	CurrencyCode currencyCode = new CurrencyCode();
            String symbol = new String();
            if(!Objects.isNull(users.getEmail()) && users.getEmail().equals("guest@gmail.com")) {
            	currencyCode = currencyCodeDao.getCurrencyConversionCode(currencyCo);
            	symbol=currencyCode.getCode();
            }else {
            	if(Objects.nonNull(users.getCurrencyCode())&&!users.getCurrencyCode().isEmpty()){
                	currencyCode = currencyCodeDao.getCurrencyConversionCode(users.getCurrencyCode());
                	symbol=currencyCode.getCode();
                }else{
                	List<UserAouth> aouthUserL = userAouthDao.loadByUserId(users.getId());
                	if(Objects.nonNull(aouthUserL)&&!aouthUserL.isEmpty()) {
                    	currencyCode = currencyCodeDao.getCurrencyConversionCode(aouthUserL.get(0).getCodeName());
                    	symbol=currencyCode.getCode();
                    } else {
                    	currencyCode.setValue(1d);
                    	symbol="INR";
                    }
                }
            }
        	List<ProductNewBean> productbeansl = new ArrayList<>();
        	List<ProductNewBean> productbeans = CacheController.getProductBeanList(users, requistBean.getStart(), requistBean.getLimit());
        	for (ProductNewBean productNewBean : productbeans) {
        		ProductNewBean priceBean = new ProductNewBean(); 
        		priceBean.setDiscountedPrice(ProductServiceImpl.getThreeDecimalFormat(currencyCode.getValue()*productNewBean.getDiscountedPrice()));
        		priceBean.setPrice(ProductServiceImpl.getThreeDecimalFormat(productNewBean.getPrice()*currencyCode.getValue()));
        		priceBean.setSymbol(symbol);
        		priceBean.setDiscountPCT(productNewBean.getDiscountPCT());
        		priceBean.setImgurl(productNewBean.getImgurl());
        		priceBean.setName(productNewBean.getName());
        		priceBean.setProductId(productNewBean.getProductId());
				productbeansl.add(priceBean);
			}
        	/*for (ProductBean productBean : productbeans) {
        		Impressions impressions = new Impressions();
                impressions.setDate(new Date());
                impressions.setProductId(productBean.getProductId());
                impressions.setUserId(users.getId());
                impressionsDao.save(impressions);	
			}
        	*/
        	//productBeans.setTotalProducts(300);
             productBeans.setProducts(productbeansl);
        
        return productBeans;
    }
    
    public ProductNewBeans getProductCacheByPrice(Users users,ProductRequistBean requistBean,String currencyCode) {
        ProductNewBeans productBeans = new ProductNewBeans();
        	try {	
        	if(!CacheController.isProductBeanListAvailableForUserPrice(users)) {
        		CacheController.buildProductBeanListPrice(users,requistBean.getLowPrice(),requistBean.getHighPrice());
        	} 
        	
        	List<ProductNewBean> productbeans = CacheController.getProductBeanListByPrice(users, requistBean.getStart(), requistBean.getLimit());
        	
        	/*for (ProductBean productBean : productbeans) {
        		Impressions impressions = new Impressions();
                impressions.setDate(new Date());
                impressions.setProductId(productBean.getProductId());
                impressions.setUserId(users.getId());
                impressionsDao.save(impressions);	
			}
        	*/
        	//productBeans.setTotalProducts(300);
             productBeans.setProducts(productbeans);
        	} catch (Exception e) {
        		
        	}
        
        return productBeans;
    }
    
    @RequestMapping(value = "/search-product", method = RequestMethod.POST)    // search product by product name
    public ProductNewBeans searchProduct(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
    	 ProductNewBeans productBeans = new ProductNewBeans();
    	try {
    	   Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
           String[] countryArray = {"USD","EUR","GBP","ILS","INR","JPY","KRW","NGN","PHP","PLN","PYG","THB","UAH","VND","KWD"};
           String countryCode=request.getHeader("X-Country-Code");
           Boolean flag = true;
           String currencyCode = "INR";
           if(countryCode != null) {
           	String cuntCode=Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
           	for (String code : countryArray) {
   			 if(cuntCode.equals(code)){
   			  flag=false;
   			  currencyCode=code;
   			  break;
   			 }
           	}
           }
           if(Objects.isNull(countryCode) || flag){
           	currencyCode="INR";
           }
           if (users != null) {
               requistBean.setUserId(users.getId());
               productBeans = productService.searchProducts(requistBean,users,currencyCode);
           } else {
               response.setStatus(401);
               productBeans.setStatusCode("401");
               productBeans.setStatus("Invalid Token.");
               this.logErrorOnAws("search product");
           }
		} catch (Exception e) {
			this.logErrorOnAws("search product exception");
		}

        return productBeans;
    }
    
    @RequestMapping(value = "/search-autoComplete", method = RequestMethod.POST)
    public List<AutoComplete> searchAutoComplete(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
        List<AutoComplete> result = new ArrayList<>();
        if (users != null) {
             result= productService.searchAutocomplete(requistBean.getName());
        } 
        return result;
    }
    
    @RequestMapping(value = "/get-product-detail", method = RequestMethod.POST) // product details by product id.
    public ProductDetailBean getProductDetails(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
    	ProductDetailBean productDetailBean = new ProductDetailBean();
    	try {
        	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
            String[] countryArray = {"USD","EUR","GBP","ILS","INR","JPY","KRW","NGN","PHP","PLN","PYG","THB","UAH","VND","KWD"};
            String countryCode=request.getHeader("X-Country-Code");
            Boolean flag = true;
            String currencyCode = "INR";
            if(countryCode != null) {
            	String cuntCode=Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
            	for (String code : countryArray) {
    			 if(cuntCode.equals(code)){
    			  flag=false;
    			  currencyCode=code;
    			  break;
    			 }
            	}
            }
            if(Objects.isNull(countryCode) || flag){
            	currencyCode="INR";
            }
            
            if (users != null) {
                productDetailBean = productService.getProductDetailsById(requistBean.getProductId(), users,currencyCode);
            } else {
                response.setStatus(401);
                productDetailBean.setStatusCode("401");
                productDetailBean.setStatus("Invalid Token.");
                this.logErrorOnAws("product detail");
            }
		} catch (Exception e) {
			this.logErrorOnAws("product detail exception");
		}

        return productDetailBean;
    }
    
    @RequestMapping(value = "/get-product-reviews", method = RequestMethod.POST) // product details by product id.
    public ProductDetailBean getProductReviews(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
    	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductDetailBean productDetailBean = new ProductDetailBean();
        if (users != null) {
            productDetailBean = productService.getProductReviewsById(requistBean.getProductId(), users.getId());
        } else {
            response.setStatus(401);
            productDetailBean.setStatusCode("401");
            productDetailBean.setStatus("Invalid Token.");
        }

        return productDetailBean;
    }
    
    @RequestMapping(value = "/save-recent-record", method = RequestMethod.POST) // product details by product id.
    public ProductDetailBean saveRecentRecord(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
    	long startTime = System.currentTimeMillis();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductDetailBean productDetailBean = new ProductDetailBean();
        if (users != null) {
            productDetailBean = productService.saveRecentRecord(requistBean.getProductId(), users.getId());
        } else {
            response.setStatus(401);
            productDetailBean.setStatusCode("401");
            productDetailBean.setStatus("Invalid Token.");
        }

        return productDetailBean;
    }
    
    @RequestMapping(value = "/get-like-unlike", method = RequestMethod.POST) // product details by product id.
    public LikeDto getLikeUnlike(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
    	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        LikeDto likeDto = new LikeDto();
        if (users != null) {
        	likeDto = productService.getLikeUnlike(requistBean.getProductId(), users.getId());
        } else {
            response.setStatus(401);
            likeDto.setStatusCode("401");
            likeDto.setStatus("Invalid Token.");
        }

        return likeDto;
    }
    	//</editor-fold>
    	

    //<editor-fold defaultstate="collapsed" desc="Cart Module">
    @RequestMapping(value = "/add-to-cart", method = RequestMethod.POST)    // add product to cart by user.
    public StatusBean addProductToCart(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        StatusBean statusBean = new StatusBean();
        try {
        	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
            if (users != null) {
                requistBean.setUserId(users.getId());
                statusBean.setStatus(productService.addProductToCart(requistBean));
                if(statusBean.getStatus().equals("failure"))
                	this.logErrorOnAws("add to cart fail");
            } else {
                response.setStatus(401);
                statusBean.setStatusCode("401");
                statusBean.setStatus("Invalid Token.");
                this.logErrorOnAws("add to cart");
            }
			
		} catch (Exception e) {
			this.logErrorOnAws("add to cart exception");
		}
        

        return statusBean;
    }

    @RequestMapping(value = "/set-property-record", method = RequestMethod.POST)    // add product to cart by user.
    public StatusBean setPropertyRecord(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
    	 StatusBean statusBean = new StatusBean();
         Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
         if (users != null) {
        	 RecordedActions actions = new RecordedActions();
        	 actions.setDate(new Date());
             actions.setProductId(requistBean.getProductId());
             actions.setUserId(users.getId());
             actions.setAction(requistBean.getName());
             RecordedActions act=recordedActionsDao.save(actions);
             if(act != null)
            	 statusBean.setStatus("success");
             requistBean.setUserId(users.getId());
             statusBean.setStatus(productService.removeProductFromCart(requistBean));
         } else {
             response.setStatus(401);
             statusBean.setStatusCode("401");
             statusBean.setStatus("Invalid Token.");
         }

         return statusBean;
    }
    @RequestMapping(value = "/cart", method = RequestMethod.POST)   // view all product of a user's cart.
    public CartBean getCart(HttpServletResponse response, HttpServletRequest request) {
        CartBean cartBean = new CartBean();
        try {
        	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
            if (users != null) {
                cartBean = productService.getCartForUser(users);
            } else {
                response.setStatus(401);
                cartBean.setStatusCode("401");
                cartBean.setStatus("Invalid Token.");
                this.logErrorOnAws("get cart");
            }
		} catch (Exception e) {
			this.logErrorOnAws("get cart exception");
		}

        return cartBean;
    }

    @RequestMapping(value = "/get-cart-count", method = RequestMethod.POST) // number of item added in cart by user.
    public UserBean getCartCount(HttpServletResponse response, HttpServletRequest request) {
        UserBean userBean = new UserBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            userBean.setCartCount(productService.getCartCountFoAUser(users.getId()));
            userBean.setStatusCode("200");
            userBean.setStatus("Success..");
        } else {
            response.setStatus(401);
            userBean.setStatusCode("401");
            userBean.setStatus("Invalid Token.");
        }

        return userBean;
    }

    @RequestMapping(value = "/remove-from-cart", method = RequestMethod.POST)   // product remove from cart.
    public StatusBean removeProductFromCart(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        StatusBean statusBean = new StatusBean();
        try {
        	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
            if (users != null) {
                requistBean.setUserId(users.getId());
                statusBean.setStatus(productService.removeProductFromCart(requistBean));
                if(statusBean.getStatus().equals("failure"))
                this.logErrorOnAws("remove from cart fail");
            } else {
                response.setStatus(401);
                statusBean.setStatusCode("401");
                statusBean.setStatus("Invalid Token.");
                this.logErrorOnAws("remove from cart");
            }
		} catch (Exception e) {
			this.logErrorOnAws("remove from cart exception");
		}

        return statusBean;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Shipping Module">
    @RequestMapping(value = "/get-shipment", method = RequestMethod.POST)   // .
    public CartBean getShipment(HttpServletRequest request, HttpServletResponse response, @RequestBody CartBean cartBean) {
        CartBean cartBean1 = new CartBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            cartBean.setUserId(users.getId());
            cartBean1 = shippingService.getCartAfterShipment(cartBean);
        } else {
            response.setStatus(401);
            cartBean1.setStatusCode("401");
            cartBean1.setStatus("Invalid Token.");
        }
        return cartBean1;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Checkout, Payment, Order">
    @RequestMapping(value = "/checkout", method = RequestMethod.POST)   // .
    public CheckoutPaymentBean paymentRequest(HttpServletRequest request, HttpServletResponse response, @RequestBody CartBean cartBean) {
        CheckoutPaymentBean paymentBean = new CheckoutPaymentBean();
        Double shippingCost = 0.0;
        List<Shipment> shipments = new ArrayList<>();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            try {
                cartBean.setUserId(users.getId());
                if (StatusConstants.IS_SHIPMENT) {
                    Object[] objects = shippingService.createShipments(cartBean);
                    if (null != objects) {
                        shippingCost = (Double) objects[0];
                        shipments = (List<Shipment>) objects[1];
                        if (null != shippingCost && shippingCost > 0.0 && !shipments.isEmpty()) {
                            if (cartBean.getPaymentGateway() == StatusConstants.PAYPAL_GATEWAY) {
                                paymentBean = paymentService.transactionRequest(request, response, cartBean, shippingCost, shipments,users);
                            } else if (cartBean.getPaymentGateway() == StatusConstants.PAYTM_GATEWAY) {
                               /* String url = request.getRequestURL().toString();
                                String uri = request.getRequestURI();
                                String ctx = request.getContextPath();
                                String base = url.substring(0, url.length() - uri.length()) + ctx;*/
                                paymentBean = paymentService.transactionRequest(users, cartBean, shippingCost, shipments);
                            }
                            if (paymentBean != null) {

                            } else {
                                response.setStatus(500);
                                paymentBean.setStatusCode("500");
                                paymentBean.setStatus("Payment not done. Please try again.");
                            }
                        } else {
                            response.setStatus(500);
                            paymentBean.setStatusCode("500");
                            paymentBean.setStatus("Shipment can not create. Please try again.");
                        }
                    } else {
                        response.setStatus(500);
                        paymentBean.setStatusCode("500");
                        paymentBean.setStatus("Shipment can not create. Please try again2.");
                    }
                } else {
                    if (cartBean.getPaymentGateway() == StatusConstants.PAYPAL_GATEWAY) {
                        paymentBean = paymentService.transactionRequest(request, response, cartBean, shippingCost, shipments,users);
                    } else if (cartBean.getPaymentGateway() == StatusConstants.PAYTM_GATEWAY) {
                        /*String url = request.getRequestURL().toString();
                        String uri = request.getRequestURI();
                        String ctx = request.getContextPath();
                        String base = url.substring(0, url.length() - uri.length()) + ctx;*/
                    	paymentBean = paymentService.transactionRequest(users, cartBean, shippingCost, shipments);
                    }
                    if (paymentBean != null) {
                    } else {
                        response.setStatus(500);
                        paymentBean.setStatusCode("500");
                        paymentBean.setStatus("Payment not done. Please try again.");
                        logger.error("(==E==)Payment not done. Please try again. ");
                    }
                }
            } catch (Exception e) {
                response.setStatus(500);
                paymentBean.setStatusCode("500");
                logger.error("(==E==)payPayPal() in controller. " + e);
            }
        } else {
            logger.error("(==E==)Invalid Token. ");
            response.setStatus(401);
            paymentBean.setStatusCode("401");
            paymentBean.setStatus("Invalid Token.");
        }
        /*if(!paymentBean.getStatusCode().equals("401") && !paymentBean.getStatusCode().equals("500") ){
        	RecordedActions actions = new RecordedActions();
        	List<ProductBean> b=cartBean.getProducts();
        	for (ProductBean productBean : b) {
        		actions.setDate(new Date());
                actions.setProductId(productBean.getProductId());
                actions.setUserId(cartBean.getUserId());
                actions.setAction("overview");
                recordedActionsDao.save(actions);
			}
        }*/
        return paymentBean;
    }

    @RequestMapping(value = "/check-payment-status", method = RequestMethod.POST)   // .
    public StatusBean checkPaymentStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductRequistBean requistBean) {
       StatusBean statusBean = new StatusBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            String status = "";
            requistBean.setUserId(users.getId());
            String[] responseArray = paymentService.getPaymentStatus(users.getId(), requistBean.getName()); //requistBean.getName() = payKey
            statusBean.setStatus(responseArray[0]);
            statusBean.setStatusCode(responseArray[1]);

        } else {
            response.setStatus(401);
            statusBean.setStatusCode("401");
            statusBean.setStatus("Invalid Token.");
        }

        return statusBean;
    }

    @RequestMapping(value = "/get-order-details", method = RequestMethod.POST)   // List of order
    public OrderDetailsBean getOrderDetails(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
        try {
        	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
            if (users != null) {
                orderDetailsBean = productService.getOrderDetails(users, requistBean.getStart(), requistBean.getLimit());
            } else {
                response.setStatus(401);
                orderDetailsBean.setStatusCode("401");
                orderDetailsBean.setStatus("Invalid Token.");
                this.logErrorOnAws("get order detail");
            }
		} catch (Exception e) {
			this.logErrorOnAws("get order detail exception");
		}

        return orderDetailsBean;
    }

    @RequestMapping(value = "/cancel-order", method = RequestMethod.POST)   //cancel order
    public StatusBean cancelOrder(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        StatusBean statusBean = new StatusBean();
        try {
        	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
            if (users != null) {
                productService.cancelOrder(requistBean.getOrderId(), users.getId());
                statusBean.setStatusCode("200");
                statusBean.setStatus("Success");
            } else {
                statusBean.setStatusCode("401");
                statusBean.setStatus("Invalid Token.");
                this.logErrorOnAws("cancel order");
            }
		} catch (Exception e) {
			this.logErrorOnAws("cancel order exception");
		}

         return statusBean;
    }
    
    @RequestMapping(value = "/order/request_return", method = RequestMethod.POST)   //return order
    public StatusBean requestReturn(HttpServletResponse response, HttpServletRequest request, @RequestParam("orderId") String orderId,@RequestParam("returnText") String returnText, @RequestParam("authToken") String authToken, @RequestParam("file") MultipartFile file) {
        StatusBean statusBean = new StatusBean();
        //System.out.println("file name is......................"+orderId+returnText+file.getOriginalFilename()+authToken);
        //productService.returnOrder(orderReturnDto,file);
        Users users = aouthService.getUserByToken(authToken);   // Check whether Auth-Token is valid, provided by user
        //Users users = aouthService.getUserById(4L);
        if (users != null) {
        	OrderReturnDto orderReturnDto = new OrderReturnDto();
            orderReturnDto.setOrderId(orderId);
            orderReturnDto.setUserId(users.getId());
            orderReturnDto.setReturnText(returnText);
            productService.returnOrder(orderReturnDto,file);
            statusBean.setStatusCode("200");
            statusBean.setStatus("Success");
        } else {
            statusBean.setStatusCode("401");
            statusBean.setStatus("Invalid Token.");
        }

        return statusBean;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Contact to admin">
    @RequestMapping(value = "/contact-us", method = RequestMethod.POST)   //send a mail to admin from user
    public StatusBean contactToAdmin(HttpServletRequest request, @RequestBody UserBean userBean) {
        StatusBean status = new StatusBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            if (userBean != null && userBean.getEmail() != null && userBean.getMobile() != null && userBean.getStatus() != null && userBean.getStatusCode() != null) { //userBean.getStatus = coments, userBean.getStatusCode = subject
                status.setStatus(usersService.contactToAdmin(userBean, users));
                status.setStatusCode("200");
            } else {
                status.setStatus("Inadequate information.");
            }
        } else {
            status.setStatusCode("401");
            status.setStatus("Invalid Token.");
        }

       return status;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Free product">
    @RequestMapping(value = "/get-free-product", method = RequestMethod.POST)
    public ProductBeans getFreeProduct(HttpServletRequest request) {
        ProductBeans productBeans = new ProductBeans();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            if (users.getFreeOfferAccepted()) {
                productBeans.setStatus("You have already claimed your free product.");
                productBeans.setStatusCode("200");
            } else {
                productBeans = productService.getFreeProducts();
                productBeans.setStatus(null != productBeans.getProducts() ? "success" : "There is no free products available right now. Please try later.");
                productBeans.setStatusCode("200");
            }
        } else {
            productBeans.setStatusCode("401");
            productBeans.setStatus("Invalid Token.");
        }

       return productBeans;
    }

    @RequestMapping(value = "/accept-free-product-offer", method = RequestMethod.POST)
    public CartBean acceptFreeProduct(HttpServletRequest request, @RequestBody CartBean cartBean) {
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null && cartBean != null && cartBean.getUserId() != null) {
            productService.acceptFreeProduct(users, cartBean); //cartBean.getUserId() = product_size_color_map id
//            cartBean.setStatusCode("200");
        } else {
            cartBean.setStatusCode("401");
            cartBean.setStatus("Invalid Token.");
        }
        return cartBean;
    }
//</editor-fold>
/**/
    //<editor-fold defaultstate="collapsed" desc="Test">
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test() {
        productService.test();
    }
//</editor-fold>
    
    @RequestMapping(value = "/save-review", method = RequestMethod.POST)   // saveUserReview
    public ReviewBean saveReview(HttpServletResponse response, HttpServletRequest request, @RequestBody ReviewDto review ) {
        ReviewBean reviewBean = new ReviewBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            review.setUserId(users.getId());
            review.setUserName(users.getName());
            reviewBean =productService.saveReview(review);
        } else {
            response.setStatus(401);
            reviewBean.setStatusCode("401");
            reviewBean.setStatus("Invalid Token.");
        }
        return reviewBean;
    }
    
    
    @RequestMapping(value = "/likeUlike", method = RequestMethod.POST)
    public LikeUnlikeProductBean updateLikeUnlike(HttpServletResponse response, HttpServletRequest request, @RequestBody LikeUnlikeProductDto likeUnlikeProductDto) {
    	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
    	LikeUnlikeProductBean likeUnlikeProductBean = new LikeUnlikeProductBean();
    	if (users != null) {
    		likeUnlikeProductDto.setUserId(users.getId());
            productService.updateLikeUnlike(likeUnlikeProductDto);
            likeUnlikeProductBean.setStatusCode("200");
            likeUnlikeProductBean.setStatus("Update Succefully.....");
        } else {
            response.setStatus(401);
            likeUnlikeProductBean.setStatusCode("401");
            likeUnlikeProductBean.setStatus("Invalid Token.");
        }
    	return likeUnlikeProductBean;
    }
}
