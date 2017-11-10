/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.controller;

import com.easypost.model.Shipment;
import com.weavers.duqhan.business.AouthService;
import com.weavers.duqhan.business.PaymentService;
import com.weavers.duqhan.business.ProductService;
import com.weavers.duqhan.business.ShippingService;
import com.weavers.duqhan.business.UsersService;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.AddressBean;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.dto.OrderDetailsBean;
import com.weavers.duqhan.dto.CheckoutPaymentBean;
import com.weavers.duqhan.dto.ProductBeans;
import com.weavers.duqhan.dto.ProductDetailBean;
import com.weavers.duqhan.dto.ProductRequistBean;
import com.weavers.duqhan.dto.ReviewBean;
import com.weavers.duqhan.dto.ReviewDto;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.dto.UserBean;
//import com.weavers.duqhan.util.AwsCloudWatchHelper;
import com.weavers.duqhan.util.DateFormater;
import com.weavers.duqhan.util.StatusConstants;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    

    private final Logger logger = Logger.getLogger(UserController.class);
//</editor-fold>

    //private AwsCloudWatchHelper awsCloudWatchHelper = new AwsCloudWatchHelper();
    //<editor-fold defaultstate="collapsed" desc="User Profile Module">
    @RequestMapping(value = "/logout", method = RequestMethod.POST) //logout, destroy auth token.
     public StatusBean logOut(HttpServletRequest request, @RequestBody LoginBean loginBean) {
        StatusBean statusBean = new StatusBean();
        loginBean.setAuthtoken(request.getHeader("X-Auth-Token"));  // Check whether Auth-Token is valid, provided by user
        statusBean.setStatus(usersService.userLogout(loginBean));
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
            userBean.setStatusCode("200");
            userBean.setStatus("Success..");
        } else {
            response.setStatus(401);
            userBean.setStatusCode("401");
            userBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        //awsCloudWatchHelper.logCount("Profile Details", "profile details count", "get-profile-details API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Profile Details", "profile details response", "get-profile-details API response time", timeTaken);
        return userBean;
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
        //awsCloudWatchHelper.logCount("Update Profile", "update profile count", "update-profile-details API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Update Profile", "update profile response", "update-profile-details API response time", timeTaken);
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
    @RequestMapping(value = "/update-profile-image", method = RequestMethod.POST)   // Update profile image.
    public UserBean updateProfileImage(HttpServletResponse response, @RequestParam MultipartFile file, @RequestParam Long userId) {
        long startTime = System.currentTimeMillis();
        UserBean userBean1 = new UserBean();
        Users users = usersService.getUserById(userId);   // Check whether Auth-Token is valid, provided by user
        if (users != null && file != null) {
            userBean1.setProfileImg(usersService.updateUserProfileImage(users, file));
        } else {
            response.setStatus(401);
            userBean1.setStatusCode("401");
            userBean1.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        //awsCloudWatchHelper.logCount("Update Profile Image", "update profile image count", "update-profile-image API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Update Profile Image", "update profile image response", "update-profile-image API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Change Password", "change password count", "change-password API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Change Password", "change password response", "change-password API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Addresses", "get addresses count", "get-addresses API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Addresses", "get addresses response", "get-addresses API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Save Address", "save address count", "save-address API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Save Address", "save address response", "save-address API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Set Default Addresses", "set default addresses count", "set-default-addresses API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Set Default Addresses", "set default addresses response", "set-default-addresses API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Default Addresses", "get default addresses count", "get-default-addresses API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Default Addresses", "get default addresses response", "get-default-addresses API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Deactivate Address", "deactivate address count", "deactivate-address API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Deactivate Address", "deactivate address response", "deactivate-address API response time", timeTaken);
        return addressBean;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Find Product">
    @RequestMapping(value = "/get-product", method = RequestMethod.POST)    // get latest product, get recent view product by user, get product by category id
    public ProductBeans getProduct(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        long startTime = System.currentTimeMillis();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductBeans productBeans = new ProductBeans();
        if (users != null) {
            Long categoryId = null;
            Boolean isRecent = null;
            categoryId = requistBean.getCategoryId();
            isRecent = requistBean.getIsRecent();
            if (isRecent == null) {
                isRecent = Boolean.FALSE;
            }

            if (categoryId != null && !isRecent) {
                //**********by category id***************//
                if (categoryId.equals(1l)) {
                    categoryId = 12l;
                }
                productBeans = productService.getProductsByCategory(categoryId, requistBean.getStart(), requistBean.getLimit());
            } else if (categoryId == null && isRecent) {
                //**********recent viewed****************//
                productBeans = productService.getProductsByRecentView(users.getId(), requistBean.getStart(), requistBean.getLimit());
            } else if (categoryId == null && !isRecent) {
                //******************all******************//
                productBeans = productService.getAllProducts(requistBean.getStart(), requistBean.getLimit());
            }
        } else {
            response.setStatus(401);
            productBeans.setStatusCode("401");
            productBeans.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        //awsCloudWatchHelper.logCount("Get Product", "get product count", "get-product API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Get Product", "get product response", "get-product API response time", timeTaken);
        return productBeans;
    }

    @RequestMapping(value = "/search-product", method = RequestMethod.POST)    // search product by product name
    public ProductBeans searchProduct(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        long startTime = System.currentTimeMillis();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductBeans productBeans = new ProductBeans();
        if (users != null) {
            requistBean.setUserId(users.getId());
            productBeans = productService.searchProducts(requistBean);
        } else {
            response.setStatus(401);
            productBeans.setStatusCode("401");
            productBeans.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        //awsCloudWatchHelper.logCount("Search Product", "search product count", "search-product API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Search Product", "search product response", "search-product API response time", timeTaken);
        return productBeans;
    }

    @RequestMapping(value = "/get-product-detail", method = RequestMethod.POST) // product details by product id.
    public ProductDetailBean getProductDettails(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        long startTime = System.currentTimeMillis();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductDetailBean productDetailBean = new ProductDetailBean();
        if (users != null) {
            productDetailBean = productService.getProductDetailsById(requistBean.getProductId(), users.getId());
        } else {
            response.setStatus(401);
            productDetailBean.setStatusCode("401");
            productDetailBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        //awsCloudWatchHelper.logCount("Product Detail", "product detail count", "get-product-detail API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Product Detail", "product detail response", "get-product-detail API response time", timeTaken);
        return productDetailBean;
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
        //awsCloudWatchHelper.logCount("Add To Cart", "add to cart count", "add-to-cart API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Add To Cart", "add to cart response", "add-to-cart API response time", timeTaken);
        return statusBean;
    }

    @RequestMapping(value = "/cart", method = RequestMethod.POST)   // view all product of a user's cart.
    public CartBean getCart(HttpServletResponse response, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        CartBean cartBean = new CartBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            cartBean = productService.getCartForUser(users.getId());
        } else {
            response.setStatus(401);
            cartBean.setStatusCode("401");
            cartBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        //awsCloudWatchHelper.logCount("Cart", "cart count", "cart API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Cart", "cart response", "cart API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Cart Count", "cart-count count", "get-cart-count API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Cart Count", "cart-count response", "get-cart-count API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Remove From Cart", "remove from cart count", "remove-from-cart API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Remove From Cart", "remove from cart response", "remove-from-cart API response time", timeTaken);
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
                                paymentBean = paymentService.transactionRequest(request, response, cartBean, shippingCost, shipments);
                            } else if (cartBean.getPaymentGateway() == StatusConstants.PAYTM_GATEWAY) {
                                String url = request.getRequestURL().toString();
                                String uri = request.getRequestURI();
                                String ctx = request.getContextPath();
                                String base = url.substring(0, url.length() - uri.length()) + ctx;
                                paymentBean = paymentService.transactionRequest(users, cartBean, shippingCost, shipments, base);
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
                        paymentBean = paymentService.transactionRequest(request, response, cartBean, shippingCost, shipments);
                    } else if (cartBean.getPaymentGateway() == StatusConstants.PAYTM_GATEWAY) {
                        String url = request.getRequestURL().toString();
                        String uri = request.getRequestURI();
                        String ctx = request.getContextPath();
                        String base = url.substring(0, url.length() - uri.length()) + ctx;
                        paymentBean = paymentService.transactionRequest(users, cartBean, shippingCost, shipments, base);
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

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        //awsCloudWatchHelper.logCount("Checkout", "checkout count", "checkout API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Checkout", "checkout response", "checkout API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Check Payment Status", "check payment status count", "check-payment-status API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Check Payment Status", "check payment status response", "check-payment-status API response time", timeTaken);
        return statusBean;
    }

    @RequestMapping(value = "/get-order-details", method = RequestMethod.POST)   // List of order
    public OrderDetailsBean getOrderDetails(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        long startTime = System.currentTimeMillis();
        OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            orderDetailsBean = productService.getOrderDetails(users.getId(), requistBean.getStart(), requistBean.getLimit());
        } else {
            response.setStatus(401);
            orderDetailsBean.setStatusCode("401");
            orderDetailsBean.setStatus("Invalid Token.");
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        //awsCloudWatchHelper.logCount("Order Details", "order details count", "get-order-details API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Order Details", "order details response", "get-order-details API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Cancel Order", "cancel order count", "cancel-order API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Cancel Order", "cancel order response", "cancel-order API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Contact Us", "contact us count", "contact-us API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Contact Us", "contact us response", "contact-us API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Free Product", "get free product count", "get-free-product API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Free Product", "get free product response", "get-free-product API response time", timeTaken);
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
        //awsCloudWatchHelper.logCount("Accept Free Product", "accept free product offer count", "accept-free-product-offer API hit counter");
        //awsCloudWatchHelper.logTimeSecounds("Accept Free Product", "accept free product offer response", "accept-free-product-offer API response time", timeTaken);
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
        return reviewBean;
    }
}
