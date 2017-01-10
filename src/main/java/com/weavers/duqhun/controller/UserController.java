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
import com.weavers.duqhun.dto.LoginBean;
import com.weavers.duqhun.dto.ProductBeans;
import com.weavers.duqhun.dto.ProductRequistBean;
import com.weavers.duqhun.dto.StatusBean;
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

    @RequestMapping(value = "/get-product", method = RequestMethod.POST)
    public ProductBeans getProduct(HttpServletResponse response, HttpServletRequest request, @RequestBody(required = false) ProductRequistBean requistBean) {
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));
        ProductBeans productBeans = new ProductBeans();
        Long categoryId = null;
        Boolean isRecent = null;
        if (requistBean != null) {
            categoryId = requistBean.getCategoryId();
            isRecent = requistBean.getIsRecent();
        }
        if (isRecent == null) {
            isRecent = Boolean.FALSE;
        }
        if (users != null) {
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
}
