/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.RecentViewDao;
import com.weavers.duqhun.domain.RecentView;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class RecentViewDaoJpa extends BaseDaoJpa<RecentView> implements RecentViewDao{
    
    public RecentViewDaoJpa() {
        super(RecentView.class, "RecentView");
    }

    @Override
    public List<RecentView> getRecentViewProductByUserId(Long userId) {
        Query query = getEntityManager().createQuery("SELECT rv FROM RecentView AS rv WHERE rv.userId=:userId ORDER BY rv.viewDate");
        return query.getResultList();
    }
    
}
