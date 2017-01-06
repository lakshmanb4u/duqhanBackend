/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.UserAouthDao;
import com.weavers.duqhun.domain.UserAouth;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class UserAouthDaoJpa extends BaseDaoJpa<UserAouth> implements UserAouthDao {

    public UserAouthDaoJpa() {
        super(UserAouth.class, "UserAouth");
    }

    @Override
    public UserAouth getTokenByMail(String email) {
        try {
            Query query = getEntityManager().createQuery("SELECT ut FROM UserAouth ut WHERE ut.email=:email");
            query.setParameter("email", email);
            return (UserAouth) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public UserAouth getTokenByMailAndUserId(String email, Long userId) {
        try {
            Query query = getEntityManager().createQuery("SELECT ut FROM UserAouth ut WHERE ut.email=:email AND ut.userId=:userId");
            query.setParameter("email", email);
            query.setParameter("userId", userId);
            return (UserAouth) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
