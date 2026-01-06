package com.attendance.project.system.line.controller;

import com.attendance.common.utils.PushWxWorkMes;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.config.WxWorkConfig;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.system.config.service.IConfigService;
import com.attendance.project.system.dept.domain.Dept;
import com.attendance.project.system.dept.service.IDeptService;
import com.attendance.project.system.line.domain.FlowLine;
import com.attendance.project.system.line.domain.RecLine;
import com.attendance.project.system.line.service.IFlowLineService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 线性流程Controller
 * 
 * @author ruoyi
 * @date 2024-11-06
 */
@Controller
@RequestMapping("/system/line")
public class FlowLineController extends BaseController
{
    private String prefix = "system/line";

    @Autowired
    private IFlowLineService flowLineService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IUserService userService;

    @Autowired
    private PushWxWorkMes pushWxWorkMes;

    @Autowired
    private IConfigService configService;

    @Autowired
    private WxWorkConfig wxWorkConfig;

    //@RequiresPermissions("system:line:view")
    @GetMapping()
    public String line()
    {
        return prefix + "/line";
    }

    /**
     * 查询线性流程列表
     */
    //@RequiresPermissions("system:line:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FlowLine flowLine)
    {
        startPage();
        List<FlowLine> list = flowLineService.selectFlowLineList(flowLine);
        return getDataTable(list);
    }

    /**
     * 新增线性流程
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        //可创建上级部门及以下的流程
        Long parentId = getSysUser().getDept().getParentId();
        if (parentId == 0){
            parentId = getSysUser().getDeptId();
        }
        Dept parentDept = deptService.selectDeptById(parentId);
        mmap.put("dept", parentDept);
        return prefix + "/add";
    }

    /**
     * 新增保存线性流程
     */
    @RequiresPermissions("system:line:add")
    @Log(title = "线性流程", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FlowLine flowLine)
    {
        flowLine.setCreateBy(getLoginName());
        return toAjax(flowLineService.insertFlowLine(flowLine));
    }

    /**
     * 修改线性流程
     */
    @RequiresPermissions("system:line:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Long parentId = getSysUser().getDept().getParentId();
        if (parentId == 0){
            parentId = getSysUser().getDeptId();
        }
        Dept parentDept = deptService.selectDeptById(parentId);
        mmap.put("dept", parentDept);
        FlowLine flowLine = flowLineService.selectFlowLineById(id);
        mmap.put("flowLine", flowLine);
        return prefix + "/edit";
    }

    /**
     * 修改保存线性流程
     */
    @RequiresPermissions("system:line:edit")
    @Log(title = "线性流程", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FlowLine flowLine)
    {
        return toAjax(flowLineService.updateFlowLine(flowLine));
    }

    /**
     * 删除线性流程
     */
    @RequiresPermissions("system:line:remove")
    @Log(title = "线性流程", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(flowLineService.deleteFlowLineByIds(ids));
    }

    /**
     * 选择流程
     */
    @GetMapping("/radio")
    public String radioLine(ModelMap mmap)
    {
        return prefix + "/radioLine";
    }

    /**
     * 选择流程后选择用户的传参方法
     * @param flowid
     * @param index
     * @param mmap
     * @return
     */
    @GetMapping("/radio/{flowid}/{index}")
    public String radioUser(@PathVariable("flowid") Long flowid, @PathVariable("index") Integer index, ModelMap mmap)
    {
        mmap.put("flowid", flowid);
        mmap.put("index", index);
        return prefix + "/radioUser";
    }

    /**
     * 根据流程及步骤选择用户
     * @param flowid
     * @param index
     * @return
     */
    @PostMapping("/userList/{flowid}/{index}")
    @ResponseBody
    public TableDataInfo userList(@PathVariable("flowid") Long flowid, @PathVariable("index") Integer index, User user)
    {
        startPage();
        FlowLine line = flowLineService.selectFlowLineById(flowid);
        String flowcode = line.getFlowcode();
        String[] codeArr = flowcode.split("#");
        String needCode = codeArr[0];
        if (index < codeArr.length){
            needCode = codeArr[index];
        }
        if (needCode.indexOf("D")>=0){
            Long deptId = Long.valueOf(needCode.substring(needCode.indexOf("D")+1, needCode.indexOf("P")));
            Long postId = Long.valueOf(needCode.substring(needCode.indexOf("P")+1));
            user.setPostId(postId);
            //查看是否为末端部门节点，末端设为查询本部门人员
            Dept deptParam = new Dept();
            deptParam.setParentId(deptId);
            List<Dept> depts = deptService.selectDeptList(deptParam);
            if(depts.size()>0){
                user.setDeptId(deptId);
            } else {
                user.setDeptId(null);
                user.setNumber(deptId.toString());
            }
        }else{
            //未选择部门则获取本部门岗位人员
            Long postId = Long.valueOf(needCode.substring(needCode.indexOf("P")+1));
            user.setPostId(postId);
            user.setDeptId(null);
            user.setNumber(getSysUser().getDept().getDeptId().toString());
        }
        List<User> users = userService.selectUserByDeptPost(user);
        return getDataTable(users);
    }

