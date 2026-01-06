package com.attendance.project.atdform.atdstatisticform.service.impl;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.project.atdform.atdstatisticform.domain.AtdStatisticForm;
import com.attendance.project.atdform.atdstatisticform.mapper.AtdStatisticFormMapper;
import com.attendance.project.atdform.atdstatisticform.service.IAtdStatisticFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 考勤表统计Service业务层处理
 * 
 * @author wangchengyan
 * @date 2022-08-02
 */
@Service
public class AtdStatisticFormServiceImpl implements IAtdStatisticFormService 
{
    @Autowired
    private AtdStatisticFormMapper atdStatisticFormMapper;

    /**
     * 查询考勤表统计
     * 
     * @param id 考勤表统计主键
     * @return 考勤表统计
     */
    @Override
    public AtdStatisticForm selectAtdStatisticFormById(Long id)
    {
        return atdStatisticFormMapper.selectAtdStatisticFormById(id);
    }

    /**
     * 查询考勤表统计列表
     * 
     * @param atdStatisticForm 考勤表统计
     * @return 考勤表统计
     */
    @Override
    public List<AtdStatisticForm> selectAtdStatisticFormList(AtdStatisticForm atdStatisticForm)
    {
        return atdStatisticFormMapper.selectAtdStatisticFormList(atdStatisticForm);
    }

/*    @Override
    public List<String> selectNames(AtdStatisticForm atdStatisticForm) {
        return atdStatisticFormMapper.selectNames(atdStatisticForm);
    }*/
    @Override
    public List<Integer> selectIds(AtdStatisticForm atdStatisticForm) {
        return atdStatisticFormMapper.selectIds(atdStatisticForm);
    }
    @Override
    public String selectNameById(Integer userid) {
        return atdStatisticFormMapper.selectNameById(userid);
    }
    /**
     * 新增考勤表统计
     * 
     * @param atdStatisticForm 考勤表统计
     * @return 结果
     */
    @Override
    public int insertAtdStatisticForm(AtdStatisticForm atdStatisticForm)
    {
        atdStatisticForm.setCreateTime(DateUtils.getNowDate());
        return atdStatisticFormMapper.insertAtdStatisticForm(atdStatisticForm);
    }

    @Override
    public AtdStatisticForm selectAtdStatistic(AtdStatisticForm atdStatisticForm)
    {

        return atdStatisticFormMapper.selectAtdStatistic(atdStatisticForm);
    }

    /**
     * 修改考勤表统计
     * 
     * @param atdStatisticForm 考勤表统计
     * @return 结果
     */
    @Override
    public int updateAtdStatisticForm(AtdStatisticForm atdStatisticForm)
    {
        return atdStatisticFormMapper.updateAtdStatisticForm(atdStatisticForm);
    }

/*    *//**
     * 查询修改值
     *
     * @return 结果
     *//*
    @Override
    public String selectOpervalue(Integer id ,String fieldValue,String atd) {
        return atdStatisticFormMapper.selectOpervalue( id , fieldValue, atd);
    }*/
    /**
     * 批量删除考勤表统计
     * 
     * @param ids 需要删除的考勤表统计主键
     * @return 结果
     */
    @Override
    public int deleteAtdStatisticFormByIds(String ids)
    {
        return atdStatisticFormMapper.deleteAtdStatisticFormByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除考勤表统计信息
     * 
     * @param id 考勤表统计主键
     * @return 结果
     */
    @Override
    public int deleteAtdStatisticFormById(Long id)
    {
        return atdStatisticFormMapper.deleteAtdStatisticFormById(id);
    }

    @Override
    public boolean judgeUserHsatdByAtdList(List<AtdStatisticForm> dataList, String name, String time) {
        boolean result = false;
        //2、核实出勤
        List<AtdStatisticForm> dataList12 = dataList.stream()
                // 首先过滤掉null元素
                .filter(Objects::nonNull)
                // 然后根据条件过滤
                .filter(item -> name.equals(item.getUsername()) && item.getTime().equals(time) &&
                        ((item.getAtddesckj() == null || (item.getAtddesckj().contains(":") || item.getAtddesckj().contains("：") || item.getAtddesckj().contains("忘") || item.getAtddesckj().contains("差")))||(
                                !"0".equals(item.getAtdstatus())
                        )))
                // 使用Collectors.toMap来基于username去重，保留第一次出现的元素
                .collect(Collectors.toMap(
                        AtdStatisticForm::getUsername, // 作为键的函数
                        item -> item, // 作为值的函数
                        (existing, replacement) -> existing // 如果键冲突，保留现有的元素
                ))
                // 将Map的值转换回List
                .values()
                .stream()
                .collect(Collectors.toList());
        if( dataList12.size()!=0){
            result = true;
        }
        return result;
    }

    @Override
    public boolean judgeUserHschidaoByAtdList(List<AtdStatisticForm> dataList, String name, String time) {
        boolean result1 = false;
        List<AtdStatisticForm> dataList4 =  dataList.stream()
                .filter(item->name.equals(item.getUsername()) && item.getTime().equals(time) && ("1".equals(item.getAtdstatus())) && (item.getAtddesckj() == null))
                .distinct()
                .collect(Collectors.toList());
        if (dataList4.size() > 0) {
            result1 = true;
        }

        boolean result2 = false;
        //4.2只有下班有数据（含只有下班备注的）
        List<AtdStatisticForm> dataList41 =  dataList.stream()
                .filter(item->name.equals(item.getUsername()) && item.getTime().equals(time))
                .distinct()
                .collect(Collectors.toList());
        if(dataList41.size()==1 &&"2".equals(dataList41.get(0).getAtdtag())){
            result2 = true;
        }
        return result1||result2;
    }

    @Override
    public boolean judgeUserHszaotuiByAtdList(List<AtdStatisticForm> dataList, String name, String time) {
        boolean result1 = false;
        List<AtdStatisticForm> dataList5 =  dataList.stream()
                .filter(item->name.equals(item.getUsername()) && item.getTime().equals(time) && ("2".equals(item.getAtdstatus())) && (item.getAtddesckj() == null ))
                .distinct()
                .collect(Collectors.toList());

        if (dataList5.size() > 0){
            result1 = true;
        }

        boolean result2 = false;
        //4.2只有下班有数据（含只有下班备注的）
        List<AtdStatisticForm> dataList51 =  dataList.stream()
                .filter(item->name.equals(item.getUsername()) && item.getTime().equals(time) )
                .distinct()
                .collect(Collectors.toList());
        if(dataList51.size()==1 &&"1".equals(dataList51.get(0).getAtdtag())){
            result2 = true;
        }
        return result1 || result2;
    }
}
