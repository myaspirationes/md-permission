package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.SearchConditionDto;
import com.yodoo.megalodon.permission.entity.SearchCondition;
import com.yodoo.megalodon.permission.entity.UserGroupCondition;
import com.yodoo.megalodon.permission.mapper.UserGroupConditionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Autowired
    private SearchConditionService searchConditionService;

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
     * 通过用户组id查询获取用户组对应的筛选条件
     * @param userGroupId
     * @return
     */
    public List<SearchConditionDto> getSearchConditionByUserGroupId(Integer userGroupId) {
        // 查询用户组与每件关系表
        Example example = getExampleByUserGroupId(userGroupId);
        List<UserGroupCondition> userGroupConditions = userGroupConditionMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(userGroupConditions)){
            return userGroupConditions.stream()
                    .filter(Objects::nonNull)
                    .filter(userGroupCondition -> userGroupCondition.getSearchConditionId() != null && userGroupCondition.getSearchConditionId() > 0)
                    .map(userGroupCondition -> {
                        SearchCondition searchCondition = searchConditionService.selectByPrimaryKey(userGroupCondition.getSearchConditionId());
                        SearchConditionDto searchConditionDto = new SearchConditionDto();
                        if (null != searchCondition) {
                            BeanUtils.copyProperties(searchCondition, searchConditionDto);
                        }
                        return searchConditionDto;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return null;
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
            searchConditionList.stream()
                    .filter(Objects::nonNull)
                    .map(searchCondition -> {
                        UserGroupCondition userGroupCondition = new UserGroupCondition();
                        // 用户组条件id
                        userGroupCondition.setSearchConditionId(searchCondition.getId());
                        // 用户组id
                        userGroupCondition.setUserGroupId(userGroupId);
                        // 运算符号
                         userGroupCondition.setOperator(searchCondition.getConditionName());
                        // 匹配值
                         userGroupCondition.setMatchValue(searchCondition.getConditionValue());
                        return userGroupConditionMapper.insertSelective(userGroupCondition);
                    }).filter(Objects::nonNull).count();
        }
    }

    /**
     * 通过用户组id 删除
     * @param userGroupId
     * @return
     */
    public Integer deleteUserGroupConditionByUserGroupId(Integer userGroupId) {
        return userGroupConditionMapper.deleteByExample(getExampleByUserGroupId(userGroupId));
    }

    /**
     * 通过用户组id 查询
     * @param userGroupId
     * @return
     */
    public Map<String, List<SearchCondition>> selectUserGroupConditionByUserGroupId(Integer userGroupId){

        // 查询出用户组条件
        List<UserGroupCondition> userGroupConditions = userGroupConditionMapper.selectByExample(getExampleByUserGroupId(userGroupId));
        if (!CollectionUtils.isEmpty(userGroupConditions)){
            // 查询出用户组对应的条件
            List<SearchCondition> searchConditionList = userGroupConditions.stream()
                    .filter(Objects::nonNull)
                    .filter(userGroupCondition -> userGroupCondition.getSearchConditionId() != null && userGroupCondition.getSearchConditionId() > 0)
                    .map(userGroupCondition -> {
                        return searchConditionService.selectByPrimaryKey(userGroupCondition.getSearchConditionId());
                    }).filter(Objects::nonNull).collect(Collectors.toList());
            // 如果条件存在，进行分组
            if (!CollectionUtils.isEmpty(searchConditionList)){
                return searchConditionList.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(SearchCondition::getConditionName));
            }
        }
        return null;
    }

    /**
     * 封装请求参数
     * @param userGroupId
     * @return
     */
    private Example getExampleByUserGroupId(Integer userGroupId){
        Example example = new Example(UserGroupCondition.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userGroupId", userGroupId);
        return example;
    }
}
