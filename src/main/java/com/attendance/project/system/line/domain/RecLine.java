package com.attendance.project.system.line.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 记录流程对象 sys_rec_line
 * 
 * @author ruoyi
 * @date 2024-11-16
 */
public class RecLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 流程ID */
    @Excel(name = "流程ID")
    private Long lineId;

    /** 记录ID */
    @Excel(name = "记录ID")
    private Long recId;

    /** 记录类型 */
    @Excel(name = "记录类型")
    private String recType;
    private String recUrl;

    /** 流程序号 */
    @Excel(name = "流程序号")
    private Integer lineIndex;

    /** 流程节点 */
    @Excel(name = "流程节点")
    private String lineNode;

    /** 操作人 */
    @Excel(name = "操作人")
    private String operateBy;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /** 操作状态 */
    @Excel(name = "操作状态")
    private String operateStatus;

    /** 操作备注 */
    @Excel(name = "操作备注")
    private String operateContent;

    private String createName;
    private String operateName;
    private String deptName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setLineId(Long lineId)
    {
        this.lineId = lineId;
    }

    public Long getLineId()
    {
        return lineId;
    }
    public void setRecId(Long recId)
    {
        this.recId = recId;
    }

    public Long getRecId()
    {
        return recId;
    }
    public void setRecType(String recType)
    {
        this.recType = recType;
    }

    public String getRecType()
    {
        return recType;
    }
    public void setLineIndex(Integer lineIndex)
    {
        this.lineIndex = lineIndex;
    }

    public String getRecUrl() {
        return recUrl;
    }

    public void setRecUrl(String recUrl) {
        this.recUrl = recUrl;
    }

    public Integer getLineIndex()
    {
        return lineIndex;
    }
    public void setLineNode(String lineNode)
    {
        this.lineNode = lineNode;
    }

    public String getLineNode()
    {
        return lineNode;
    }
    public void setOperateBy(String operateBy)
    {
        this.operateBy = operateBy;
    }

    public String getOperateBy()
    {
        return operateBy;
    }
    public void setOperateTime(Date operateTime)
    {
        this.operateTime = operateTime;
    }

    public Date getOperateTime()
    {
        return operateTime;
    }
    public void setOperateStatus(String operateStatus)
    {
        this.operateStatus = operateStatus;
    }

    public String getOperateStatus()
    {
        return operateStatus;
    }
    public void setOperateContent(String operateContent)
    {
        this.operateContent = operateContent;
    }

    public String getOperateContent()
    {
        return operateContent;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("lineId", getLineId())
            .append("recId", getRecId())
            .append("recType", getRecType())
            .append("lineIndex", getLineIndex())
            .append("lineNode", getLineNode())
            .append("operateBy", getOperateBy())
            .append("operateTime", getOperateTime())
            .append("operateStatus", getOperateStatus())
            .append("operateContent", getOperateContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
