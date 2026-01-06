package com.attendance.project.atwork.working.service;

import java.util.List;
import com.attendance.project.atwork.working.domain.working;

/**
 * 在岗人员统计Service接口
 * 
 * @author ruoyi
 * @date 2022-07-28
 */
public interface IworkingService 
{

    /**
     * 查询在岗人员统计列表
     * 
     * @param working 在岗人员统计
     * @return 在岗人员统计集合
     */
    public List<working> selectworkingList(working working);


}
