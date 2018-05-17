/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.UserAouthDao;
import com.weavers.duqhan.domain.UserAouth;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
    
    @Override
    public UserAouth getTokenByUserId(Long userId) {
        try {
            Query query = getEntityManager().createQuery("SELECT ut FROM UserAouth ut WHERE ut.userId=:userId");
            query.setParameter("userId", userId);
            return (UserAouth) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public UserAouth getTokenByEmailAndToken(String email, String aouthToken) {
        try {
            Query query = getEntityManager().createQuery("SELECT ut FROM UserAouth ut WHERE ut.email=:email AND ut.aouthToken=:aouthToken");
            query.setParameter("email", email);
            query.setParameter("aouthToken", aouthToken);
            return (UserAouth) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException nre) {
            return null;
        }
    }

    @Override
    public Long getUserIdByTokenIfValid(String aouthToken) {
        try {
            Query query = getEntityManager().createQuery("SELECT ut.userId FROM UserAouth ut WHERE ut.aouthToken=:aouthToken");
            query.setParameter("thisTime", new Date());
            query.setParameter("aouthToken", aouthToken);
            return (Long) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException nre) {
            return null;
        }
    }

    @Override
    public List<UserAouth> loadByUserId(Long userId) {
        Query query = getEntityManager().createQuery("SELECT ut FROM UserAouth ut WHERE ut.userId=:userId");
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
