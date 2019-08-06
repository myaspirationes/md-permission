package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.entity.UserGroupPermissionDetails;
import com.yodoo.megalodon.permission.mapper.UserGroupPermissionDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @Description ：用户组权限组关系
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserGroupPermissionDetailsService {

    @Autowired
    private UserGroupPermissionDetailsMapper userGroupPermissionDetailsMapper;

    /**
     * 通过权限组查询
     *
     * @param permissionGroupId
     * @return
     */
    public Integer selectUserGroupPermissionDetailsCountByPermissionGroupId(Integer permissionGroupId) {
        return userGroupPermissionDetailsMapper.selectCount(new UserGroupPermissionDetails(null, permissionGroupId));
    }

    /**
     * 通过用户组id 删除
     * @param userGroupId
     * @return
     */
    public Integer deleteUserGroupPermissionDetailsByUserGroupId(Integer userGroupId) {
        if (userGroupId != null && userGroupId > 0){
            UserGroupPermissionDetails userGroupPermissionDetails = new UserGroupPermissionDetails();
            userGroupPermissionDetails.setUserGroupId(userGroupId);
            return userGroupPermissionDetailsMapper.delete(userGroupPermissionDetails);
        }
        return null;
    }

    /**
     * 插入用户组权限组关系表数据
     * @param userGroupId
     * @param permissionGroupIds
     * @return
     */
    public Long insertUserGroupPermissionDetails(Integer userGroupId, Set<Integer> permissionGroupIds) {
        Long count = null;
        if (userGroupId != null && userGroupId > 0 && !CollectionUtils.isEmpty(permissionGroupIds)){
            count = permissionGroupIds.stream()
                    .filter(Objects::nonNull)
                    .map(permissionGroupId -> {
                        return userGroupPermissionDetailsMapper.insertSelective(new UserGroupPermissionDetails(userGroupId, permissionGroupId));
                    }).filter(Objects::nonNull).count();
        }
        return count;
    }
}
