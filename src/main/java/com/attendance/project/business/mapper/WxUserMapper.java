package com.attendance.project.business.mapper;

import com.attendance.project.business.domain.WxUser;

import java.util.List;

public interface WxUserMapper
{
    public List<WxUser> selectWxUserList(WxUser wxUser);

    public List<WxUser> selectWxUserByOpenid(String openid);

    public Integer updateWxUser(WxUser wxUser);

    public Integer insertWxUser(WxUser wxUser);

    public Integer deleteWxUser(WxUser wxUser);
}
