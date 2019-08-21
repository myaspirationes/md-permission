package com.yodoo.megalodon.permission.service;

import com.yodoo.feikongbao.provisioning.domain.system.service.GroupsService;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetDto;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetGroupDetailsDto;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
import com.yodoo.megalodon.permission.entity.UserPermissionTargetGroupDetails;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.UserPermissionTargetGroupDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author houzhen
 * @Date 14:45 2019/8/6
**/
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionTargetGroupDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserPermissionTargetGroupDetailsService.class);

    @Autowired
    private UserPermissionTargetGroupDetailsMapper userPermissionTargetGroupDetailsMapper;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    @Autowired
    private GroupsService groupsService;

    /**
     * 变更权限用户管理目标集团
     * @Author houzhen
     * @Date 10:23 2019/8/6
     **/
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public void updateUserPermissionTargetGroups(UserPermissionTargetDto userPermissionTargetDto) {
        // 参数校验
        updateUserPermissionTargetGroupsParameterCheck(userPermissionTargetDto);
        // 先删除
        Example example = getExampleByUserIdAndPermissionId(new UserPermissionDetails(userPermissionTargetDto.getUserId(), userPermissionTargetDto.getPermissionId()));
        userPermissionTargetGroupDetailsMapper.deleteByExample(example);
        // 添加
        userPermissionTargetDto.getTargetIds().stream()
                .filter(Objects::nonNull)
                .map(targetGroupId -> {
                    UserPermissionTargetGroupDetails userPermissionTargetGroupDetails = new UserPermissionTargetGroupDetails();
                    userPermissionTargetGroupDetails.setUserId(userPermissionTargetDto.getUserId());
                    userPermissionTargetGroupDetails.setPermissionId(userPermissionTargetDto.getPermissionId());
                    userPermissionTargetGroupDetails.setTargetGroupId(targetGroupId);
                    return userPermissionTargetGroupDetailsMapper.insertSelective(userPermissionTargetGroupDetails);
                }).count();
    }

    /**
     * 通过用户id 和 权限id 查询用户管理目标集团
     * @param userPermissionIdList
     * @return
     */
    public Set<Integer>  getGroupIdsByUserIdPermissionId(List<UserPermissionDetails> userPermissionIdList) {
        Set<Integer> groupsIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(userPermissionIdList)){
            userPermissionIdList.stream()
                    .filter(Objects::nonNull)
                    .forEach(userPermissionDetails -> {
                        Example example = getExampleByUserIdAndPermissionId(userPermissionDetails);
                        List<UserPermissionTargetGroupDetails> list = userPermissionTargetGroupDetailsMapper.selectByExample(example);
                        if (!CollectionUtils.isEmpty(list)){
                            Set<Integer> targetGroupIds = list.stream().filter(Objects::nonNull).map(UserPermissionTargetGroupDetails::getTargetGroupId).collect(Collectors.toSet());
                            if (!CollectionUtils.isEmpty(targetGroupIds)){
                                groupsIds.addAll(targetGroupIds);
                            }
                        }
                    });
        }
        return groupsIds;
    }

    /**
     * 通过用户权限 id 查询
     *
     * @param permissionId
     * @return
     */
    public List<UserPermissionTargetGroupDetails> selectUserPermissionTargetGroupDetailsByUserPermissionId(Integer permissionId) {
        Example example = new Example(UserPermissionTargetGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("permissionId", permissionId);
        return userPermissionTargetGroupDetailsMapper.selectByExample(example);
    }

    /**
     * 通过集团 id 查询
     *
     * @param groupId
     * @return
     */
    public Integer selectUserPermissionTargetGroupDetailsCountByGroupId(Integer groupId) {
        Example example = new Example(UserPermissionTargetGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId",groupId);
        return userPermissionTargetGroupDetailsMapper.selectCountByExample(example);
    }

    /**
     * 通过用户id 和 权限 id 查询
     * @param userPermissionDetailsList
     * @return
     */
    public List<UserPermissionTargetGroupDetailsDto> getTargetGroupDetailsByUserIdAndPermissionId(List<UserPermissionDetails> userPermissionDetailsList) {
        List<UserPermissionTargetGroupDetailsDto> userPermissionTargetGroupDetailsDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userPermissionDetailsList)){
            userPermissionDetailsList.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionDetails -> {
                        Example example = getExampleByUserIdAndPermissionId(userPermissionDetails);
                        List<UserPermissionTargetGroupDetails> list = userPermissionTargetGroupDetailsMapper.selectByExample(example);
                        if (!CollectionUtils.isEmpty(list)){
                            List<UserPermissionTargetGroupDetailsDto> collect = list.stream()
                                    .filter(Objects::nonNull)
                                    .map(userPermissionTargetGroupDetails -> {
                                        UserPermissionTargetGroupDetailsDto userPermissionTargetGroupDetailsDto = new UserPermissionTargetGroupDetailsDto();
                                        BeanUtils.copyProperties(userPermissionTargetGroupDetails, userPermissionTargetGroupDetailsDto);
                                        return userPermissionTargetGroupDetailsDto;
                                    }).filter(Objects::nonNull).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(collect)){
                                userPermissionTargetGroupDetailsDtoList.addAll(collect);
                            }
                        }
                        return null;
                    }).count();
        }
        return userPermissionTargetGroupDetailsDtoList;
    }

    /**
     * 更新目标公司参数校验
     * @param userPermissionTargetDto
     */
    private void updateUserPermissionTargetGroupsParameterCheck(UserPermissionTargetDto userPermissionTargetDto) {
        // 非空校验
        if (userPermissionTargetDto == null || userPermissionTargetDto.getUserId() == null || userPermissionTargetDto.getUserId() < 0
                || userPermissionTargetDto.getPermissionId() == null || userPermissionTargetDto.getPermissionId() < 0
                || CollectionUtils.isEmpty(userPermissionTargetDto.getTargetIds())) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 查询用户权限是否存在
        List<UserPermissionDetails> list = userPermissionDetailsService.selectUserPermissionDetailsByUserIdAndPermissionId(new UserPermissionDetails(userPermissionTargetDto.getUserId(), userPermissionTargetDto.getPermissionId()));
        if (CollectionUtils.isEmpty(list)){
            throw new PermissionException(PermissionBundleKey.USER_PERMISSION_NOT_EXIST, PermissionBundleKey.USER_PERMISSION_NOT_EXIST_MSG);
        }
        // 查询集团是否存在
        Long groupsNoExistCount = groupsService.selectGroupsNoExistCountByIds(userPermissionTargetDto.getTargetIds());
        if (groupsNoExistCount != null && groupsNoExistCount > 0){
            throw new PermissionException(PermissionBundleKey.GROUPS_NOT_EXIST, PermissionBundleKey.GROUPS_NOT_EXIST_MSG);
        }
    }

    /**
     * 获取 example
     * @param userPermissionDetails
     * @return
     */
    private Example getExampleByUserIdAndPermissionId(UserPermissionDetails userPermissionDetails) {
        Example example = new Example(UserPermissionTargetGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        if (userPermissionDetails.getUserId() != null && userPermissionDetails.getUserId() > 0){
            criteria.andEqualTo("userId", userPermissionDetails.getUserId());
        }
        if (userPermissionDetails.getPermissionId() != null && userPermissionDetails.getPermissionId() > 0){
            criteria.andEqualTo("permissionId", userPermissionDetails.getPermissionId());
        }
        return example;
    }
}
