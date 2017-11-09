/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.RecentViewDao;
import com.weavers.duqhan.domain.RecentView;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class RecentViewDaoJpa extends BaseDaoJpa<RecentView> implements RecentViewDao {

    public RecentViewDaoJpa() {
        super(RecentView.class, "RecentView");
    }

    @Override
    public List<RecentView> getRecentViewProductByUserId(Long userId) {
        Query query = getEntityManager().createQuery("SELECT rv FROM RecentView AS rv WHERE rv.userId=:userId ORDER BY rv.viewDate");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public RecentView getRecentViewProductByUserIdProductId(Long userId, Long productId) {
        try {
            Query query = getEntityManager().createQuery("SELECT rv FROM RecentView AS rv WHERE rv.userId=:userId AND rv.productId=:productId");
            query.setParameter("productId", productId);
            query.setParameter("userId", userId);
            return (RecentView) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
