package com.attendance.project.business.dto;

import java.util.Date;

public class ZhangjmCallbackData {
    private String deviceKey;
    private String personId;
    private Long time; // 毫秒级时间戳
    private String type;
    private String imgBase64;
    private String data; // 附加数据，如身份证信息、IC卡号等
    private String ip;
    private String searchScore;
    private String livenessScore;

    // Getters and setters

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSearchScore() {
        return searchScore;
    }

    public void setSearchScore(String searchScore) {
        this.searchScore = searchScore;
    }

    public String getLivenessScore() {
        return livenessScore;
    }

    public void setLivenessScore(String livenessScore) {
        this.livenessScore = livenessScore;
    }

    // Helper method to convert timestamp to Date
    public Date getAtdTime() {
        if (this.time == null) {
            return null;
        }
        return new Date(this.time);
    }

    @Override
    public String toString() {
        return "ZhangjmCallbackData{" +
               "deviceKey='" + deviceKey + '\'' +
               ", personId='" + personId + '\'' +
               ", time=" + time +
               ", type='" + type + '\'' +
               ", imgBase64='" + imgBase64 + '\'' +
               ", data='" + data + '\'' +
               ", ip='" + ip + '\'' +
               ", searchScore='" + searchScore + '\'' +
               ", livenessScore='" + livenessScore + '\'' +
               '}';
    }
}