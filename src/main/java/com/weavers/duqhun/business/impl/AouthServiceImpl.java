/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.business.impl;

import com.weavers.duqhun.business.AouthService;
import com.weavers.duqhun.dao.UserAouthDao;
import com.weavers.duqhun.dao.UsersDao;
import com.weavers.duqhun.domain.UserAouth;
import com.weavers.duqhun.domain.Users;
import com.weavers.duqhun.dto.AouthBean;
import com.weavers.duqhun.util.RandomCodeGenerator;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Android-3
 */
public class AouthServiceImpl implements AouthService {

    @Autowired
    UserAouthDao userAouthDao;
    @Autowired
    UsersDao usersDao;

    private String createToken(Long userId) {
        String token = null;
        Calendar cal = Calendar.getInstance();
        if (userId != null) {
            token = RandomCodeGenerator.getNumericCode(4) + userId + cal.getTimeInMillis();
        }
        return token;
    }

    private Date nextUpdate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    @Override
    public AouthBean generatAccessToken(String email, Long userId) {
        AouthBean aouthBean = new AouthBean();
        UserAouth userAouth = userAouthDao.getTokenByMailAndUserId(email, userId);
        String token = this.createToken(userId);
        if (userAouth != null) {
            userAouth.setAouthToken(token);
            userAouth.setValidTill(nextUpdate());
            userAouthDao.save(userAouth);
        } else {
            UserAouth userAouth2 = new UserAouth();
            userAouth2.setId(null);
            userAouth2.setEmail(email);
            userAouth2.setUserId(userId);
            userAouth2.setAouthToken(token);
            userAouth2.setValidTill(nextUpdate());
            userAouthDao.save(userAouth2);
        }
        aouthBean.setAouthToken(token);
        return aouthBean;
    }

    @Override
    public String invalidatedToken(String email, String token) {
        String status = "failure";
        UserAouth userAouth = userAouthDao.getTokenByEmailAndToken(email, token);
        if (userAouth != null) {
            userAouthDao.delete(userAouth);
            status = "success";
        }
        return status;
    }

    @Override
    public Users getUserByToken(String token) {
        Long userId = userAouthDao.getUserIdByTokenIfValid(token);
        if (userId != null) {
            return usersDao.loadById(userId);
        } else {
            return null;
        }
    }

    @Override
    public void updateEmailByUserId(Long userId, String newEmail) {
        List<UserAouth> userAouths = userAouthDao.loadByUserId(userId);
        for (UserAouth userAouth : userAouths) {
            userAouth.setEmail(newEmail);
            userAouthDao.save(userAouth);
        }
    }

}
