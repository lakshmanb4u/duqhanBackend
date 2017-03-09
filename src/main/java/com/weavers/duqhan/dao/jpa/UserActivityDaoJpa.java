/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.UserActivityDao;
import com.weavers.duqhan.domain.UserActivity;

/**
 *
 * @author weaversAndroid
 */
public class UserActivityDaoJpa extends BaseDaoJpa<UserActivity> implements UserActivityDao {

    public UserActivityDaoJpa() {
        super(UserActivity.class, "UserActivity");
    }

}
