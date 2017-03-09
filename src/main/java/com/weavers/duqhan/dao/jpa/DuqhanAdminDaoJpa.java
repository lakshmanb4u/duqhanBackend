/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.DuqhanAdminDao;
import com.weavers.duqhan.domain.DuqhanAdmin;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author weaversAndroid
 */
public class DuqhanAdminDaoJpa extends BaseDaoJpa<DuqhanAdmin> implements DuqhanAdminDao {

    public DuqhanAdminDaoJpa() {
        super(DuqhanAdmin.class, "DuqhanAdmin");
    }

    @Override
    public DuqhanAdmin getAdminByEmailAndPassword(String email, String password) {
        try {
            Query query = getEntityManager().createQuery("SELECT a FROM DuqhanAdmin AS a WHERE a.email=:email AND a.password=:password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            return (DuqhanAdmin) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
