package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.entity.UserGroupDetails;
import com.yodoo.megalodon.permission.mapper.UserGroupDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description ：用户组关系明细
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class UserGroupDetailsService {

    @Autowired
    private UserGroupDetailsMapper userGroupDetailsMapper;

    /**
     * 通过用户组id查询总数
     * @param userGroupId
     * @return
     */
    public Integer selectCountByUserGroupId(Integer userGroupId) {
        return userGroupDetailsMapper.selectCountByExample(getExample(userGroupId));
    }

    /**
     * 通过用户组id 删除
     * @param userGroupId
     */
    public void deleteUserGroupDetailsByUserGroupId(Integer userGroupId) {
        userGroupDetailsMapper.deleteByExample(getExample(userGroupId));
    }

    /**
     * 更新用户组详情
     * @param userId
     * @param userGroupIds
     */
    public void updateUserGroupDetails(Integer userId, Set<Integer> userGroupIds){
        if (userId != null && userId > 0){
            Example example = new Example(UserGroupDetails.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            userGroupDetailsMapper.deleteByExample(example);
        }
        if (userId != null && userId > 0 && !CollectionUtils.isEmpty(userGroupIds)){
            userGroupIds.stream()
                    .filter(Objects::nonNull)
                    .map(userGroupId -> {
                        return userGroupDetailsMapper.insertSelective(new UserGroupDetails(userGroupId, userId));
                    }).filter(Objects::nonNull).count();
        }
    }

    /**
     * 批量处理时维护用户与用户组关系表
     * @param userGroupId
     * @param userIdList
     */
    public void updateUserGroupDetailsBatch(Integer userGroupId, Set<Integer> userIdList) {
        if (userGroupId != null && userGroupId > 0){
            deleteUserGroupDetailsByUserGroupId(userGroupId);
        }
        if (userGroupId != null && userGroupId > 0 && !CollectionUtils.isEmpty(userIdList)){
            userIdList.stream()
                    .filter(Objects::nonNull)
                    .map(userId -> {
                       return userGroupDetailsMapper.insertSelective(new UserGroupDetails(userGroupId,userId));
                    }).filter(Objects::nonNull).count();
        }
    }

    /**
     * 通过用户组id查询用户ids
     * @param userGroupId
     */
    public Set<Integer>  selectUserIdsByUserGroupId(Integer userGroupId) {
        List<UserGroupDetails> userGroupDetails = userGroupDetailsMapper.selectByExample(getExample(userGroupId));
        Set<Integer> userIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(userGroupDetails)){
            userIds = userGroupDetails.stream().filter(Objects::nonNull).map(UserGroupDetails::getUserId).filter(Objects::nonNull).collect(Collectors.toSet());
        }
        return userIds;
    }

    private Example getExample(Integer userGroupId){
        Example example = new Example(UserGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userGroupId", userGroupId);
        return example;
    }
}
