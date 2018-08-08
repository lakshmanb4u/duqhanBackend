/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.controller;

import com.weavers.duqhan.business.MailService;
import com.weavers.duqhan.business.ProductService;
import com.weavers.duqhan.business.UsersService;
import com.weavers.duqhan.dto.CategorysBean;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.dto.ProductRequistBean;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.dto.UserBean;
import com.weavers.duqhan.util.AwsCloudWatchHelper;
import java.io.IOException;
import java.util.Currency;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
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
public class HomeController {

    @Autowired
    UsersService usersService;
    @Autowired
    ProductService productService;
    @Autowired
    MailService mailService;
   
    private AwsCloudWatchHelper awsCloudWatchHelper = new AwsCloudWatchHelper();
    private final Logger logger = Logger.getLogger(HomeController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(HttpServletResponse response) throws IOException {
        return "API APP DEPLOYED SUCCESSFULLY.....";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST) // User registration by email id.
    public UserBean signup(HttpServletResponse response, @RequestBody LoginBean loginBean) {
    	UserBean  userBean = new UserBean();
    	try {
        	userBean = usersService.userRegistration(loginBean);
            response.setStatus(Integer.valueOf(userBean.getStatusCode()));	
		} catch (Exception e) {
			this.logErrorOnAws("signup exception");
		}

       return userBean;
    }

    @RequestMapping(value = "/fb-login", method = RequestMethod.POST)   // login by FaceBook old user as well as new user. Auth-Token generate.
    public UserBean fbLogin(HttpServletResponse response, @RequestBody LoginBean loginBean) {
    	UserBean userBean = usersService.fbUserLogin(loginBean);
        response.setStatus(Integer.valueOf(userBean.getStatusCode()));
        return userBean;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)  // Log in by email only register user. Auth-Token generate.
    public UserBean login(HttpServletResponse response, @RequestBody LoginBean loginBean) {
//        UserBean userBean = usersService.userLogin(loginBean);
//        response.setStatus(Integer.valueOf(userBean.getStatusCode()));
    	long loginStartTime = System.currentTimeMillis();
        UserBean userBean = null;
        userBean = usersService.userLogin(loginBean,loginStartTime);
        response.setStatus(Integer.valueOf(userBean.getStatusCode()));
        return userBean;
    }
    @RequestMapping(value = "/guest-fcm-token", method = RequestMethod.POST)  // Log in by email only register user. Auth-Token generate.
    public void guestFcmToken(HttpServletResponse response, @RequestBody LoginBean loginBean) {
    	
        usersService.guestUserFcmToken(loginBean);
        
    }
    @RequestMapping(value = "/guest-login", method = RequestMethod.POST)  // Log in by email only register user. Auth-Token generate.
    public UserBean guestLogin(HttpServletResponse response, @RequestBody LoginBean loginBean) {
    	long loginStartTime = System.currentTimeMillis();
        UserBean userBean = null;
        userBean = usersService.guestUserLogin(loginBean,loginStartTime);
        response.setStatus(Integer.valueOf(userBean.getStatusCode()));
        return userBean;
    }

    @RequestMapping(value = "/request-password-reset", method = RequestMethod.POST) // Password reset request send a 6 digits OPT to user's register email 
    public UserBean passwordResetRequest(HttpServletResponse response, @RequestBody LoginBean loginBean) {
    	UserBean userBean = usersService.passwordResetRequest(loginBean.getEmail());
        System.out.println("loginBean.getEmail() = " + loginBean.getEmail());
        response.setStatus(Integer.valueOf(userBean.getStatusCode()));
        return userBean;
    }

    @RequestMapping(value = "/confirm-password_reset", method = RequestMethod.POST) // Password will change if user provide correct OPT which was send to their mail
    public UserBean passwordReset(HttpServletResponse response, @RequestBody LoginBean loginBean) {
    	UserBean userBean = usersService.passwordReset(loginBean);
        response.setStatus(Integer.valueOf(userBean.getStatusCode()));
        return userBean;
    }
    public StatusBean logErrorOnAws(String name) {
      	 CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
           	return awsCloudWatchHelper.logCount("Error"+name, "Error "+name+" count", name+" "+"API hit counter");
           });
           StatusBean statusBean = new StatusBean();
           statusBean.setStatus("success");
           mailService.errorLogToAdmint(name);
           return statusBean;
       }
   
    @RequestMapping(value = "/get-child-category", method = RequestMethod.POST) // get child category
    public CategorysBean getChildCategory(@RequestBody ProductRequistBean requistBean) {
    	CategorysBean categorysBean = new CategorysBean();
    	try {
    	    categorysBean = productService.getChildByIdAndActive(requistBean.getCategoryId());	
		} catch (Exception e) {
			this.logErrorOnAws("get child category exception");
		}
        
//        response.setStatus(Integer.valueOf(userBean.getStatusCode()));
        return categorysBean;
    }
    
    @RequestMapping(value = "/get-all-child-category", method = RequestMethod.POST) // get child category
    public CategorysBean getAllChildCategory(@RequestBody ProductRequistBean requistBean) {
    	CategorysBean categorysBean = new CategorysBean();
    	try {
    	 categorysBean = productService.getAllChildCategory(requistBean.getCategoryId());
    	} catch (Exception e) {
			this.logErrorOnAws("get all category exception");
		}
        return categorysBean;
    }
    
    @RequestMapping(value = "/get-child-category-byid", method = RequestMethod.POST) // get child category
    public CategorysBean getChildCategoryById(@RequestBody ProductRequistBean requistBean) {
    	CategorysBean categorysBean = new CategorysBean();
    	try {
    	 categorysBean = productService.getChildById(requistBean.getCategoryId());
    	} catch (Exception e) {
			this.logErrorOnAws("get child category byid exception");
		}	
//        response.setStatus(Integer.valueOf(userBean.getStatusCode()));
       return categorysBean;
    }
    /*
    @RequestMapping(value = "/test", method = RequestMethod.GET) // for test
    public String test() {
//        System.out.println("ddddd " + NotificationPusher.pushOnSingleDevice("title_new", "body_new", "ejZtGLpjmqk:APA91bFCfB6aW2SsZVEG_0vau7ZaTa507NjzDJ9qgx-5knIvETJkyE3pVVFPCd03Xy70mVotCzhkP5HV_cGSpPlwzyfAEAv6mDceVDvIopCQf_-jBSqM6EEJT-XR70u_5Vo2nP92cYXh").getResults().size());
        productService.test();
        return "OOKK";
    }

    @RequestMapping(value = "/test-order-details", method = RequestMethod.GET) // for test
    public List<Map<String, Object>> test1(@RequestParam("orderids") String... orderids) {
        return productService.getTestOrderDetails(Arrays.asList(orderids));
    }*/
}
