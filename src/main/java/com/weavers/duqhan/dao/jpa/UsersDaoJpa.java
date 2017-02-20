/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.domain.Users;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class UsersDaoJpa extends BaseDaoJpa<Users> implements UsersDao {

    public UsersDaoJpa() {
        super(Users.class, "Users");
    }

    @Override
    public Users loadByuserId(Long id) {
        Query query = getEntityManager().createQuery("SELECT u FROM Users u WHERE u.id=:id");
        query.setParameter("id", id);
        return (Users) query.getSingleResult();
    }

    @Override
    public Users loadByEmail(String email) {
        try {
            Query query = getEntityManager().createQuery("SELECT u FROM Users u WHERE u.email=:email");
            query.setParameter("email", email);
            return (Users) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public Users loadByEmailAndPass(String email, String password) {
        try {
            Query query = getEntityManager().createQuery("SELECT u FROM Users u WHERE u.email=:email AND u.password=:password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            return (Users) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public String GetFcmTokenByuserId(Long userId) {
        try {
            Query query = getEntityManager().createQuery("SELECT u.fcmToken FROM Users AS u WHERE u.id=:userId");
            query.setParameter("userId", userId);
            return (String) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
