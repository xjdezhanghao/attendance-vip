package com.attendance.project.performance.mapper;

import java.util.List;
import com.attendance.project.performance.domain.PerfGatherOverview;

/**
 * 绩效采集主Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface PerfGatherOverviewMapper 
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
     * 删除绩效采集主
     * 
     * @param overviewId 绩效采集主主键
     * @return 结果
     */
    public int deletePerfGatherOverviewByOverviewId(Long overviewId);

    /**
     * 批量删除绩效采集主
     * 
     * @param overviewIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePerfGatherOverviewByOverviewIds(String[] overviewIds);
}
