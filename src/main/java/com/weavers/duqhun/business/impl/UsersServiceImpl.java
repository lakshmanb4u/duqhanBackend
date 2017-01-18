/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.business.impl;

import com.weavers.duqhun.business.AouthService;
import com.weavers.duqhun.business.UsersService;
import com.weavers.duqhun.dao.OtpTableDao;
import com.weavers.duqhun.dao.UsersDao;
import com.weavers.duqhun.domain.OtpTable;
import com.weavers.duqhun.domain.Users;
import com.weavers.duqhun.dto.AouthBean;
import com.weavers.duqhun.dto.LoginBean;
import com.weavers.duqhun.dto.UserBean;
import com.weavers.duqhun.util.Crypting;
import com.weavers.duqhun.util.DateFormater;
import com.weavers.duqhun.util.MailSender;
import com.weavers.duqhun.util.RandomCodeGenerator;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersDao usersDao;

    @Autowired
    OtpTableDao otpTableDao;

    @Autowired
    AouthService aouthService;

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
        return userses;
    }

    @Override
    public UserBean userRegistration(LoginBean loginBean) {
        Users user = usersDao.loadByEmail(loginBean.getEmail());
        UserBean userBean = new UserBean();
        if (user != null) {
            userBean.setStatusCode("403");
            userBean.setStatus("A user has already signed up with the supplied email");
        } else {
            String pass = Crypting.encrypt(loginBean.getPassword());    // password encription
            Users user2 = new Users();
            user2.setId(null);
            user2.setName(loginBean.getName());
            user2.setPassword(pass);
            user2.setEmail(loginBean.getEmail());
            user2.setRegDate(new Date());
            Users saveUser = usersDao.save(user2);  // new user registration.
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
    public UserBean fbUserLogin(LoginBean loginBean) {
        Users user = usersDao.loadByEmail(loginBean.getEmail());
        UserBean userBean = new UserBean();
        if (user != null) { // if user already exist
            userBean.setName(user.getName());
            userBean.setEmail(user.getEmail());
            userBean.setStatusCode("200");
            userBean.setStatus("Success");
            user.setLastloginDate(new Date());
            user.setFbid(loginBean.getFbid());
            Users user2 = usersDao.save(user);
            AouthBean aouthBean = aouthService.generatAccessToken(user2.getEmail(), user2.getId());
            userBean.setAuthtoken(aouthBean.getAouthToken());
        } else { // if user not exist
            Users user2 = new Users();
            Date newDate = new Date();
            user2.setId(null);
            user2.setName(loginBean.getName());
            user2.setEmail(loginBean.getEmail());
            user2.setRegDate(newDate);
            user2.setLastloginDate(newDate);
            user2.setFbid(loginBean.getFbid());
            Users saveUser = usersDao.save(user2);  // new registration
            if (saveUser != null) {
                AouthBean aouthBean = aouthService.generatAccessToken(saveUser.getEmail(), saveUser.getId());   // generate token
                userBean.setAuthtoken(aouthBean.getAouthToken());
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
    public UserBean userLogin(LoginBean loginBean) {
        String pass = Crypting.encrypt(loginBean.getPassword());
        Users user = usersDao.loadByEmailAndPass(loginBean.getEmail(), pass);
        UserBean userBean = new UserBean();
        if (user != null) {
            userBean.setName(user.getName());
            userBean.setEmail(user.getEmail());
            userBean.setStatusCode("200");
            userBean.setStatus("Success");
            user.setLastloginDate(new Date());
            Users user2 = usersDao.save(user);
            AouthBean aouthBean = aouthService.generatAccessToken(user2.getEmail(), user2.getId()); // generate token
            userBean.setAuthtoken(aouthBean.getAouthToken());
        } else {
            userBean.setStatusCode("403");
            userBean.setStatus("Wrong email or password");
        }
        return userBean;
    }

    @Override
    public String userLogout(LoginBean loginBean) {
        String status;
        status = aouthService.invalidatedToken(loginBean.getEmail(), loginBean.getAuthtoken()); // token invalidated
        return status;
    }

    @Override
    public UserBean passwordResetRequest(String email) {
        Users user = usersDao.loadByEmail(email);
        UserBean userBean = new UserBean();
        if (user != null) {
            String otp = RandomCodeGenerator.getNumericCode(6); // generate OTP
            String status = MailSender.sendEmail(user.getEmail(), "OTP For Password Resset", "Your OTP is:  " + otp, "");// send mail to user with otp.
            if (status.equals("success")) { // if mail send...
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
            OtpTable otpTable = otpTableDao.getValidOtp(user.getId(), loginBean.getEmail(), loginBean.getResetCode());
            if (otpTable != null) { // check OTP
                String pass = Crypting.encrypt(loginBean.getNewPassword());
                user.setPassword(pass);
                usersDao.save(user);
                userBean.setStatusCode("200");
                userBean.setStatus("Password resetted successfully");
                otpTableDao.delete(otpTable);
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

    @Override
    public UserBean updateUserProfile(Users user, UserBean userBean1) {
        UserBean userBean = new UserBean();
        userBean.setStatusCode("403");
        userBean.setStatus("Profile can not be update..");
        if (userBean1.getEmail() != null) { // whether user present with that email or not. 
            user.setDob(DateFormater.formateToDate(userBean1.getDob()));
            user.setEmail(userBean1.getEmail());
            user.setGender(userBean1.getGender());
            user.setMobile(userBean1.getMobile());
            user.setName(userBean1.getName());
            Users users = usersDao.save(user);
            if (users != null) {    // change email in auth table
                aouthService.updateEmailByUserId(users.getId(), users.getEmail());
                userBean.setDob(DateFormater.formate(users.getDob(), "dd/MM/yyyy"));
                userBean.setEmail(users.getEmail());
                userBean.setGender(users.getGender());
                userBean.setMobile(users.getMobile());
                userBean.setName(users.getName());
                userBean.setProfileImg(users.getProfileImg());
                userBean.setStatusCode("200");
                userBean.setStatus("Profile update successfully");
            }
        }
        return userBean;
    }

    @Override
    public UserBean updateUserProfileImage(Users user, UserBean userBean1) {
        UserBean userBean = new UserBean();
        userBean.setStatusCode("403");
        userBean.setStatus("Profile image can not be update..");
        user.setProfileImg(userBean1.getProfileImg());
        Users users = usersDao.save(user);  // update user profile image.
        if (users != null) {
            userBean.setProfileImg(users.getProfileImg());
            userBean.setStatusCode("200");
            userBean.setStatus("Profile image update successfully");
        }
        return userBean;
    }
}
