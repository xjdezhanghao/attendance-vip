package com.attendance.project.system.line.mapper;

import com.attendance.project.system.line.domain.RecLine;

import java.util.List;

/**
 * 记录流程Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-16
 */
public interface RecLineMapper 
{
    /**
     * 查询记录流程
     * 
     * @param id 记录流程主键
     * @return 记录流程
     */
    public RecLine selectRecLineById(Long id);

    /**
     * 查询记录流程列表
     * 
     * @param recLine 记录流程
     * @return 记录流程集合
     */
    public List<RecLine> selectRecLineList(RecLine recLine);

    /**
     * 新增记录流程
     * 
     * @param recLine 记录流程
     * @return 结果
     */
    public int insertRecLine(RecLine recLine);

    /**
     * 修改记录流程
     * 
     * @param recLine 记录流程
     * @return 结果
     */
    public int updateRecLine(RecLine recLine);

    /**
     * 删除记录流程
     * 
     * @param id 记录流程主键
     * @return 结果
     */
    public int deleteRecLineById(Long id);

    /**
     * 批量删除记录流程
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRecLineByIds(String[] ids);

    public int deleteRecLineByRecId(Long id);
}
