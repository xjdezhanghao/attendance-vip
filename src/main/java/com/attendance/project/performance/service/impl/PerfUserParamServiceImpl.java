package com.attendance.project.performance.service.impl;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.StringUtils;
import com.attendance.common.utils.security.ShiroUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.project.performance.domain.PerfUserParam;
import com.attendance.project.performance.domain.PerfUserPost;
import com.attendance.project.performance.mapper.PerfPostMapper;
import com.attendance.project.performance.mapper.PerfUserParamMapper;
import com.attendance.project.performance.mapper.PerfUserPostMapper;
import com.attendance.project.performance.service.IPerfUserParamService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.domain.UserPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户参数Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-23
 */
@Service
public class PerfUserParamServiceImpl implements IPerfUserParamService
{
    @Autowired
    private PerfUserParamMapper perfUserParamMapper;

    @Autowired
    private PerfUserPostMapper  userPostMapper;

    /**
     * 查询用户参数
     *
     * @param id 用户参数主键
     * @return 用户参数
     */
    @Override
    public PerfUserParam selectPerfUserParamById(Long id)
    {
        return perfUserParamMapper.selectPerfUserParamById(id);
    }

    /**
     * 查询用户参数列表
     *
     * @param perfUserParam 用户参数
     * @return 用户参数
     */
    @Override
    public List<PerfUserParam> selectPerfUserParamList(PerfUserParam perfUserParam)
    {
        return perfUserParamMapper.selectPerfUserParamList(perfUserParam);
    }

    @Override
    public List<PerfUserParam> selectPerfUserParamListOnly(PerfUserParam perfUserParam) {
        return perfUserParamMapper.selectPerfUserParamListOnly(perfUserParam);
    }

    /**
     * 新增用户参数
     *
     * @param perfUserParam 用户参数
     * @return 结果
     */
    @Override
    public int insertPerfUserParam(PerfUserParam perfUserParam)
    {
        perfUserParam.setCreateBy(ShiroUtils.getLoginName());
        perfUserParam.setCreateTime(DateUtils.getNowDate());
        // 新增用户与岗位管理
        insertUserPost(perfUserParam);
        return perfUserParamMapper.insertPerfUserParam(perfUserParam);
    }

    @Override
    public int batchInsertPerfUserParam(List<PerfUserParam> userParams) {
        return perfUserParamMapper.batchInsertPerfUserParam(userParams);
    }

    /**
     * 修改用户参数
     *
     * @param perfUserParam 用户参数
     * @return 结果
     */
    @Override
    public int updatePerfUserParam(PerfUserParam perfUserParam)
    {
        Long userId = perfUserParam.getUserId();
        perfUserParam.setUpdateBy(ShiroUtils.getLoginName());
        perfUserParam.setUpdateTime(DateUtils.getNowDate());
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(perfUserParam);
        return perfUserParamMapper.updatePerfUserParam(perfUserParam);
    }

    /**
     * 批量删除用户参数
     *
     * @param ids 需要删除的用户参数主键
     * @return 结果
     */
    @Override
    public int deletePerfUserParamByIds(String ids)
    {
        return perfUserParamMapper.deletePerfUserParamByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用户参数信息
     *
     * @param id 用户参数主键
     * @return 结果
     */
    @Override
    public int deletePerfUserParamById(Long id)
    {
        return perfUserParamMapper.deletePerfUserParamById(id);
    }

    public void insertUserPost(PerfUserParam user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // 新增用户与岗位管理
            List<PerfUserPost> list = new ArrayList<PerfUserPost>();
            for (Long postId : user.getPostIds())
            {
                PerfUserPost up = new PerfUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0)
            {
                userPostMapper.batchUserPost(list);
            }
        }
    }
}
