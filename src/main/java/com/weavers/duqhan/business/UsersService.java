/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business;

import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.AddressBean;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.dto.UserBean;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author clb14
 */
public interface UsersService {

    Users getUserById(Long userId);

    Users getAllUser();

    UserBean userRegistration(LoginBean loginBean);

    UserBean fbUserLogin(LoginBean loginBean);

    UserBean userLogin(LoginBean loginBean, long loginStartTime);

    String userLogout(LoginBean loginBean);

    UserBean passwordResetRequest(String email);

    UserBean passwordReset(LoginBean loginBean);

    UserBean updateUserProfile(Users users, UserBean userBean);

    String updateUserProfileImage(Users users, MultipartFile file);

    AddressBean saveUserAddress(AddressDto addressDto);

    void saveUsersEmailAndPhone(Users users, UserBean userBean);

    AddressBean getUserActiveAddresses(Long userId);

    AddressBean getUserDefaultAddress(Long userId);

    AddressBean setUserAddressesAsDefault(Long userId, Long addressId);

    AddressBean deactivateUserAddress(Long userId, Long addressId);

    StatusBean changePassword(LoginBean loginBean, Users users);

    String contactToAdmin(UserBean contactBean, Users users);

	UserBean guestUserLogin(LoginBean loginBean, long loginStartTime);

}
