/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.interceptor;

import com.weavers.duqhan.domain.DuqhanAdmin;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author weaversAndroid
 */
public class AdminInterceptor extends HandlerInterceptorAdapter  {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
        DuqhanAdmin admin = (DuqhanAdmin) session.getAttribute("admin");
        request.getSession();
        /*if (user != null) {
            response.sendRedirect("/");
            return false;
        } else if (user == null && admin == null) {
            response.sendRedirect("/adminlogin");
            return false;
        }*/
        if (admin == null) {
            response.sendRedirect("/web/adminlogin");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        if (!(modelAndView == null)) {
            modelAndView.addObject("admin", session.getAttribute("admin"));
        }
    }
}
