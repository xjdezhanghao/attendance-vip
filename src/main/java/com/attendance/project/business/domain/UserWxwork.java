package com.attendance.project.business.domain;
import com.attendance.framework.web.domain.BaseEntity;

public class UserWxwork extends BaseEntity{

    private String login_name;
    private String user_name;

    private String touser;

    public String getLoginName() {
        return login_name;
    }

    public void setLoginName(String login_name) {
        this.login_name = login_name;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }
}
