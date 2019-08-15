package com.yodoo.megalodon.permission.mapper;

import com.yodoo.megalodon.permission.common.BaseMapper;
import com.yodoo.megalodon.permission.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Description ：用户
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询除 ids 以外
     * @param userIds
     * @return
     */
    List<User> selectUserNotInIds(@Param("userIds") Set<Integer> userIds);

    /**
     * 条件 sql 查询
     * @param operator
     * @return
     */
    List<User> selectByCondition(@Param("operator") String operator);
}