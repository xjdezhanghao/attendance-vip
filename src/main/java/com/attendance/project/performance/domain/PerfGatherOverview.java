package com.attendance.project.performance.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.attendance.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.attendance.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 绩效采集主对象 perf_gather_overview
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public class PerfGatherOverview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 采集记录ID */
    private Long overviewId;

    /** 组织架构ID（关联sys_dept） */
    @Excel(name = "组织架构ID", readConverterExp = "关=联sys_dept")
    private Long deptId;

    /** 员工ID（关联sys_user） */
    @Excel(name = "员工ID", readConverterExp = "关=联sys_user")
    private Long userId;

    /** 考核项目ID（关联perf_ind_project） */
    @Excel(name = "考核项目ID", readConverterExp = "关=联perf_ind_project")
    private Long projectId;

    /** 采集日期（格式：YYYY-MM-DD） */
    @Excel(name = "采集日期", readConverterExp = "格式：YYYY-MM-DD")
    private String gatherDate;

    /** 考核总分 */
    @Excel(name = "考核总分")
    private BigDecimal totalScore;

    /** 采集状态：0未采集 1采集中 2已提交 3已审核 */
    @Excel(name = "采集状态：0未采集 1采集中 2已提交 3已审核")
    private Integer gatherStatus;

    /** 采集人ID（关联sys_user） */
    @Excel(name = "采集人ID", readConverterExp = "关=联sys_user")
    private Long gatherId;

    /** 审核人ID（关联sys_user） */
    @Excel(name = "审核人ID", readConverterExp = "关=联sys_user")
    private Long auditId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    // 关联表字段
    private String userName;
    private String deptName;
    private String paramRank;
    private String paramPost;
    private String postName;
    private Long postId;

    public void setOverviewId(Long overviewId)
    {
        this.overviewId = overviewId;
    }

    public Long getOverviewId()
    {
        return overviewId;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public Long getProjectId()
    {
        return projectId;
    }
    public void setGatherDate(String gatherDate)
    {
        this.gatherDate = gatherDate;
    }

    public String getGatherDate()
    {
        return gatherDate;
    }
    public void setTotalScore(BigDecimal totalScore)
    {
        this.totalScore = totalScore;
    }

    public BigDecimal getTotalScore()
    {
        return totalScore;
    }
    public void setGatherStatus(Integer gatherStatus)
    {
        this.gatherStatus = gatherStatus;
    }

    public Integer getGatherStatus()
    {
        return gatherStatus;
    }
    public void setGatherId(Long gatherId)
    {
        this.gatherId = gatherId;
    }

    public Long getGatherId()
    {
        return gatherId;
    }
    public void setAuditId(Long auditId)
    {
        this.auditId = auditId;
    }

    public Long getAuditId()
    {
        return auditId;
    }
    public void setAuditTime(Date auditTime)
    {
        this.auditTime = auditTime;
    }

    public Date getAuditTime()
    {
        return auditTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getParamRank() {
        return paramRank;
    }

    public void setParamRank(String paramRank) {
        this.paramRank = paramRank;
    }

    public String getParamPost() {
        return paramPost;
    }

    public void setParamPost(String paramPost) {
        this.paramPost = paramPost;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("overviewId", getOverviewId())
            .append("deptId", getDeptId())
            .append("userId", getUserId())
            .append("projectId", getProjectId())
            .append("gatherDate", getGatherDate())
            .append("totalScore", getTotalScore())
            .append("gatherStatus", getGatherStatus())
            .append("gatherId", getGatherId())
            .append("auditId", getAuditId())
            .append("auditTime", getAuditTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
