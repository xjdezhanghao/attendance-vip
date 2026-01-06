package com.attendance.project.business.service;

import com.attendance.framework.config.ZmConfig;
import com.attendance.project.business.domain.AtdRecord;
import com.zhangjingmai.socket.bean.RequestBean;
import com.zhangjingmai.socket.bean.ResponseBean;
import com.zhangjingmai.socket.bean.ResponseDataBean;
import com.zhangjingmai.socket.service.ClientService;
import com.zhangjingmai.socket.service.ClientServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class ZhangjmServiceImpl implements IZhangjmService
{
    @Autowired
    private ZmConfig zmConfig;

    @Autowired
    IAtdRecordService atdRecordService;

    ClientService clientService = null;
    public String conDateStr = "";
    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public boolean isRequesting = false;

    @Override
    public void devConnect() {
        if (!Boolean.valueOf(zmConfig.getEnable())) return;;
        clientService = new ClientService(zmConfig.getIp(), zmConfig.getPort(), zmConfig.getDev());
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        conDateStr = df2.format(new Date());
        System.out.println(conDateStr);
        clientService.devConnect(new ClientServiceInf.OnDevConnectCallBack() {
            /**
             * 发送请求回调
             * @param requestBean 响应的数据对象
             */
            @Override
            public void onSend(RequestBean requestBean) {
                //System.out.println(df2.format(new Date()) + "连接请求");
            }
            /**
             * 连接设备响应结果
             * @param responseBean 响应的数据对象
             */
            @Override
            public void onResponse(ResponseBean responseBean) {
                //System.out.println(df2.format(new Date()) + "连接响应");
            }
            /**
             * 连接设备错误
             * @param errorCode 错误码
             * @param errorMsg 错误信息
             */
            @Override
            public void onFailure(int errorCode, String errorMsg) {
                //System.out.println(df2.format(new Date()) + "连接失败");
                System.out.println(errorMsg.toString());
            }
            /**
             * 断开连接
             */
            @Override
            public void onDisconnect() {
                //System.out.println(df2.format(new Date()) + "连接断开");
            }
        }, 2 * 60 * 1000);
    }

    @Override
    public boolean isConnected() {
        //System.out.println("连接判断：" + conDateStr);
        return (clientService != null && clientService.isConnected());
    }

    @Override
    public RequestBean getRequestBean(String apiType) {
        return clientService.newRequestBean(apiType);
    }

    @Override
    public void heartbeat() {
        clientService.heartbeat(new ClientServiceInf.CallBackImpl() {
            /**
             * 客户端发送成功的回调
             * @param requestBean 客户端发送请求数据对象
             */
            @Override
            public void onSend(RequestBean requestBean) {
                //System.out.println(df2.format(new Date()) + "心跳请求");
            }
            /**
             * 响应结果
             * @param responseBean 响应的数据对象
             */
            @Override
            public void onResponse(ResponseBean responseBean) {
                //System.out.println(df2.format(new Date()) + "心跳响应");
            }
            /**
             * 错误
             * @param errorCode 错误码
             * @param errorMsg 错误信息
             */
            @Override
            public void onFailure(int errorCode, String errorMsg) {
                //System.out.println(df2.format(new Date()) + "心跳失败");
            }
        }, true, 10000);
    }

    @Override
    public boolean sendRequestData(RequestBean requestBean) {
       boolean result = clientService.sendRequestData(requestBean, new ClientServiceInf.CallBackImpl() {
            public void onSend(RequestBean requestBean) {
                isRequesting = true;
                System.out.println(df2.format(new Date()) + "调起请求");
                System.out.println(requestBean.getData());
            }
            @Override
            public void onResponse(ResponseBean responseBean) {
                System.out.println(df2.format(new Date()) + "调起响应");
                System.out.println(responseBean.getData());
                ResponseDataBean responseDataBean = responseBean.getData();
                if ("balance".equals(responseDataBean.getAction())){
                    String optType =  responseDataBean.getObj().get("optType").toString();
                    if ("atd".equals(optType)){
                        String userId = responseDataBean.getUserId();
                        if (userId != null){
                            AtdRecord atdRecord = new AtdRecord();
                            atdRecord.setUserid(Long.valueOf(userId));
                            atdRecordService.atdRecordProcess(atdRecord);
                        }
                    }
                }
                isRequesting = false;
            }
            @Override
            public void onFailure(int errorCode, String errorMsg) {
                isRequesting = false;
                System.out.println(df2.format(new Date()) + "调起失败");
                System.out.println(errorMsg.toString());
            }
        });
        return result;
    }

    @Autowired

    @Override
    public boolean isRequesting() {
        return isRequesting;
    }
}
