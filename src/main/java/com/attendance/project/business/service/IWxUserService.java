package com.attendance.project.business.service;

import com.attendance.project.business.domain.WxUser;
import com.attendance.project.system.user.domain.User;

import java.util.List;

/**
 * 部门管理 服务层
 * 
 * @author june
 */
public interface IWxUserService
{

    public List<WxUser> selectWxUsertList(WxUser wxUser);

    public WxUser selectWxUserByOpenid(String openid);

    public int insertWxUser(WxUser wxUser);

    public int updateWxUser(WxUser wxUser);

    public User wxUserRelateUser(WxUser wxUser);

    public Integer deleteWxUser(WxUser wxUser);
}
