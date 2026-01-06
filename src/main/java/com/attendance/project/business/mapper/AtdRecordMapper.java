package com.attendance.project.business.mapper;

import com.attendance.framework.aspectj.lang.annotation.DataScope;
import com.attendance.project.business.domain.AtdRecord;

import java.util.List;

public interface AtdRecordMapper
{
    public List<AtdRecord> selectAtdRecordList(AtdRecord atdRecord);

    public Integer updateAtdRecord(AtdRecord atdRecord);

    public Integer insertAtdRecord(AtdRecord atdRecord);

    @DataScope(deptAlias = "d", userAlias = "u")
    public List<AtdRecord> getStatisticAtdRecord(AtdRecord atdRecord);

    public Integer deleteAtdRecordByAtd(AtdRecord atdRecord);
}
