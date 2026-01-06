package com.attendance.project.financial.mapper;

import com.attendance.project.financial.domain.FinSalRec;

import java.util.List;

/**
 * 工资记录Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-21
 */
public interface FinSalRecMapper 
{
    /**
     * 查询工资记录
     * 
     * @param id 工资记录主键
     * @return 工资记录
     */
    public FinSalRec selectFinSalRecById(Long id);

    /**
     * 查询工资记录列表
     * 
     * @param finSalRec 工资记录
     * @return 工资记录集合
     */
    public List<FinSalRec> selectFinSalRecList(FinSalRec finSalRec);

    /**
     * 新增工资记录
     * 
     * @param finSalRec 工资记录
     * @return 结果
     */
    public int insertFinSalRec(FinSalRec finSalRec);

    /**
     * 修改工资记录
     * 
     * @param finSalRec 工资记录
     * @return 结果
     */
    public int updateFinSalRec(FinSalRec finSalRec);

    /**
     * 删除工资记录
     * 
     * @param id 工资记录主键
     * @return 结果
     */
    public int deleteFinSalRecById(Long id);

    /**
     * 批量删除工资记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinSalRecByIds(String[] ids);

    public int batchInsertFinSalRec(List<FinSalRec> finSalRecList);

    public int deleteFinSalRecBySalids(String[] ids);

    public int deleteFinSalRecOverview(FinSalRec finSalRec);

    //结转
    public List<FinSalRec> selectForwardFinItems(FinSalRec finSalRec);
    public List<FinSalRec> selectForwardFinDeducts(FinSalRec finSalRec);
    public List<FinSalRec> selectForwardFinCounts(FinSalRec finSalRec);
    public List<Integer> selectSpecificUserids(FinSalRec finSalRec);

}
