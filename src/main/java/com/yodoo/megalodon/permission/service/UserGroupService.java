package com.yodoo.megalodon.permission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.SearchConditionDto;
import com.yodoo.megalodon.permission.dto.UserGroupDto;
import com.yodoo.megalodon.permission.entity.*;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.UserGroupMapper;
import org.apache.commons.lang3.StringUtils;
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
 * @Description ：用户组
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class UserGroupService {

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private PermissionGroupService permissionGroupService;

    @Autowired
    private UserGroupPermissionDetailsService userGroupPermissionDetailsService;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    @Autowired
    private UserPermissionTargetUserGroupDetailsService userPermissionTargetUserGroupDetailsService;

    @Autowired
    private UserGroupDetailsService userGroupDetailsService;

    @Autowired
    private SearchConditionService searchConditionService;

    @Autowired
    private UserGroupConditionService userGroupConditionService;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionGroupDetailsService permissionGroupDetailsService;

    /**
     * 条件分页查询
     * @param userGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public PageInfoDto<UserGroupDto> queryUserGroupList(UserGroupDto userGroupDto) {
        Example example = new Example(UserGroup.class);
        Example.Criteria criteria = example.createCriteria();
        // 用户组代码
        if (StringUtils.isNoneBlank(userGroupDto.getGroupCode())){
            criteria.andEqualTo("groupCode",userGroupDto.getGroupCode());
        }
        // 用户组名称
        if (StringUtils.isNoneBlank(userGroupDto.getGroupName())){
            criteria.andEqualTo("groupName",userGroupDto.getGroupName());
        }
        Page<?> pages = PageHelper.startPage(userGroupDto.getPageNum(), userGroupDto.getPageSize());
        List<UserGroup> userGroupList = userGroupMapper.selectByExample(example);

        List<UserGroupDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userGroupList)) {
            collect = userGroupList.stream()
                    .filter(Objects::nonNull)
                    .map(userGroup -> {
                        UserGroupDto userGroupDtoResponse = new UserGroupDto();
                        BeanUtils.copyProperties(userGroup, userGroupDtoResponse);
                        // 查询当前用户组下有多少个用户
                        Integer userCount = userGroupDetailsService.selectCountByUserGroupId(userGroup.getId());
                        if (userCount != null && userCount > 0){
                            userGroupDtoResponse.setUserTotal(userCount);
                        }
                        return userGroupDtoResponse;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return new PageInfoDto<UserGroupDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 通过id 查询，统计不存在的数量
     * @param userGroupIds
     * @return
     */
    public Long selectUserGroupNoExistCountByIds(Set<Integer> userGroupIds) {
        Long count = null;
        if (!CollectionUtils.isEmpty(userGroupIds)){
            count = userGroupIds.stream()
                    .filter(Objects::nonNull)
                    .map(id -> {
                        return selectByPrimaryKey(id);
                    })
                    .filter(userGroup -> userGroup == null)
                    .count();
        }
        return count;
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public UserGroup selectByPrimaryKey(Integer id) {
        return userGroupMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加用户组
     * @param userGroupDto
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer addUserGroup(UserGroupDto userGroupDto) {
        // 参数校验
        addUserGroupParameterCheck(userGroupDto);

        // 插入数据
        UserGroup userGroup = new UserGroup(userGroupDto.getGroupCode(), userGroupDto.getGroupName());
        Integer insertCount = userGroupMapper.insertSelective(userGroup);
        if (insertCount != null && insertCount > 0){
            updateUserGroupPermissionDetailsAndUserGroupConditionAndUserPermission(userGroup.getId(),userGroupDto.getPermissionGroupIds(),userGroupDto.getSearchConditionIds());
        }
        return insertCount;
    }

    /**
     * 修改用户组
     * @param userGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer editUserGroup(UserGroupDto userGroupDto) {
        // 参数校验
        UserGroup userGroup = editUserGroupParameterCheck(userGroupDto);

        // 更新
        userGroup.setGroupCode(userGroupDto.getGroupCode());
        userGroup.setGroupName(userGroupDto.getGroupName());
        Integer updateCount = userGroupMapper.updateByPrimaryKeySelective(userGroup);
        if (updateCount != null && updateCount > 0){
            updateUserGroupPermissionDetailsAndUserGroupConditionAndUserPermission(userGroup.getId(),userGroupDto.getPermissionGroupIds(),userGroupDto.getSearchConditionIds());
        }
        // 权限组不为空，更新用户权限组详情
        if (updateCount != null && updateCount > 0){
            userGroupPermissionDetailsService.updateUserGroupPermissionDetails(userGroup.getId(),userGroupDto.getPermissionGroupIds());
        }
        return updateCount;
    }

    /**
     * 更新
     * @param userGroupId
     * @param permissionGroupIds
     * @param searchConditionIds
     */
    private void updateUserGroupPermissionDetailsAndUserGroupConditionAndUserPermission(Integer userGroupId, Set<Integer> permissionGroupIds, List<Integer> searchConditionIds) {
        // 如果权限组id 不为空， 更新 更新用户组权限组关系数据
        userGroupPermissionDetailsService.updateUserGroupPermissionDetails(userGroupId,permissionGroupIds);
        // 用户组条件不为空
        List<SearchCondition>  searchConditionList = searchConditionIds.stream()
                .filter(Objects::nonNull)
                .map(searchConditionId -> {
                    return searchConditionService.selectByPrimaryKey(searchConditionId);
                }).filter(Objects::nonNull).collect(Collectors.toList());

        userGroupConditionService.updateUserGroupCondition(userGroupId,searchConditionList);
        // 根据条件查询适合组的用户，添加用户权限表，用户管理用户组权限表数据
        updateUserPermissionAndUserPermissionTargetUserGroupDetailsByCondition(userGroupId,searchConditionList,permissionGroupIds);
    }

    /**
     * 删除用户组
     * @param userGroupId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer deleteUserGroup(Integer userGroupId) {
        if (userGroupId == null || userGroupId < 0){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        Set<Integer> userIds = userGroupDetailsService.selectUserIdsByUserGroupId(userGroupId);
        // 删除用户组条件
        userGroupConditionService.deleteUserGroupConditionByUserGroupId(userGroupId);
        // 删除用户权限
        if (!CollectionUtils.isEmpty(userIds)){
            userIds.stream()
                    .filter(Objects::nonNull)
                    .forEach(userId -> {
                        userPermissionDetailsService.deleteUserPermissionDetailsByUserId(userId);
                    });
        }
        // 用户管理用户组权限表
        userPermissionTargetUserGroupDetailsService.deleteUserPermissionTargetUserGroupDetailsByUserGroupId(userGroupId);
        // 用户组权限组关系
        userGroupPermissionDetailsService.deleteUserGroupPermissionDetailsByUserGroupId(userGroupId);
        // 删除用户组
       return userGroupMapper.deleteByPrimaryKey(userGroupId);
    }

    /**
     * 获取所有的用户组数据
     * @return
     */
    public List<UserGroupDto> getUserGroupAll() {
        Example example = new Example(UserGroup.class);
        example.setOrderByClause("group_name ASC");
        Example.Criteria criteria = example.createCriteria();
        List<UserGroup> userGroups = userGroupMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(userGroups)){
            return userGroups.stream()
                    .filter(Objects::nonNull)
                    .map(userGroup -> {
                        UserGroupDto userGroupDto = new UserGroupDto();
                        BeanUtils.copyProperties(userGroup, userGroupDto);
                        return userGroupDto;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 根据条件查询适合组的用户，添加用户权限表，用户管理用户组权限表数据
     * @param userGroupId
     * @param searchConditionList
     */
    private void updateUserPermissionAndUserPermissionTargetUserGroupDetailsByCondition(Integer userGroupId, List<SearchCondition> searchConditionList, Set<Integer> permissionGroupIds) {
        // 用户列表
        List<User> userList = userService.selectUserListByCondition(searchConditionList);
        Set<Integer> userIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(userList)){
            Set<Integer> collect = userList.stream().filter(Objects::nonNull).map(User::getId).collect(Collectors.toSet());
        }
        // 权限组ids
        Set<Integer> permissionGroupIdList = new HashSet<>();
        if (!CollectionUtils.isEmpty(permissionGroupIds)){
            List<PermissionGroup> collect = permissionGroupIds.stream()
                    .filter(Objects::nonNull)
                    .map(permissionGroupId -> {
                        return permissionGroupService.selectByPrimaryKey(permissionGroupId);
                    }).filter(Objects::nonNull).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)){
                permissionGroupIdList = collect.stream().filter(Objects::nonNull).map(PermissionGroup::getId).collect(Collectors.toSet());
            }
        }
        // 如果权限组列表不为空，更新用户组权限组关系数据
        if (!CollectionUtils.isEmpty(permissionGroupIdList)){
            userGroupPermissionDetailsService.updateUserGroupPermissionDetails(userGroupId, permissionGroupIdList);
        }
        // 查询权限组对应权限的ids
        Set<Integer> permissionIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(permissionGroupIdList)){
            permissionIds = permissionGroupDetailsService.getPermissionIds(permissionGroupIdList);
        }
        // 更新用户权限，用户管理用户组权限表数据
        if (!CollectionUtils.isEmpty(userIds) && !CollectionUtils.isEmpty(permissionIds)){
            userPermissionDetailsService.updateUserPermission(userGroupId,userIds,permissionIds);
        }
    }

    /**
     * 修改参数校验
     * @param userGroupDto
     */
    private UserGroup editUserGroupParameterCheck(UserGroupDto userGroupDto) {
        if (userGroupDto == null || userGroupDto.getId() == null || userGroupDto.getId() < 0
                || StringUtils.isBlank(userGroupDto.getGroupCode()) || StringUtils.isBlank(userGroupDto.getGroupName())) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }

        // 不存在不修改
        UserGroup userGroup = selectByPrimaryKey(userGroupDto.getId());
        if (userGroup == null){
            throw new PermissionException(PermissionBundleKey.USER_GROUP_NOT_EXIST, PermissionBundleKey.USER_GROUP_NOT_EXIST_MSG);
        }

        // 查询除自己以外是否有相同 groupCode 的数据
        UserGroup selectOtherThanOneself = userGroupMapper.selectOtherThanOneself(userGroupDto.getId(), userGroupDto.getGroupCode());
        if (selectOtherThanOneself != null){
            throw new PermissionException(PermissionBundleKey.USER_GROUP_ALREADY_EXIST, PermissionBundleKey.USER_GROUP_ALREADY_EXIST_MSG);
        }

        // 如果权限组和用户权限不为空,查询权限组不存在，不操作
        checkPermissionGroupIdsAndUserPermissionIds(userGroupDto);
        return userGroup;
    }

    /**
     * 添加用户组校验
     * @param userGroupDto
     */
    private void addUserGroupParameterCheck(UserGroupDto userGroupDto) {
        if (userGroupDto == null || StringUtils.isBlank(userGroupDto.getGroupCode()) || StringUtils.isBlank(userGroupDto.getGroupName())){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }

        // 用户组 code 是否存在，存在不操作
        UserGroup userGroup = new UserGroup();
        userGroup.setGroupCode(userGroupDto.getGroupCode());
        UserGroup selectUserGroupByGroupCode = userGroupMapper.selectOne(userGroup);
        if (selectUserGroupByGroupCode != null){
            throw new PermissionException(PermissionBundleKey.USER_GROUP_ALREADY_EXIST, PermissionBundleKey.USER_GROUP_ALREADY_EXIST_MSG);
        }

        // 如果权限组和用户权限不为空,查询权限组不存在，不操作
        checkPermissionGroupIdsAndUserPermissionIds(userGroupDto);
    }

    /**
     * 如果权限组不为空,查询权限组不存在，不操作
     * @param userGroupDto
     */
    private void checkPermissionGroupIdsAndUserPermissionIds(UserGroupDto userGroupDto) {
        // 权限组、通过id 查询，统计不存在的数量
        if (!CollectionUtils.isEmpty(userGroupDto.getPermissionGroupIds())){
            Long permissionGroupNoExistCount = permissionGroupService.selectPermissionGroupNoExistCountByIds(userGroupDto.getPermissionGroupIds());
            if (permissionGroupNoExistCount != null && permissionGroupNoExistCount > 0){
                throw new PermissionException(PermissionBundleKey.PERMISSION_GROUP_NOT_EXIST, PermissionBundleKey.PERMISSION_GROUP_NOT_EXIST_MSG);
            }
        }
        // 条件是否为空
        if (!CollectionUtils.isEmpty(userGroupDto.getSearchConditionIds())){
            Long  searchConditionNoExistCount = searchConditionService.selectSearchConditionNoExistCountByIds(userGroupDto.getSearchConditionIds());
            if (searchConditionNoExistCount != null && searchConditionNoExistCount > 0){
                throw new PermissionException(PermissionBundleKey.SEARCH_CONDITION_NOT_EXIST, PermissionBundleKey.SEARCH_CONDITION_NOT_EXIST_MSG);
            }
        }
    }
}
