package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.entity.UserPermissionTargetUserGroupDetails;
import com.yodoo.megalodon.permission.mapper.UserPermissionTargetUserGroupDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @Description ：目标用户组
 * @Author ：jinjun_luo
 * @Date ： 2019/8/7 0007
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
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
            UserPermissionTargetUserGroupDetails userPermissionTargetUserGroupDetails = new UserPermissionTargetUserGroupDetails();
            userPermissionTargetUserGroupDetails.setUserGroupId(userGroupId);
            userPermissionTargetUserGroupDetailsMapper.delete(userPermissionTargetUserGroupDetails);
        }
        // 插入目标用户组数据
        if (userGroupId != null && userGroupId > 0 && !CollectionUtils.isEmpty(userPermissionIds)){
            userPermissionIds.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionId -> {
                        return userPermissionTargetUserGroupDetailsMapper.insertSelective(new UserPermissionTargetUserGroupDetails(userGroupId, userPermissionId));
                    })
                    .filter(Objects::nonNull)
                    .count();
        }
    }
}
