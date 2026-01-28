package com.attendance.project.business.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance.project.atdform.atdstatisticform.domain.AtdPlan;
import com.attendance.project.atdform.atdstatisticform.mapper.AtdPlanMapper;
import com.attendance.project.business.domain.AtdRecord;
import com.attendance.project.business.mapper.AtdRecordMapper;
import com.attendance.project.system.user.mapper.UserMapper;

@Service
public class AtdRecordServiceImpl implements IAtdRecordService
{
    @Autowired
    private AtdRecordMapper atdRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AtdPlanMapper atdPlanMapper;

    @Override
    public List<AtdRecord> selectAtdRecordList(AtdRecord atdRecord) {
        return atdRecordMapper.selectAtdRecordList(atdRecord);
    }

    @Override
    public List<AtdRecord> getUserMonthAtd(AtdRecord atdRecord) {
        return atdRecordMapper.selectAtdRecordList(atdRecord);
    }

    @Override
    public List<AtdRecord> getUserWeekAtd(AtdRecord atdRecord) {
        return atdRecordMapper.selectAtdRecordList(atdRecord);
    }

    @Override
    public Integer updateAtdRecord(AtdRecord atdRecord) {
        String atddesc = atdRecord.getAtddesc();
        if(atddesc == null) atdRecord.setAtddesc("");
        return atdRecordMapper.updateAtdRecord(atdRecord);
    }

    @Override
    public Integer insertAtdRecord(AtdRecord atdRecord) {
        return atdRecordMapper.insertAtdRecord(atdRecord);
    }

    @Override
    public List<AtdRecord> getStatisticAtdRecord(AtdRecord atdRecord) {
        return atdRecordMapper.getStatisticAtdRecord(atdRecord);
    }

    @Override
    public Integer deleteAtdRecordByAtd(AtdRecord atdRecord) {
        return atdRecordMapper.deleteAtdRecordByAtd(atdRecord);
    }

    @Override
    public void atdRecordProcess(AtdRecord atdRecord) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //创建对比时间
        Date tmpDate = new Date();
        tmpDate.setSeconds(0);
        calendar.setTime(tmpDate);
        calendar.set(Calendar.MILLISECOND, 0);
        tmpDate = calendar.getTime();
        tmpDate.setHours(8);tmpDate.setMinutes(41);
        Long startTime = tmpDate.getTime();
        tmpDate.setHours(16);tmpDate.setMinutes(0);
        Long endTime = tmpDate.getTime();
        tmpDate.setHours(8);tmpDate.setMinutes(30);
        Long endTime2 = tmpDate.getTime();
        //获取用户id及签到类型
        Long userid = atdRecord.getUserid();
        Date atdtime = new Date();
        atdtime.setHours(0);atdtime.setMinutes(0);atdtime.setSeconds(0);
        calendar.setTime(atdtime);
        calendar.set(Calendar.MILLISECOND, 0);
        atdtime = calendar.getTime();
        //获取昨今两天计划类型
        atdRecord.setAtdtime(atdtime);
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 减去一天
        Date yesterday = calendar.getTime();
        String yesterdayStr = sdf.format(yesterday);
        String todayStr = sdf.format(atdtime);
        AtdPlan atdPlan = new AtdPlan();
        atdPlan.setUserId(userid);
        atdPlan.setPlanDate(yesterdayStr);
        String yesterdayType = "0";
        List<AtdPlan> yesterdayPlans = atdPlanMapper.selectAtdPlanList(atdPlan);
        if (yesterdayPlans.size() > 0){
            yesterdayType = yesterdayPlans.get(0).getEnable();
        }
        atdPlan.setPlanDate(todayStr);
        List<AtdPlan> todayPlans = atdPlanMapper.selectAtdPlanList(atdPlan);
        String todayType = "0";
        if (todayPlans.size() > 0){
            todayType = todayPlans.get(0).getEnable();
        }
        //获取昨天实际出勤情况
        boolean yesterdayStatus = true;
        AtdRecord findParamOld = new AtdRecord();
        findParamOld.setAtdtime(yesterday);
        findParamOld.setUserid(userid);
        List<AtdRecord> oldAtds = selectAtdRecordList(findParamOld);
        if (oldAtds.size() <= 0){
            yesterdayStatus = false;
        }
        //实际签到时间
        Date createTime = new Date();
        Long nowTime = createTime.getTime();
        
        // 创建通用设备记录对象
        AtdRecord deviceRecord = new AtdRecord();
        deviceRecord.setCreateTime(createTime);
        deviceRecord.setAtdtime(atdtime);
        deviceRecord.setAtdloc(atdRecord.getAtdloc()); // 使用传入的设备类型
        deviceRecord.setUserid(userid);
        
        // 当天签到判断迟到早退
        atdRecord.setAtdtag("1");
        List<AtdRecord> todayShbAtds = selectAtdRecordList(atdRecord);
        if (todayShbAtds.size() <= 0) {
            // 新增上班记录
            if (!("2".equals(yesterdayType) && yesterdayStatus)) {
                // 今天已排班
                if (!"3".equals(todayType)) {
                    if (nowTime > startTime) {
                        deviceRecord.setAtdstatus("1"); // 迟到
                    }
                }
            }
            deviceRecord.setAtdtag("1"); // 上班标记
            insertAtdRecord(deviceRecord);
        } else {
            // 处理下班记录
            atdRecord.setAtdtag("2");
            List<AtdRecord> todayXbAtds = selectAtdRecordList(atdRecord);
            
            if (!("0".equals(todayType) && "2".equals(yesterdayType) && yesterdayStatus)) {
                if (!"3".equals(todayType) && !"2".equals(todayType)) {
                    if (nowTime < endTime) {
                        deviceRecord.setAtdstatus("2"); // 早退
                    }
                }
            } else {
                // 昨天倒班正常且今天未排班
                if (nowTime < endTime2) {
                    deviceRecord.setAtdstatus("2"); // 早退
                }
            }
            
            if (todayXbAtds.size() <= 0) {
                // 新增下班记录
                deviceRecord.setAtdtag("2");
                insertAtdRecord(deviceRecord);
            } else {
                // 更新现有记录
                AtdRecord existingRecord = todayXbAtds.get(0);
                existingRecord.setCreateTime(deviceRecord.getCreateTime());
                existingRecord.setAtdstatus(deviceRecord.getAtdstatus());
                if (existingRecord.getAtdstatus() == null) {
                    existingRecord.setAtdstatus("");
                }
                updateAtdRecord(existingRecord);
            }
        }
    }
}
