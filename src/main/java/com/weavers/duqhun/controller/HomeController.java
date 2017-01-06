/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.controller;

import com.weavers.duqhun.business.UsersService;
import com.weavers.duqhun.dto.LoginBean;
import com.weavers.duqhun.dto.UserBean;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author clb14
 */
@CrossOrigin
@RestController
public class HomeController {

  @Autowired
  UsersService usersService;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home() {
    return "OK....";
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public UserBean login(HttpServletResponse response, @RequestBody LoginBean loginBean) {
    UserBean userBean = usersService.userLogin(loginBean);
    response.setStatus(Integer.valueOf(userBean.getStatusCode()));
    return userBean;
  }

  @RequestMapping(value = "/signup", method = RequestMethod.POST)
  public UserBean signup(HttpServletResponse response, @RequestBody LoginBean loginBean) {
    UserBean userBean = usersService.userRegistration(loginBean);
    response.setStatus(Integer.valueOf(userBean.getStatusCode()));
    return userBean;
  }

  @RequestMapping(value = "/request-password-reset", method = RequestMethod.POST)
  public UserBean passwordResetRequest(HttpServletResponse response, @RequestBody String email) {
    UserBean userBean = usersService.passwordResetRequest(email);
    response.setStatus(Integer.valueOf(userBean.getStatusCode()));
    return userBean;
  }

  @RequestMapping(value = "/confirm-password_reset", method = RequestMethod.POST)
  public UserBean passwordReset(HttpServletResponse response, @RequestBody LoginBean loginBean) {
    UserBean userBean = usersService.passwordReset(loginBean);
    response.setStatus(Integer.valueOf(userBean.getStatusCode()));
    return userBean;
  }
}
