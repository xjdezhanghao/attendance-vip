package com.attendance.project.performance.statistics.vo;

import java.util.List;

/**
 * 绩效统计详情视图
 */
public class StatisticsDetailView {
    private List<StatisticsProjectOverviewVO> overviewList;
    private List<List<StatisticsDetailItemVO>> detailList;

    public List<StatisticsProjectOverviewVO> getOverviewList() {
        return overviewList;
    }

    public void setOverviewList(List<StatisticsProjectOverviewVO> overviewList) {
        this.overviewList = overviewList;
    }

    public List<List<StatisticsDetailItemVO>> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<List<StatisticsDetailItemVO>> detailList) {
        this.detailList = detailList;
    }
}
