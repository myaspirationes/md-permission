package com.yodoo.megalodon.permission.mapper;

import com.yodoo.megalodon.permission.common.BaseMapper;
import com.yodoo.megalodon.permission.entity.UserGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description ：用户组
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public interface UserGroupMapper extends BaseMapper<UserGroup> {

    /**
     * 查询除自己以外是否有相同的数据
     * @param id
     * @param groupCode
     * @return
     */
    UserGroup selectOtherThanOneself(@Param("id") Integer id, @Param("groupCode") String groupCode);

    /**
     * 查询所有用户组数据
     * @return
     */
    List<UserGroup> getUserGroupAll();

}