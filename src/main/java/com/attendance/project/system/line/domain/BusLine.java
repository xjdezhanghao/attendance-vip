package com.attendance.project.system.line.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * sys_bus_line
 * 
 * @author ruoyi
 * @date 2025-01-07
 */
public class BusLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 业务名称 */
    @Excel(name = "业务名称")
    private String busName;

    /** 流程主键 */
    @Excel(name = "流程主键")
    private Long lineId;

    /** 岗位代码 */
    @Excel(name = "岗位代码")
    private String postCode;

    private List<String> postCodes;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setBusName(String busName)
    {
        this.busName = busName;
    }

    public String getBusName()
    {
        return busName;
    }
    public void setLineId(Long lineId)
    {
        this.lineId = lineId;
    }

    public Long getLineId()
    {
        return lineId;
    }
    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }

    public String getPostCode()
    {
        return postCode;
    }

    public List<String> getPostCodes() {
        return postCodes;
    }

    public void setPostCodes(List<String> postCodes) {
        this.postCodes = postCodes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("busName", getBusName())
            .append("lineId", getLineId())
            .append("postCode", getPostCode())
            .toString();
    }
}
