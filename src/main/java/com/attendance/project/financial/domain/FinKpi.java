package com.attendance.project.financial.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 绩效对象 fin_kpi
 * 
 * @author ruoyi
 * @date 2024-12-02
 */
public class FinKpi extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 记录名称 */
    @Excel(name = "记录名称")
    private String recname;

    /** 记录时间 */
    @Excel(name = "记录时间")
    private String rectime;

    /** 记录类型 */
    @Excel(name = "记录类型")
    private String rectype;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptid;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 文件路径 */
    private String filepath;

    //流程相关：审核状态
    private String opstatus;

    //流程相关：最后操作时间
    private Date maxtime;

    private Long[] ids;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setRecname(String recname)
    {
        this.recname = recname;
    }

    public String getRecname()
    {
        return recname;
    }
    public void setRectime(String rectime)
    {
        this.rectime = rectime;
    }

    public String getRectime()
    {
        return rectime;
    }
    public void setRectype(String rectype)
    {
        this.rectype = rectype;
    }

    public String getRectype()
    {
        return rectype;
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
    public void setFilepath(String filepath)
    {
        this.filepath = filepath;
    }

    public String getFilepath()
    {
        return filepath;
    }

    public String getOpstatus() {
        return opstatus;
    }

    public void setOpstatus(String opstatus) {
        this.opstatus = opstatus;
    }

    public Date getMaxtime() {
        return maxtime;
    }

    public void setMaxtime(Date maxtime) {
        this.maxtime = maxtime;
    }

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("recname", getRecname())
            .append("rectime", getRectime())
            .append("rectype", getRectype())
            .append("deptid", getDeptid())
            .append("deptName", getDeptName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("filepath", getFilepath())
            .toString();
    }
}
