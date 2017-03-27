/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.weavers.duqhan.business.AdminService;
import com.weavers.duqhan.dao.DuqhanAdminDao;
import com.weavers.duqhan.domain.DuqhanAdmin;
import com.weavers.duqhan.domain.UserAouth;
import com.weavers.duqhan.dto.AouthBean;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.util.RandomCodeGenerator;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author weaversAndroid
 */
public class AdminServiceImpl implements AdminService {

    @Autowired
    DuqhanAdminDao duqhanAdminDao;

    @Override
    public String adminLogin(LoginBean loginBean, HttpSession session) {
        DuqhanAdmin duqhanAdmin = duqhanAdminDao.getAdminByEmailAndPassword(loginBean.getEmail(), loginBean.getPassword());
        if (duqhanAdmin != null) {
            session.setAttribute("admin", duqhanAdmin);
            return "admin/add-product";
        } else {
            return "web/adminlogin";
        }
    }

    private String createToken(Long userId) {
        String token = null;
        Calendar cal = Calendar.getInstance();
        if (userId != null) {
            token = RandomCodeGenerator.getNumericCode(4) + userId + cal.getTimeInMillis(); // generate new token
        }
        return token;
    }

    private Date nextUpdate() {
        Calendar calendar = Calendar.getInstance(); // set token validity
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    @Override
    public AouthBean generatAccessToken(LoginBean loginBean) {
        AouthBean aouthBean = new AouthBean();
        DuqhanAdmin duqhanAdmin = duqhanAdminDao.getAdminByEmailAndPassword(loginBean.getEmail(), loginBean.getPassword());
        String token = "";
        if (duqhanAdmin != null) {    // for existing user update token
            token = this.createToken(duqhanAdmin.getId());
            duqhanAdmin.setAouthToken(token);
            duqhanAdmin.setValidTill(nextUpdate());
            duqhanAdminDao.save(duqhanAdmin);
        }/* else {                    // for new user create new token
            DuqhanAdmin duqhanAdmin1 = new DuqhanAdmin();
            duqhanAdmin1.setId(null);
            duqhanAdmin1.setEmail(email);
            duqhanAdmin1.setUserId(userId);
            duqhanAdmin1.setAouthToken(token);
            duqhanAdmin1.setValidTill(nextUpdate());
            duqhanAdminDao.save(duqhanAdmin1);
        }*/
        aouthBean.setAouthToken(token);
        return aouthBean;
    }

    @Override
    public String invalidatedToken(String email, String token) {
        String status = "failure";
        DuqhanAdmin duqhanAdmin = duqhanAdminDao.getTokenByEmailAndToken(email, token);
        if (duqhanAdmin != null) {
            duqhanAdmin.setAouthToken(null); // Token invalidated when user log out
            duqhanAdminDao.save(duqhanAdmin);
            status = "success";
        }
        return status;
    }

    @Override
    public DuqhanAdmin getUserByToken(String token) {
        DuqhanAdmin duqhanAdmin = duqhanAdminDao.getUserIdByTokenIfValid(token);  // Find user id by token.
        if (duqhanAdmin != null) {
            return duqhanAdmin;
        } else {
            return null;
        }
    }

}
