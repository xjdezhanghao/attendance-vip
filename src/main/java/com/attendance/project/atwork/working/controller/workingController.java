package com.attendance.project.atwork.working.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.project.atwork.working.domain.working;
import com.attendance.project.atwork.working.service.IworkingService;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.framework.web.page.TableDataInfo;

/**
 * 在岗人员统计Controller
 * 
 * @author ruoyi
 * @date 2022-07-28
 */
@Controller
@RequestMapping("/atwork/working")
public class workingController extends BaseController
{
    private String prefix = "atwork/working";

    @Autowired
    private IworkingService workingService;

    @GetMapping()
    public String working()
    {
        return prefix + "/working";
    }

    /**
     * 查询在岗人员统计列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(working working)
    {
        startPage();
        List<working> list = workingService.selectworkingList(working);
        return getDataTable(list);
    }

}
