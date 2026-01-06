package com.attendance.project.financial.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 扣除对象 fin_deduct
 * 
 * @author ruoyi
 * @date 2024-12-04
 */
public class FinDeduct extends BaseEntity
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
    @Excel(name = "文件路径")
    private String filepath;

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
