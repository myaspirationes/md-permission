package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.GroupsDto;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetGroupDetailsDto;
import com.yodoo.megalodon.permission.entity.Groups;
import com.yodoo.megalodon.permission.entity.Permission;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
import com.yodoo.megalodon.permission.entity.UserPermissionTargetGroupDetails;
import com.yodoo.megalodon.permission.exception.BundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.GroupsMapper;
import com.yodoo.megalodon.permission.mapper.UserPermissionTargetGroupDetailsMapper;
import com.yodoo.megalodon.permission.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author houzhen
 * @Date 14:45 2019/8/6
**/
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionTargetGroupDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserPermissionTargetGroupDetailsService.class);

    @Autowired
    private UserPermissionTargetGroupDetailsMapper userPermissionTargetGroupDetailsMapper;

    @Autowired
    private GroupsMapper groupsMapper;

    /**
     * 根据用户id查询目标集团
     * @Author houzhen
     * @Date 14:56 2019/8/6
    **/
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<GroupsDto> getTargetGroupsByUserId(Integer userId) {
        logger.info("UserPermissionTargetGroupDetailsService.getTargetGroupsByUserId userId:{}", userId);
        List<GroupsDto> responseList = new ArrayList<>();
        if (userId == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        List<Groups> groupsList = this.selectGroupsByUserId(userId);
        if (!CollectionUtils.isEmpty(groupsList)) {
            responseList = groupsList.stream().map(groups -> {
                GroupsDto dto = new GroupsDto();
                BeanUtils.copyProperties(groups, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return responseList;
    }

    /**
     * 查询可选目标集团
     * @Author houzhen
     * @Date 9:43 2019/8/6
    **/
    public List<GroupsDto> getAvailableGroupsByUserId(Integer userId) {
        logger.info("UserPermissionTargetGroupDetailsService.getAvailableGroupsByUserId userId:{}", userId);
        List<GroupsDto> responseList = new ArrayList<>();
        if (userId == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        List<Groups> groupsList = this.selectGroupsByUserId(userId);
        List<Groups> availableGroupsList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(groupsList)) {
            List<Integer> groupIdList = groupsList.stream().map(Groups::getId).collect(Collectors.toList());
            Example example = new Example(Permission.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andNotIn("id", groupIdList);
            example.and(criteria);
            availableGroupsList = groupsMapper.selectByExample(example);
        } else {
            availableGroupsList = groupsMapper.selectAll();
        }
        if (!CollectionUtils.isEmpty(availableGroupsList)) {
            responseList = availableGroupsList.stream().map(groups -> {
                GroupsDto dto = new GroupsDto();
                BeanUtils.copyProperties(groups, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return responseList;
    }

    private List<Groups> selectGroupsByUserId(Integer userId) {
        List<Groups> responseList = new ArrayList<>();
        if (userId == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example userPermissionExa = new Example(UserPermissionTargetGroupDetails.class);
        Example.Criteria userPermissionCri = userPermissionExa.createCriteria();
        userPermissionCri.andEqualTo("userId", userId);
        userPermissionExa.and(userPermissionCri);
        List<UserPermissionTargetGroupDetails> targetGroupDetailsList = userPermissionTargetGroupDetailsMapper.selectByExample(userPermissionExa);
        if (!CollectionUtils.isEmpty(targetGroupDetailsList)) {
            List<Integer> groupIdList = targetGroupDetailsList.stream().map(UserPermissionTargetGroupDetails::getGroupId).collect(Collectors.toList());
            Example groupExa = new Example(Groups.class);
            Example.Criteria permissionCri = groupExa.createCriteria();
            permissionCri.andIn("id", groupIdList);
            groupExa.and(permissionCri);
            responseList = groupsMapper.selectByExample(permissionCri);
        }
        return responseList;
    }

    /**
     * 变更权限
     * @Author houzhen
     * @Date 10:23 2019/8/6
     **/
    public void updateUserPermissionTargetGroups(List<UserPermissionTargetGroupDetailsDto> userPermissionTargetGroupDetailsDtoList, Integer userId) {
        logger.info("UserPermissionTargetGroupDetailsService.updateUserPermissionTargetGroups userPermissionDetailsDtoList:{}", JsonUtils.obj2json(userPermissionTargetGroupDetailsDtoList));
        // 参数判断
        if (userId == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 先删除旧的数据
        Example example = new Example(UserPermissionDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        example.and(criteria);
        userPermissionTargetGroupDetailsMapper.deleteByExample(example);
        // 增加修改后的权限
        if (!CollectionUtils.isEmpty(userPermissionTargetGroupDetailsDtoList)) {
            List<UserPermissionTargetGroupDetails> addList = userPermissionTargetGroupDetailsDtoList.stream().map(dto ->{
                UserPermissionTargetGroupDetails userPermissionTargetGroupDetails = new UserPermissionTargetGroupDetails();
                BeanUtils.copyProperties(dto, userPermissionTargetGroupDetails);
                return userPermissionTargetGroupDetails;
            }).collect(Collectors.toList());
            userPermissionTargetGroupDetailsMapper.insertList(addList);
        }
    }
}
