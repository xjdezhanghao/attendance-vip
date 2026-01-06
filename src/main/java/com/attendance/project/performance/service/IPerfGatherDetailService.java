package com.attendance.project.performance.service;

import java.util.List;
import com.attendance.project.performance.domain.PerfGatherDetail;

/**
 * 绩效小项采集Service接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface IPerfGatherDetailService 
{
    /**
     * 查询绩效小项采集
     * 
     * @param detailId 绩效小项采集主键
     * @return 绩效小项采集
     */
    public PerfGatherDetail selectPerfGatherDetailByDetailId(Long detailId);

    /**
     * 查询绩效小项采集列表
     * 
     * @param perfGatherDetail 绩效小项采集
     * @return 绩效小项采集集合
     */
    public List<PerfGatherDetail> selectPerfGatherDetailList(PerfGatherDetail perfGatherDetail);

    /**
     * 新增绩效小项采集
     * 
     * @param perfGatherDetail 绩效小项采集
     * @return 结果
     */
    public int insertPerfGatherDetail(PerfGatherDetail perfGatherDetail);

    /**
     * 修改绩效小项采集
     * 
     * @param perfGatherDetail 绩效小项采集
     * @return 结果
     */
    public int updatePerfGatherDetail(PerfGatherDetail perfGatherDetail);

    /**
     * 批量删除绩效小项采集
     * 
     * @param detailIds 需要删除的绩效小项采集主键集合
     * @return 结果
     */
    public int deletePerfGatherDetailByDetailIds(String detailIds);

    /**
     * 删除绩效小项采集信息
     * 
     * @param detailId 绩效小项采集主键
     * @return 结果
     */
    public int deletePerfGatherDetailByDetailId(Long detailId);
}
