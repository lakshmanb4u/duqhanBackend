/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.weavers.duqhan.business.AouthService;
import com.weavers.duqhan.business.UsersService;
import com.weavers.duqhan.dao.OtpTableDao;
import com.weavers.duqhan.dao.UserAddressDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.domain.OtpTable;
import com.weavers.duqhan.domain.UserAddress;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.AddressBean;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.AouthBean;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.dto.UserBean;
import com.weavers.duqhan.util.Crypting;
import com.weavers.duqhan.util.DateFormater;
import com.weavers.duqhan.util.MailSender;
import com.weavers.duqhan.util.RandomCodeGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersDao usersDao;

    @Autowired
    OtpTableDao otpTableDao;

    @Autowired
    AouthService aouthService;

    @Autowired
    UserAddressDao userAddressDao;

    private UserBean setUserBean(Users users) {
        UserBean userBean = new UserBean();
        if (users != null) {
            userBean.setName(users.getName());
            userBean.setEmail(userBean.getEmail());
        }
        return userBean;
    }

    @Override
    public Users getAllUser() {
        Users userses = usersDao.loadById(1l);
        System.out.println("ddd" + userses.getEmail());
        return userses;
    }

    @Override
    public UserBean userRegistration(LoginBean loginBean) {
        Users user = usersDao.loadByEmail(loginBean.getEmail());
        UserBean userBean = new UserBean();
        if (user != null) {
            userBean.setStatusCode("403");
            userBean.setStatus("A user has already signed up with the supplied email");
        } else {
            String pass = Crypting.encrypt(loginBean.getPassword());    // password encription
            Users user2 = new Users();
            user2.setId(null);
            user2.setName(loginBean.getName());
            user2.setPassword(pass);
            user2.setEmail(loginBean.getEmail());
            user2.setRegDate(new Date());
            Users saveUser = usersDao.save(user2);  // new user registration.
            if (saveUser != null) {
                userBean.setName(user2.getName());
                userBean.setEmail(user2.getEmail());
                userBean.setStatusCode("200");
                userBean.setStatus("Success");
            } else {
                userBean.setStatusCode("500");
                userBean.setStatus("Server side exception");
            }
        }
        return userBean;
    }

    @Override
    public UserBean fbUserLogin(LoginBean loginBean) {
        Users user = usersDao.loadByEmail(loginBean.getEmail());
        UserBean userBean = new UserBean();
        if (user != null) { // if user already exist
            userBean.setName(user.getName());
            userBean.setEmail(user.getEmail());
            userBean.setStatusCode("200");
            userBean.setStatus("Success");
            user.setLastloginDate(new Date());
            user.setFbid(loginBean.getFbid());
            user.setFcmToken(loginBean.getFcmToken());
            Users user2 = usersDao.save(user);
            AouthBean aouthBean = aouthService.generatAccessToken(user2.getEmail(), user2.getId());
            userBean.setAuthtoken(aouthBean.getAouthToken());
        } else { // if user not exist
            Users user2 = new Users();
            Date newDate = new Date();
            user2.setId(null);
            user2.setName(loginBean.getName());
            user2.setEmail(loginBean.getEmail());
            user2.setRegDate(newDate);
            user2.setLastloginDate(newDate);
            user2.setFbid(loginBean.getFbid());
            user2.setFcmToken(loginBean.getFcmToken());
            Users saveUser = usersDao.save(user2);  // new registration
            if (saveUser != null) {
                AouthBean aouthBean = aouthService.generatAccessToken(saveUser.getEmail(), saveUser.getId());   // generate token
                userBean.setAuthtoken(aouthBean.getAouthToken());
                userBean.setName(user2.getName());
                userBean.setEmail(user2.getEmail());
                userBean.setStatusCode("200");
                userBean.setStatus("Success");
            } else {
                userBean.setStatusCode("500");
                userBean.setStatus("Server side exception");
            }
        }
        return userBean;
    }

    @Override
    public UserBean userLogin(LoginBean loginBean) {
        String pass = Crypting.encrypt(loginBean.getPassword());
        Users user = usersDao.loadByEmailAndPass(loginBean.getEmail(), pass);
        UserBean userBean = new UserBean();
        if (user != null) {
            userBean.setName(user.getName());
            userBean.setEmail(user.getEmail());
            userBean.setStatusCode("200");
            userBean.setStatus("Success");
            user.setLastloginDate(new Date());
            user.setFcmToken(loginBean.getFcmToken());
            Users user2 = usersDao.save(user);
            AouthBean aouthBean = aouthService.generatAccessToken(user2.getEmail(), user2.getId()); // generate token
            userBean.setAuthtoken(aouthBean.getAouthToken());
        } else {
            userBean.setStatusCode("403");
            userBean.setStatus("Wrong email or password");
        }
        return userBean;
    }

    @Override
    public String userLogout(LoginBean loginBean) {
        String status;
        status = aouthService.invalidatedToken(loginBean.getEmail(), loginBean.getAuthtoken()); // token invalidated
        return status;
    }

    @Override
    public UserBean passwordResetRequest(String email) {
        Users user = usersDao.loadByEmail(email);
        UserBean userBean = new UserBean();
        if (user != null) {
            String otp = RandomCodeGenerator.getNumericCode(6); // generate OTP
            String status = MailSender.sendEmail(user.getEmail(), "OTP For Password Resset", "Your OTP is:  " + otp, "");// send mail to user with otp.
            if (status.equals("success")) { // if mail send...
                OtpTable otpTable = new OtpTable();
                otpTable.setId(null);
                otpTable.setUserId(user.getId());
                otpTable.setUserMail(user.getEmail());
                otpTable.setOtp(otp);
                otpTable.setSendTime(new Date());
                otpTableDao.save(otpTable);
                userBean.setEmail(user.getEmail());
                userBean.setStatusCode("200");
                userBean.setStatus("A 6 digit password reset code has been sent to your email");
            } else {
                userBean.setStatusCode("403");
                userBean.setStatus("Could not able to send the 6 digit password reset code");
            }
        } else {
            userBean.setStatusCode("403");
            userBean.setStatus("No user has signed up with the supplied email");
        }
        return userBean;
    }

    @Override
    public UserBean passwordReset(LoginBean loginBean) {
        Users user = usersDao.loadByEmail(loginBean.getEmail());
        UserBean userBean = new UserBean();
        if (user != null) {
            OtpTable otpTable = otpTableDao.getValidOtp(user.getId(), loginBean.getEmail(), loginBean.getResetCode());
            if (otpTable != null) { // check OTP
                String pass = Crypting.encrypt(loginBean.getNewPassword());
                user.setPassword(pass);
                usersDao.save(user);
                userBean.setStatusCode("200");
                userBean.setStatus("Password resetted successfully");
                otpTableDao.delete(otpTable);
            } else {
                userBean.setStatusCode("403");
                userBean.setStatus("Wrong password reset code or code expired");
            }
        } else {
            userBean.setStatusCode("500");
            userBean.setStatus("Server side exception");
        }
        return userBean;
    }

    @Override
    public UserBean updateUserProfile(Users user, UserBean userBean1) {
        UserBean userBean = new UserBean();
        userBean.setStatusCode("403");
        userBean.setStatus("Profile can not be update..");
        if (userBean1.getEmail() != null) { // whether user present with that email or not. 
            user.setDob(DateFormater.formateToDate(userBean1.getDob()));
            user.setEmail(userBean1.getEmail());
            user.setGender(userBean1.getGender());
            user.setMobile(userBean1.getMobile());
            user.setName(userBean1.getName());
            Users users = usersDao.save(user);
            if (users != null) {    // change email in auth table
                aouthService.updateEmailByUserId(users.getId(), users.getEmail());
                userBean.setDob(DateFormater.formate(users.getDob(), "dd/MM/yyyy"));
                userBean.setEmail(users.getEmail());
                userBean.setGender(users.getGender());
                userBean.setMobile(users.getMobile());
                userBean.setName(users.getName());
                userBean.setProfileImg(users.getProfileImg());
                userBean.setStatusCode("200");
                userBean.setStatus("Profile update successfully");
            }
        }
        return userBean;
    }

    @Override
    public UserBean updateUserProfileImage(Users user, UserBean userBean1) {
        UserBean userBean = new UserBean();
        userBean.setStatusCode("403");
        userBean.setStatus("Profile image can not be update..");
        user.setProfileImg(userBean1.getProfileImg());
        Users users = usersDao.save(user);  // update user profile image.
        if (users != null) {
            userBean.setProfileImg(users.getProfileImg());
            userBean.setStatusCode("200");
            userBean.setStatus("Profile image update successfully");
        }
        return userBean;
    }
