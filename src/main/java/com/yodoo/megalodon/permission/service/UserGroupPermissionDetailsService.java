package com.yodoo.megalodon.permission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.PermissionGroupDto;
import com.yodoo.megalodon.permission.dto.UserGroupPermissionDetailsDto;
import com.yodoo.megalodon.permission.entity.PermissionGroup;
import com.yodoo.megalodon.permission.entity.UserGroupPermissionDetails;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.UserGroupPermissionDetailsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description ：用户组权限组关系
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class UserGroupPermissionDetailsService {

    @Autowired
    private UserGroupPermissionDetailsMapper userGroupPermissionDetailsMapper;

    @Autowired
    private PermissionGroupService permissionGroupService;

    @Autowired
    private PermissionGroupDetailsService permissionGroupDetailsService;

    /**
     * 用户组组权限组列表
     * @param userGroupPermissionDetailsDto
     * @return
     */
    public PageInfoDto<UserGroupPermissionDetailsDto> queryUserGroupPermissionDetailsList(UserGroupPermissionDetailsDto userGroupPermissionDetailsDto) {
        List<UserGroupPermissionDetailsDto> responseLst = new ArrayList<>();
        Example example = new Example(UserGroupPermissionDetails.class);
        Example.Criteria criteria = example.createCriteria();
        if (userGroupPermissionDetailsDto.getUserGroupId() != null && userGroupPermissionDetailsDto.getUserGroupId() > 0) {
            criteria.andEqualTo("userGroupId", userGroupPermissionDetailsDto.getUserGroupId());
        }
        if (userGroupPermissionDetailsDto.getPermissionGroupId() != null && userGroupPermissionDetailsDto.getPermissionGroupId() > 0) {
            criteria.andEqualTo("permissionGroupId", userGroupPermissionDetailsDto.getPermissionGroupId());
        }
        Page<?> pages = PageHelper.startPage(userGroupPermissionDetailsDto.getPageNum(), userGroupPermissionDetailsDto.getPageSize());
        List<UserGroupPermissionDetails> userGroupPermissionDetailsList = userGroupPermissionDetailsMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(userGroupPermissionDetailsList)) {
            responseLst = userGroupPermissionDetailsList.stream()
                    .filter(Objects::nonNull)
                    .map(userGroupPermissionDetails -> {
                        UserGroupPermissionDetailsDto dto = new UserGroupPermissionDetailsDto();
                        BeanUtils.copyProperties(userGroupPermissionDetails, dto);
                        PermissionGroup permissionGroup = permissionGroupService.selectByPrimaryKey(userGroupPermissionDetails.getPermissionGroupId());
                        if (permissionGroup != null){
                            PermissionGroupDto permissionGroupDto = new PermissionGroupDto();
                            BeanUtils.copyProperties(permissionGroup, permissionGroupDto);
                            dto.setPermissionGroupDto(permissionGroupDto);
                        }
                        return dto;
                    }).collect(Collectors.toList());
        }
        return new PageInfoDto<UserGroupPermissionDetailsDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), responseLst);
    }

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
     * 更新用户组权限组关系数据
     * @param userGroupId
     * @param permissionGroupIds
     */
    public void updateUserGroupPermissionDetails(Integer userGroupId, Set<Integer> permissionGroupIds){
        // 通过用户组id 删除
        if (userGroupId != null && userGroupId > 0){
            deleteUserGroupPermissionDetailsByUserGroupId(userGroupId);
        }
        // 插入用户组权限组关系表数据
        if (userGroupId != null && userGroupId > 0 && !CollectionUtils.isEmpty(permissionGroupIds)){
            permissionGroupIds.stream()
                    .filter(Objects::nonNull)
                    .map(permissionGroupId -> {
                        return userGroupPermissionDetailsMapper.insertSelective(new UserGroupPermissionDetails(userGroupId, permissionGroupId));
                    }).filter(Objects::nonNull).count();
        }
    }

    /**
     * 通过用户组id 查询权限组id,再通过权限组id查询权限详情表获取 权限 id
     * @param userGroupIds
     * @return
     */
    public Map<Integer, Set<Integer>> getPermissionIdsByUserGroupIds(Set<Integer> userGroupIds) {
        // 每个用户组对应的权限ids
        Map<Integer, Set<Integer>> permissionIdMap = new HashMap<>(userGroupIds.size());

        if (!CollectionUtils.isEmpty(userGroupIds)){
            Set<Integer> permissionGroupIdList = new HashSet<>();
                    userGroupIds.stream()
                    .filter(Objects::nonNull)
                    .forEach(userGroupId -> {
                        // 通过用户组id  查询权限组id
                        Example example = getExample(userGroupId);
                        List<UserGroupPermissionDetails> userGroupPermissionDetailsList = userGroupPermissionDetailsMapper.selectByExample(example);

                        if (!CollectionUtils.isEmpty(userGroupPermissionDetailsList)){
                            // 获取权限组ids
                            Set<Integer> permissionGroupIds = userGroupPermissionDetailsList.stream().filter(Objects::nonNull).map(UserGroupPermissionDetails::getPermissionGroupId).filter(Objects::nonNull).collect(Collectors.toSet());
                            if (!CollectionUtils.isEmpty(permissionGroupIds)){
                                // 通过权限组id 查询权限id
                                Set<Integer> permissionIds = permissionGroupDetailsService.getPermissionIds(permissionGroupIds);
                                if (!CollectionUtils.isEmpty(permissionIds)){
                                    permissionIdMap.put(userGroupId, permissionIds);
                                }
                            }
                        }
                    });
        }
        return permissionIdMap;
    }

    /**
     * 通过用户组id删除
     * @param userGroupId
     * @return
     */
    public Integer deleteUserGroupPermissionDetailsByUserGroupId(Integer userGroupId) {
        Example example = getExample(userGroupId);
        return userGroupPermissionDetailsMapper.deleteByExample(example);
    }

    /**
     * 通过用户组id获取权限ids
     * @param userGroupId
     * @return
     */
    public Set<Integer> getPermissionIdsByUserGroupId(Integer userGroupId) {
        if (userGroupId == null || userGroupId < 0){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 先查询用户组与权限组关系表，获取权限组id
        UserGroupPermissionDetails userGroupPermissionDetailsRequest = new UserGroupPermissionDetails();
        userGroupPermissionDetailsRequest.setUserGroupId(userGroupId);
        List<UserGroupPermissionDetails> select = userGroupPermissionDetailsMapper.select(userGroupPermissionDetailsRequest);

        Set<Integer> permissionIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(select)){
            Set<Integer> collect = select.stream().filter(Objects::nonNull).map(UserGroupPermissionDetails::getPermissionGroupId).collect(Collectors.toSet());
            permissionIds = permissionGroupDetailsService.getPermissionIds(collect);
        }
        return permissionIds;
    }

    /**
     * 获取example
     * @param userGroupId
     * @return
     */
    private Example getExample(Integer userGroupId){
        Example example = new Example(UserGroupPermissionDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userGroupId", userGroupId);
        return example;
    }
}
