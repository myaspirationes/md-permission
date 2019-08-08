package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.GroupsDto;
import com.yodoo.megalodon.permission.entity.Groups;
import com.yodoo.megalodon.permission.mapper.GroupsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description ：集团
 * @Author ：jinjun_luo
 * @Date ： 2019/8/8 0008
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class GroupsService {

    @Autowired
    private GroupsMapper groupsMapper;

    /**
     * 查询除ids 以外的集团
     * @param groupsIdsListSet
     * @return
     */
    public List<GroupsDto> selectGroupsNotInIds(Set<Integer> groupsIdsListSet) {
        List<GroupsDto> groupsDtoList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(groupsIdsListSet)){
            Example example = new Example(Groups.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andNotIn("id",groupsIdsListSet);
            List<Groups> groupsList = groupsMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(groupsList)){
                groupsDtoList = groupsList.stream()
                        .filter(Objects::nonNull)
                        .map(groups -> {
                            GroupsDto groupsDto = new GroupsDto();
                            BeanUtils.copyProperties(groups, groupsDto);
                            return groupsDto;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        }
        return groupsDtoList;
    }

    /**
     * 通过主键查询
     * @param groupsId
     * @return
     */
    public GroupsDto selectGroupById(Integer groupsId) {
        Example example = new Example(Groups.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", groupsId);
        Groups groups = groupsMapper.selectOneByExample(example);
        if (groups != null){
            GroupsDto groupsDto = new GroupsDto();
            BeanUtils.copyProperties(groupsDto, groups);
            return groupsDto;
        }
        return null;
    }
}
