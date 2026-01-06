package com.attendance.project.atwork.working.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 在岗人员统计对象 working
 * 
 * @author ruoyi
 * @date 2022-07-28
 */
public class working extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long userid;

    /**  */
    @Excel(name = "")
    private String username;

    /**  */
    private Long deptid;

    /**  */
    @Excel(name = "")
    private String atdtag;

    /**  */
    @Excel(name = "")
    private String deptname;

    /**  */
    @Excel(name = "")
    private String phone;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createtime;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getDeptid() {
        return deptid;
    }

    public void setDeptid(Long deptid) {
        this.deptid = deptid;
    }

    public String getAtdtag() {
        return atdtag;
    }

    public void setAtdtag(String atdtag) {
        this.atdtag = atdtag;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("userid", getUserid())
            .append("username", getUsername())
            .append("deptname", getDeptname())
            .append("phone", getPhone())
                .append("deptid", getDeptid())
                .append("atdtag", getAtdtag())
                .append("createtime", getCreatetime())
            .toString();
    }
}
