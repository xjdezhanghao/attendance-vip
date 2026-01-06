package com.attendance.project.financial.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 财务参数细则对象 fin_param_detail
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
public class FinParamDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 参数名称 */
    @Excel(name = "参数名称")
    private String detailName;

    /** 参数编码 */
    @Excel(name = "参数编码")
    private String detailCode;

    /** 参数值 */
    @Excel(name = "参数键")
    private String detailKey;

    /** 参数值 */
    @Excel(name = "参数值")
    private String detailValue;

    /** 参数类型 */
    @Excel(name = "参数类型")
    private String detailType;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    private Long pid;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDetailName(String detailName)
    {
        this.detailName = detailName;
    }

    public String getDetailName()
    {
        return detailName;
    }
    public void setDetailCode(String detailCode)
    {
        this.detailCode = detailCode;
    }

    public String getDetailCode()
    {
        return detailCode;
    }
    public void setDetailValue(String detailValue)
    {
        this.detailValue = detailValue;
    }

    public String getDetailKey() {
        return detailKey;
    }

    public void setDetailKey(String detailKey) {
        this.detailKey = detailKey;
    }

    public String getDetailValue()
    {
        return detailValue;
    }
    public void setDetailType(String detailType)
    {
        this.detailType = detailType;
    }

    public String getDetailType()
    {
        return detailType;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("detailName", getDetailName())
            .append("detailCode", getDetailCode())
            .append("detailValue", getDetailValue())
            .append("detailType", getDetailType())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
                .append("pid", getPid())
            .toString();
    }
}
