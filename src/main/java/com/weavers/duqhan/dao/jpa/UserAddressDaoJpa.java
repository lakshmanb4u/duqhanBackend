/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.UserAddressDao;
import com.weavers.duqhan.domain.UserAddress;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class UserAddressDaoJpa extends BaseDaoJpa<UserAddress> implements UserAddressDao {

    public UserAddressDaoJpa() {
        super(UserAddress.class, "UserAddress");
    }

    @Override
    public UserAddress getAddressByIdAndUserId(Long userId, Long id) {
        try {
            Query query = getEntityManager().createQuery("SELECT ua FROM UserAddress AS ua WHERE ua.id=:id AND ua.userId=:userId");
            query.setParameter("id", id);
            query.setParameter("userId", userId);
            return (UserAddress) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<UserAddress> getAddressByUserId(Long userId) {
        Query query = getEntityManager().createQuery("SELECT ua FROM UserAddress AS ua WHERE ua.userId=:userId");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<UserAddress> getActiveAddressByUserId(Long userId) {
        Query query = getEntityManager().createQuery("SELECT ua FROM UserAddress AS ua WHERE ua.userId=:userId AND ua.status IN(1,2)");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public UserAddress getDefaultAddressByUserId(Long userId) {
        try {
            Query query = getEntityManager().createQuery("SELECT ua FROM UserAddress AS ua WHERE ua.status=:status AND ua.userId=:userId");
            query.setParameter("status", 1L);
            query.setParameter("userId", userId);
            return (UserAddress) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
