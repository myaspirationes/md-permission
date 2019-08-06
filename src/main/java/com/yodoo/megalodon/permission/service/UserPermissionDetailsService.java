package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
import com.yodoo.megalodon.permission.mapper.UserPermissionDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Description ：用户权限
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionDetailsService {

    @Autowired
    private UserPermissionDetailsMapper userPermissionDetailsMapper;

    /**
     * 通过用户 id 查询 用户权限列表
     *
     * @param userId
     * @return
     */
    public List<UserPermissionDetails> selectUserPermissionDetailsByUserId(Integer userId) {
        Example example = new Example(UserPermissionDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        return userPermissionDetailsMapper.selectByExample(example);
    }

    /**
     * 通过用户id删除
     * @param userId
     * @return
     */
    public Integer deleteUserPermissionDetailsByUserId(Integer userId) {
        UserPermissionDetails userPermissionDetails = new UserPermissionDetails();
        userPermissionDetails.setUserId(userId);
        return userPermissionDetailsMapper.delete(userPermissionDetails);
    }

    /**
     * 添加
     * @param userId
     * @param permissionIds
     */
    public void insertUserPermissionDetails(Integer userId, Set<Integer> permissionIds) {
        if (!CollectionUtils.isEmpty(permissionIds)){
            permissionIds.stream()
                    .filter(Objects::nonNull)
                    .map(permissionId -> {
                        return userPermissionDetailsMapper.insertSelective(new UserPermissionDetails(userId, permissionId));
                    }).filter(Objects::nonNull).count();
        }
    }
}
