package com.attendance.project.atdform.atdstatisticform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.attendance.common.utils.http.HttpUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.project.atdform.atdstatisticform.domain.AtdHoliday;
import com.attendance.project.atdform.atdstatisticform.mapper.AtdHolidayMapper;
import com.attendance.project.atdform.atdstatisticform.service.IAtdHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AtdHolidayServiceImpl implements IAtdHolidayService
{
    @Autowired
    private AtdHolidayMapper atdHolidayMapper;

    @Override
    public AtdHoliday selectAtdHolidayById(Long id)
    {
        return atdHolidayMapper.selectAtdHolidayById(id);
    }

    @Override
    public List<AtdHoliday> selectAtdHolidayList(AtdHoliday atdHoliday)
    {
        return atdHolidayMapper.selectAtdHolidayList(atdHoliday);
    }

    @Override
    public int insertAtdHoliday(AtdHoliday atdHoliday)
    {
        return atdHolidayMapper.insertAtdHoliday(atdHoliday);
    }

    @Override
    public int updateAtdHoliday(AtdHoliday atdHoliday)
    {
        return atdHolidayMapper.updateAtdHoliday(atdHoliday);
    }

    @Override
    public int deleteAtdHolidayByIds(String ids)
    {
        return atdHolidayMapper.deleteAtdHolidayByIds(Convert.toStrArray(ids));
    }

    @Override
    public int deleteAtdHolidayById(Long id)
    {
        return atdHolidayMapper.deleteAtdHolidayById(id);
    }

    @Override
    public int getHolidayByYear(String url, String key, String year) {
        String response = HttpUtils.sendGet(url, "key="+key+"&date="+year+"&type="+1);
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray holidayList = result.getJSONArray("list");
        List<AtdHoliday> atdHolidays = new ArrayList<AtdHoliday>();
        for (int i=0; i<holidayList.size(); i++){
            JSONObject holiday = holidayList.getJSONObject(i);
            String vocationStr = holiday.getString("vacation");
            List<String> vocations = Arrays.asList(vocationStr.split("\\|"));
            String wageStr = holiday.getString("wage");
            List<String> wages = Arrays.asList(wageStr.split("\\|"));
            String remarkStr = holiday.getString("remark");
            List<String> remarks = Arrays.asList(remarkStr.split("\\|"));
            for (int j=0; j<vocations.size(); j++){
                AtdHoliday atdHoliday = new AtdHoliday();
                String vocation = vocations.get(j);
                if (vocation.trim().length() == 0) continue;
                atdHoliday.setYear(year);
                atdHoliday.setDate(vocation);
                if (wages.contains(vocation)){
                    atdHoliday.setType("1");
                }else{
                    atdHoliday.setType("2");
                }
                atdHolidays.add(atdHoliday);
            }
            for (int j=0; j<remarks.size(); j++){
                AtdHoliday atdHoliday = new AtdHoliday();
                String remark = remarks.get(j);
                if (remark.trim().length() == 0) continue;
                atdHoliday.setYear(year);
                atdHoliday.setDate(remark);
                atdHoliday.setType("3");
                atdHolidays.add(atdHoliday);
            }
        }
        return atdHolidayMapper.batchInsertAtdHoliday(atdHolidays);
    }

    @Override
    public List<String> selectAtdHolidayStringList(AtdHoliday atdHoliday) {
        return atdHolidayMapper.selectAtdHolidayStringList(atdHoliday);
    }
}
