package com.attendance.project.performance.service;

import java.util.List;
import com.attendance.project.performance.domain.PerfPost;

/**
 * 岗位信息 服务层
 * 
 * @author june
 */
public interface IPerfPostService
{
    /**
     * 查询岗位信息集合
     * 
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    public List<PerfPost> selectPostList(PerfPost post);

    /**
     * 查询所有岗位
     * 
     * @return 岗位列表
     */
    public List<PerfPost> selectPostAll();

    /**
     * 根据用户ID查询岗位
     * 
     * @param userId 用户ID
     * @return 岗位列表
     */
    public List<PerfPost> selectPostsByUserId(Long userId);

    /**
     * 通过岗位ID查询岗位信息
     * 
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    public PerfPost selectPostById(Long postId);

    /**
     * 批量删除岗位信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePostByIds(String ids);

    /**
     * 新增保存岗位信息
     * 
     * @param post 岗位信息
     * @return 结果
     */
    public int insertPost(PerfPost post);

    /**
     * 修改保存岗位信息
     * 
     * @param post 岗位信息
     * @return 结果
     */
    public int updatePost(PerfPost post);

    /**
     * 通过岗位ID查询岗位使用数量
     * 
     * @param postId 岗位ID
     * @return 结果
     */
    public int countUserPostById(Long postId);

    /**
     * 校验岗位名称
     * 
     * @param post 岗位信息
     * @return 结果
     */
    public String checkPostNameUnique(PerfPost post);

    /**
     * 校验岗位编码
     * 
     * @param post 岗位信息
     * @return 结果
     */
    public String checkPostCodeUnique(PerfPost post);
}
