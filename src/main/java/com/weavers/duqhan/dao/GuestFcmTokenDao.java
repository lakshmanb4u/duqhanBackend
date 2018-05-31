/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao;

import com.weavers.duqhan.domain.GuestFcmToken;

/**
 *
 * @author clb14
 */

public interface GuestFcmTokenDao extends BaseDao<GuestFcmToken> {

   GuestFcmToken getByUuid(String uuid);
}
