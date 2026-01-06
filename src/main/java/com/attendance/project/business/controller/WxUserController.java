package com.attendance.project.business.controller;

import com.alibaba.fastjson.JSONObject;
import com.attendance.common.utils.file.FileUploadUtils;
import com.attendance.common.utils.http.HttpUtils;
import com.attendance.framework.config.WxConfig;
import com.attendance.framework.shiro.token.CustomToken;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.project.business.domain.WxUser;
import com.attendance.project.business.service.IWxUserService;
import com.attendance.project.system.config.service.IConfigService;
import com.attendance.project.system.post.service.IPostService;
import com.attendance.project.system.role.service.IRoleService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/system/wxuser")
public class WxUserController extends BaseController {

    private String prefix = "system/wxuser";

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPostService postService;

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IConfigService configService;

    @ApiOperation("获取当前时间")
    @GetMapping("/getNowTime")
    @ResponseBody
    @CrossOrigin
    public AjaxResult getNowTime()
    {
        AjaxResult result = new AjaxResult();
        result.put("nowtime", new Date().getTime());
        return result;
    }

    /**
     * 获取用户微信信息
     */
    @ApiOperation("获取微信用户信息")
    @PostMapping("/getWxUserInfo")
    @ResponseBody
    @CrossOrigin
    public AjaxResult getWxUserInfo(@Param("code") String code)
    {
        AjaxResult result = new AjaxResult();
        String appid = wxConfig.getAppid();
        String appsecret = wxConfig.getAppsecret();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                + appid + "&secret="
                + appsecret + "&js_code="
                + code
                + "&grant_type=authorization_code";
        String httpResult = HttpUtils.sendGet(url);

        JSONObject responseObj = JSONObject.parseObject(httpResult);//解析从微信服务器上获取到的json字符串
        Map wxUserInfo = new HashMap<String, String>();
        wxUserInfo.put("openid", responseObj.get("openid"));
        wxUserInfo.put("session_key", responseObj.get("session_key"));
        result.put("wxUserInfo", wxUserInfo);
        return result;
    }

    @ApiOperation("获取用户信息")
    @PostMapping("/getUserInfo")
    @ResponseBody
    @CrossOrigin
    public AjaxResult getUserInfo(@Param("openid") String openid)
    {
        AjaxResult result = new AjaxResult();
        WxUser userInfo = wxUserService.selectWxUserByOpenid(openid);
        User sysUser = getSysUser();
        if (sysUser == null && userInfo != null){
            //单点登录
            Long userid = userInfo.getUserid();
            sysUser = userService.selectUserById(userid);
            CustomToken token = new CustomToken(sysUser.getLoginName());
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        }
        result.put("userInfo", userInfo);
        return result;
    }

    /**
     * 保存用户微信信息
     */
    @ApiOperation("保存微信用户")
    @PostMapping("/saveWxUserFile")
    @ResponseBody
    @CrossOrigin
    public AjaxResult saveWxUserFile(@RequestParam("file") MultipartFile file , HttpServletRequest request)
    {
        AjaxResult result = new AjaxResult();

        String openid = request.getParameter("openid");
        System.out.println("openid："+openid);
        String name = request.getParameter("name").toString();
        String phone = request.getParameter("phone").toString();
        String idcard = request.getParameter("idcard").toString();
        String number = request.getParameter("number").toString();
        if(idcard != null){
            idcard = idcard.toUpperCase();
            if(idcard.indexOf("NULL") >= 0){
                idcard = null;
            }
        }
        if(phone != null){
            phone = phone.toUpperCase();
            if(phone.indexOf("NULL")>=0){
                phone = null;
            }
        }
        if(number != null){
            number = number.toUpperCase();
            if(number.indexOf("NULL")>=0){
                number = null;
            }
        }
        String uploadFileName = file.getOriginalFilename();
        System.out.println("上传文件名："+ uploadFileName);
        //上传路径保存设置
        String baseDir = FileUploadUtils.getDefaultBaseDir();
        String fileDir = baseDir;
        String img = "";
        try {
            img = FileUploadUtils.upload(fileDir, file);
        } catch (Exception e){
            e.printStackTrace();
        }
        WxUser oldWxUser = wxUserService.selectWxUserByOpenid(openid);
        //创建用户对象
        WxUser wxUser = new WxUser();
        wxUser.setOpenid(openid);
        wxUser.setName(name);
        wxUser.setPhone(phone);
        wxUser.setIdcard(idcard);
        wxUser.setImg(img);
        wxUser.setNumber(number);
        if (oldWxUser != null){
            wxUserService.updateWxUser(wxUser);
        }else{
            wxUserService.insertWxUser(wxUser);
        }
        //关联用户
        User user = wxUserService.wxUserRelateUser(wxUser);
        if(user != null){
            wxUser.setUserid(user.getUserId());
            wxUser = wxUserService.selectWxUserByOpenid(wxUser.getOpenid());
            result.put("userInfo", wxUser);
        }
        return result;
    }

    /**
     * 保存用户微信信息
     */
    @ApiOperation("保存微信用户")
    @PostMapping("/saveWxUser")
    @ResponseBody
    @CrossOrigin
    public AjaxResult saveWxUser(@RequestBody WxUser wxUser)
    {
        AjaxResult result = new AjaxResult();

        String openid = wxUser.getOpenid();
        WxUser oldWxUser = wxUserService.selectWxUserByOpenid(openid);

        String idcard = wxUser.getIdcard();
        if (idcard != null) {
            idcard = idcard.toUpperCase();
            if(idcard.indexOf("NULL")>=0){
                idcard = null;
            }
            wxUser.setIdcard(idcard);
        }
        String phone = wxUser.getPhone();
        if (phone != null) {
            phone = phone.toUpperCase();
            if(phone.indexOf("NULL")>=0){
                phone = null;
            }
            wxUser.setPhone(phone);
        }
        String number = wxUser.getNumber();
        if (number != null) {
            number = number.toUpperCase();
            if(number.indexOf("NULL")>=0){
                number = null;
            }
            wxUser.setNumber(number);
        }
        if (oldWxUser != null){
            wxUserService.updateWxUser(wxUser);
        }else{
            wxUserService.insertWxUser(wxUser);
        }

        //关联用户
        User user = wxUserService.wxUserRelateUser(wxUser);
        if(user != null) {
            wxUser.setUserid(user.getUserId());
            wxUser = wxUserService.selectWxUserByOpenid(wxUser.getOpenid());
            result.put("userInfo", wxUser);
        }
        return result;
    }

    /**
     * 删除用户微信信息
     */
    @ApiOperation("删除微信用户")
    @PostMapping("/delWxUser")
    @ResponseBody
    @CrossOrigin
    public AjaxResult delWxUser(@RequestBody WxUser wxUser)
    {
        AjaxResult result = new AjaxResult();
        int count = wxUserService.deleteWxUser(wxUser);
        result.put("count", count);
        return result;
    }

    /**
     * 获取用户信息页面展示标识
     */
    @ApiOperation("获取用户信息页面展示标识")
    @GetMapping("/getWxUserInfoTag")
    @ResponseBody
    @CrossOrigin
    public AjaxResult getWxUserInfoTag()
    {
        AjaxResult result = new AjaxResult();
        String config = configService.selectConfigByKey("app.user.info");
        result.put("config", config);
        return result;
    }
}