//===========================================Address moduses start========================================//
// <editor-fold defaultstate="collapsed" desc="Address moduses">
// <editor-fold defaultstate="collapsed" desc="setAddressDto">

    private AddressDto setAddressDto(UserAddress userAddress) {
        AddressDto addressDto = new AddressDto();
        if (userAddress != null) {
            addressDto.setAddressId(userAddress.getId());
            addressDto.setCity(userAddress.getCity());
            addressDto.setCompanyName(userAddress.getCompanyName());
            addressDto.setContactName(userAddress.getContactName());
            addressDto.setCountry(userAddress.getCountry());
            addressDto.setEmail(userAddress.getEmail());
            addressDto.setIsResidential(userAddress.getResidential());
            addressDto.setPhone(userAddress.getPhone());
            addressDto.setState(userAddress.getState());
            addressDto.setStatus(userAddress.getStatus());
            addressDto.setStreetOne(userAddress.getStreetOne());
            addressDto.setStreetTwo(userAddress.getStreetTwo());
            addressDto.setUserId(userAddress.getUserId());
            addressDto.setZipCode(userAddress.getZipCode());
        }
        return addressDto;
    }
// </editor-fold>

    @Override
    public AddressBean saveUserAddress(AddressDto address) {
        AddressBean addressBean = new AddressBean();
        List<AddressDto> addressDtos = new ArrayList<>();
        UserAddress userAddress2;
        if (address.getAddressId() != null) {
            UserAddress userAddress = userAddressDao.getAddressByIdAndUserId(address.getUserId(), address.getAddressId());
            if (userAddress != null) {
                userAddress.setCity(address.getCity());
                userAddress.setCompanyName(address.getCompanyName());
                userAddress.setContactName(address.getContactName());
                userAddress.setCountry(address.getCountry());
                userAddress.setEmail(address.getEmail());
                userAddress.setPhone(address.getPhone());
                userAddress.setResidential(address.getIsResidential());
                userAddress.setState(address.getState());
                userAddress.setStatus(2l);
                userAddress.setStreetOne(address.getStreetOne());
                userAddress.setStreetTwo(address.getStreetTwo());
                userAddress.setUserId(address.getUserId());
                userAddress.setZipCode(address.getZipCode());
                try {
                    userAddress2 = userAddressDao.save(userAddress);
                    addressDtos.add(this.setAddressDto(userAddress2));
                    if (address.getStatus() != null && address.getStatus().equals(1L)) {
                        return this.setUserAddressesAsDefault(userAddress2.getUserId(), userAddress2.getId());
                    }
                    addressBean.setAddresses(addressDtos);
                    addressBean.setStatus("Success address update");
                    addressBean.setStatusCode("200");
                    return addressBean;
                } catch (Exception e) {
                    addressBean.setAddresses(null);
                    addressBean.setStatus("Address can not be update.");
                    addressBean.setStatusCode("500");
                    return addressBean;
                }
            } else {
                addressBean.setAddresses(null);
                addressBean.setStatus("Address can not be update...null");
                addressBean.setStatusCode("500");
                return addressBean;
            }
        } else {
            UserAddress userAddress = new UserAddress();
            userAddress.setId(null);
            userAddress.setCity(address.getCity());
            userAddress.setCompanyName(address.getCompanyName());
            userAddress.setContactName(address.getContactName());
            userAddress.setCountry(address.getCountry());
            userAddress.setEmail(address.getEmail());
            userAddress.setPhone(address.getPhone());
            userAddress.setResidential(address.getIsResidential());
            userAddress.setState(address.getState());
            userAddress.setStatus(2l);
            userAddress.setStreetOne(address.getStreetOne());
            userAddress.setStreetTwo(address.getStreetTwo());
            userAddress.setUserId(address.getUserId());
            userAddress.setZipCode(address.getZipCode());
            try {
                userAddress2 = userAddressDao.save(userAddress);
                if (address.getStatus() != null && address.getStatus().equals(1L)) {
                    return this.setUserAddressesAsDefault(userAddress2.getUserId(), userAddress2.getId());
                }
                addressDtos.add(this.setAddressDto(userAddress2));
                addressBean.setAddresses(addressDtos);
                addressBean.setStatus("Success.. address saved");
                addressBean.setStatusCode("200");
                return addressBean;
            } catch (Exception e) {
                addressBean.setAddresses(null);
                addressBean.setStatus("Address can not be saved.");
                addressBean.setStatusCode("500");
                return addressBean;
            }
        }
    }

    @Override
    public AddressBean getUserActiveAddresses(Long userId) {
        AddressBean addressBean = new AddressBean();
        List<AddressDto> addressDtos = new ArrayList<>();
        List<UserAddress> userAddresses = userAddressDao.getActiveAddressByUserId(userId);
        for (UserAddress userAddress : userAddresses) {
            addressDtos.add(this.setAddressDto(userAddress));
        }
        addressBean.setAddresses(addressDtos);
        addressBean.setStatus("Success");
        addressBean.setStatusCode("200");
        return addressBean;
    }

    @Override
    public AddressBean setUserAddressesAsDefault(Long userId, Long addressId) {
        AddressBean addressBean = new AddressBean();
        List<AddressDto> addressDtos = new ArrayList<>();
        List<UserAddress> userAddresses = userAddressDao.getActiveAddressByUserId(userId);
        for (UserAddress userAddress : userAddresses) {
            if (!userAddress.getId().equals(addressId) && userAddress.getStatus() == 1L) {
                userAddress.setStatus(2L);
                userAddressDao.save(userAddress);
            }
            if (userAddress.getId().equals(addressId)) {
                userAddress.setStatus(1L);
                UserAddress userAddress2 = userAddressDao.save(userAddress);
                addressDtos.add(this.setAddressDto(userAddress2));
            }
        }
        addressBean.setAddresses(addressDtos);
        addressBean.setStatus("Success");
        addressBean.setStatusCode("200");
        return addressBean;
    }

    @Override
    public AddressBean deactivateUserAddress(Long userId, Long addressId) {
        UserAddress userAddress = userAddressDao.getAddressByIdAndUserId(userId, addressId);
        AddressBean addressBean = new AddressBean();
        List<AddressDto> addressDtos = new ArrayList<>();
        if (userAddress != null) {
            userAddress.setStatus(0L);
            try {
                UserAddress userAddress2 = userAddressDao.save(userAddress);
                addressDtos.add(this.setAddressDto(userAddress2));
                addressBean.setAddresses(addressDtos);
                addressBean.setStatus("Success");
                addressBean.setStatusCode("200");
                return addressBean;
            } catch (Exception e) {
                addressBean.setAddresses(null);
                addressBean.setStatus("Address can not be deactivate.");
                addressBean.setStatusCode("500");
                return addressBean;
            }
        }
        addressBean.setAddresses(null);
        addressBean.setStatus("No address found");
        addressBean.setStatusCode("500");
        return addressBean;
    }

    @Override
    public AddressBean getUserDefaultAddress(Long userId) {
        AddressBean addressBean = new AddressBean();
        addressBean.setStatus("No default address found.");
        addressBean.setStatusCode("200");
        List<AddressDto> addresses = new ArrayList<>();
        UserAddress userAddress = userAddressDao.getDefaultAddressByUserId(userId);
        if (userAddress != null) {
            addresses.add(this.setAddressDto(userAddress));
            addressBean.setStatus("success");
        }
        addressBean.setAddresses(addresses);
        return addressBean;
    }
// </editor-fold>
//===========================================Address moduses end==========================================//

}
