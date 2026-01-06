package com.attendance.project.business.service;

import com.attendance.project.business.domain.UserWxwork;
import com.attendance.project.business.mapper.UserWxworkMapper;
//import com.attendance.project.system.user.domain.User;
//import com.attendance.project.system.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IUserWxworkServiceImpl implements IUserWxworkService
{
    @Autowired
    private UserWxworkMapper userWxworkMapper;

    //@Autowired
   // private UserMapper userMapper;

    @Override
    public List<UserWxwork> selectWxWorkUserList(UserWxwork userWxwork) {
        return userWxworkMapper.selectWxWorkUserList(userWxwork);
    }

    @Override
    public UserWxwork selectWxWorkUserByLoginName(String login_name) {
        List<UserWxwork> userWxworks = userWxworkMapper.selectWxWorkUserByLoginName(login_name);
        UserWxwork result = null;
        if (userWxworks.size() > 0) result = userWxworks.get(0);
        return result;
    }

    @Override
    public int insertWxWorkUser(UserWxwork userWxwork) {
        return userWxworkMapper.insertWxWorkUser(userWxwork);
    }

    @Override
    public int updateWxWorkUser(UserWxwork userWxwork)
    {
        return userWxworkMapper.updateWxWorkUser(userWxwork);
    }

//    @Override
//    public User wxUserRelateUser(WxUser wxUser) {
//        User param = new User();
//        param.setUserName(wxUser.getName());
//        param.setIdcard(wxUser.getIdcard());
//        param.setPhonenumber(wxUser.getPhone());
//        param.setNumber(wxUser.getNumber());
//        User user = userMapper.selectUserByNumber(param);
//        if(user == null){
//            user = userMapper.selectUserByNameAndIdcard(param);
//        }
//        if (user == null){
//            user = userMapper.selectUserByNameAndPhonenumber(param);
//        }
//        if (user != null){
//            Long userid = user.getUserId();
//            wxUser.setUserid(userid);
//            if(wxUser.getName() == null || "".equals(wxUser.getName())) wxUser.setName(user.getUserName());
//            if(wxUser.getIdcard() == null || "".equals(wxUser.getIdcard())) wxUser.setIdcard(user.getIdcard());
//            if(wxUser.getPhone() == null || "".equals(wxUser.getPhone())) wxUser.setPhone(user.getPhonenumber());
//            wxUserMapper.updateWxUser(wxUser);
//        }
//        return user;
//    }

    //@Override
    //public int deleteWxWorkUser(UserWxwork userWxwork)
//    {
//        return userWxworkMapper.deleteWxWorkUser(userWxwork);
//    }
    /**
     * 删除用户和企业微信账号关联信息
     *
     * @param loginName 用户和企业微信账号关联主键
     * @return 结果
     */
    @Override
    public int deleteUserWxworkByLoginName(String loginName)
    {
        return userWxworkMapper.deleteUserWxworkByLoginName(loginName);
    }
}
