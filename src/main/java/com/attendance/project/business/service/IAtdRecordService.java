package com.attendance.project.business.service;

import com.attendance.project.business.domain.AtdRecord;

import java.util.List;

/**
 * 部门管理 服务层
 * 
 * @author june
 */
public interface IAtdRecordService
{
    public List<AtdRecord> selectAtdRecordList(AtdRecord atdRecord);

    public List<AtdRecord> getUserMonthAtd(AtdRecord atdRecord);

    public List<AtdRecord> getUserWeekAtd(AtdRecord atdRecord);

    public Integer updateAtdRecord(AtdRecord atdRecord);

    public Integer insertAtdRecord(AtdRecord atdRecord);

    public List<AtdRecord> getStatisticAtdRecord(AtdRecord atdRecord);

    public Integer deleteAtdRecordByAtd(AtdRecord atdRecord);

    public void atdRecordProcess(AtdRecord atdRecord);
}
