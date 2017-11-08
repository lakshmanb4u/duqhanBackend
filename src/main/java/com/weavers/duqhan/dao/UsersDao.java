/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao;

import com.weavers.duqhan.domain.Users;

/**
 *
 * @author clb14
 */
public interface UsersDao extends BaseDao<Users> {

    Users loadByuserId(Long id);

    Users loadByEmail(String email);

    Users loadByFbId(Long fbid);

    Users loadByEmailAndPass(String email, String pass);

    String GetFcmTokenByuserId(Long userId);

    Users getLastLoginOfUser(Long userId);
}
