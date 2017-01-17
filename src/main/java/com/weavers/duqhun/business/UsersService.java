/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.business;

import com.weavers.duqhun.domain.Users;
import com.weavers.duqhun.dto.LoginBean;
import com.weavers.duqhun.dto.UserBean;

/**
 *
 * @author clb14
 */
public interface UsersService {

    Users getAllUser();

    UserBean userRegistration(LoginBean loginBean);

    UserBean fbUserLogin(LoginBean loginBean);

    UserBean userLogin(LoginBean loginBean);

    String userLogout(LoginBean loginBean);

    UserBean passwordResetRequest(String email);

    UserBean passwordReset(LoginBean loginBean);

    UserBean updateUserProfile(Users users, UserBean userBean);
    
    UserBean updateUserProfileImage(Users users, UserBean userBean);
    
}
