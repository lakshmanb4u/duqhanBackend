/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.interceptor;

import com.weavers.duqhan.business.AouthService;
import com.weavers.duqhan.dao.UserActivityDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.domain.UserActivity;
import com.weavers.duqhan.domain.Users;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import com.weavers.duqhan.util.AwsCloudWatchHelper;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author weaversAndroid
 */
public class UserInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    AouthService aouthService;
    @Autowired
    UserActivityDao userActivityDao;
    @Autowired
    UsersDao usersDao;

    private final Logger logger = Logger.getLogger(UserInterceptor.class);
    private AwsCloudWatchHelper awsCloudWatchHelper = new AwsCloudWatchHelper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	long startTime = System.currentTimeMillis();
    	request.setAttribute("startTime", startTime);
    	Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        String requesturl = request.getRequestURL().toString();
        if (users != null) {
            Users lastLogin = usersDao.getLastLoginOfUser(users.getId());
            UserActivity activity = new UserActivity();
            activity.setId(null);
            activity.setUserId(users.getId());
            activity.setEmail(users.getEmail());
            activity.setActivity(requesturl);
            activity.setActivityTime(new Date());
            activity.setLatitude(lastLogin.getLatitude());
            activity.setLongitude(lastLogin.getLongitude());
            activity.setUserAgent(lastLogin.getUserAgent());
            userActivityDao.save(activity);
//            logger.info("\n(==I==)\n======================================USER ACTIVITY========================================\nUser Id = " + users.getId() + "\nRequist = " + requesturl + "\nTime = " + new Date());
        }
        return true;
    }
    
    @Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
    		String requesturl = request.getRequestURL().toString();
    		long startTime=(long)request.getAttribute("startTime");
	    	long endTime = System.currentTimeMillis();
	        double executionTime = (endTime - startTime) / 1000.0;
	        String uri = request.getRequestURI();
	        String ctx = request.getContextPath();
	        String base = uri.substring(ctx.length());
	        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
	            return awsCloudWatchHelper.logCount(base, base+"count", base+"API hit counter");
	            });
	            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
	            return awsCloudWatchHelper.logTimeSecounds(base, base+"response", base+"API response time", executionTime);
	            });
    		System.out.println("time required for the api"+executionTime);
	}
}
