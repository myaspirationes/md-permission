package com.yodoo.megalodon.permission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.UserGroupDto;
import com.yodoo.megalodon.permission.entity.UserGroup;
import com.yodoo.megalodon.permission.exception.BundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.UserGroupMapper;
import org.apache.commons.lang3.StringUtils;
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
 * @Description ：用户组
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserGroupService {

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private PermissionGroupService permissionGroupService;

    @Autowired
    private UserGroupPermissionDetailsService userGroupPermissionDetailsService;

    /**
     * 条件分页查询
     * @param userGroupDto
     * @return
     */
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
    public Integer addUserGroup(UserGroupDto userGroupDto) {
        // 参数校验
        addUserGroupParameterCheck(userGroupDto);

        // 插入数据
        UserGroup userGroup = new UserGroup(userGroupDto.getGroupCode(), userGroupDto.getGroupName());
        Integer insertCount = userGroupMapper.insertSelective(userGroup);

        // 如果权限组id 不为空， 更新 更新用户组权限组关系数据
        if (!CollectionUtils.isEmpty(userGroupDto.getPermissionGroupIds()) && insertCount != null && insertCount > 0){
            updateUserGroupPermissionDetails(userGroup.getId(),userGroupDto.getPermissionGroupIds());
        }

        return insertCount;
    }

    /**
     * 修改用户组
     * @param userGroupDto
     * @return
     */
    public Integer editUserGroup(UserGroupDto userGroupDto) {
        // 参数校验
        UserGroup userGroup = editUserGroupParameterCheck(userGroupDto);

        // 更新
        userGroup.setGroupCode(userGroupDto.getGroupCode());
        userGroup.setGroupName(userGroupDto.getGroupName());
        Integer updateCount = userGroupMapper.updateByPrimaryKeySelective(userGroup);

        // 权限组不为空，更新用户权限组详情
        if (!CollectionUtils.isEmpty(userGroupDto.getPermissionGroupIds()) && updateCount != null && updateCount > 0){
            updateUserGroupPermissionDetails(userGroup.getId(),userGroupDto.getPermissionGroupIds());
        }
        return updateCount;
    }

    /**
     * 更新用户组权限组关系数据
     * @param userGroupId
     * @param permissionGroupIds
     */
    private void updateUserGroupPermissionDetails(Integer userGroupId, Set<Integer> permissionGroupIds) {
        userGroupPermissionDetailsService.deleteUserGroupPermissionDetailsByUserGroupId(userGroupId);
        userGroupPermissionDetailsService.insertUserGroupPermissionDetails(userGroupId, permissionGroupIds);
    }

    /**
     * 修改参数校验
     * @param userGroupDto
     */
    private UserGroup editUserGroupParameterCheck(UserGroupDto userGroupDto) {
        if (userGroupDto == null || userGroupDto.getId() == null || userGroupDto.getId() < 0
                || StringUtils.isBlank(userGroupDto.getGroupCode()) || StringUtils.isBlank(userGroupDto.getGroupName())) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }

        // 不存在不修改
        UserGroup userGroup = selectByPrimaryKey(userGroupDto.getId());
        if (userGroup == null){
            throw new PermissionException(BundleKey.USER_GROUP_NOT_EXIST, BundleKey.USER_GROUP_NOT_EXIST_MSG);
        }

        // 查询除自己以外是否有相同 groupCode 的数据
        Example example = new Example(UserGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", userGroupDto.getId());
        criteria.andEqualTo("groupCode", userGroupDto.getGroupCode());
        UserGroup selectOneByExample = userGroupMapper.selectOneByExample(example);
        if (selectOneByExample != null){
            throw new PermissionException(BundleKey.USER_GROUP_ALREADY_EXIST, BundleKey.USER_GROUP_ALREADY_EXIST_MSG);
        }

        // 如果权限组不为空,查询权限组不存在，不操作
        if (!CollectionUtils.isEmpty(userGroupDto.getPermissionGroupIds())){
            checkPermissionGroupIds(userGroupDto.getPermissionGroupIds());
        }
        return userGroup;
    }

    /**
     * 添加用户组校验
     * @param userGroupDto
     */
    private void addUserGroupParameterCheck(UserGroupDto userGroupDto) {
        if (userGroupDto == null || StringUtils.isBlank(userGroupDto.getGroupCode()) || StringUtils.isBlank(userGroupDto.getGroupName())){
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }

        // 用户组 code 是否存在，存在不操作
        Example example = new Example(UserGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupCode", userGroupDto.getGroupCode());
        UserGroup selectUserGroupByGroupCode = userGroupMapper.selectOneByExample(example);
        if (selectUserGroupByGroupCode != null){
            throw new PermissionException(BundleKey.USER_GROUP_ALREADY_EXIST, BundleKey.USER_GROUP_ALREADY_EXIST_MSG);
        }

        // 如果权限组不为空,查询权限组不存在，不操作
        if (!CollectionUtils.isEmpty(userGroupDto.getPermissionGroupIds())){
            checkPermissionGroupIds(userGroupDto.getPermissionGroupIds());
        }
    }

    /**
     * 如果权限组不为空,查询权限组不存在，不操作
     * @param permissionGroupIds
     */
    private void checkPermissionGroupIds(Set<Integer> permissionGroupIds) {
        Long permissionGroupNoExistCount = permissionGroupService.selectPermissionGroupNoExistCountByIds(permissionGroupIds);
        if (permissionGroupNoExistCount != null && permissionGroupNoExistCount > 0){
            throw new PermissionException(BundleKey.PERMISSION_GROUP_NOT_EXIST, BundleKey.PERMISSION_GROUP_NOT_EXIST_MSG);
        }
    }
}
