package com.attendance.project.financial.mapper;

import com.attendance.framework.aspectj.lang.annotation.DataScope;
import com.attendance.project.financial.domain.FinSal;

import java.util.List;

/**
 * 工资记录Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-21
 */
public interface FinSalMapper
{
    /**
     * 查询工资记录
     * 
     * @param id 工资记录主键
     * @return 工资记录
     */
    public FinSal selectFinSalById(Long id);

    /**
     * 查询工资记录列表
     * 
     * @param finSal 工资记录
     * @return 工资记录集合
     */
    @DataScope(deptAlias = "u", userAlias = "u")
    public List<FinSal> selectFinSalList(FinSal finSal);

    /**
     * 新增工资记录
     * 
     * @param finSal 工资记录
     * @return 结果
     */
    public int insertFinSal(FinSal finSal);

    /**
     * 修改工资记录
     * 
     * @param finSal 工资记录
     * @return 结果
     */
    public int updateFinSal(FinSal finSal);

    /**
     * 删除工资记录
     * 
     * @param id 工资记录主键
     * @return 结果
     */
    public int deleteFinSalById(Long id);

    /**
     * 批量删除工资记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinSalByIds(String[] ids);
}
