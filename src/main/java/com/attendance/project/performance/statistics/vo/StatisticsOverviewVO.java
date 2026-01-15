package com.attendance.project.performance.statistics.vo;

import java.math.BigDecimal;

/**
 * 绩效统计列表视图对象
 */
public class StatisticsOverviewVO {
    private Long userId;
    private String userName;
    private Long deptId;
    private String deptName;
    private Long postId;
    private String postName;
    private String gatherDate;
    private BigDecimal totalScore;

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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getGatherDate() {
        return gatherDate;
    }

    public void setGatherDate(String gatherDate) {
        this.gatherDate = gatherDate;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }
}
