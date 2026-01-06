package com.attendance.project.atdform.atdstatisticform.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 排班计划对象 atd_plan
 * 
 * @author ruoyi
 * @date 2025-01-10
 */
public class AtdPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户主键 */
    @Excel(name = "用户主键")
    private Long userId;

    /** 用户姓名 */
    @Excel(name = "用户姓名")
    private String userName;

    /** 部门主键 */
    @Excel(name = "部门主键")
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 计划日期 */
    @Excel(name = "计划日期")
    private String planDate;

    /** 是否生效 */
    @Excel(name = "是否生效")
    private String enable;

    /** 计划内容 */
    @Excel(name = "计划内容")
    private String content;

    private String startDate;

    private String endDate;

    private List<String> dateList;

    private List<Integer> userIdList;

    private Boolean autoGenerate;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
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
    public void setPlanDate(String planDate)
    {
        this.planDate = planDate;
    }

    public String getPlanDate()
    {
        return planDate;
    }
    public void setEnable(String enable)
    {
        this.enable = enable;
    }

    public String getEnable()
    {
        return enable;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<Integer> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Integer> userIdList) {
        this.userIdList = userIdList;
    }

    public Boolean getAutoGenerate() {
        return autoGenerate;
    }

    public void setAutoGenerate(Boolean autoGenerate) {
        this.autoGenerate = autoGenerate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("planDate", getPlanDate())
            .append("enable", getEnable())
            .append("content", getContent())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
