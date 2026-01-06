package com.attendance.project.financial.service;

import com.attendance.project.financial.domain.*;
import com.attendance.project.system.user.domain.User;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.ui.ModelMap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 工资记录Service接口
 * 
 * @author ruoyi
 * @date 2024-10-21
 */
public interface IFinSalRecService 
{
    public FinSal selectFinSalById(Long id);
    public int updateFinSal(FinSal finSal);
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
     * 批量删除工资记录
     * 
     * @param ids 需要删除的工资记录主键集合
     * @return 结果
     */
    public int deleteFinSalRecByIds(String ids);

    /**
     * 删除工资记录信息
     * 
     * @param id 工资记录主键
     * @return 结果
     */
    public int deleteFinSalRecById(Long id);

    public int insertFinSal(FinSal finSal);

    public List<FinSal> selectFinSalList(FinSal finSal);

    public int deleteFinSalByIds(String ids);

    public int batchInsertFinSalRec(List<FinSalRec> finSalRecList);

    public int deleteFinSalRecBySalids(String ids);

    public int deleteFinSalRecOverview(FinSalRec finSalRec);

    public FinSalRec calcUserFinSalRec(User user, FinSalRec finSalRec, List<FinParam> finParams, List<FinOvertime> overtimes, List<FinKpi> kpis, List<FinDeduct> deducts) throws Exception;

    public FinSalRec calcUserXygz(User user, FinSalRec finSalRec);

    public FinSal getFinSalOverview(FinSal finSal, HSSFWorkbook sheets) throws Exception;

    public List<FinSalRec> calcFinSalOverview(FinSal finSal, List<FinSalRec> finSalRecs);

    public void writeForwardExcel(Workbook wb, ModelMap mmap);

    public List<FinSalRec> selectForwardFinItems(FinSalRec finSalRec);
    public List<FinSalRec> selectForwardFinDeducts(FinSalRec finSalRec);
    public List<FinSalRec> selectForwardFinCounts(FinSalRec finSalRec);
    public List<Integer> selectSpecificUserids(FinSalRec finSalRec);

    public void writeFinSalWord(OutputStream os, ModelMap mmap) throws IOException;
}
