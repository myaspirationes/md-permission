package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.entity.SearchCondition;
import com.yodoo.megalodon.permission.entity.UserGroupCondition;
import com.yodoo.megalodon.permission.mapper.UserGroupConditionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;

/**
 * @Description ：查询条件用户组关系表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/8 0008
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class UserGroupConditionService {

    @Autowired
    private UserGroupConditionMapper userGroupConditionMapper;

    /**
     * 通过查询条件id查询
     * @param searchConditionId
     * @return
     */
    public List<UserGroupCondition> selectUserGroupConditionBySearchConditionId(Integer searchConditionId) {
        Example example = new Example(UserGroupCondition.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("searchConditionId", searchConditionId);
        return userGroupConditionMapper.selectByExample(example);
    }

    /**
     * 更新用户组时，更新用户组条件表
     * @param userGroupId
     * @param searchConditionList
     */
    public void updateUserGroupCondition(Integer userGroupId, List<SearchCondition> searchConditionList) {
        // 删除
        if (userGroupId != null && userGroupId > 0){
            deleteUserGroupConditionByUserGroupId(userGroupId);
        }
        // 插入
        if (userGroupId != null && userGroupId > 0 && !CollectionUtils.isEmpty(searchConditionList)){
            StringBuilder sqlBuffer = new StringBuilder();
            searchConditionList.stream()
                    .filter(Objects::nonNull)
                    .forEach(searchCondition -> {
                        sqlBuffer.append(searchCondition.getConditionName());
                        sqlBuffer.append(" ");
                        sqlBuffer.append(searchCondition.getConditionCode());
                        sqlBuffer.append(" ");
                        sqlBuffer.append(searchCondition.getConditionValue());
                        sqlBuffer.append(" and ");
                    });
            String sqlBufferString = sqlBuffer.toString();
            String sqlRequest = sqlBufferString.substring(0, sqlBufferString.length() - 5);

            UserGroupCondition userGroupCondition = new UserGroupCondition();
            userGroupCondition.setUserGroupId(userGroupId);
            userGroupCondition.setOperator(sqlRequest);
            userGroupConditionMapper.insertSelective(userGroupCondition);
        }
    }

    /**
     * 通过用户组id 删除
     * @param userGroupId
     * @return
     */
    public Integer deleteUserGroupConditionByUserGroupId(Integer userGroupId) {
        Example example = new Example(UserGroupCondition.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userGroupId", userGroupId);
        return userGroupConditionMapper.deleteByExample(example);
    }

    /**
     * 通过用户组id 查询
     * @param userGroupId
     * @return
     */
    public UserGroupCondition selectUserGroupConditionByUserGroupId(Integer userGroupId){
        UserGroupCondition userGroupCondition = new UserGroupCondition();
        userGroupCondition.setUserGroupId(userGroupId);
        return userGroupConditionMapper.selectOne(userGroupCondition);
    }
}
