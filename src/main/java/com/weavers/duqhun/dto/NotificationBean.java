/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dto;

import java.util.Map;

/**
 *
 * @author Android-3
 */
public class NotificationBean {

    private NotificationDto notification;
    private Map<String, String> data;
    private String to;
    private String priority;
    private String restrictedPackageName;

    /**
     * @return the notification
     */
    public NotificationDto getNotification() {
        return notification;
    }

    /**
     * @param notification the notification to set
     */
    public void setNotification(NotificationDto notification) {
        this.notification = notification;
    }

    /**
     * @return the data
     */
    public Map<String, String> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Map<String, String> data) {
        this.data = data;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * @return the restrictedPackageName
     */
    public String getRestrictedPackageName() {
        return restrictedPackageName;
    }

    /**
     * @param restrictedPackageName the restrictedPackageName to set
     */
    public void setRestrictedPackageName(String restrictedPackageName) {
        this.restrictedPackageName = restrictedPackageName;
    }
}
