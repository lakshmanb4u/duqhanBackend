/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.business.impl;

import com.weavers.duqhun.business.UsersService;
import com.weavers.duqhun.dao.OtpTableDao;
import com.weavers.duqhun.dao.UsersDao;
import com.weavers.duqhun.domain.OtpTable;
import com.weavers.duqhun.domain.Users;
import com.weavers.duqhun.dto.LoginBean;
import com.weavers.duqhun.dto.UserBean;
import com.weavers.duqhun.util.Crypting;
import com.weavers.duqhun.util.RandomCodeGenerator;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersDao usersDao;

    @Autowired
    OtpTableDao otpTableDao;

    private UserBean setUserBean(Users users) {
        UserBean userBean = new UserBean();
        if (users != null) {
            userBean.setName(users.getName());
            userBean.setEmail(userBean.getEmail());
        }
        return userBean;
    }

    @Override
    public Users getAllUser() {
        Users userses = usersDao.loadById(1l);
        System.out.println("ddd" + userses.getEmail());
//List<Users> userses= null;
        return userses;
    }

    @Override
    public UserBean userLogin(LoginBean loginBean) {
        String pass = Crypting.encrypt(loginBean.getPassword());
        Users user = usersDao.loadByEmailAndPass(loginBean.getEmail(), pass);
        UserBean userBean = new UserBean();
        if (user != null) {
            userBean.setName(user.getName());
            userBean.setEmail(user.getEmail());
            userBean.setStatusCode("200");
            userBean.setStatus("Success");
        } else {
            userBean.setStatusCode("403");
            userBean.setStatus("Wrong email or password");
        }
        return userBean;
    }

    @Override
    public UserBean userRegistration(LoginBean loginBean) {
        Users user = usersDao.loadByEmail(loginBean.getEmail());
        UserBean userBean = new UserBean();
        if (user != null) {
            userBean.setStatusCode("403");
            userBean.setStatus("A user has already signed up with the supplied email");
        } else {
            String pass = Crypting.encrypt(loginBean.getPassword());
            Users user2 = new Users();
            user2.setId(null);
            user2.setName(loginBean.getName());
            user2.setPassword(pass);
            user2.setEmail(loginBean.getEmail());
            user2.setRegDate(new Date());
            Users saveUser = usersDao.save(user2);
            if (saveUser != null) {
                userBean.setName(user2.getName());
                userBean.setEmail(user2.getEmail());
                userBean.setStatusCode("200");
                userBean.setStatus("Success");
            } else {
                userBean.setStatusCode("500");
                userBean.setStatus("Server side exception");
            }
        }
        return userBean;
    }

    @Override
    public UserBean passwordResetRequest(String email) {
        Users user = usersDao.loadByEmail(email);
        UserBean userBean = new UserBean();
        if (user != null) {
            String otp = RandomCodeGenerator.getNumericCode(6);
            // send mail to user with otp.
            if (true) { // if mail send...
                OtpTable otpTable = new OtpTable();
                otpTable.setId(null);
                otpTable.setUserId(user.getId());
                otpTable.setUserMail(user.getEmail());
                otpTable.setOtp(otp);
                otpTable.setSendTime(new Date());
                otpTableDao.save(otpTable);
                userBean.setEmail(user.getEmail());
                userBean.setStatusCode("200");
                userBean.setStatus("A 6 digit password reset code has been sent to your email");
            } else {
                userBean.setStatusCode("403");
                userBean.setStatus("Could not able to send the 6 digit password reset code");
            }
        } else {
            userBean.setStatusCode("403");
            userBean.setStatus("No user has signed up with the supplied email");
        }
        return userBean;
    }

    @Override
    public UserBean passwordReset(LoginBean loginBean) {
        Users user = usersDao.loadByEmail(loginBean.getEmail());
        UserBean userBean = new UserBean();
        if (user != null) {
            boolean isValid = otpTableDao.isValidOtp(user.getId(), loginBean.getEmail());
            if (isValid) {
                user.setPassword(loginBean.getNewPassword());
                usersDao.save(user);
                userBean.setStatusCode("200");
                userBean.setStatus("Password resetted successfully");
            } else {
                userBean.setStatusCode("403");
                userBean.setStatus("Wrong password reset code or code expired");
            }
        } else {
            userBean.setStatusCode("500");
            userBean.setStatus("Server side exception");
        }
        return userBean;
    }
}
