package com.attendance.project.performance.statistics.dto;

import com.attendance.framework.web.domain.BaseEntity;

/**
 * 绩效统计列表查询参数
 */
public class StatisticsOverviewQuery extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String startDate;
    private String endDate;
    private Long deptId;
    private Long userId;
    private String userName;
    private Long postId;

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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
