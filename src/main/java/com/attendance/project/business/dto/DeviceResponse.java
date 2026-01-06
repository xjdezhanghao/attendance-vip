package com.attendance.project.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 通用设备响应DTO
 * @param <T> data字段的具体类型
 */
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略JSON中未知字段
public class DeviceResponse<T> {
    private int result;      // 接口是否调通, 1成功, 0失败
    private boolean success; // 操作是否成功, 成功为true, 失败为false
    private String code;     // 返回码
    private String msg;      // 返回信息
    private T data;          // 返回的数据对象 (可以是JsonNode或特定类型的对象)

    // 构造函数 (可以有多个)
    public DeviceResponse() {
    }

    // Getters and Setters
    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DeviceResponse{" +
                "result=" + result +
                ", success=" + success +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + (data != null ? data.toString() : "null") +
                '}';
    }
}
