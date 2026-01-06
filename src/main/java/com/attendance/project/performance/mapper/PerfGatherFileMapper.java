package com.attendance.project.performance.mapper;

import java.util.List;
import com.attendance.project.performance.domain.PerfGatherFile;

/**
 * 绩效采集文件关联Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface PerfGatherFileMapper 
{
    /**
     * 查询绩效采集文件关联
     * 
     * @param fileId 绩效采集文件关联主键
     * @return 绩效采集文件关联
     */
    public PerfGatherFile selectPerfGatherFileByFileId(Long fileId);

    /**
     * 查询绩效采集文件关联列表
     * 
     * @param perfGatherFile 绩效采集文件关联
     * @return 绩效采集文件关联集合
     */
    public List<PerfGatherFile> selectPerfGatherFileList(PerfGatherFile perfGatherFile);

    /**
     * 新增绩效采集文件关联
     * 
     * @param perfGatherFile 绩效采集文件关联
     * @return 结果
     */
    public int insertPerfGatherFile(PerfGatherFile perfGatherFile);

    /**
     * 修改绩效采集文件关联
     * 
     * @param perfGatherFile 绩效采集文件关联
     * @return 结果
     */
    public int updatePerfGatherFile(PerfGatherFile perfGatherFile);

    /**
     * 删除绩效采集文件关联
     * 
     * @param fileId 绩效采集文件关联主键
     * @return 结果
     */
    public int deletePerfGatherFileByFileId(Long fileId);

    /**
     * 批量删除绩效采集文件关联
     * 
     * @param fileIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePerfGatherFileByFileIds(String[] fileIds);
}
