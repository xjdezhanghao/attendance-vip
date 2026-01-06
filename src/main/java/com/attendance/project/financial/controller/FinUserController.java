package com.attendance.project.financial.controller;

import com.attendance.common.utils.text.Convert;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.financial.domain.FinUserParam;
import com.attendance.project.financial.service.IFinSalRecService;
import com.attendance.project.financial.service.IFinUserParamService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工资记录Controller
 * 
 * @author ruoyi
 * @date 2024-10-21
 */
@Controller
@RequestMapping("/financial/user")
public class FinUserController extends BaseController
{
    private String prefix = "financial/user";

    @Autowired
    private IFinSalRecService finSalRecService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IFinUserParamService userParamService;

    @RequiresPermissions("fin:user:view")
    @GetMapping()
    public String rec()
    {
        return prefix + "/user";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(User user)
    {
        List<User> users = userService.selectUserList(user);
        startPage();
        String ids = "";
        for (int i=0; i<users.size(); i++){
            User innerUser = users.get(i);
            if (i != users.size()-1){
                ids += innerUser.getUserId()+",";
            }else {
                ids += innerUser.getUserId();
            }
        }
        FinUserParam userParam = new FinUserParam();
        userParam.setIds(Convert.toStrArray(ids));
        List<FinUserParam> userParams = userParamService.selectFinUserParamList(userParam);
        return getDataTable(userParams);
    }

    @RequiresPermissions("fin:user:edit")
    @PostMapping("/syncData")
    @ResponseBody
    public AjaxResult syncData(FinUserParam userParam)
    {
        List<User> list = userService.selectUserList(new User());
        List<FinUserParam> userParams = userParamService.selectFinUserParamList(userParam);
        List<FinUserParam> newUserParams = new ArrayList<FinUserParam>();
        for (int i=0; i<list.size(); i++){
            User user = list.get(i);
            FinUserParam newUserParam = new FinUserParam();
            newUserParam.setUserId(user.getUserId());
            if (!userParams.contains(newUserParam)){
                newUserParam.setCreateBy(getLoginName());
                newUserParam.setCreateTime(new Date());
                newUserParam.setParamRank("1");
                newUserParam.setParamPost("1");
                newUserParam.setSfhy("N");
                newUserParams.add(newUserParam);
            }
        }
        return AjaxResult.success(userParamService.batchInsertFinUserParam(newUserParams));
    }

    @RequiresPermissions("fin:user:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinUserParam userParam = new FinUserParam();
        userParam.setUserId(id);
        List<FinUserParam> userParams = userParamService.selectFinUserParamList(userParam);
        if (userParams.size() > 0){
            userParam = userParams.get(0);
        }
        mmap.put("userParam", userParam);
        return prefix + "/edit";
    }

    /**
     * 修改保存工资记录
     */
    @RequiresPermissions("fin:user:edit")
    @Log(title = "工资记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FinUserParam userParam)
    {
        return toAjax(userParamService.updateFinUserParam(userParam));
    }

}
