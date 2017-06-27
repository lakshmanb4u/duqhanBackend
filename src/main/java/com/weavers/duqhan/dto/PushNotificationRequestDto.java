/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;

import com.google.gson.Gson;
import java.util.Map;

/**
 *
 * @author weaversAndroid
 */
public class PushNotificationRequestDto {

    public PushNotificationRequestDto() {
        this.sound = false;
    }

    private Map<String, String> data;

    private String to;

    private String title;
    private String body;
    private Boolean sound;

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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the sound
     */
    public Boolean getSound() {
        return sound;
    }

    /**
     * @param sound the sound to set
     */
    public void setSound(Boolean sound) {
        this.sound = sound;
    }

    public String getNotificationMessages() {
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();

        sb.append("{");
        if (data != null) {
            sb.append("\"data\":");
            String json = gson.toJson(data);
            sb.append(json);
            sb.append(",");
        }
        sb.append("\"notification\":{");
        if (sound) {
            sb.append("\"sound\": \"default\",");
        }
        sb.append("\"title\": \"");
        sb.append(title);
        sb.append("\",");

        sb.append("\"body\": \"");
        sb.append(body);
        sb.append("\",");

        sb.append("},");

        sb.append("\"to\": \"");
        sb.append(to);
        sb.append("\"");

        sb.append("}");
        return sb.toString();
    }
}
