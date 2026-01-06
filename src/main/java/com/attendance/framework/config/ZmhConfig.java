package com.attendance.framework.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * HTTP通信掌（静）脉设备配置类
 * 用于加载 application.properties 或 application.yml 中以 "zmh" 为前缀的配置项。
 */
@Component
@ConfigurationProperties(prefix = "zmh") // 确保前缀是 "zmh"
public class ZmhConfig {

    /**
     * 是否启用HTTP掌脉设备服务
     */
    private String enable;

    /**
     * HTTP掌脉设备的API基础URL (兼容旧配置)
     */
    private String baseUrl;

    /**
     * 调用HTTP掌脉设备API所需的密码 (兼容旧配置)
     */
    private String devicePassword;
    
    /**
     * 应用程序接收设备回调的完整URL
     * 例如: http://your-app-server-ip:your-app-port/api/device/callback/attendance
     * 这个URL必须是设备可以访问到的您的后端应用的URL。
     */
    private String appFullCallbackUrl; 

    /**
     * （可选）HTTP连接超时时间（毫秒）
     */
    private Integer connectTimeout;

    /**
     * （可选）HTTP读取超时时间（毫秒）
     */
    private Integer readTimeout;

    /**
     * 多个设备配置列表
     */
    private List<ZmhDeviceConfig> devices = new ArrayList<>();

    // --- Getters and Setters ---

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
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

    public Integer getConnectTimeout() {
        // 如果未在配置文件中设置，可以提供一个默认值
        return connectTimeout == null ? 5000 : connectTimeout; // 默认5秒
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getReadTimeout() {
        // 如果未在配置文件中设置，可以提供一个默认值
        return readTimeout == null ? 10000 : readTimeout; // 默认10秒
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getAppFullCallbackUrl() {
        return appFullCallbackUrl;
    }

    public void setAppFullCallbackUrl(String appFullCallbackUrl) {
        this.appFullCallbackUrl = appFullCallbackUrl;
    }

    public List<ZmhDeviceConfig> getDevices() {
        return devices;
    }

    public void setDevices(List<ZmhDeviceConfig> devices) {
        this.devices = devices;
    }

    /**
     * 根据IP地址获取设备配置
     * @param ip 设备IP地址
     * @return 设备配置，如果未找到则返回null
     */
    public ZmhDeviceConfig getDeviceByIp(String ip) {
        if (ip == null || devices == null || devices.isEmpty()) {
            return null;
        }
        
        for (ZmhDeviceConfig device : devices) {
            if (ip.equals(device.getIp())) {
                return device;
            }
        }
        
        return null;
    }

    /**
     * 根据IP地址获取设备对应的atdloc
     * @param ip 设备IP地址
     * @return 设备对应的atdloc，如果未找到则返回默认值"zhangmai"
     */
    public String getAtdlocByIp(String ip) {
        ZmhDeviceConfig device = getDeviceByIp(ip);
        return device != null ? device.getAtdloc() : "zhangmai";
    }
}
