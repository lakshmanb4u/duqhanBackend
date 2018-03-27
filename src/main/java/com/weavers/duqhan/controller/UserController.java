/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.controller;

import com.easypost.model.Shipment;
import com.easypost.model.User;
import com.weavers.duqhan.business.AouthService;
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
import com.weavers.duqhan.dto.AwsBean;
import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.dto.OrderDetailsBean;
import com.weavers.duqhan.dto.OrderReturnDto;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
    	long startTime = System.currentTimeMillis();
        StatusBean statusBean = new StatusBean();
        loginBean.setAuthtoken(request.getHeader("X-Auth-Token"));  // Check whether Auth-Token is valid, provided by user
        statusBean.setStatus(usersService.userLogout(loginBean));
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        	return awsCloudWatchHelper.logCount("Logout", "Logout count", "Logout API hit counter");
        });
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
        	return awsCloudWatchHelper.logTimeSecounds("Logout", "Logout response", "Logout API response time", timeTaken);
        });
        return statusBean;
    }

    @RequestMapping(value = "/get-profile-details", method = RequestMethod.POST)    // viewe user's profile.
    public UserBean getProfileDetails(HttpServletResponse response, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        UserBean userBean = new UserBean();
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
            response.setStatus(401);
            userBean.setStatusCode("401");
            userBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        return awsCloudWatchHelper.logCount("Profile Details", "profile details count", "get-profile-details API hit counter");
        });
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
        return awsCloudWatchHelper.logTimeSecounds("Profile Details", "profile details response", "get-profile-details API response time", timeTaken);
        });
        return userBean;
    }
    
    @RequestMapping(value = "/get-country-code", method = RequestMethod.POST)    // viewe user's profile.
    public CurrencyBean getCountryCode(HttpServletResponse response, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
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

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Profile Details", "profile details count", "get-profile-details API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Profile Details", "profile details response", "get-profile-details API response time", timeTaken);
            });
        
        
        return currencyBean;
    }

    @RequestMapping(value = "/update-profile-details", method = RequestMethod.POST) // Update user profile.
    public UserBean updateProfileDetails(HttpServletResponse response, HttpServletRequest request, @RequestBody UserBean userBean) {
        long startTime = System.currentTimeMillis();
        UserBean userBean1 = new UserBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            userBean1 = usersService.updateUserProfile(users, userBean);
        } else {
            response.setStatus(401);
            userBean1.setStatusCode("401");
            userBean1.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Update Profile", "update profile count", "update-profile-details API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Update Profile", "update profile response", "update-profile-details API response time", timeTaken);
            });
        
        
        
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
        long startTime = System.currentTimeMillis();
        UserBean userBean1 = new UserBean();
        Users users = usersService.getUserById(userId);   // Check whether Auth-Token is valid, provided by user
        if (users != null && file != null) {
        	System.out.println(file.getOriginalFilename()+ "==" + userId);
            userBean1.setProfileImg(usersService.updateUserProfileImage(users, file));
        } else {
            response.setStatus(401);
            userBean1.setStatusCode("401");
            userBean1.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Update Profile Image", "update profile image count", "update-profile-image API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Update Profile Image", "update profile image response", "update-profile-image API response time", timeTaken);
            });
        
        
        return userBean1;
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public StatusBean changePassword(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginBean loginBean) {
        long startTime = System.currentTimeMillis();
        StatusBean statusBean = new StatusBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            statusBean = usersService.changePassword(loginBean, users);
        } else {
            statusBean.setStatusCode("401");
            statusBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Change Password", "change password count", "change-password API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Change Password", "change password response", "change-password API response time", timeTaken);
            });
        
        
        return statusBean;
    }

    @RequestMapping(value = "/get-user-email-phone", method = RequestMethod.POST)    // viewe user's profile.
    public UserBean getUserEmail(HttpServletResponse response, HttpServletRequest request) {
    	 long startTime = System.currentTimeMillis();
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
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Get User Email", "Get User Email count", "get-user-email-phone API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Get User Email", "Get User Email response", "get-user-email-phone API response time", timeTaken);
            });
        
        
        return userBean;
    }

    @RequestMapping(value = "/set-user-email-phone", method = RequestMethod.POST)    // viewe user's profile.
    public UserBean setUserEmail(HttpServletResponse response, HttpServletRequest request, @RequestBody UserBean userBean) {
    	 long startTime = System.currentTimeMillis();
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
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Set User Email", "Set User Email count", "set-user-email-phone API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Set User Email", "Set User Email response", "set-user-email-phone API response time", timeTaken);
            });
        
        
        return userBean;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="User address module">
    @RequestMapping(value = "/get-addresses", method = RequestMethod.POST)   // get Addresses.
    public AddressBean getUserAddresses(HttpServletResponse response, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        AddressBean addressBean = new AddressBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            addressBean = usersService.getUserActiveAddresses(users.getId());
        } else {
            response.setStatus(401);
            addressBean.setStatusCode("401");
            addressBean.setStatus("Invalid Token.");
        }
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Addresses", "get addresses count", "get-addresses API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Addresses", "get addresses response", "get-addresses API response time", timeTaken);
            });
        
        
        return addressBean;
    }

    @RequestMapping(value = "/save-address", method = RequestMethod.POST)   // saveUserAddress
    public AddressBean saveOrUpdateUserAddress(HttpServletResponse response, HttpServletRequest request, @RequestBody AddressDto address) {
        long startTime = System.currentTimeMillis();
        AddressBean addressBean = new AddressBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            address.setUserId(users.getId());
//            StatusBean statusBean = shippingService.verifyAddress(address);
//            if (statusBean.getStatusCode().equals("200")) {
            addressBean = usersService.saveUserAddress(address);
//            } else {
//                response.setStatus(402);
//                addressBean.setStatusCode(statusBean.getStatusCode());
//                addressBean.setStatus(statusBean.getStatus());
//            }
        } else {
            response.setStatus(401);
            addressBean.setStatusCode("401");
            addressBean.setStatus("Invalid Token.");
        }
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Save Address", "save address count", "save-address API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Save Address", "save address response", "save-address API response time", timeTaken);
            });
        
        
        return addressBean;
    }

    @RequestMapping(value = "/set-default-addresses", method = RequestMethod.POST)   // setUserAddressesAsDefault
    public AddressBean setDefaultAddresses(HttpServletResponse response, HttpServletRequest request, @RequestBody AddressDto address) {
        long startTime = System.currentTimeMillis();
        AddressBean addressBean = new AddressBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            addressBean = usersService.setUserAddressesAsDefault(users.getId(), address.getAddressId());
        } else {
            response.setStatus(401);
            addressBean.setStatusCode("401");
            addressBean.setStatus("Invalid Token.");
        }
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Set Default Addresses", "set default addresses count", "set-default-addresses API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Set Default Addresses", "set default addresses response", "set-default-addresses API response time", timeTaken);
            });
        
        
        return addressBean;
    }

    @RequestMapping(value = "/get-default-addresses", method = RequestMethod.POST)   // get default Address
    public AddressBean getDefaultAddresses(HttpServletResponse response, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        AddressBean addressBean = new AddressBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            addressBean = usersService.getUserDefaultAddress(users.getId());
        } else {
            response.setStatus(401);
            addressBean.setStatusCode("401");
            addressBean.setStatus("Invalid Token.");
        }
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Default Addresses", "get default addresses count", "get-default-addresses API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Default Addresses", "get default addresses response", "get-default-addresses API response time", timeTaken);
            });
        
        
        return addressBean;
    }

    @RequestMapping(value = "/deactivate-address", method = RequestMethod.POST)   // deactivateAddress
    public AddressBean deactivateAddress(HttpServletResponse response, HttpServletRequest request, @RequestBody AddressDto address) {
        long startTime = System.currentTimeMillis();
        AddressBean addressBean = new AddressBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            addressBean = usersService.deactivateUserAddress(users.getId(), address.getAddressId());
        } else {
            response.setStatus(401);
            addressBean.setStatusCode("401");
            addressBean.setStatus("Invalid Token.");
        }
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Deactivate Address", "deactivate address count", "deactivate-address API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Deactivate Address", "deactivate address response", "deactivate-address API response time", timeTaken);
            });
        
        
        return addressBean;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Find Product">
    @RequestMapping(value = "/get-product", method = RequestMethod.POST)    // get latest product, get recent view product by user, get product by category id
    public ProductNewBeans getProduct(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        long startTime = System.currentTimeMillis();
        System.out.println("Start Of User auth==========================="+(startTime-System.currentTimeMillis()));
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        System.out.println("End Of User auth==========================="+(startTime-System.currentTimeMillis()));
        ProductNewBeans productBeans = new ProductNewBeans();
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
                	productBeans = productService.getProductsByPrice(categoryId, requistBean.getStart(), requistBean.getLimit(), requistBean,startTime,users,requistBean.getLowPrice(),requistBean.getHighPrice());	
                }else {
                productBeans = productService.getProductsByCategory(categoryId, requistBean.getStart(), requistBean.getLimit(), requistBean,startTime,users);
                }
            } else if (categoryId == null && isRecent) {
                //**********recent viewed****************//
            	 if(Objects.nonNull(requistBean.getLowPrice()) && Objects.nonNull(requistBean.getHighPrice())) {
                 	productBeans = productService.getProductsByRecentViewPrice(users.getId(), requistBean.getStart(), requistBean.getLimit(), requistBean,users,requistBean.getLowPrice(),requistBean.getHighPrice());	
                 }else {
                productBeans = productService.getProductsByRecentView(users.getId(), requistBean.getStart(), requistBean.getLimit(), requistBean,users);
                 }
                 } else if (categoryId == null && !isRecent) {
                //******************all******************//
               // productBeans = productService.getAllProducts(requistBean.getStart(), requistBean.getStart(), requistBean);
                	 if(Objects.nonNull(requistBean.getLowPrice()) && Objects.nonNull(requistBean.getHighPrice())) {
                		 productBeans = this.getProductCacheByPrice(users, requistBean);
                      }else {
                        productBeans = this.getProductV1(users,requistBean);
                      }
            }
        } else {
            response.setStatus(401);
            productBeans.setStatusCode("401");
            productBeans.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        System.out.println("Total time taken for api==========================="+timeTaken);
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Get Product", "get product count", "get-product API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Get Product", "get product response", "get-product API response time", timeTaken);
            });
        return productBeans;
    }
    
    //@RequestMapping(value = "/get-product-new", method = RequestMethod.POST)    // get latest product, get recent view product by user, get product by category id
    public ProductNewBeans getProductV1(Users users,ProductRequistBean requistBean) {
        ProductNewBeans productBeans = new ProductNewBeans();
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        	try {	
        	if(!CacheController.isProductBeanListAvailableForUser(users)) {
        		CacheController.buildProductBeanList(users);
        	} 
        	CurrencyCode currencyCode = new CurrencyCode();
            String symbol = new String();
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
        	List<ProductNewBean> productbeansl = new ArrayList<>();
        	List<ProductNewBean> productbeans = CacheController.getProductBeanList(users, requistBean.getStart(), requistBean.getLimit());
        	for (ProductNewBean productNewBean : productbeans) {
        		ProductNewBean priceBean = new ProductNewBean(); 
        		priceBean.setDiscountedPrice(ProductServiceImpl.getTwoDecimalFormat(currencyCode.getValue()*productNewBean.getDiscountedPrice()));
        		priceBean.setPrice(ProductServiceImpl.getTwoDecimalFormat(productNewBean.getPrice()*currencyCode.getValue()));
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
        	} catch (Exception e) {
        		
        	}
        
        return productBeans;
    }
    
    public ProductNewBeans getProductCacheByPrice(Users users,ProductRequistBean requistBean) {
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
        long startTime = System.currentTimeMillis();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductNewBeans productBeans = new ProductNewBeans();
        if (users != null) {
            requistBean.setUserId(users.getId());
            productBeans = productService.searchProducts(requistBean,users);
        } else {
            response.setStatus(401);
            productBeans.setStatusCode("401");
            productBeans.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Search Product", "search product count", "search-product API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Search Product", "search product response", "search-product API response time", timeTaken);
            });
        
        
        return productBeans;
    }
    
    @RequestMapping(value = "/get-product-detail", method = RequestMethod.POST) // product details by product id.
    public ProductDetailBean getProductDetails(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        long startTime = System.currentTimeMillis();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductDetailBean productDetailBean = new ProductDetailBean();
        if (users != null) {
            productDetailBean = productService.getProductDetailsById(requistBean.getProductId(), users);
        } else {
            response.setStatus(401);
            productDetailBean.setStatusCode("401");
            productDetailBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Product Detail", "Product Detail count", "get-product-detail API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Product Detail", "Product Detail response", "get-product-detail API response time", timeTaken);
            });
        
        
        return productDetailBean;
    }
    
    @RequestMapping(value = "/get-product-reviews", method = RequestMethod.POST) // product details by product id.
    public ProductDetailBean getProductReviews(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
    	long startTime = System.currentTimeMillis();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductDetailBean productDetailBean = new ProductDetailBean();
        if (users != null) {
            productDetailBean = productService.getProductReviewsById(requistBean.getProductId(), users.getId());
        } else {
            response.setStatus(401);
            productDetailBean.setStatusCode("401");
            productDetailBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Product Reviews", "product Reviews count", "get-product-reviews API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Product Reviews", "product Reviews response", "get-product-reviews API response time", timeTaken);
            });
        
        
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

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Save Recent Product", "Save Recent Product count", "save-recent-record API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Save Recent Product", "Save Recent Product response", "save-recent-record API response time", timeTaken);
            });
        
        
        return productDetailBean;
    }
    
    @RequestMapping(value = "/get-like-unlike", method = RequestMethod.POST) // product details by product id.
    public LikeDto getLikeUnlike(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
    	long startTime = System.currentTimeMillis();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        LikeDto likeDto = new LikeDto();
        if (users != null) {
        	likeDto = productService.getLikeUnlike(requistBean.getProductId(), users.getId());
        } else {
            response.setStatus(401);
            likeDto.setStatusCode("401");
            likeDto.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Get Like Unlike", "Get Like Unlike count", "get-like-unlike API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Get Like Unlike", "Get Like Unlike response", "get-like-unlike API response time", timeTaken);
            });
        
        
        return likeDto;
    }
    	//</editor-fold>
    	

    //<editor-fold defaultstate="collapsed" desc="Cart Module">
    @RequestMapping(value = "/add-to-cart", method = RequestMethod.POST)    // add product to cart by user.
    public StatusBean addProductToCart(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        long startTime = System.currentTimeMillis();
        StatusBean statusBean = new StatusBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            requistBean.setUserId(users.getId());
            statusBean.setStatus(productService.addProductToCart(requistBean));
        } else {
            response.setStatus(401);
            statusBean.setStatusCode("401");
            statusBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Add To Cart", "add to cart count", "add-to-cart API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Add To Cart", "add to cart response", "add-to-cart API response time", timeTaken);
            });
        
        
        return statusBean;
    }

    @RequestMapping(value = "/set-property-record", method = RequestMethod.POST)    // add product to cart by user.
    public StatusBean setPropertyRecord(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
    	 long startTime = System.currentTimeMillis();
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

         long endTime = System.currentTimeMillis();
         double timeTaken = (endTime - startTime) / 1000.0;
         CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
             return awsCloudWatchHelper.logCount("Set Property Record", "Set Property Record count", "set-property-record API hit counter");
             });
             CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
             return awsCloudWatchHelper.logTimeSecounds("Set Property Record", "Set Property Record response", "set-property-record API response time", timeTaken);
             });
         
         
         return statusBean;
    }
    @RequestMapping(value = "/cart", method = RequestMethod.POST)   // view all product of a user's cart.
    public CartBean getCart(HttpServletResponse response, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        CartBean cartBean = new CartBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            cartBean = productService.getCartForUser(users);
        } else {
            response.setStatus(401);
            cartBean.setStatusCode("401");
            cartBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Cart", "cart count", "cart API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Cart", "cart response", "cart API response time", timeTaken);
            });
        
        
        return cartBean;
    }

    @RequestMapping(value = "/get-cart-count", method = RequestMethod.POST) // number of item added in cart by user.
    public UserBean getCartCount(HttpServletResponse response, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
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

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Cart Count", "cart-count count", "get-cart-count API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Cart Count", "cart-count response", "get-cart-count API response time", timeTaken);
            });
        
        
        return userBean;
    }

    @RequestMapping(value = "/remove-from-cart", method = RequestMethod.POST)   // product remove from cart.
    public StatusBean removeProductFromCart(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        long startTime = System.currentTimeMillis();
        StatusBean statusBean = new StatusBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            requistBean.setUserId(users.getId());
            statusBean.setStatus(productService.removeProductFromCart(requistBean));
        } else {
            response.setStatus(401);
            statusBean.setStatusCode("401");
            statusBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Remove From Cart", "remove from cart count", "remove-from-cart API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Remove From Cart", "remove from cart response", "remove-from-cart API response time", timeTaken);
            });
        
        
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
        long startTime = System.currentTimeMillis();
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
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Checkout", "checkout count", "checkout API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Checkout", "checkout response", "checkout API response time", timeTaken);
            });
        
        
        return paymentBean;
    }

    @RequestMapping(value = "/check-payment-status", method = RequestMethod.POST)   // .
    public StatusBean checkPaymentStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductRequistBean requistBean) {
        long startTime = System.currentTimeMillis();
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

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Check Payment Status", "check payment status count", "check-payment-status API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Check Payment Status", "check payment status response", "check-payment-status API response time", timeTaken);
            });
        
        
        return statusBean;
    }

    @RequestMapping(value = "/get-order-details", method = RequestMethod.POST)   // List of order
    public OrderDetailsBean getOrderDetails(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        long startTime = System.currentTimeMillis();
        OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            orderDetailsBean = productService.getOrderDetails(users, requistBean.getStart(), requistBean.getLimit());
        } else {
            response.setStatus(401);
            orderDetailsBean.setStatusCode("401");
            orderDetailsBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Order Details", "order details count", "get-order-details API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Order Details", "order details response", "get-order-details API response time", timeTaken);
            });
        
        
        return orderDetailsBean;
    }

    @RequestMapping(value = "/cancel-order", method = RequestMethod.POST)   //cancel order
    public StatusBean cancelOrder(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        long startTime = System.currentTimeMillis();
        StatusBean statusBean = new StatusBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            productService.cancelOrder(requistBean.getOrderId(), users.getId());
            statusBean.setStatusCode("200");
            statusBean.setStatus("Success");
        } else {
            statusBean.setStatusCode("401");
            statusBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Cancel Order", "cancel order count", "cancel-order API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Cancel Order", "cancel order response", "cancel-order API response time", timeTaken);
            });
        
        
        return statusBean;
    }
    
    @RequestMapping(value = "/order/request_return", method = RequestMethod.POST)   //return order
    public StatusBean requestReturn(HttpServletResponse response, HttpServletRequest request, @RequestParam("orderId") String orderId,@RequestParam("returnText") String returnText, @RequestParam("authToken") String authToken, @RequestParam("file") MultipartFile file) {
        long startTime = System.currentTimeMillis();
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

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Return Order", "cancel order count", "order/request_return API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Return Order", "Return order response", "order/request_return API response time", timeTaken);
            });
        
        
        return statusBean;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Contact to admin">
    @RequestMapping(value = "/contact-us", method = RequestMethod.POST)   //send a mail to admin from user
    public StatusBean contactToAdmin(HttpServletRequest request, @RequestBody UserBean userBean) {
        long startTime = System.currentTimeMillis();
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

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Contact Us", "contact us count", "contact-us API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Contact Us", "contact us response", "contact-us API response time", timeTaken);
            });
        
        
        return status;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Free product">
    @RequestMapping(value = "/get-free-product", method = RequestMethod.POST)
    public ProductBeans getFreeProduct(HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
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

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Free Product", "get free product count", "get-free-product API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Free Product", "get free product response", "get-free-product API response time", timeTaken);
            });
        
        
        return productBeans;
    }

    @RequestMapping(value = "/accept-free-product-offer", method = RequestMethod.POST)
    public CartBean acceptFreeProduct(HttpServletRequest request, @RequestBody CartBean cartBean) {
        long startTime = System.currentTimeMillis();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null && cartBean != null && cartBean.getUserId() != null) {
            productService.acceptFreeProduct(users, cartBean); //cartBean.getUserId() = product_size_color_map id
//            cartBean.setStatusCode("200");
        } else {
            cartBean.setStatusCode("401");
            cartBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Accept Free Product", "accept free product offer count", "accept-free-product-offer API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Accept Free Product", "accept free product offer response", "accept-free-product-offer API response time", timeTaken);
            });

        
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
        long startTime = System.currentTimeMillis();
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
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Save Review", "Save Review count", "save-review API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Save Review", "Save Review response", "save-review API response time", timeTaken);
            });
        
        
        return reviewBean;
    }
    
    
    @RequestMapping(value = "/likeUlike", method = RequestMethod.POST)
    public LikeUnlikeProductBean updateLikeUnlike(HttpServletResponse response, HttpServletRequest request, @RequestBody LikeUnlikeProductDto likeUnlikeProductDto) {
    	long startTime = System.currentTimeMillis();
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
    	long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logCount("Like", "Like count", "likeUlike API hit counter");
            });
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            return awsCloudWatchHelper.logTimeSecounds("Like", "Like response", "likeUlike API response time", timeTaken);
            });
        
        
    	return likeUnlikeProductBean;
    }
}
