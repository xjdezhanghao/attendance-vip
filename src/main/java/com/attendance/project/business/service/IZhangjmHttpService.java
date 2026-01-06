package com.attendance.project.business.service;

import com.attendance.project.business.dto.DeviceResponse;

/**
 * 与掌静脉/人脸设备通过HTTP通信的服务接口 (修订版)
 */
public interface IZhangjmHttpService { // 名称已修改

    // --- 设备基础管理与配置 ---

    /**
     * 设置识别回调地址 (对应设备API: /setIdentifyCallBack)
     * 当设备识别到人员（包括人脸、掌静脉等）后，会向此URL发送POST请求。
     * 使用默认配置的baseUrl和devicePassword
     * @param callbackUrl 后端接收回调的完整URL
     * @return 设备响应，data中包含设置的回调地址
     */
    DeviceResponse<String> setIdentifyCallbackUrl(String callbackUrl);
    
    /**
     * 设置识别回调地址 (对应设备API: /setIdentifyCallBack)
     * 当设备识别到人员（包括人脸、掌静脉等）后，会向此URL发送POST请求。
     * 使用指定的baseUrl和devicePassword
     * @param baseUrl 设备的API基础URL
     * @param devicePassword 设备密码
     * @param callbackUrl 后端接收回调的完整URL
     * @return 设备响应，data中包含设置的回调地址
     */
    DeviceResponse<String> setIdentifyCallbackUrl(String baseUrl, String devicePassword, String callbackUrl);


    // --- 其他辅助方法 (通常由实现类内部使用，或提供给特定管理功能) ---
    /**
     * 检查服务是否启用 (从ZmConfig获取)
     * @return true 如果服务启用
     */
    boolean isServiceEnabled();

    /**
     * 获取设备密码 (从ZmConfig获取)
     * @return 设备密码
     */
    String getDevicePasswordFromConfig();

    /**
     * 获取设备基础URL (从ZmConfig获取)
     * @return 设备基础URL
     */
    String getBaseUrlFromConfig();
}
