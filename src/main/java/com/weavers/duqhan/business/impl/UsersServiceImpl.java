/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business.impl;

import com.weavers.duqhan.business.AouthService;
import com.weavers.duqhan.business.MailService;
import com.weavers.duqhan.business.UsersService;
import com.weavers.duqhan.controller.CacheController;
import com.weavers.duqhan.dao.OfferProductsDao;
import com.weavers.duqhan.dao.OtpTableDao;
import com.weavers.duqhan.dao.UserActivityDao;
import com.weavers.duqhan.dao.UserAddressDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.domain.OfferProducts;
import com.weavers.duqhan.domain.OtpTable;
import com.weavers.duqhan.domain.UserActivity;
import com.weavers.duqhan.domain.UserAddress;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.AddressBean;
import com.weavers.duqhan.dto.AddressDto;
import com.weavers.duqhan.dto.AouthBean;
import com.weavers.duqhan.dto.LoginBean;
import com.weavers.duqhan.dto.StatusBean;
import com.weavers.duqhan.dto.UserBean;
import com.weavers.duqhan.util.Crypting;
import com.weavers.duqhan.util.DateFormater;
import com.weavers.duqhan.util.GoogleBucketFileUploader;
import com.weavers.duqhan.util.RandomCodeGenerator;
import com.weavers.duqhan.util.StatusConstants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersDao usersDao;

    @Autowired
    OtpTableDao otpTableDao;

    @Autowired
    AouthService aouthService;

    @Autowired
    UserAddressDao userAddressDao;

    @Autowired
    MailService mailService;

    @Autowired
    UserActivityDao userActivityDao;

    @Autowired
    OfferProductsDao offerProductsDao;

    private final Logger logger = Logger.getLogger(UsersServiceImpl.class);
    private final String guestMail = "guest@gmail.com";
    private final String guestPass = "dukhan123";
    private final String guestToken = "dukhan123"; 

    private UserBean setUserBean(Users users) {
        UserBean userBean = new UserBean();
        if (users != null) {
            userBean.setName(users.getName());
            userBean.setEmail(userBean.getEmail());
        }
        return userBean;
    }

    @Override
    public Users getUserById(Long userId) {
        Users users = usersDao.loadById(userId);
        return users;
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
            UserActivity activity = new UserActivity();
            activity.setId(null);
            activity.setUserId(null);
            activity.setEmail(loginBean.getEmail());
            activity.setActivity(StatusConstants.NEW_RAGISTRATION);
            activity.setActivityTime(new Date());
            activity.setLatitude(loginBean.getLatitude());
            activity.setLongitude(loginBean.getLongitude());
            activity.setUserAgent(loginBean.getUserAgent());
            userActivityDao.save(activity);
            String pass = Crypting.encrypt(loginBean.getPassword());    // password encription
            Users user2 = new Users();
            user2.setId(null);
            user2.setName(loginBean.getName());
            user2.setPassword(pass);
            user2.setEmail(loginBean.getEmail());
            user2.setRegDate(new Date());
            user2.setLatitude(loginBean.getLatitude());
            user2.setLongitude(loginBean.getLongitude());
            user2.setUserAgent(loginBean.getUserAgent());
            user2.setFreeOfferAccepted(false);
            user2.setMobile(loginBean.getMobile());
            Users saveUser = usersDao.save(user2);  // new user registration.
            if (saveUser != null) {
                userBean.setName(user2.getName());
                userBean.setEmail(user2.getEmail());
                userBean.setMobile(user2.getMobile());
                userBean.setStatusCode("200");
                userBean.setStatus("Success");
                mailService.sendNewRegistrationToAdmin(saveUser);
                mailService.sendWelcomeMailToUser(saveUser);
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
        if (null == user) {
            user = usersDao.loadByFbId(loginBean.getFbid());
        }
        UserBean userBean = new UserBean();
        Date newDate = new Date();
        UserActivity activity = new UserActivity();
        userBean.setIsFirstLogin(false);

        if (user != null) { // if user already exist
        	CacheController.emptyUserCacheList(user);
            activity.setUserId(user.getId());
            activity.setActivity(StatusConstants.LOGIN);
            userBean.setName(user.getName());
            userBean.setEmail(user.getEmail());
            userBean.setMobile(user.getMobile());
            userBean.setProfileImg(user.getProfileImg());
            userBean.setStatusCode("200");
            userBean.setStatus("Success");
            user.setLastloginDate(new Date());
            user.setFbid(loginBean.getFbid());
            user.setFcmToken(loginBean.getFcmToken());
            user.setLatitude(loginBean.getLatitude());
            user.setLongitude(loginBean.getLongitude());
            user.setUserAgent(loginBean.getUserAgent());
            Users user2 = usersDao.save(user);
            AouthBean aouthBean = aouthService.generatAccessToken(user2.getEmail(), user2.getId(),loginBean.getCountryCode());
            userBean.setAuthtoken(aouthBean.getAouthToken());
            userBean.setFreeProductEligibility(false);
            if (!user.getFreeOfferAccepted()) {
                List<OfferProducts> offerProducts = offerProductsDao.loadAll();
                if (!offerProducts.isEmpty()) {
                    userBean.setFreeProductEligibility(true);
                }
            }
        } else { // if user not exist
            userBean.setIsFirstLogin(true);
            Users user2 = new Users();
            activity.setUserId(null);
            activity.setActivity(StatusConstants.NEW_RAGISTRATION);
            user2.setId(null);
            user2.setName(loginBean.getName());
            user2.setEmail(loginBean.getEmail());
            user2.setRegDate(newDate);
            user2.setLastloginDate(newDate);
            user2.setFbid(loginBean.getFbid());
            user2.setFcmToken(loginBean.getFcmToken());
            user2.setLatitude(loginBean.getLatitude());
            user2.setLongitude(loginBean.getLongitude());
            user2.setUserAgent(loginBean.getUserAgent());
            user2.setFreeOfferAccepted(false);
            Users saveUser = usersDao.save(user2);  // new registration
            if (saveUser != null) {
                AouthBean aouthBean = aouthService.generatAccessToken(saveUser.getEmail(), saveUser.getId(),loginBean.getCountryCode());   // generate token
                userBean.setAuthtoken(aouthBean.getAouthToken());
                userBean.setName(user2.getName());
                userBean.setEmail(user2.getEmail());
                userBean.setMobile(user2.getMobile());
                userBean.setProfileImg(user2.getProfileImg());
                userBean.setStatusCode("200");
                userBean.setStatus("Success");
                mailService.sendNewRegistrationToAdmin(saveUser);
                if (user2.getEmail() != null) {
                    mailService.sendWelcomeMailToUser(saveUser);
                }
            } else {
                userBean.setStatusCode("500");
                userBean.setStatus("Server side exception");
            }
        }
        activity.setId(null);
        activity.setEmail(loginBean.getEmail());
        activity.setActivityTime(newDate);
        activity.setLatitude(loginBean.getLatitude());
        activity.setLongitude(loginBean.getLongitude());
        activity.setUserAgent(loginBean.getUserAgent());
        userActivityDao.save(activity);
        return userBean;
    }

    @Override
    public UserBean userLogin(LoginBean loginBean,long loginStartTime) {
    	if(loginBean.getEmail().equals(guestMail)){
    		return this.guestUserLogin(loginBean, loginStartTime);
    	
    	}else {
        String pass = Crypting.encrypt(loginBean.getPassword());
        Users user = usersDao.loadByEmailAndPass(loginBean.getEmail(), pass);
        System.out.println("after first data base connectivity"+(loginStartTime-System.currentTimeMillis()));
        UserBean userBean = new UserBean();
        Date newDate = new Date();
        if (user != null) {
        	CacheController.emptyUserCacheList(user);
            UserActivity activity = new UserActivity();
            activity.setId(null);
            activity.setUserId(user.getId());
            activity.setEmail(loginBean.getEmail());
            activity.setActivity(StatusConstants.LOGIN);
            activity.setActivityTime(newDate);
            activity.setLatitude(loginBean.getLatitude());
            activity.setLongitude(loginBean.getLongitude());
            activity.setUserAgent(loginBean.getUserAgent());
            userActivityDao.save(activity);
            System.out.println("after second data base connectivity"+(loginStartTime-System.currentTimeMillis()));
            userBean.setName(user.getName());
            userBean.setEmail(user.getEmail());
            userBean.setDob(DateFormater.formate(user.getDob()));
            userBean.setMobile(user.getMobile());
            userBean.setProfileImg(user.getProfileImg());
            userBean.setStatusCode("200");
            userBean.setStatus("Success");
            user.setLastloginDate(newDate);
            user.setFcmToken(loginBean.getFcmToken());
            user.setLatitude(loginBean.getLatitude());
            user.setLongitude(loginBean.getLongitude());
            user.setUserAgent(loginBean.getUserAgent());
            Users user2 = usersDao.save(user);
            System.out.println("after third data base connectivity"+(loginStartTime-System.currentTimeMillis()));
            AouthBean aouthBean = aouthService.generatAccessToken(user2.getEmail(), user2.getId(), loginBean.getCountryCode()); // generate token
            System.out.println("after Token authen data base connectivity"+(loginStartTime-System.currentTimeMillis()));
            userBean.setAuthtoken(aouthBean.getAouthToken());
            userBean.setFreeProductEligibility(false);
            if (!user.getFreeOfferAccepted()) {
                List<OfferProducts> offerProducts = offerProductsDao.loadAll();
                if (!offerProducts.isEmpty()) {
                    userBean.setFreeProductEligibility(true);
                }
            }
        } else {
            userBean.setStatusCode("403");
            userBean.setStatus("Wrong email or password");
        }
        System.out.println("After Verfication in coad"+(loginStartTime-System.currentTimeMillis()));
        return userBean;
    	}
    }
    
    @Override
    public UserBean guestUserLogin(LoginBean loginBean,long loginStartTime) {
        Users user = usersDao.loadByEmailAndPass(guestMail, guestPass);
        UserBean userBean = new UserBean();
        if (user != null) {
            userBean.setName(user.getName());
            userBean.setEmail(user.getEmail());
            userBean.setStatusCode("200");
            userBean.setStatus("Success");
            Users user2 = usersDao.save(user);
            userBean.setAuthtoken(guestToken);
            userBean.setFreeProductEligibility(false);
            if (!user.getFreeOfferAccepted()) {
                List<OfferProducts> offerProducts = offerProductsDao.loadAll();
                if (!offerProducts.isEmpty()) {
                    userBean.setFreeProductEligibility(true);
                }
            }
        } else {
            userBean.setStatusCode("403");
            userBean.setStatus("Wrong email or password");
        }
        System.out.println("After Verfication in coad"+(loginStartTime-System.currentTimeMillis()));
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
            String status = mailService.sendOTPforPasswordReset(user.getEmail(), otp);// send mail to user with otp.
            if (status.equals("success")) { // if mail send...
                OtpTable otpTable = otpTableDao.getOtpTableByUserId(user.getId());
                if (null != otpTable) {
                } else {
                    otpTable = new OtpTable();
                    otpTable.setId(null);
                }
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
        Users exsistUser = usersDao.loadByEmail(userBean1.getEmail());
        if (userBean1.getEmail() != null && (exsistUser == null || exsistUser.getId().equals(user.getId()))) { // whether user present with that email or not. 
            user.setDob(DateFormater.formateToDate(userBean1.getDob()));
            user.setEmail(userBean1.getEmail());
            user.setGender(userBean1.getGender());
            user.setMobile(userBean1.getMobile());
            user.setName(userBean1.getName());
            user.setCurrencyCode(userBean1.getCurrencyCode());
            Users users = usersDao.save(user);
            if (users != null) {    // change email in auth table
                aouthService.updateEmailByUserId(users.getId(), users.getEmail());
                userBean.setDob(DateFormater.formate(users.getDob(), "dd/MM/yyyy"));
                userBean.setEmail(users.getEmail());
                userBean.setGender(users.getGender());
                userBean.setMobile(users.getMobile());
                userBean.setName(users.getName());
                userBean.setProfileImg(users.getProfileImg());
                userBean.setCurrencyCode(users.getCurrencyCode());
                userBean.setStatusCode("200");
                userBean.setStatus("Profile update successfully");
            }
        }
        return userBean;
    }

    @Override
    public String updateUserProfileImage(Users user, MultipartFile file) {
        UserBean userBean = new UserBean();
        userBean.setStatusCode("403");
        userBean.setStatus("Profile image can not be update..");
        String oldImgUrl = user.getProfileImg();
        if (oldImgUrl != null && oldImgUrl.contains("duqhan-users/")) {
            String imgName = oldImgUrl.split("duqhan-users/")[1];
            GoogleBucketFileUploader.deleteProfileImg(imgName);
        }
        String imgUrl = GoogleBucketFileUploader.uploadProfileImage(file, user.getId());
        if (!imgUrl.equals("failure")) {
            user.setProfileImg(imgUrl);
            Users users = usersDao.save(user);  // update user profile image.
            if (users != null) {
                userBean.setProfileImg(users.getProfileImg());
                userBean.setStatusCode("200");
                userBean.setStatus("Profile image update successfully");
            }
        }
        return imgUrl;
    }

    @Override
    public void saveUsersEmailAndPhone(Users users, UserBean userBean) {
        Users exsistUser = usersDao.loadById(users.getId());
        if (exsistUser == null || exsistUser.getId().equals(users.getId())) {
            users.setEmail(userBean.getEmail());
            if (userBean.getMobile() != null) {
                users.setMobile(userBean.getMobile());
            }
            usersDao.save(users);
        }
    }
//===========================================Address module start========================================//
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
//                userAddress.setCountry(address.getCountry());
                userAddress.setCountry("IN");
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
//            userAddress.setCountry(address.getCountry());
            userAddress.setCountry("IN");
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
//===========================================Address module end==========================================//
    @Override
    public StatusBean changePassword(LoginBean loginBean, Users user) {
        StatusBean statusBean = new StatusBean();
        if (user.getPassword().equals(Crypting.encrypt(loginBean.getPassword()))) {
            String pass = Crypting.encrypt(loginBean.getNewPassword());
            user.setPassword(pass);
            usersDao.save(user);
            statusBean.setStatusCode("200");
            statusBean.setStatus("Password change successfully");
        } else {
            statusBean.setStatusCode("403");
            statusBean.setStatus("password did not match");
        }
        return statusBean;
    }

    @Override
    public String contactToAdmin(UserBean contactBean, Users users) {
        if (users.getEmail() == null || users.getMobile() == null) {
            this.saveUsersEmailAndPhone(users, contactBean);
        }
        return mailService.sendMailToAdminByUser(contactBean, users);
    }
    
}
