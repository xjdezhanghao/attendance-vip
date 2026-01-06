package com.attendance.framework.config;

/**
 * 单个HTTP掌脉设备的配置类
 */
public class ZmhDeviceConfig {

    /**
     * 设备IP地址
     */
    private String ip;

    /**
     * HTTP掌脉设备的API基础URL
     * 例如: http://192.168.1.108:8090
     */
    private String baseUrl;

    /**
     * 调用HTTP掌脉设备API所需的密码
     */
    private String devicePassword;

    /**
     * 设备对应的考勤位置标识
     */
    private String atdloc;

    // --- Getters and Setters ---

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getDevicePassword() {
        return devicePassword;
    }

    public void setDevicePassword(String devicePassword) {
        this.devicePassword = devicePassword;
    }

    public String getAtdloc() {
        return atdloc;
    }

    public void setAtdloc(String atdloc) {
        this.atdloc = atdloc;
    }
}