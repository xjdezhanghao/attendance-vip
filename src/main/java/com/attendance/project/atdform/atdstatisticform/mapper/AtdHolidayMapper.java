package com.attendance.project.atdform.atdstatisticform.mapper;

import com.attendance.project.atdform.atdstatisticform.domain.AtdHoliday;

import java.util.List;

public interface AtdHolidayMapper 
{
    public AtdHoliday selectAtdHolidayById(Long id);

    public List<AtdHoliday> selectAtdHolidayList(AtdHoliday atdHoliday);

    public int insertAtdHoliday(AtdHoliday atdHoliday);

    public int updateAtdHoliday(AtdHoliday atdHoliday);

    public int deleteAtdHolidayById(Long id);

    public int deleteAtdHolidayByIds(String[] ids);

    public int batchInsertAtdHoliday(List<AtdHoliday> atdHolidays);

    public List<String> selectAtdHolidayStringList(AtdHoliday atdHoliday);

}
