package com.attendance.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.attendance.project.business.dto.DeviceResponse;
import com.attendance.project.business.service.IZhangjmHttpService;

/**
 * 应用程序启动时执行的任务，用于向HTTP掌脉设备配置回调URL。
 */
@Component
public class DeviceCallbackSetupRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DeviceCallbackSetupRunner.class);

    private final IZhangjmHttpService zhangjmHttpService;
    private final ZmhConfig zmhConfig;

    @Autowired
    public DeviceCallbackSetupRunner(IZhangjmHttpService zhangjmHttpService, ZmhConfig zmhConfig) {
        this.zhangjmHttpService = zhangjmHttpService;
        this.zmhConfig = zmhConfig;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("DeviceCallbackSetupRunner 开始执行...");

        // 检查HTTP掌脉设备服务是否启用
        if (zmhConfig.getEnable() == null || !Boolean.parseBoolean(zmhConfig.getEnable())) {
            logger.info("HTTP掌脉设备服务未启用 (zmh.enable is not true)，跳过回调URL配置。");
            return;
        }

        String callbackUrl = zmhConfig.getAppFullCallbackUrl();

        if (callbackUrl == null || callbackUrl.trim().isEmpty()) {
            logger.warn("未在配置中找到HTTP设备的回调URL (zmh.appFullCallbackUrl)，无法配置设备回调。");
            return;
        }

        // 检查是否有多个设备配置
        if (zmhConfig.getDevices() != null && !zmhConfig.getDevices().isEmpty()) {
            logger.info("检测到{}个HTTP掌脉设备配置，准备向所有设备配置回调URL", zmhConfig.getDevices().size());
            
            // 向每个设备发送回调配置
            for (ZmhDeviceConfig deviceConfig : zmhConfig.getDevices()) {
                configureDeviceCallback(deviceConfig, callbackUrl);
            }
        } else {
            // 兼容旧配置，使用单一设备配置
            logger.info("未检测到多设备配置，使用单一设备配置向HTTP掌脉设备配置回调URL");
            configureDeviceCallback(null, callbackUrl);
        }

        logger.info("DeviceCallbackSetupRunner 执行完毕。");
    }

    /**
     * 向单个设备配置回调URL
     * @param deviceConfig 设备配置，如果为null则使用全局配置
     * @param callbackUrl 回调URL
     */
    private void configureDeviceCallback(ZmhDeviceConfig deviceConfig, String callbackUrl) {
        String deviceIp = deviceConfig != null ? deviceConfig.getIp() : "默认设备";
        String deviceBaseUrl = deviceConfig != null ? deviceConfig.getBaseUrl() : zmhConfig.getBaseUrl();
        String devicePassword = deviceConfig != null ? deviceConfig.getDevicePassword() : zmhConfig.getDevicePassword();
        
        if (deviceBaseUrl == null || deviceBaseUrl.trim().isEmpty()) {
            logger.warn("设备 {} 的baseUrl未配置，跳过该设备的回调配置", deviceIp);
            return;
        }
        
        if (devicePassword == null || devicePassword.trim().isEmpty()) {
            logger.warn("设备 {} 的密码未配置，跳过该设备的回调配置", deviceIp);
            return;
        }

        logger.info("准备向设备 {} (URL: {}) 配置识别回调URL: {}", deviceIp, deviceBaseUrl, callbackUrl);

        try {
            // 调用服务层方法设置回调URL到设备
            DeviceResponse<String> response = zhangjmHttpService.setIdentifyCallbackUrl(deviceBaseUrl, devicePassword, callbackUrl);

            if (response != null && response.isSuccess()) {
                logger.info("成功向设备 {} 配置识别回调URL。设备返回: {}", deviceIp, response.getData());
            } else {
                String errorMsg = (response != null && response.getMsg() != null) ? response.getMsg() : "设备无明确成功响应或响应为空";
                logger.error("向设备 {} 配置识别回调URL失败。设备消息: {}", deviceIp, errorMsg);
            }
        } catch (Exception e) {
            // 捕获调用过程中可能发生的任何异常，例如网络连接问题
            logger.error("在配置设备 {} 识别回调URL时发生严重错误: {}", deviceIp, e.getMessage(), e);
        }
    }
}
