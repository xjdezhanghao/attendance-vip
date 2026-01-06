package com.attendance.project.business.service;

import com.attendance.project.business.domain.UserWxwork;

import java.util.List;

/**
 * 企业微信账号管理 服务层
 * 
 *
 */
public interface IUserWxworkService
{

    public List<UserWxwork> selectWxWorkUserList(UserWxwork userWxwork);

    public UserWxwork selectWxWorkUserByLoginName(String login_name);

    public int insertWxWorkUser(UserWxwork userWxwork);

    public int updateWxWorkUser(UserWxwork userWxwork);

    //public int deleteWxWorkUser(UserWxwork userWxwork);
    /**
     * 删除用户和企业微信账号关联信息
     *
     * @param loginName 用户和企业微信账号关联主键
     * @return 结果
     */
    public int deleteUserWxworkByLoginName(String loginName);
}
