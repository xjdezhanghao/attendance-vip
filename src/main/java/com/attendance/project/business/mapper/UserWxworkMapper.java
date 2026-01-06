package com.attendance.project.business.mapper;

import com.attendance.project.business.domain.UserWxwork;

import java.util.List;



public interface UserWxworkMapper
{
    public List<UserWxwork> selectWxWorkUserList(UserWxwork userWxwork);

    public List<UserWxwork> selectWxWorkUserByLoginName(String login_name);

    public Integer updateWxWorkUser(UserWxwork userWxwork);

    public Integer insertWxWorkUser(UserWxwork userWxwork);

    //public Integer deleteWxWorkUser(UserWxwork userWxwork);
    /**
     * 删除用户和企业微信账号关联
     *
     * @param loginName 用户和企业微信账号关联主键
     * @return 结果
     */
    public int deleteUserWxworkByLoginName(String loginName);
}
