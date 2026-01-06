package com.attendance.project.atdform.atdstatisticform.service;

import com.attendance.project.atdform.atdstatisticform.domain.AtdStatisticForm;

import java.util.List;

/**
 * 考勤表统计Service接口
 * 
 * @author wangchengyan
 * @date 2022-08-02
 */
public interface IAtdStatisticFormService 
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
     * 查询考勤表统计列表
     *
     * @param atdStatisticForm 考勤表统计
     * @return 考勤表统计集合
     */
    //public List<String>  selectNames( AtdStatisticForm atdStatisticForm);
    public List<Integer>  selectIds( AtdStatisticForm atdStatisticForm);

    public String selectNameById(Integer userid) ;
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
/*
    */
/**
     * 新增考勤表修改记录
     *
     * @return 结果
     *//*

    public int insertOperate( AtdOperate atdOperate);
    */
/**
     * 查询修改值
     *
     * @return 结果
     *//*

    public String selectOpervalue(Integer id ,String fieldValue,String atd) ;
*/


    /**
     * 批量删除考勤表统计
     * 
     * @param ids 需要删除的考勤表统计主键集合
     * @return 结果
     */
    public int deleteAtdStatisticFormByIds(String ids);

    /**
     * 删除考勤表统计信息
     * 
     * @param id 考勤表统计主键
     * @return 结果
     */
    public int deleteAtdStatisticFormById(Long id);


    //通过某段时间考勤记录计算某人是否核实出勤
    public boolean judgeUserHsatdByAtdList(List<AtdStatisticForm> dataList, String name, String time);

    public boolean judgeUserHschidaoByAtdList(List<AtdStatisticForm> dataList, String name, String time);

    public boolean judgeUserHszaotuiByAtdList(List<AtdStatisticForm> dataList, String name, String time);
}
