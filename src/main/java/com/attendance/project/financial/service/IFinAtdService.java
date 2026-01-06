package com.attendance.project.financial.service;

import com.attendance.project.financial.domain.FinAtd;

import java.util.List;

/**
 * 考勤Service接口
 * 
 * @author ruoyi
 * @date 2024-12-02
 */
public interface IFinAtdService
{
    /**
     * 查询考勤
     * 
     * @param id 考勤主键
     * @return 考勤
     */
    public FinAtd selectFinAtdById(Long id);

    /**
     * 查询考勤列表
     * 
     * @param finAtd 考勤
     * @return 考勤集合
     */
    public List<FinAtd> selectFinAtdList(FinAtd finAtd);

    /**
     * 新增考勤
     * 
     * @param finAtd 考勤
     * @return 结果
     */
    public int insertFinAtd(FinAtd finAtd);

    public int updateFinAtd(FinAtd finAtd);

    /**
     * 批量删除考勤
     * 
     * @param ids 需要删除的考勤主键集合
     * @return 结果
     */
    public int deleteFinAtdByIds(String ids);

    /**
     * 删除考勤信息
     * 
     * @param id 考勤主键
     * @return 结果
     */
    public int deleteFinAtdById(Long id);

}
