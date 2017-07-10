/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;

/**
 *
 * @author Android-3
 */
public class UserBean {

    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * @return the regDate
     */
    public String getRegDate() {
        return regDate;
    }

    /**
     * @param regDate the regDate to set
     */
    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    /**
     * @return the lastloginDate
     */
    public String getLastloginDate() {
        return lastloginDate;
    }

    /**
     * @param lastloginDate the lastloginDate to set
     */
    public void setLastloginDate(String lastloginDate) {
        this.lastloginDate = lastloginDate;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the authtoken
     */
    public String getAuthtoken() {
        return authtoken;
    }

    /**
     * @param authtoken the authtoken to set
     */
    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
    private String name;
    private String profileImg;
    private String mobile;
    private String email;
    private String gender;
    private String dob;
    private String regDate;
    private String lastloginDate;
    private String password;
    private String authtoken;
    private String statusCode;
    private String status;
    private Long cartCount;
    private Long id;

    /**
     * @return the profileImg
     */
    public String getProfileImg() {
        return profileImg;
    }

    /**
     * @param profileImg the profileImg to set
     */
    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    /**
     * @return the cartCount
     */
    public Long getCartCount() {
        return cartCount;
    }

    /**
     * @param cartCount the cartCount to set
     */
    public void setCartCount(Long cartCount) {
        this.cartCount = cartCount;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

}
