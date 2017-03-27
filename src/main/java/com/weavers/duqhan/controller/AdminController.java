/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.controller;

import com.weavers.duqhan.business.AdminService;
import com.weavers.duqhan.business.NotificationService;
import com.weavers.duqhan.business.PaymentService;
import com.weavers.duqhan.business.ProductService;
import com.weavers.duqhan.business.ShippingService;
import com.weavers.duqhan.business.VendorService;
import com.weavers.duqhan.domain.DuqhanAdmin;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.CategoryDto;
import com.weavers.duqhan.dto.ColorAndSizeDto;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.dto.ProductBeans;
import com.weavers.duqhan.dto.ProductDetailBean;
import com.weavers.duqhan.dto.ProductRequistBean;
import com.weavers.duqhan.dto.SizeDto;
import com.weavers.duqhan.dto.SpecificationDto;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.util.StatusConstants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author weaversAndroid
 */
@CrossOrigin
@Controller
@RequestMapping("/admin/**")
public class AdminController {

    @Autowired
    ProductService productService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    VendorService vendorService;
    @Autowired
    ShippingService shippingService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/logout", method = RequestMethod.POST) //logout, destroy auth token.
    @ResponseBody
    public StatusBean logOut(HttpServletRequest request, @RequestBody LoginBean loginBean) {
        StatusBean statusBean = new StatusBean();
        loginBean.setAuthtoken(request.getHeader("X-Auth-Token"));  // Check whether Auth-Token is valid, provided by user
        statusBean.setStatus(adminService.invalidatedToken(loginBean.getEmail(), loginBean.getAuthtoken()));
        return statusBean;
    }

