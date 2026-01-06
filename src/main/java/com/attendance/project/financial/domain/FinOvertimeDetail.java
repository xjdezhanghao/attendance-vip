package com.attendance.project.financial.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 加班申请详情对象 fin_overtime_detail
 * 
 * @author ruoyi
 * @date 2024-11-08
 */
public class FinOvertimeDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userid;

    /** 用户姓名 */
    @Excel(name = "用户姓名")
    private String userName;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptid;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 身份证 */
    @Excel(name = "身份证")
    private String idcard;

    /** 加班时间 */
    @Excel(name = "加班时间")
    private String overtime;

    @Excel(name = "加班天数")
    private String overdays;

    /** 加班事项 */
    @Excel(name = "加班事项")
    private String content;

    /** 外键 */
    @Excel(name = "外键")
    private Long oid;

    @Excel(name = "加班类型")
    private String overtype;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserid(Long userid)
    {
        this.userid = userid;
    }

    public Long getUserid()
    {
        return userid;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setDeptid(Long deptid)
    {
        this.deptid = deptid;
    }

    public Long getDeptid()
    {
        return deptid;
    }
    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDeptName()
    {
        return deptName;
    }
    public void setIdcard(String idcard)
    {
        this.idcard = idcard;
    }

    public String getIdcard()
    {
        return idcard;
    }
    public void setOvertime(String overtime)
    {
        this.overtime = overtime;
    }

    public String getOvertime()
    {
        return overtime;
    }

    public String getOverdays() {
        return overdays;
    }

    public void setOverdays(String overdays) {
        this.overdays = overdays;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setOid(Long oid)
    {
        this.oid = oid;
    }

    public Long getOid()
    {
        return oid;
    }

    public String getOvertype() {
        return overtype;
    }

    public void setOvertype(String overtype) {
        this.overtype = overtype;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userid", getUserid())
            .append("userName", getUserName())
            .append("deptid", getDeptid())
            .append("deptName", getDeptName())
            .append("idcard", getIdcard())
            .append("overtime", getOvertime())
                .append("overdays", getOverdays())
                .append("overtype", getOvertype())
            .append("content", getContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("oid", getOid())
            .toString();
    }
}
