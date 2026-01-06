package com.attendance.project.financial.controller;

import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.financial.domain.FinParam;
import com.attendance.project.financial.domain.FinParamDetail;
import com.attendance.project.financial.service.IFinParamService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 财务参数Controller
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
@Controller
@RequestMapping("/financial/param")
public class FinParamController extends BaseController
{
    private String prefix = "financial/param";

    @Autowired
    private IFinParamService finParamService;


    @RequiresPermissions("fin:param:view")
    @GetMapping()
    public String param()
    {
        return prefix + "/param";
    }

    /**
     * 查询财务参数列表
     */
    @RequiresPermissions("fin:param:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FinParam finParam)
    {
        startPage();
        List<FinParam> list = finParamService.selectFinParamList(finParam);
        return getDataTable(list);
    }

    /**
     * 导出财务参数列表
     */
    @RequiresPermissions("fin:param:export")
    @Log(title = "财务参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FinParam finParam)
    {
        List<FinParam> list = finParamService.selectFinParamList(finParam);
        ExcelUtil<FinParam> util = new ExcelUtil<FinParam>(FinParam.class);
        return util.exportExcel(list, "财务参数数据");
    }

    /**
     * 新增财务参数
     */
    @GetMapping("/add")
    public String add(ModelMap mmp)
    {
        if (getSysUser().getDept().getParentId() == 0){
            mmp.put("deptId", getSysUser().getDeptId());
        } else {
            mmp.put("deptId", getSysUser().getDept().getParentId());
        }
        return prefix + "/add";
    }

    /**
     * 新增保存财务参数
     */
    @RequiresPermissions("fin:param:add")
    @Log(title = "财务参数", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FinParam finParam)
    {
        finParam.setCreateBy(getLoginName());
        finParam.setCreateTime(new Date());
        return toAjax(finParamService.insertFinParam(finParam));
    }

    /**
     * 修改财务参数
     */
    @RequiresPermissions("fin:param:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinParam finParam = finParamService.selectFinParamById(id);
        mmap.put("finParam", finParam);
        return prefix + "/detail";
    }

    /**
     * 修改保存财务参数
     */
    @RequiresPermissions("fin:param:edit")
    @Log(title = "财务参数", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FinParam finParam)
    {
        return toAjax(finParamService.updateFinParam(finParam));
    }

    /**
     * 删除财务参数
     */
    @RequiresPermissions("fin:param:remove")
    @Log(title = "财务参数", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(finParamService.deleteFinParamByIds(ids));
    }


    @PostMapping("/detailList/{id}")
    @ResponseBody
    public TableDataInfo detailList(@PathVariable("id") Long id, FinParamDetail detail)
    {
        detail.setPid(id);
        startPage();
        List<FinParamDetail> list = finParamService.selectFinParamDetailList(detail);
        return getDataTable(list);
    }

    /**
     * 新增财务参数细则
     */
    @GetMapping("/addDetail/{id}")
    public String addDetail(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinParam finParam = finParamService.selectFinParamById(id);
        mmap.put("finParam", finParam);
        return prefix + "/addDetail";
    }

    /**
     * 新增保存财务参数细则
     */
    @Log(title = "财务参数细则", businessType = BusinessType.INSERT)
    @PostMapping("/addDetail")
    @ResponseBody
    public AjaxResult addDetailSave(FinParamDetail finParamDetail)
    {
        finParamDetail.setCreateBy(getLoginName());
        return toAjax(finParamService.insertFinParamDetail(finParamDetail));
    }

    /**
     * 修改财务参数细则
     */
    @GetMapping("/editDetail/{id}")
    public String editDetail(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinParamDetail finParamDetail = finParamService.selectFinParamDetailById(id);
        mmap.put("finParamDetail", finParamDetail);
        return prefix + "/editDetail";
    }

    /**
     * 修改保存财务参数细则
     */
    @Log(title = "财务参数细则", businessType = BusinessType.UPDATE)
    @PostMapping("/editDetail")
    @ResponseBody
    public AjaxResult editDetailSave(FinParamDetail finParamDetail)
    {
        return toAjax(finParamService.updateFinParamDetail(finParamDetail));
    }

    @PostMapping( "/removeDetail")
    @ResponseBody
    public AjaxResult removeDetail(String ids)
    {
        return toAjax(finParamService.deleteFinParamDetailByIds(ids));
    }
}
