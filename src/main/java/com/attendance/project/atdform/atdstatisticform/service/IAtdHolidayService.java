package com.attendance.project.atdform.atdstatisticform.service;

import com.attendance.project.atdform.atdstatisticform.domain.AtdHoliday;

import java.util.List;

public interface IAtdHolidayService 
{

    public AtdHoliday selectAtdHolidayById(Long id);

    public List<AtdHoliday> selectAtdHolidayList(AtdHoliday atdHoliday);

    public int insertAtdHoliday(AtdHoliday atdHoliday);

    public int updateAtdHoliday(AtdHoliday atdHoliday);

    public int deleteAtdHolidayByIds(String ids);

    public int deleteAtdHolidayById(Long id);

    public int getHolidayByYear(String url, String key, String year);

    public List<String> selectAtdHolidayStringList(AtdHoliday atdHoliday);
}
