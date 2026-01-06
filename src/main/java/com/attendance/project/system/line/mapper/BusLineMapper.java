package com.attendance.project.system.line.mapper;

import com.attendance.project.system.line.domain.BusLine;
import com.attendance.project.system.line.domain.FlowLine;

import java.util.List;

/**
 *  Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-07
 */
public interface BusLineMapper 
{
    public BusLine selectBusLineById(Long id);

    public List<BusLine> selectBusLineList(BusLine busLine);

    public int insertBusLine(BusLine busLine);

    public int updateBusLine(BusLine busLine);

    public int deleteBusLineById(Long id);

    public int deleteBusLineByIds(String[] ids);

    public List<FlowLine> selectFlowLineList(BusLine busLine);
}
