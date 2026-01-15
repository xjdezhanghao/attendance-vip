package com.attendance.project.performance.statistics.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 绩效统计项目汇总原始行
 */
public class StatisticsProjectSummaryRow {
    private Long overviewId;
    private Long projectId;
    private String projectName;
    private BigDecimal totalScore;
    private String remark;
    private Date createTime;
    private String gatherDate;

    public Long getOverviewId() {
        return overviewId;
    }

    public void setOverviewId(Long overviewId) {
        this.overviewId = overviewId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGatherDate() {
        return gatherDate;
    }

    public void setGatherDate(String gatherDate) {
        this.gatherDate = gatherDate;
    }
}