    /*@RequestMapping(value = "/add-product", method = RequestMethod.GET) // open main view page
    public String addProduct() {
        return "addProduct";
    }*/
//    @RequestMapping(value = "/add-product", method = RequestMethod.GET) // have to remove when admin palen is ready.
//    public String addProduct(ModelMap modelMap) {
//        ColorAndSizeDto colorAndSizeDto = productService.getColorSizeList();
//        modelMap.addAttribute("sizeAndColor", colorAndSizeDto);
//        return "addProduct";
//    }
    @RequestMapping(value = "/get-category", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getCategory(HttpServletResponse response, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ColorAndSizeDto colorAndSizeDto;
        if (admin != null) {
            colorAndSizeDto = productService.getCategories();
        } else {
            colorAndSizeDto = new ColorAndSizeDto();
            colorAndSizeDto.setStatusCode("401");
            colorAndSizeDto.setStatus("Invalid Token.");
        }
        return colorAndSizeDto;
    }

    @RequestMapping(value = "/get-size", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getSize(HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ColorAndSizeDto colorAndSizeDto;
        if (admin != null) {
            colorAndSizeDto = productService.getSizes();
        } else {
            colorAndSizeDto = new ColorAndSizeDto();
            colorAndSizeDto.setStatusCode("401");
            colorAndSizeDto.setStatus("Invalid Token.");
        }
        return colorAndSizeDto;
    }

    @RequestMapping(value = "/get-sizegroup", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getSizegroup(HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ColorAndSizeDto colorAndSizeDto;
        if (admin != null) {

            colorAndSizeDto = productService.getSizeGroupe();
        } else {
            colorAndSizeDto = new ColorAndSizeDto();
            colorAndSizeDto.setStatusCode("401");
            colorAndSizeDto.setStatus("Invalid Token.");
        }
        return colorAndSizeDto;
    }

    @RequestMapping(value = "/get-color", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getColor(HttpServletRequest request) {

        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ColorAndSizeDto colorAndSizeDto;
        if (admin != null) {
            colorAndSizeDto = productService.getColors();
        } else {
            colorAndSizeDto = new ColorAndSizeDto();
            colorAndSizeDto.setStatusCode("401");
            colorAndSizeDto.setStatus("Invalid Token.");
        }
        return colorAndSizeDto;
    }

    @RequestMapping(value = "/get-vendor", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getVendor(HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ColorAndSizeDto colorAndSizeDto;
        if (admin != null) {
            colorAndSizeDto = productService.getVendors();
        } else {
            colorAndSizeDto = new ColorAndSizeDto();
            colorAndSizeDto.setStatusCode("401");
            colorAndSizeDto.setStatus("Invalid Token.");
        }
        return colorAndSizeDto;
    }

    @RequestMapping(value = "/get-specifications", method = RequestMethod.GET) // have to remove when admin palen is ready.
    @ResponseBody
    public ColorAndSizeDto getSpecifications(@RequestParam Long categoryId, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ColorAndSizeDto colorAndSizeDto;
        if (admin != null) {
            colorAndSizeDto = productService.getSpecificationsByCategoryId(categoryId);
        } else {
            colorAndSizeDto = new ColorAndSizeDto();
            colorAndSizeDto.setStatusCode("401");
            colorAndSizeDto.setStatus("Invalid Token.");
        }
        return colorAndSizeDto;
    }

    @RequestMapping(value = "/save-product", method = RequestMethod.POST)   // save a new product.
    @ResponseBody
    public StatusBean addingProduct(@RequestBody ProductBean productBean, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        StatusBean response = new StatusBean();
        if (admin != null) {
            response.setStatus(productService.saveProduct(productBean));
        } else {
            response.setStatusCode("401");
            response.setStatus("Invalid Token.");
        }
        return response;
    }

    @RequestMapping(value = "/save-category", method = RequestMethod.POST)  // save a new category
    @ResponseBody
    public StatusBean saveCategory(@RequestBody CategoryDto categoryDto, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        StatusBean response = new StatusBean();
        if (admin != null) {
            response.setStatus(productService.saveCategory(categoryDto));
        } else {
            response.setStatusCode("401");
            response.setStatus("Invalid Token.");
        }
        return response;
    }

    @RequestMapping(value = "/save-size", method = RequestMethod.POST)  // save new size
    @ResponseBody
    public StatusBean saveSize(@RequestBody SizeDto sizeDto, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        StatusBean response = new StatusBean();
        if (admin != null) {
            response.setStatus(productService.saveSize(sizeDto));
        } else {
            response.setStatusCode("401");
            response.setStatus("Invalid Token.");
        }
        return response;
    }

    @RequestMapping(value = "/save-sizegroup", method = RequestMethod.POST) // save new sizegroup
    @ResponseBody
    public StatusBean saveSizeGroup(@RequestBody ProductRequistBean requistBean, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        StatusBean response = new StatusBean();
        if (admin != null) {
            response.setStatus(productService.saveSizeGroup(requistBean.getName()));
        } else {
            response.setStatusCode("401");
            response.setStatus("Invalid Token.");
        }
        return response;
    }

    @RequestMapping(value = "/save-color", method = RequestMethod.POST) // save new color
    @ResponseBody
    public StatusBean saveColor(@RequestBody ProductRequistBean requistBean, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        StatusBean response = new StatusBean();
        if (admin != null) {
            response.setStatus(productService.saveColor(requistBean.getName()));
        } else {
            response.setStatusCode("401");
            response.setStatus("Invalid Token.");
        }
        return response;
    }

    @RequestMapping(value = "/save-product-image", method = RequestMethod.POST)   // save product image.
    @ResponseBody
    public StatusBean addingProductImage(@RequestBody ProductBean productBean, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        StatusBean response = new StatusBean();
        if (admin != null) {
            response.setStatus("failure");
            response.setStatus(productService.saveProductImage(productBean));
        } else {
            response.setStatusCode("401");
            response.setStatus("Invalid Token.");
        }
        return response;
    }

    @RequestMapping(value = "/save-vendor", method = RequestMethod.POST)  // save a new category
    @ResponseBody
    public StatusBean saveVendor(@RequestBody AddressDto addressDto, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        StatusBean response = new StatusBean();
        if (admin != null) {
            if (StatusConstants.IS_SHIPMENT) {
                StatusBean statusBean = shippingService.verifyAddress(addressDto);
                if (statusBean.getStatusCode().equals("200")) {
                    response.setStatus(vendorService.saveVendor(addressDto));
                } else {
                    response.setStatusCode(statusBean.getStatusCode());
                    response.setStatus(statusBean.getStatus());
                }
            } else {
                response.setStatus(vendorService.saveVendor(addressDto));
            }
        } else {
            response.setStatusCode("401");
            response.setStatus("Invalid Token.");
        }
        return response;
    }

    @RequestMapping(value = "/save-specification", method = RequestMethod.POST)  // save a new Specification
    @ResponseBody
    public StatusBean saveSpecification(@RequestBody SpecificationDto specificationDto, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        StatusBean response = new StatusBean();
        if (admin != null) {
            response.setStatus(productService.saveSpecification(specificationDto));
        } else {
            response.setStatusCode("401");
            response.setStatus("Invalid Token.");
        }
        return response;
    }

    @RequestMapping(value = "/save-specification-value", method = RequestMethod.POST)  // save a new Specification values
    @ResponseBody
    public StatusBean saveSpecificationValue(@RequestBody SpecificationDto specificationDto, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        StatusBean response = new StatusBean();
        if (admin != null) {
            response.setStatus(productService.saveSpecificationValue(specificationDto));
        } else {
            response.setStatusCode("401");
            response.setStatus("Invalid Token.");
        }
        return response;
    }

    @RequestMapping(value = "/get-product-lise", method = RequestMethod.POST)  // 
    @ResponseBody
    public ProductBeans getProductList(@RequestBody ProductRequistBean requistBean, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductBeans productBeans;
        if (admin != null) {
            productBeans = productService.getAllProducts(requistBean.getStart(), requistBean.getLimit());
        } else {
            productBeans = new ProductBeans();
            productBeans.setStatusCode("401");
            productBeans.setStatus("Invalid Token.");
        }
        return productBeans;
    }

    @RequestMapping(value = "/update-product-details", method = RequestMethod.POST)  // 
    @ResponseBody
    public StatusBean updateProduct(@RequestBody ProductBean productBean, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        StatusBean response = new StatusBean();
        if (admin != null) {
            if (productBean != null) {
                response.setStatus(productService.updateProduct(productBean));
            }
        } else {
            response.setStatusCode("401");
            response.setStatus("Invalid Token.");
        }
        return response;
    }

    @RequestMapping(value = "/get-product-inventory", method = RequestMethod.POST)  // 
    @ResponseBody
    public ProductBean getProductInventory(@RequestBody ProductRequistBean requistBean, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductBean productBean;
        if (admin != null) {
            productBean = productService.getProductInventoryById(requistBean.getProductId());
        } else {
            productBean = new ProductBean();
            productBean.setStatusCode("401");
            productBean.setStatus("Invalid Token.");
        }
        return productBean;
    }

    @RequestMapping(value = "/update-product-inventory", method = RequestMethod.POST)  // 
    @ResponseBody
    public StatusBean updateProductInventory(@RequestBody ProductBean productBean, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        StatusBean response = new StatusBean();
        if (admin != null) {
            response.setStatus(productService.updateProductInventory(productBean));
        } else {
            response.setStatusCode("401");
            response.setStatus("Invalid Token.");
        }
        return response;
    }

    @RequestMapping(value = "/get-specifications", method = RequestMethod.POST)  // 
    @ResponseBody
    public ProductDetailBean getProductSpecifications(@RequestBody ProductRequistBean requistBean, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        ProductDetailBean productDetailBean;
        if (admin != null) {
            productDetailBean = productService.getProductSpecifications(requistBean.getCategoryId());
        } else {
            productDetailBean = new ProductDetailBean();
            productDetailBean.setStatusCode("401");
            productDetailBean.setStatus("Invalid Token.");
        }
        return productDetailBean;
    }

    @RequestMapping(value = "/get-specification-value", method = RequestMethod.POST)  // 
    @ResponseBody
    public SpecificationDto getProductSpecificationValue(@RequestBody ProductRequistBean requistBean, HttpServletRequest request) {
        DuqhanAdmin admin = adminService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        SpecificationDto specificationDto;
        if (admin != null) {
//        getOrderId()
            specificationDto = productService.getProductSpecificationValue(requistBean.getCategoryId());
        } else {
            specificationDto = new SpecificationDto();
            specificationDto.setStatusCode("401");
            specificationDto.setStatus("Invalid Token.");
        }
        return specificationDto;
    }
}
