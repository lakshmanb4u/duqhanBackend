/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.controller;

import com.weavers.duqhun.business.AouthService;
import com.weavers.duqhun.business.ProductService;
import com.weavers.duqhun.business.UsersService;
import com.weavers.duqhun.domain.Users;
import com.weavers.duqhun.dto.CartBean;
import com.weavers.duqhun.dto.LoginBean;
import com.weavers.duqhun.dto.ProductBeans;
import com.weavers.duqhun.dto.ProductDetailBean;
import com.weavers.duqhun.dto.ProductRequistBean;
import com.weavers.duqhun.dto.StatusBean;
import com.weavers.duqhun.dto.UserBean;
import com.weavers.duqhun.util.DateFormater;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public StatusBean logOut(HttpServletRequest request, @RequestBody LoginBean loginBean) {
        StatusBean statusBean = new StatusBean();
        loginBean.setAuthtoken(request.getHeader("X-Auth-Token"));
        statusBean.setStatus(usersService.userLogout(loginBean));
        return statusBean;
    }

    @RequestMapping(value = "/get-profile-details", method = RequestMethod.POST)
    public UserBean getProfileDetails(HttpServletResponse response, HttpServletRequest request) {
        UserBean userBean = new UserBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
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

    @RequestMapping(value = "/update-profile-details", method = RequestMethod.POST)
    public UserBean updateProfileDetails(HttpServletResponse response, HttpServletRequest request, @RequestBody UserBean userBean) {
        UserBean userBean1 = new UserBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
        if (users != null) {
            userBean1 = usersService.updateUserProfile(users, userBean);
        } else {
            response.setStatus(401);
            userBean1.setStatusCode("401");
            userBean1.setStatus("Invalid Token.");
        }
        return userBean1;
    }
    
    @RequestMapping(value = "/update-profile-image", method = RequestMethod.POST)
    public UserBean updateProfileImage(HttpServletResponse response, HttpServletRequest request, @RequestBody UserBean userBean) {
        UserBean userBean1 = new UserBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
        if (users != null) {
            userBean1 = usersService.updateUserProfileImage(users, userBean);
        } else {
            response.setStatus(401);
            userBean1.setStatusCode("401");
            userBean1.setStatus("Invalid Token.");
        }
        return userBean1;
    }

    @RequestMapping(value = "/get-product", method = RequestMethod.POST)
    public ProductBeans getProduct(HttpServletResponse response, HttpServletRequest request, @RequestBody(required = false) ProductRequistBean requistBean) {
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
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
                //by category id
                productBeans = productService.getProductsByCategory(categoryId);
            } else if (categoryId == null && isRecent) {
                //recent viewed
                productBeans = productService.getProductsByRecentView(users.getId());
            } else if (categoryId == null && !isRecent) {
                //all
                productBeans = productService.getAllProducts();
            }
        } else {
            response.setStatus(401);
            productBeans.setStatusCode("401");
            productBeans.setStatus("Invalid Token.");
        }
        return productBeans;
    }

    @RequestMapping(value = "/get-product-detail", method = RequestMethod.POST)
    public ProductDetailBean getProductDettails(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
        ProductDetailBean productDetailBean = new ProductDetailBean();
        if (users != null) {
            productDetailBean = productService.getProductDetailsById(requistBean.getProductId());
        } else {
            response.setStatus(401);
            productDetailBean.setStatusCode("401");
            productDetailBean.setStatus("Invalid Token.");
        }
        return productDetailBean;
    }

    @RequestMapping(value = "/add-to-cart", method = RequestMethod.POST)
    public StatusBean getAddToCart(HttpServletResponse response, HttpServletRequest request, @RequestBody ProductRequistBean requistBean) {
        StatusBean statusBean = new StatusBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
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

    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public CartBean getCart(HttpServletResponse response, HttpServletRequest request) {
        CartBean cartBean = new CartBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
        if (users != null) {
            cartBean = productService.getCartFoAUser(users.getId());
        } else {
            response.setStatus(401);
            cartBean.setStatusCode("401");
            cartBean.setStatus("Invalid Token.");
        }
        return cartBean;
    }

    @RequestMapping(value = "/get-cart-count", method = RequestMethod.POST)
    public UserBean getCartCount(HttpServletResponse response, HttpServletRequest request) {
        UserBean userBean = new UserBean();
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
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
}
