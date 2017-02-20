/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.interceptor;

import com.weavers.duqhan.business.AouthService;
import com.weavers.duqhan.domain.Users;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author weaversAndroid
 */
public class UserInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    AouthService aouthService;

    private final Logger logger = Logger.getLogger(UserInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Users users = aouthService.getUserByToken(request.getHeader("X-Auth-Token"));   // Check whether Auth-Token is valid, provided by user
        String requesturl = request.getRequestURL().toString();
        if (users != null) {
            logger.info("\n(==I==)\n======================================USER ACTIVITY========================================\nUser Id = " + users.getId() + "\nRequist = " + requesturl + "\nTime = " + new Date());
        }
        return true;
    }
}
