package com.attendance.project.performance.statistics.dto;

import com.attendance.framework.web.domain.BaseEntity;

/**
 * 绩效统计详情查询参数
 */
public class StatisticsDetailQuery extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String startDate;
    private String endDate;
    private String gatherDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getGatherDate() {
        return gatherDate;
    }

    public void setGatherDate(String gatherDate) {
        this.gatherDate = gatherDate;
    }
}
