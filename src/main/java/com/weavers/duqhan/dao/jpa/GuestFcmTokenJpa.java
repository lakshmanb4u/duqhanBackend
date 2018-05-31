/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import javax.persistence.Query;

import com.weavers.duqhan.dao.GuestFcmTokenDao;
import com.weavers.duqhan.domain.GuestFcmToken;

public class GuestFcmTokenJpa extends BaseDaoJpa<GuestFcmToken> implements GuestFcmTokenDao {

    public GuestFcmTokenJpa() {
        super(GuestFcmToken.class, "GuestFcmToken");
    }

	@Override
	public GuestFcmToken getByUuid(String uuid) {
		 try {
	            Query query = getEntityManager().createQuery("SELECT c FROM GuestFcmToken As c WHERE c.uuid = :uuid");
	            query.setParameter("uuid", uuid);
	            return (GuestFcmToken) query.getSingleResult();
	        } catch (Exception e) {
	            return null;
	        }
	}

}
