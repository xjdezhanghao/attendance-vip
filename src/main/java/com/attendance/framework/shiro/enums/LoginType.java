package com.attendance.framework.shiro.enums;

public enum LoginType {
    /** 密码登录 */
    PASSWORD("PASSWORD"),
    /** 密码登录 */
    NOPASSWD("NOPASSWORD");
    /** 状态值 */
    private String code;

    private LoginType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
