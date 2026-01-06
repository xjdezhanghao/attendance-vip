package com.attendance.project.business.controller;

import java.util.HashMap;
import java.util.Map;

import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.framework.config.ZmhConfig;
import com.attendance.project.business.domain.AtdRecord;
import com.attendance.project.business.service.IAtdRecordService;

/**
 * 用于接收和处理来自掌静脉/人脸识别设备HTTP回调的Controller。
 */
@RestController
@RequestMapping("/business/callback") // 定义此Controller所有请求的基础路径
public class ZhangjmCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(ZhangjmCallbackController.class);

    private final IAtdRecordService atdRecordService;
    private final ZmhConfig zmhConfig;
    @Autowired
    private IUserService userService;

    @Autowired
    public ZhangjmCallbackController(IAtdRecordService atdRecordService, ZmhConfig zmhConfig) {
        this.atdRecordService = atdRecordService;
        this.zmhConfig = zmhConfig;
    }

    /**
     * 处设备识别成功后的回调请求。
     * 设备文档P14-P15 "识别回调请求参数说明"
     * 设备会向此URL (例如: /api/device/callback/attendance) 发送POST请求。
     * 请求体格式为 application/x-www-form-urlencoded。理
     *
     * @param deviceKey      设备序列号
     * @param personId       人员ID (设备端识别出的人员ID)
     * @param time           识别记录时间戳 (设备时间为准, 毫秒级字符串)
     * @param type           识别类型 (例如 "palm_0", "face_0")
     * @param imgBase64      抓拍照片的Base64编码字符串 (可选)
     * @param data           附加数据 (可选, 内容根据识别模式变化)
     * @param ip             设备局域网IP地址
     * @param searchScore    识别比分 (可选, 供参考)
     * @param livenessScore  活体比分 (可选, 供参考)
     * @param temperature    体温数据 (可选)
     * @param temperatureState 体温状态 (可选)
     * @param mask           口罩佩戴状态 (可选)
     * @return ResponseEntity 包含标准响应的Map对象 ({"result":1, "code":"000"})
     */
    @PostMapping("/attendance") // 具体的子路径，例如用于考勤回调
    public ResponseEntity<Map<String, Object>> handleAttendanceCallback(
            @RequestParam String deviceKey,
            @RequestParam String personId,
            @RequestParam String time, // 注意：这里是字符串，需要转换为long或Date
            @RequestParam String type,
            @RequestParam(required = false) String imgBase64,
            @RequestParam(required = false) String data, // 例如人证模式的身份证json，刷卡模式的IC卡号等
            @RequestParam String ip,
            @RequestParam(required = false) String searchScore,
            @RequestParam(required = false) String livenessScore,
            @RequestParam(required = false) String temperature,
            @RequestParam(required = false) String temperatureState,
            @RequestParam(required = false) String mask
            // 可以根据实际需要添加或移除更多 @RequestParam
    ) {
        logger.info("收到设备回调: deviceKey={}, personId={}, time={}, type={}, ip={}",
                deviceKey, personId, time, type, ip);
        logger.debug("详细回调参数: imgBase64 (length)={}, data={}, searchScore={}, livenessScore={}, temperature={}, temperatureState={}, mask={}",
                imgBase64 != null ? imgBase64.length() : "null", data, searchScore, livenessScore, temperature, temperatureState, mask);

        Map<String, Object> responseMap = new HashMap<>();

        try {
            // 1. 初步参数校验和解析
            if (personId == null || personId.isEmpty() || "STRANGERBABY".equalsIgnoreCase(personId)) {
                logger.warn("无效的personId或陌生人回调: {}", personId);
                responseMap.put("result", 1); // 仍告诉设备收到，但业务上可能不处理或特殊处理
                responseMap.put("code", "000"); // 或者一个特定的代码表示“已收到但不处理”
                responseMap.put("msg", "无效用户ID或陌生人");
                return ResponseEntity.ok(responseMap);
            }

            long recordTimeMillis;
            try {
                recordTimeMillis = Long.parseLong(time);
            } catch (NumberFormatException e) {
                logger.error("时间戳格式错误: {}", time, e);
                responseMap.put("result", 0);
                responseMap.put("code", "CALLBACK_PARAM_ERROR");
                responseMap.put("msg", "时间戳格式错误");
                return ResponseEntity.badRequest().body(responseMap);
            }

            User user = userService.selectUserByIdcard(personId);

            if (user != null){
                // 2. 将数据传递给Service层进行业务处理
                // 这里我们假设回调主要是为了记录考勤
                AtdRecord atdRecord = new AtdRecord();
                atdRecord.setUserid(user.getUserId()); // 假设personId可以安全地转为Long

                // 根据设备IP地址设置atdloc
                String atdloc = zmhConfig.getAtdlocByIp(ip);
                logger.debug("根据设备IP: {} 设置atdloc: {}", ip, atdloc);
                atdRecord.setAtdloc(atdloc);

                // 调用业务服务处理考勤记录
                atdRecordService.atdRecordProcess(atdRecord);
                logger.info("考勤记录已成功传递给服务层处理 for personId: {}", personId);
            }

            // 3. 按设备API文档要求，返回成功响应
            // 设备文档 P17 "识别回调请求响应字符串": {"result":1,"code": "000"}
            responseMap.put("result", 1);
            responseMap.put("code", "000");
            responseMap.put("msg", "回调处理成功"); // 可选的消息
            return ResponseEntity.ok(responseMap);

        } catch (NumberFormatException e) {
            logger.error("处理回调时personId转Long失败: {}", personId, e);
            responseMap.put("result", 0);
            responseMap.put("code", "CALLBACK_DATA_INVALID");
            responseMap.put("msg", "人员ID格式错误");
            return ResponseEntity.badRequest().body(responseMap);
        } catch (Exception e) {
            logger.error("处理设备回调时发生未知错误: {}", e.getMessage(), e);
            responseMap.put("result", 0); // 表示处理失败
            responseMap.put("code", "CALLBACK_INTERNAL_ERROR");
            responseMap.put("msg", "服务器内部错误，无法处理回调");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

    // 未来可以根据需要添加其他回调端点，例如：
    // @PostMapping("/heartbeat")
    // public ResponseEntity<Map<String, Object>> handleHeartbeatCallback(...) {}
    //
    // @PostMapping("/photoRegistration")
    // public ResponseEntity<Map<String, Object>> handlePhotoRegistrationCallback(...) {}
}