    /**
     * 流程描述
     * 每个流程由部门和岗位组合的节点组成，并有节点序号定位具体节点
     * 每次修改流程操作均会生成记录，初始提交会生成一个节点标识的节点操作者
     * 后续审核均会修改当前节点记录状态与操作者，且除终止节点（退回操作或结尾步骤）外还会生成下节点标识下节点操作者
     * 当流程未结束时，流程最后一条操作记录始终为待审核（待操作）状态
     * @param recLine
     * @return
     */
    @Log(title = "线性流程记录", businessType = BusinessType.INSERT)
    @PostMapping("/addRecLine")
    @ResponseBody
    public AjaxResult addRecLine(RecLine recLine) throws Exception
    {
        //修改现有节点，本步骤
        AjaxResult result = new AjaxResult();
        if (recLine.getId() != null){
            RecLine updateParam = new RecLine();
            updateParam.setId(recLine.getId());
            recLine.setId(null);
            //操作时间与操作状态
            updateParam.setOperateTime(new Date());
            updateParam.setOperateStatus(recLine.getOperateStatus());
            //与退回有关，退回备注
            if (recLine.getOperateContent() != null && !"".equals(recLine.getOperateContent())){
                updateParam.setOperateContent(recLine.getOperateContent());
                recLine.setOperateContent(null);
            }
            flowLineService.updateRecLine(updateParam);

        }

        //生成下步节点，下一步骤
        Long lineId = recLine.getLineId();
        Long recId = recLine.getRecId();
        int lineIndex = recLine.getLineIndex();

        FlowLine flowLine = flowLineService.selectFlowLineById(lineId);
        String flowcode = flowLine.getFlowcode();
        String[] nodeArr = flowcode.split("#");
        if(lineIndex <= nodeArr.length-1){
            //-1为退回至创建者，lineNode为空
            if(lineIndex >= 0){
                recLine.setLineNode(nodeArr[lineIndex]);
            }
            recLine.setCreateBy(getLoginName());
            recLine.setCreateTime(new Date());
            recLine.setOperateStatus("0");
            flowLineService.insertRecLine(recLine);
        }
        //推送
        String title = recLine.getRecType();
        String descr = "您有一条 " + title + " 申请待审批，点击查看详情";
        String recUserName = recLine.getOperateBy();
        if (recUserName == null){
            RecLine findParam = new RecLine();
            findParam.setRecId(recLine.getRecId());
            List<RecLine> recLines = flowLineService.selectRecLineList(findParam);
            if(recLines.size() > 0){
                recUserName = recLines.get(0).getCreateBy();
                descr = "您发起的 "+ title + " 已完成，点击查看详情";
            }
        }
        if (recUserName != null){
            String urlPrefix = configService.selectConfigByKey("system.url.prefix");
            String url = urlPrefix + recLine.getRecUrl();
            url = url.replace("financial", "financial/mobile");
            String encodedString = URLEncoder.encode(url, "UTF-8");
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                    "appid="+wxWorkConfig.getCorpid()+"&redirect_uri="+encodedString+"&" +
                    "response_type=code&scope=snsapi_base&state=#wechat_redirect";
            ArrayList<String> userNameList = new ArrayList<String>();
            userNameList.add(recUserName);
            pushWxWorkMes.sendMessage(userNameList,title, descr, url);
        }
        //推送完毕
        RecLine param = new RecLine();
        param.setRecId(recId);
        result = flowLineService.operateInfo(getSysUser(), param);
        return result;
    }

    /**
     * 获取当前用户是否可操作及审核记录
     * createBy传入记录本身创建者即申请人
     * @param recLine
     * @return
     * canOperate true 可操作 false 不可操作
     * recLines 审核记录
     */
    @PostMapping("/operateInfo")
    @ResponseBody
    public AjaxResult operateInfo(RecLine recLine)
    {
        AjaxResult result = flowLineService.operateInfo(getSysUser(), recLine);
        return result;
    }

    @GetMapping("/matter")
    public String matter(ModelMap mmap)
    {

        return prefix + "/matter";
    }

    @PostMapping("/matterList/{tag}")
    @ResponseBody
    public TableDataInfo matterList(@PathVariable("tag") String tag)
    {
        startPage();
        List<RecLine> list = new ArrayList<RecLine>();
        RecLine recLine = new RecLine();
        recLine.setRemark("matter");
        //需特殊处理
        //0待处理 1已处理 2我发起
        if ("1".equals(tag)){
            recLine.setOperateStatus(tag);
            recLine.setOperateBy(getLoginName());
            List<RecLine> tmp = flowLineService.selectRecLineList(recLine);
            list.addAll(tmp);
            recLine.setOperateStatus("2");
            List<RecLine> tmp2 = flowLineService.selectRecLineList(recLine);
            list.addAll(tmp2);
        } else if ("9".equals(tag)){
            recLine.setOperateStatus(tag);
            recLine.setLineIndex(0);
            recLine.setCreateBy(getLoginName());
            List<RecLine> tmp = flowLineService.selectRecLineList(recLine);
            list.addAll(tmp);
        } else {
            recLine.setOperateStatus(tag);
            recLine.setOperateBy(getLoginName());
            List<RecLine> tmp = flowLineService.selectRecLineList(recLine);
            list.addAll(tmp);
        }
        return getDataTable(list);
    }

    @GetMapping("/timeline/{recId}")
    public String timeline(@PathVariable("recId") Long recId, ModelMap mmap)
    {
        RecLine param = new RecLine();
        param.setRecId(recId);
        List<RecLine> recLines = flowLineService.selectRecLineList(param);
        mmap.put("recLines", recLines);
        return prefix + "/timeline";
    }
}
