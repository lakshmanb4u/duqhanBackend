/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.weavers.duqhan.business.AdminService;
import com.weavers.duqhan.dao.DuqhanAdminDao;
import com.weavers.duqhan.domain.DuqhanAdmin;
import com.weavers.duqhan.dto.LoginBean;
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

}
