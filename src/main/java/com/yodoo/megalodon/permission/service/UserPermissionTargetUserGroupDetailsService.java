package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.entity.UserPermissionTargetUserGroupDetails;
import com.yodoo.megalodon.permission.mapper.UserPermissionTargetUserGroupDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description ：目标用户组
 * @Author ：jinjun_luo
 * @Date ： 2019/8/7 0007
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionTargetUserGroupDetailsService {

    @Autowired
    private UserPermissionTargetUserGroupDetailsMapper userPermissionTargetUserGroupDetailsMapper;

    /**
     * 更新目标用户组
     * @param userGroupId
     * @param userPermissionIds
     */
    public void updateUserPermissionTargetUserGroupDetails(Integer userGroupId, Set<Integer> userPermissionIds){
        // 通过用户组id 删除
        if (userGroupId != null && userGroupId > 0){
            deleteUserPermissionTargetUserGroupDetailsByUserGroupId(userGroupId);
        }
        // 插入目标用户组数据
        if (userGroupId != null && userGroupId > 0 && !CollectionUtils.isEmpty(userPermissionIds)){
            userPermissionIds.stream()
                    .filter(Objects::nonNull)
                    .forEach(userPermissionId -> {
                        insertUserPermissionDetails(userGroupId, userPermissionId);
                    });
        }
    }

    /**
     * 插入
     * @param userGroupId
     * @param userPermissionId
     */
    public void insertUserPermissionDetails(Integer userGroupId, Integer userPermissionId) {
        userPermissionTargetUserGroupDetailsMapper.insertSelective(new UserPermissionTargetUserGroupDetails(userGroupId, userPermissionId));
    }

    /**
     * 通过用户组id 删除
     * @param userGroupId
     */
    public Integer deleteUserPermissionTargetUserGroupDetailsByUserGroupId(Integer userGroupId) {
        return userPermissionTargetUserGroupDetailsMapper.deleteByExample(getExample(userGroupId));
    }

    /**
     *  通过用户组id查询用户权限id
     * @param userGroupId
     * @return
     */
    public Set<Integer> getUserPermissionIdsByUserGroupId(Integer userGroupId) {
        List<UserPermissionTargetUserGroupDetails> userPermissionTargetUserGroupDetails = userPermissionTargetUserGroupDetailsMapper.selectByExample(getExample(userGroupId));
        if (!CollectionUtils.isEmpty(userPermissionTargetUserGroupDetails)) {
            return userPermissionTargetUserGroupDetails.stream()
                    .filter(Objects::nonNull).map(UserPermissionTargetUserGroupDetails::getUserPermissionId).collect(Collectors.toSet());
        }
        return null;
    }

    /**
     * 通过用户组id 获取查询条件
     * @param userGroupId
     * @return
     */
    private Example getExample(Integer userGroupId){
        Example example = new Example(UserPermissionTargetUserGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        if (userGroupId != null && userGroupId > 0){
            criteria.andEqualTo("userGroupId", userGroupId);
        }
        return example;
    }
}
