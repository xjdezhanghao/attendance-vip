package com.attendance.project.business.service;

import com.zhangjingmai.socket.bean.RequestBean;


public interface IZhangjmService
{
    public void devConnect();
    public boolean isConnected();
    public RequestBean getRequestBean(String apiType);
    public void heartbeat();
    public boolean sendRequestData(RequestBean requestDataBean);
    public boolean isRequesting();
}
