package com.attendance.project.atwork.working.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.attendance.project.atwork.working.mapper.workingMapper;
import com.attendance.project.atwork.working.domain.working;
import com.attendance.project.atwork.working.service.IworkingService;
import com.attendance.common.utils.text.Convert;

/**
 * 在岗人员统计Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-07-28
 */
@Service
public class workingServiceImpl implements IworkingService 
{
    @Autowired
    private workingMapper workingMapper;


    /**
     * 查询在岗人员统计列表
     * 
     * @param working 在岗人员统计
     * @return 在岗人员统计
     */
    @Override
    public List<working> selectworkingList(working working)
    {
        return workingMapper.selectworkingList(working);
    }


}
