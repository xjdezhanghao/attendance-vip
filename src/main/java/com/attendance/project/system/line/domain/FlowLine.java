package com.attendance.project.system.line.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 线性流程对象 sys_flow_line
 * 
 * @author ruoyi
 * @date 2024-11-06
 */
public class FlowLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 流程名称 */
    @Excel(name = "流程名称")
    private String flowname;

    /** 流程节点 */
    @Excel(name = "流程节点")
    private String flownode;

    @Excel(name = "流程编码")
    private String flowcode;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setFlowname(String flowname)
    {
        this.flowname = flowname;
    }

    public String getFlowname()
    {
        return flowname;
    }
    public void setFlownode(String flownode)
    {
        this.flownode = flownode;
    }

    public String getFlownode()
    {
        return flownode;
    }

    public String getFlowcode() {
        return flowcode;
    }

    public void setFlowcode(String flowcode) {
        this.flowcode = flowcode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("flowname", getFlowname())
            .append("flownode", getFlownode())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
