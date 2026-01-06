package com.attendance.framework.shiro.token;

import com.attendance.framework.shiro.enums.LoginType;
import org.apache.shiro.authc.UsernamePasswordToken;

public class CustomToken extends UsernamePasswordToken {

    private static final long serialVersionUID = -2564928913725078138L;

    private LoginType type;

    public CustomToken() {
        super();
    }

    /**
     * 免密登录
     */
    public CustomToken(String username) {
        super(username, "", false, null);
        this.type = LoginType.NOPASSWD;
    }

    /**
     * 账号密码登录
     */
    public CustomToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe, null);
        this.type = LoginType.PASSWORD;
    }

    public LoginType getType() {
        return type;
    }

    public void setType(LoginType type) {
        this.type = type;
    }

}
