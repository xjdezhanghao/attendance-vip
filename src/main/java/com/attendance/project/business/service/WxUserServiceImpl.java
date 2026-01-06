package com.attendance.project.business.service;

import com.attendance.project.business.domain.WxUser;
import com.attendance.project.business.mapper.WxUserMapper;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WxUserServiceImpl implements IWxUserService
{
    @Autowired
    private WxUserMapper wxUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<WxUser> selectWxUsertList(WxUser wxUser) {
        return wxUserMapper.selectWxUserList(wxUser);
    }

    @Override
    public WxUser selectWxUserByOpenid(String openid) {
        List<WxUser> wxUsers = wxUserMapper.selectWxUserByOpenid(openid);
        WxUser result = null;
        if (wxUsers.size() > 0) result = wxUsers.get(0);
        return result;
    }

    @Override
    public int insertWxUser(WxUser wxUser) {
        return wxUserMapper.insertWxUser(wxUser);
    }

    @Override
    public int updateWxUser(WxUser wxUser) {
        return wxUserMapper.updateWxUser(wxUser);
    }

    @Override
    public User wxUserRelateUser(WxUser wxUser) {
        User param = new User();
        param.setUserName(wxUser.getName());
        param.setIdcard(wxUser.getIdcard());
        param.setPhonenumber(wxUser.getPhone());
        param.setNumber(wxUser.getNumber());
        User user = userMapper.selectUserByNumber(param);
        if(user == null){
            user = userMapper.selectUserByNameAndIdcard(param);
        }
        if (user == null){
            user = userMapper.selectUserByNameAndPhonenumber(param);
        }
        if (user != null){
            Long userid = user.getUserId();
            wxUser.setUserid(userid);
            if(wxUser.getName() == null || "".equals(wxUser.getName())) wxUser.setName(user.getUserName());
            if(wxUser.getIdcard() == null || "".equals(wxUser.getIdcard())) wxUser.setIdcard(user.getIdcard());
            if(wxUser.getPhone() == null || "".equals(wxUser.getPhone())) wxUser.setPhone(user.getPhonenumber());
            wxUserMapper.updateWxUser(wxUser);
        }
        return user;
    }

    @Override
    public Integer deleteWxUser(WxUser wxUser) {
        return wxUserMapper.deleteWxUser(wxUser);
    }
}
