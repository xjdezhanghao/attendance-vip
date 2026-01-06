package com.attendance.project.system.line.service.impl;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.project.system.line.domain.BusLine;
import com.attendance.project.system.line.domain.FlowLine;
import com.attendance.project.system.line.domain.RecLine;
import com.attendance.project.system.line.mapper.BusLineMapper;
import com.attendance.project.system.line.mapper.FlowLineMapper;
import com.attendance.project.system.line.mapper.RecLineMapper;
import com.attendance.project.system.line.service.IFlowLineService;
import com.attendance.project.system.post.domain.Post;
import com.attendance.project.system.post.mapper.PostMapper;
import com.attendance.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 线性流程Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-06
 */
@Service
public class FlowLineServiceImpl implements IFlowLineService 
{
    @Autowired
    private FlowLineMapper flowLineMapper;

    @Autowired
    private RecLineMapper recLineMapper;

    @Autowired
    private BusLineMapper busLineMapper;

    @Autowired
    private PostMapper postMapper;

    /**
     * 查询线性流程
     * 
     * @param id 线性流程主键
     * @return 线性流程
     */
    @Override
    public FlowLine selectFlowLineById(Long id)
    {
        return flowLineMapper.selectFlowLineById(id);
    }

    /**
     * 查询线性流程列表
     * 
     * @param flowLine 线性流程
     * @return 线性流程
     */
    @Override
    public List<FlowLine> selectFlowLineList(FlowLine flowLine)
    {
        return flowLineMapper.selectFlowLineList(flowLine);
    }

    /**
     * 新增线性流程
     * 
     * @param flowLine 线性流程
     * @return 结果
     */
    @Override
    public int insertFlowLine(FlowLine flowLine)
    {
        flowLine.setCreateTime(DateUtils.getNowDate());
        return flowLineMapper.insertFlowLine(flowLine);
    }

    /**
     * 修改线性流程
     * 
     * @param flowLine 线性流程
     * @return 结果
     */
    @Override
    public int updateFlowLine(FlowLine flowLine)
    {
        return flowLineMapper.updateFlowLine(flowLine);
    }

    /**
     * 批量删除线性流程
     * 
     * @param ids 需要删除的线性流程主键
     * @return 结果
     */
    @Override
    public int deleteFlowLineByIds(String ids)
    {
        return flowLineMapper.deleteFlowLineByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除线性流程信息
     * 
     * @param id 线性流程主键
     * @return 结果
     */
    @Override
    public int deleteFlowLineById(Long id)
    {
        return flowLineMapper.deleteFlowLineById(id);
    }

    @Override
    public int insertRecLine(RecLine recLine) {
        return recLineMapper.insertRecLine(recLine);
    }

    @Override
    public int updateRecLine(RecLine recLine) {
        return recLineMapper.updateRecLine(recLine);
    }

    @Override
    public List<RecLine> selectRecLineList(RecLine recLine) {
        return recLineMapper.selectRecLineList(recLine);
    }

    @Override
    public AjaxResult operateInfo(User curUser, RecLine recLine) {
        AjaxResult result = new AjaxResult();
        List<RecLine> recLines = selectRecLineList(recLine);

        boolean canOperate = false;
        boolean canBack = false;//需结合可操作即canOperate使用
        if (recLines.size() > 0){
            RecLine lastRecline = recLines.get(recLines.size()-1);
            if (lastRecline.getOperateBy().equals(curUser.getLoginName()) && "0".equals(lastRecline.getOperateStatus())){
                canOperate = true;
            }
            //不是创建者的中间流程均可退回
            if (!recLines.get(0).getCreateBy().equals(curUser.getLoginName())){
                canBack = true;
            }
            //判断是否最后一步
            Long lineId = lastRecline.getLineId();;
            FlowLine line = flowLineMapper.selectFlowLineById(lineId);
            String[] flowCodeArr = line.getFlowcode().split("#");
            String[] flowNodeArr = line.getFlownode().split(" -- ");
            result.put("flowCodeArr", flowCodeArr);
            result.put("flowNodeArr", flowNodeArr);
            Integer lastNum = flowCodeArr.length-1;
            if (lastNum == lastRecline.getLineIndex()){
                result.put("hasFinal", true);
            } else {
                result.put("hasFinal", false);
                //获取后续步骤
                List<RecLine> conRecLines = new ArrayList<RecLine>();
                for (int i=lastRecline.getLineIndex()+1; i<flowCodeArr.length; i++){
                    RecLine conRecLine = new RecLine();
                    conRecLine.setLineIndex(i);
                    String flowNode = flowNodeArr[i];
                    if (flowNode.indexOf("D")>=0){
                        flowNode = flowNode.replaceAll("D", "");
                        flowNode = flowNode.replaceAll("P", " ");
                    } else {
                        flowNode = flowNode.replaceAll("P", "");
                    }
                    conRecLine.setOperateBy(flowNode);
                    conRecLines.add(conRecLine);
                }
                result.put("conRecLines", conRecLines);
            }
        } else {
            if (recLine.getCreateBy() != null && recLine.getCreateBy().equals(curUser.getLoginName())){
                canOperate = true;
            }
        }
        result.put("canBack", canBack);
        result.put("canOperate", canOperate);
        result.put("recLines", recLines);
        return result;
    }

    @Override
    public List<BusLine> selectBusLineList(BusLine busLine) {
        return busLineMapper.selectBusLineList(busLine);
    }

    @Override
    public List<FlowLine> selectFlowLineListByPostAndBusiness(BusLine busLine, User user) {
        List<Post> posts = postMapper.selectPostsByUserId(user.getUserId());
        List<String> postCodes = new ArrayList<String>();
        postCodes.add("user");
        for (int i=0; i<posts.size(); i++){
            postCodes.add(posts.get(i).getPostCode());
        }
        busLine.setPostCodes(postCodes);
        return busLineMapper.selectFlowLineList(busLine);
    }

    @Override
    public int deleteRecLineByRecId(Long id) {
        return recLineMapper.deleteRecLineByRecId(id);
    }
}
