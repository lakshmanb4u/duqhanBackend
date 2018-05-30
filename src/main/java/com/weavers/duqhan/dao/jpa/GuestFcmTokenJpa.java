/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.GuestFcmTokenDao;
import com.weavers.duqhan.domain.GuestFcmToken;

public class GuestFcmTokenJpa extends BaseDaoJpa<GuestFcmToken> implements GuestFcmTokenDao {

    public GuestFcmTokenJpa() {
        super(GuestFcmToken.class, "GuestFcmToken");
    }

}
