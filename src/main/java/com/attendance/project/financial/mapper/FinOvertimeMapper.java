package com.attendance.project.financial.mapper;

import com.attendance.framework.aspectj.lang.annotation.DataScope;
import com.attendance.project.financial.domain.FinOvertime;

import java.util.List;

/**
 * 加班申请Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-08
 */
public interface FinOvertimeMapper 
{
    /**
     * 查询加班申请
     * 
     * @param id 加班申请主键
     * @return 加班申请
     */
    public FinOvertime selectFinOvertimeById(Long id);

    /**
     * 查询加班申请列表
     * 
     * @param finOvertime 加班申请
     * @return 加班申请集合
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<FinOvertime> selectFinOvertimeList(FinOvertime finOvertime);

    /**
     * 新增加班申请
     * 
     * @param finOvertime 加班申请
     * @return 结果
     */
    public int insertFinOvertime(FinOvertime finOvertime);

    /**
     * 修改加班申请
     * 
     * @param finOvertime 加班申请
     * @return 结果
     */
    public int updateFinOvertime(FinOvertime finOvertime);

    /**
     * 删除加班申请
     * 
     * @param id 加班申请主键
     * @return 结果
     */
    public int deleteFinOvertimeById(Long id);

    /**
     * 批量删除加班申请
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinOvertimeByIds(String[] ids);
}
