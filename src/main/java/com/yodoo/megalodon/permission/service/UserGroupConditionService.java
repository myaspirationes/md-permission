package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.ConditionDto;
import com.yodoo.megalodon.permission.entity.UserGroupCondition;
import com.yodoo.megalodon.permission.mapper.UserGroupConditionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Description ：查询条件用户组关系表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/8 0008
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
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
     * @param conditionDtoList
     */
    public void updateUserGroupCondition(Integer userGroupId, Set<ConditionDto> conditionDtoList) {
        // 删除
        if (userGroupId != null && userGroupId > 0){
            Example example = new Example(UserGroupCondition.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userGroupId", userGroupId);
            userGroupConditionMapper.deleteByExample(example);
        }
        // 插入
        if (userGroupId != null && userGroupId > 0 && !CollectionUtils.isEmpty(conditionDtoList)){
            conditionDtoList.stream()
                    .filter(Objects::nonNull)
                    .map(conditionDto -> {
                        UserGroupCondition userGroupCondition = new UserGroupCondition();
                        userGroupCondition.setUserGroupId(userGroupId);
                        userGroupCondition.setSearchConditionId(conditionDto.getSearchConditionId());
                        userGroupCondition.setOperator(conditionDto.getOperator());
                        userGroupCondition.setMatchValue(conditionDto.getMatchValue());
                        return userGroupConditionMapper.insertSelective(userGroupCondition);
                    }).filter(Objects::nonNull).count();
        }
    }
}
