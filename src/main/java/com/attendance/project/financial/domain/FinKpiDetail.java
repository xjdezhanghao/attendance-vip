package com.attendance.project.financial.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 绩效详情对象 fin_kpi_detail
 * 
 * @author ruoyi
 * @date 2024-12-02
 */
public class FinKpiDetail extends BaseEntity
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

    /** 绩效等级 */
    @Excel(name = "绩效等级")
    private String jxdj;

    /** 奖励绩效 */
    @Excel(name = "奖励绩效")
    private String jljx;

    /** 扣发绩效 */
    @Excel(name = "扣发绩效")
    private String kfjx;

    /** 考核总分 */
    @Excel(name = "考核总分")
    private String khzf;

    /** 奖励分 */
    @Excel(name = "奖励分")
    private String jlf;

    /** 扣罚分 */
    @Excel(name = "扣罚分")
    private String kff;

    /** 奖惩事由 */
    @Excel(name = "奖惩事由")
    private String content;

    /** 否决指标 */
    @Excel(name = "否决指标")
    private String fjzb;

    /** 外键 */
    @Excel(name = "外键")
    private Long pid;

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
    public void setJxdj(String jxdj)
    {
        this.jxdj = jxdj;
    }

    public String getJxdj()
    {
        return jxdj;
    }
    public void setJljx(String jljx)
    {
        this.jljx = jljx;
    }

    public String getJljx()
    {
        return jljx;
    }
    public void setKfjx(String kfjx)
    {
        this.kfjx = kfjx;
    }

    public String getKfjx()
    {
        return kfjx;
    }
    public void setKhzf(String khzf)
    {
        this.khzf = khzf;
    }

    public String getKhzf()
    {
        return khzf;
    }
    public void setJlf(String jlf)
    {
        this.jlf = jlf;
    }

    public String getJlf()
    {
        return jlf;
    }
    public void setKff(String kff)
    {
        this.kff = kff;
    }

    public String getKff()
    {
        return kff;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setFjzb(String fjzb)
    {
        this.fjzb = fjzb;
    }

    public String getFjzb()
    {
        return fjzb;
    }
    public void setPid(Long pid)
    {
        this.pid = pid;
    }

    public Long getPid()
    {
        return pid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userid", getUserid())
            .append("userName", getUserName())
            .append("deptid", getDeptid())
            .append("deptName", getDeptName())
            .append("jxdj", getJxdj())
            .append("jljx", getJljx())
            .append("kfjx", getKfjx())
            .append("khzf", getKhzf())
            .append("jlf", getJlf())
            .append("kff", getKff())
            .append("content", getContent())
            .append("fjzb", getFjzb())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("pid", getPid())
            .toString();
    }
}
