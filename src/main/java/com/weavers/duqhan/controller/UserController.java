/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.controller;

import com.weavers.duqhan.business.AouthService;
import com.weavers.duqhan.business.PaymentService;
import com.weavers.duqhan.business.ProductService;
import com.weavers.duqhan.business.UsersService;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.AddressBean;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.CartBean;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.dto.OrderDetailsBean;
import com.weavers.duqhan.dto.ProductBeans;
import com.weavers.duqhan.dto.ProductDetailBean;
import com.weavers.duqhan.dto.ProductRequistBean;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.dto.UserBean;
import com.weavers.duqhan.util.DateFormater;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Android-3
 */
@CrossOrigin
@RestController
@RequestMapping("/user/**")
public class UserController {

    @Autowired
    UsersService usersService;
    @Autowired
    ProductService productService;
    @Autowired
    AouthService aouthService;
    @Autowired
    PaymentService paymentService;

    @RequestMapping(value = "/logout", method = RequestMethod.POST) //logout, destroy auth token.
    public StatusBean logOut(HttpServletRequest request, @RequestBody LoginBean loginBean) {
        StatusBean statusBean = new StatusBean();
        loginBean.setAuthtoken(request.getHeader("X-Auth-Token"));  // Check whether Auth-Token is valid, provided by user
        statusBean.setStatus(usersService.userLogout(loginBean));
        return statusBean;
    }

    @RequestMapping(value = "/get-profile-details", method = RequestMethod.POST)    // viewe user's profile.
    public UserBean getProfileDetails(HttpServletResponse response, HttpServletRequest request) {
        UserBean userBean = new UserBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
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
        return userBean;
    }

    @RequestMapping(value = "/update-profile-details", method = RequestMethod.POST) // Update user profile.
    public UserBean updateProfileDetails(HttpServletResponse response, HttpServletRequest request, @RequestBody UserBean userBean) {
        UserBean userBean1 = new UserBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            userBean1 = usersService.updateUserProfile(users, userBean);
        } else {
            response.setStatus(401);
            userBean1.setStatusCode("401");
            userBean1.setStatus("Invalid Token.");
        }
        return userBean1;
    }

    @RequestMapping(value = "/update-profile-image", method = RequestMethod.POST)   // Update profile image.
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
    }

    @RequestMapping(value = "/get-product", method = RequestMethod.POST)    // get latest product, get recent view product by user, get product by category id
    public ProductBeans getProduct(HttpServletResponse response, HttpServletRequest request, @RequestBody(required = false) ProductRequistBean requistBean) {
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductBeans productBeans = new ProductBeans();
        if (users != null) {
            Long categoryId = null;
            Boolean isRecent = null;
            if (requistBean != null) {
                categoryId = requistBean.getCategoryId();
                isRecent = requistBean.getIsRecent();
            }
            if (isRecent == null) {
                isRecent = Boolean.FALSE;
            }

            if (categoryId != null && !isRecent) {
                //**********by category id***************//
                productBeans = productService.getProductsByCategory(categoryId);
            } else if (categoryId == null && isRecent) {
                //**********recent viewed****************//
                productBeans = productService.getProductsByRecentView(users.getId());
            } else if (categoryId == null && !isRecent) {
                //******************all******************//
                productBeans = productService.getAllProducts();
            }
        } else {
            response.setStatus(401);
            productBeans.setStatusCode("401");
            productBeans.setStatus("Invalid Token.");
        }
        return productBeans;
    }

    @RequestMapping(value = "/get-product-detail", method = RequestMethod.POST) // product details by product id.
    public ProductDetailBean getProductDettails(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductDetailBean productDetailBean = new ProductDetailBean();
        if (users != null) {
            productDetailBean = productService.getProductDetailsById(requistBean.getProductId(), users.getId());
        } else {
            response.setStatus(401);
            productDetailBean.setStatusCode("401");
            productDetailBean.setStatus("Invalid Token.");
        }
        return productDetailBean;
    }

    @RequestMapping(value = "/add-to-cart", method = RequestMethod.POST)    // add product to cart by user.
    public StatusBean addProductToCart(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
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
        return statusBean;
    }

    @RequestMapping(value = "/cart", method = RequestMethod.POST)   // view all product of a user's cart.
    public CartBean getCart(HttpServletResponse response, HttpServletRequest request) {
        CartBean cartBean = new CartBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            cartBean = productService.getCartFoAUser(users.getId());
        } else {
            response.setStatus(401);
            cartBean.setStatusCode("401");
            cartBean.setStatus("Invalid Token.");
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
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            requistBean.setUserId(users.getId());
            statusBean.setStatus(productService.removeProductFromCart(requistBean));
        } else {
            response.setStatus(401);
            statusBean.setStatusCode("401");
            statusBean.setStatus("Invalid Token.");
        }
        return statusBean;
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)   // .
    public StatusBean payPayPal(HttpServletRequest request, HttpServletResponse response, @RequestBody CartBean cartBean) {
        StatusBean statusBean = new StatusBean();
        String[] stringStr = new String[2];
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            cartBean.setUserId(users.getId());
            stringStr = paymentService.transactionRequest(request, response, cartBean);
            statusBean.setStatusCode(stringStr[1]); // pay key
            statusBean.setStatus(stringStr[0]); // Paypal url
        } else {
            response.setStatus(401);
            statusBean.setStatusCode("401");
            statusBean.setStatus("Invalid Token.");
        }
        return statusBean;
    }

    @RequestMapping(value = "/check-payment-status", method = RequestMethod.POST)   // .
    public StatusBean checkPaymentStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductRequistBean requistBean) {
        StatusBean statusBean = new StatusBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            requistBean.setUserId(users.getId());
            statusBean.setStatusCode("200");
            statusBean.setStatus(paymentService.getPaymentStatus(users.getId(), requistBean.getName()));
        } else {
            response.setStatus(401);
            statusBean.setStatusCode("401");
            statusBean.setStatus("Invalid Token.");
        }
        return statusBean;
    }

    @RequestMapping(value = "/get-addresses", method = RequestMethod.POST)   // get Addresses.
    public AddressBean getUserAddresses(HttpServletResponse response, HttpServletRequest request) {
        AddressBean addressBean = new AddressBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            addressBean = usersService.getUserActiveAddresses(users.getId());
        } else {
            response.setStatus(401);
            addressBean.setStatusCode("401");
            addressBean.setStatus("Invalid Token.");
        }
        return addressBean;
    }

    @RequestMapping(value = "/save-address", method = RequestMethod.POST)   // saveUserAddress
    public AddressBean saveOrUpdateUserAddress(HttpServletResponse response, HttpServletRequest request, @RequestBody AddressDto address) {
        AddressBean addressBean = new AddressBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            address.setUserId(users.getId());
            addressBean = usersService.saveUserAddress(address);
        } else {
            response.setStatus(401);
            addressBean.setStatusCode("401");
            addressBean.setStatus("Invalid Token.");
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

    @RequestMapping(value = "/get-order-details", method = RequestMethod.POST)   // deactivateAddress
    public OrderDetailsBean getOrderDetails(HttpServletResponse response, HttpServletRequest request) {
        OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        if (users != null) {
            orderDetailsBean = productService.getOrderDetails(users.getId());
        } else {
            response.setStatus(401);
            orderDetailsBean.setStatusCode("401");
            orderDetailsBean.setStatus("Invalid Token.");
        }
        return orderDetailsBean;
    }
}
