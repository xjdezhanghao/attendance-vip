package com.attendance.project.financial.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 财务参数对象 fin_param
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
public class FinParam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 参数名称 */
    @Excel(name = "参数名称")
    private String paramName;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptid;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setParamName(String paramName)
    {
        this.paramName = paramName;
    }

    public String getParamName()
    {
        return paramName;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("paramName", getParamName())
            .append("deptid", getDeptid())
            .append("deptName", getDeptName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
