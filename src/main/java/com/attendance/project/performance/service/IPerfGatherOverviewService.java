package com.attendance.project.performance.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.attendance.project.performance.domain.PerfGatherOverview;

/**
 * 绩效采集主Service接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface IPerfGatherOverviewService 
{
    /**
     * 查询绩效采集主
     * 
     * @param overviewId 绩效采集主主键
     * @return 绩效采集主
     */
    public PerfGatherOverview selectPerfGatherOverviewByOverviewId(Long overviewId);

    /**
     * 查询绩效采集主列表
     * 
     * @param perfGatherOverview 绩效采集主
     * @return 绩效采集主集合
     */
    public List<PerfGatherOverview> selectPerfGatherOverviewList(PerfGatherOverview perfGatherOverview);

    public List<PerfGatherOverview> selectPerfGatherOverviewListAll(PerfGatherOverview perfGatherOverview);


    /**
     * 新增绩效采集主
     * 
     * @param perfGatherOverview 绩效采集主
     * @return 结果
     */
    public int insertPerfGatherOverview(PerfGatherOverview perfGatherOverview);

    /**
     * 修改绩效采集主
     * 
     * @param perfGatherOverview 绩效采集主
     * @return 结果
     */
    public int updatePerfGatherOverview(PerfGatherOverview perfGatherOverview);

    /**
     * 批量删除绩效采集主
     * 
     * @param overviewIds 需要删除的绩效采集主主键集合
     * @return 结果
     */
    public int deletePerfGatherOverviewByOverviewIds(String overviewIds);

    /**
     * 删除绩效采集主信息
     * 
     * @param overviewId 绩效采集主主键
     * @return 结果
     */
    public int deletePerfGatherOverviewByOverviewId(Long overviewId);

    /*
     * 定时任务相关
     */
    /**
     * 生成每日考核采集记录
     */
    void generateDateGatherRecords();

    /**
     * 批量更新考核项的评分、备注和图片路径
     * @param overviewId 概览ID
     * @param scores 评分映射
     * @param remarks 备注映射
     * @param imagePaths 图片路径映射
     */
    public void updateScoresAndRemarks(Long overviewId, Map<Long, BigDecimal> scores, Map<Long, String> remarks, Map<Long, String> imagePaths);

}
