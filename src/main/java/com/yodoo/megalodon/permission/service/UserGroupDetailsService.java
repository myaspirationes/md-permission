package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.entity.UserGroupDetails;
import com.yodoo.megalodon.permission.mapper.UserGroupDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Objects;
import java.util.Set;

/**
 * @Description ：用户组关系明细
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserGroupDetailsService {

    @Autowired
    private UserGroupDetailsMapper userGroupDetailsMapper;

    /**
     * 通过用户 id 删除
     * @param userId
     * @return
     */
    public Integer deleteUserGroupByUserId(Integer userId) {
        if (userId != null && userId > 0){
            UserGroupDetails userGroupDetails = new UserGroupDetails();
            userGroupDetails.setUserId(userId);
            return userGroupDetailsMapper.delete(userGroupDetails);
        }
        return null;
    }

    /**
     * 插入用户组详情数据
     * @param userId
     * @param userGroupIds
     * @return
     */
    public Long insertUserGroupDetails(Integer userId, Set<Integer> userGroupIds) {
        Long count = null;
        if (userId != null && userId > 0 && !CollectionUtils.isEmpty(userGroupIds)){
            count = userGroupIds.stream()
                    .filter(Objects::nonNull)
                    .map(userGroupId -> {
                        return userGroupDetailsMapper.insertSelective(new UserGroupDetails(userGroupId, userId));
                    }).filter(Objects::nonNull).count();
        }
        return count;
    }

    /**
     * 通过用户组id查询总数
     * @param userGroupId
     * @return
     */
    public Integer selectCountByUserGroupId(Integer userGroupId) {
        Example example = new Example(UserGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userGroupId", userGroupId);
        return userGroupDetailsMapper.selectCountByExample(example);
    }
}
