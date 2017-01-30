/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dto;

/**
 *
 * @author Android-3
 */
public class PaymentBean {
    private String billingAddCity;
    private String billingAddCountry;
    private String billingAddLine1;
    private String billingAddPostalCode;
    private String billingAddState;
    private String creditCardType;
    private String creditCardNum;
    private int creditCardExpMonth;
    private int creditCardExpYear;
    private String creditCardFirstName;
    private String creditCardLastName;
    private int creditCardCvvNo;
    
    private String amount;
    private String currency;
    private String cardOption;
    private Double implicitSendingAmont;
    private String payKey;
    private String paymentId;
    private String paymentStatus;
    private String paymentDate;
    private String paymentdate1;
    private Long userId;
    private String userEmailId;
    private String userPaypalEmailId;
    private Long adminId;
    private String adminEmailId;
    private String payerId;
    private Double withdrawAmount;
    private String accounttype;
    private String userSkrillEmailId;

    /**
     * @return the billingAddCity
     */
    public String getBillingAddCity() {
        return billingAddCity;
    }

    /**
     * @param billingAddCity the billingAddCity to set
     */
    public void setBillingAddCity(String billingAddCity) {
        this.billingAddCity = billingAddCity;
    }

    /**
     * @return the billingAddCountry
     */
    public String getBillingAddCountry() {
        return billingAddCountry;
    }

    /**
     * @param billingAddCountry the billingAddCountry to set
     */
    public void setBillingAddCountry(String billingAddCountry) {
        this.billingAddCountry = billingAddCountry;
    }

    /**
     * @return the billingAddLine1
     */
    public String getBillingAddLine1() {
        return billingAddLine1;
    }

    /**
     * @param billingAddLine1 the billingAddLine1 to set
     */
    public void setBillingAddLine1(String billingAddLine1) {
        this.billingAddLine1 = billingAddLine1;
    }

    /**
     * @return the billingAddPostalCode
     */
    public String getBillingAddPostalCode() {
        return billingAddPostalCode;
    }

    /**
     * @param billingAddPostalCode the billingAddPostalCode to set
     */
    public void setBillingAddPostalCode(String billingAddPostalCode) {
        this.billingAddPostalCode = billingAddPostalCode;
    }

    /**
     * @return the billingAddState
     */
    public String getBillingAddState() {
        return billingAddState;
    }

    /**
     * @param billingAddState the billingAddState to set
     */
    public void setBillingAddState(String billingAddState) {
        this.billingAddState = billingAddState;
    }

    /**
     * @return the creditCardType
     */
    public String getCreditCardType() {
        return creditCardType;
    }

    /**
     * @param creditCardType the creditCardType to set
     */
    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    /**
     * @return the creditCardNum
     */
    public String getCreditCardNum() {
        return creditCardNum;
    }

    /**
     * @param creditCardNum the creditCardNum to set
     */
    public void setCreditCardNum(String creditCardNum) {
        this.creditCardNum = creditCardNum;
    }

    /**
     * @return the creditCardExpMonth
     */
    public int getCreditCardExpMonth() {
        return creditCardExpMonth;
    }

    /**
     * @param creditCardExpMonth the creditCardExpMonth to set
     */
    public void setCreditCardExpMonth(int creditCardExpMonth) {
        this.creditCardExpMonth = creditCardExpMonth;
    }

    /**
     * @return the creditCardExpYear
     */
    public int getCreditCardExpYear() {
        return creditCardExpYear;
    }

    /**
     * @param creditCardExpYear the creditCardExpYear to set
     */
    public void setCreditCardExpYear(int creditCardExpYear) {
        this.creditCardExpYear = creditCardExpYear;
    }

    /**
     * @return the creditCardFirstName
     */
    public String getCreditCardFirstName() {
        return creditCardFirstName;
    }

    /**
     * @param creditCardFirstName the creditCardFirstName to set
     */
    public void setCreditCardFirstName(String creditCardFirstName) {
        this.creditCardFirstName = creditCardFirstName;
    }

    /**
     * @return the creditCardLastName
     */
    public String getCreditCardLastName() {
        return creditCardLastName;
    }

    /**
     * @param creditCardLastName the creditCardLastName to set
     */
    public void setCreditCardLastName(String creditCardLastName) {
        this.creditCardLastName = creditCardLastName;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the cardOption
     */
    public String getCardOption() {
        return cardOption;
    }

    /**
     * @param cardOption the cardOption to set
     */
    public void setCardOption(String cardOption) {
        this.cardOption = cardOption;
    }

    /**
     * @return the implicitSendingAmont
     */
    public Double getImplicitSendingAmont() {
        return implicitSendingAmont;
    }

    /**
     * @param implicitSendingAmont the implicitSendingAmont to set
     */
    public void setImplicitSendingAmont(Double implicitSendingAmont) {
        this.implicitSendingAmont = implicitSendingAmont;
    }

    /**
     * @return the creditCardCvvNo
     */
    public int getCreditCardCvvNo() {
        return creditCardCvvNo;
    }

    /**
     * @param creditCardCvvNo the creditCardCvvNo to set
     */
    public void setCreditCardCvvNo(int creditCardCvvNo) {
        this.creditCardCvvNo = creditCardCvvNo;
    }

    public String getPayKey() {
        return payKey;
    }

    public void setPayKey(String payKey) {
        this.payKey = payKey;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentdate1() {
        return paymentdate1;
    }

    public void setPaymentdate1(String paymentdate1) {
        this.paymentdate1 = paymentdate1;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the userEmailId
     */
    public String getUserEmailId() {
        return userEmailId;
    }

    /**
     * @param userEmailId the userEmailId to set
     */
    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    /**
     * @return the adminId
     */
    public Long getAdminId() {
        return adminId;
    }

    /**
     * @param adminId the adminId to set
     */
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    /**
     * @return the adminEmailId
     */
    public String getAdminEmailId() {
        return adminEmailId;
    }

    /**
     * @param adminEmailId the adminEmailId to set
     */
    public void setAdminEmailId(String adminEmailId) {
        this.adminEmailId = adminEmailId;
    }

    /**
     * @return the userPaypalEmailId
     */
    public String getUserPaypalEmailId() {
        return userPaypalEmailId;
    }

    /**
     * @param userPaypalEmailId the userPaypalEmailId to set
     */
    public void setUserPaypalEmailId(String userPaypalEmailId) {
        this.userPaypalEmailId = userPaypalEmailId;
    }

    /**
     * @return the payerId
     */
    public String getPayerId() {
        return payerId;
    }

    /**
     * @param payerId the payerId to set
     */
    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    /**
     * @return the withdrawAmount
     */
    public Double getWithdrawAmount() {
        return withdrawAmount;
    }

    /**
     * @param withdrawAmount the withdrawAmount to set
     */
    public void setWithdrawAmount(Double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public String getUserSkrillEmailId() {
        return userSkrillEmailId;
    }

    public void setUserSkrillEmailId(String userSkrillEmailId) {
        this.userSkrillEmailId = userSkrillEmailId;
    }
}
