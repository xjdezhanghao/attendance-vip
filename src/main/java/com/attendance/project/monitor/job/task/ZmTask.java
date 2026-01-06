package com.attendance.project.monitor.job.task;

import com.attendance.project.atdform.atdstatisticform.service.IAtdPlanService;
import com.attendance.project.business.service.IAtdRecordService;
import com.attendance.project.business.service.IZhangjmService;
import com.attendance.project.system.config.service.IConfigService;
import com.zhangjingmai.socket.bean.RequestBean;
import com.zhangjingmai.socket.bean.RequestDataBean;
import com.zhangjingmai.socket.service.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务调度测试
 *
 * @author june
 */
@Component("zmTask")
@EnableScheduling
public class ZmTask
{
    @Autowired
    private IZhangjmService zhangjmService;

    @Autowired
    private IAtdRecordService atdRecordService;

    @Autowired
    private IAtdPlanService atdPlanService;

    @Autowired
    private IConfigService configService;

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void openLinkWithZm() {
        zhangjmService.devConnect();
    }

    @Scheduled(cron = "*/10 * 7-19 * * *")
    //@Scheduled(cron = "*/10 * * * * *")
    public void checkLinkHeart(){
        if (zhangjmService.isConnected()){
            zhangjmService.heartbeat();
        } else {
            zhangjmService.devConnect();
        }
    }

    @Scheduled(cron = "*/1 * 7-19 * * *")
    //@Scheduled(cron = "*/1 * * * * *")
    public void invokeZmDevice(){
        if (zhangjmService.isConnected() && !zhangjmService.isRequesting()){
            System.out.println(sdf.format(new Date()) + "调起请求");
            RequestBean requestBean = zhangjmService.getRequestBean(Api.USER_IDENTIFY);
            requestBean.setTimeout(80 * 1000);
            RequestDataBean requestDataBean= requestBean.getData();
            // 设置识别超时时间
            requestDataBean.setIdentifyTimeout(60 * 1000);
            // 设置操作类型
            requestDataBean.setAction("balance");
            // 客户端请求传的其他参数，当掌纹设备响应客户端时，将会返回该数据
            Map<String, Object> map = new HashMap<>();
            map.put("optType", "atd");
            requestDataBean.setObj(map);
            Boolean result = zhangjmService.sendRequestData(requestBean);
            System.out.println(sdf.format(new Date()) + result);
        }
    }

}
