package com.attendance.project.atdform.atdstatisticform.mapper;

import java.util.List;

import com.attendance.framework.aspectj.lang.annotation.DataScope;
import com.attendance.project.atdform.atdstatisticform.domain.AtdStatisticForm;

/**
 * 考勤表统计Mapper接口
 * 
 * @author wangchengyan
 * @date 2022-08-02
 */
public interface AtdStatisticFormMapper 
{
    /**
     * 查询考勤表统计
     * 
     * @param id 考勤表统计主键
     * @return 考勤表统计
     */
    public AtdStatisticForm selectAtdStatisticFormById(Long id);

    /**
     * 查询考勤表统计列表
     * 
     * @param atdStatisticForm 考勤表统计
     * @return 考勤表统计集合
     */
    public List<AtdStatisticForm> selectAtdStatisticFormList(AtdStatisticForm atdStatisticForm);


    /**
     * 查询名字
     *
     * @param atdStatisticForm 考勤表统计
     * @return 考勤表统计集合
     */
   // public List<String> selectNames( AtdStatisticForm atdStatisticForm);
    @DataScope(deptAlias = "sys_dept", userAlias = "sys_user")
    public List<Integer> selectIds( AtdStatisticForm atdStatisticForm);

    public String selectNameById(Integer userid);

    /**
     * 新增考勤表统计
     * 
     * @param atdStatisticForm 考勤表统计
     * @return 结果
     */
    public int insertAtdStatisticForm(AtdStatisticForm atdStatisticForm);
    public AtdStatisticForm selectAtdStatistic(AtdStatisticForm atdStatisticForm);

    /**
     * 修改考勤表统计
     * 
     * @param atdStatisticForm 考勤表统计
     * @return 结果
     */
    public int updateAtdStatisticForm(AtdStatisticForm atdStatisticForm);

    /**
     * 查询修改值
     *
     * @return 结果
     */

    public String selectOpervalue(Integer id ,String fieldValue,String atd);
    /**
     * 删除考勤表统计
     * 
     * @param id 考勤表统计主键
     * @return 结果
     */
    public int deleteAtdStatisticFormById(Long id);

    /**
     * 批量删除考勤表统计
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAtdStatisticFormByIds(String[] ids);
}
