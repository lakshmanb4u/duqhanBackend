/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.OfferProductUsedDao;
import com.weavers.duqhan.domain.OfferProductUsed;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author weaversAndroid
 */
public class OfferProductUsedDaoJpa extends BaseDaoJpa<OfferProductUsed> implements OfferProductUsedDao {

    public OfferProductUsedDaoJpa() {
        super(OfferProductUsed.class, "OfferProductUsed");
    }

    @Override
    public OfferProductUsed loadByUserId(Long userId) {
        try {
            Query query = getEntityManager().createQuery("SELECT opu FROM OfferProductUsed as opu WHERE opu.userId=:userId");
            query.setParameter("userId", userId);
            return (OfferProductUsed) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException nure) {
            return new OfferProductUsed();
        }
    }

}
