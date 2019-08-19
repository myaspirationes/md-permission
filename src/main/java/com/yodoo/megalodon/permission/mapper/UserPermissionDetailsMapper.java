package com.yodoo.megalodon.permission.mapper;

import com.yodoo.megalodon.permission.common.BaseMapper;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description ：用户权限表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public interface UserPermissionDetailsMapper extends BaseMapper<UserPermissionDetails> {

    /**
     * 通过用户id 查询权限
     * @param userId
     * @return
     */
    List<UserPermissionDetails> selectUserPermissionDetailsByUserId(@Param("userId") Integer userId);
}