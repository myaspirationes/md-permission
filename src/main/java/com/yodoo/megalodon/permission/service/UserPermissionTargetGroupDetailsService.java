package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.GroupsDto;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetGroupDetailsDto;
import com.yodoo.megalodon.permission.entity.Groups;
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

import java.util.*;
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

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    /**
     * 根据用户id查询目标集团
     * @Author houzhen
     * @Date 14:56 2019/8/6
    **/
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<GroupsDto> getTargetGroupsByUserId(Integer userId) {
        logger.info("UserPermissionTargetGroupDetailsService.getTargetGroupsByUserId userId:{}", userId);
        if (userId == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        List<GroupsDto> groupsDtoList = new ArrayList<>();
        // 通过用户id 查询用户权限表，获取用户权限表id
        List<Integer> userPermissionIds = userPermissionDetailsService.getUserPermissionIdsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionIds)){
            userPermissionIds.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionId -> {
                        // 通过用户权限id 查询用户管理目标集团，获取集团 ids
                        List<Integer> groupIds = this.getGroupIdsByUserPermissionId(userPermissionId);
                        if (!CollectionUtils.isEmpty(groupIds)) {
                            // 通过集团id 查询集团表信息
                            List<GroupsDto> collect= groupIds.stream()
                                    .filter(Objects::nonNull)
                                    .map(groupId -> {
                                        return this.selectGroupById(groupId);
                                    }).filter(Objects::nonNull).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(collect)){
                                groupsDtoList.addAll(collect);
                            }
                        }
                        return null;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return groupsDtoList;
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
        // 公司 ids
        Set<Integer> groupsIdsListSet = new HashSet<>();

        // 通过用户id查询用户权限表，获取用户权限表ids
        List<Integer> userPermissionIds = userPermissionDetailsService.getUserPermissionIdsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionIds)){
            List<List<Integer>> groupsIdsList = userPermissionIds.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionId -> {
                        // 通过用户权限表id 查询用户管理目标集团表，获取集团ids
                        return this.getGroupIdsByUserPermissionId(userPermissionId);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            // 整合公司ids
            if (!CollectionUtils.isEmpty(groupsIdsList)){
                groupsIdsList.stream()
                        .filter(Objects::nonNull)
                        .forEach(groupsIds -> {
                            groupsIdsListSet.addAll(groupsIds);
                        });
            }
        }
        return selectGroupsNotInIds(groupsIdsListSet);
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
        // 先通过用户 id 查询用户权限表，获取用户权限表 ids
        List<Integer> userPermissionIds = userPermissionDetailsService.getUserPermissionIdsByUserId(userId);
        // 通过用户权限 ids 删除 用户管理目标集团 数据
        if (!CollectionUtils.isEmpty(userPermissionIds)){
            Example example = new Example(UserPermissionTargetGroupDetails.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("userPermissionId",userPermissionIds);
            userPermissionTargetGroupDetailsMapper.deleteByExample(example);
        }
        // 增加修改后的权限
        if (!CollectionUtils.isEmpty(userPermissionTargetGroupDetailsDtoList)) {
            userPermissionTargetGroupDetailsDtoList.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionTargetGroupDetailsDto ->{
                        return userPermissionTargetGroupDetailsMapper.insertSelective(new UserPermissionTargetGroupDetails(
                                userPermissionTargetGroupDetailsDto.getUserPermissionId(), userPermissionTargetGroupDetailsDto.getGroupId()));
                    }).count();
        }
    }

    /**
     * 查询除ids 以外的集团
     * @param groupsIdsListSet
     * @return
     */
    private List<GroupsDto> selectGroupsNotInIds(Set<Integer> groupsIdsListSet) {
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
    private GroupsDto selectGroupById(Integer groupsId) {
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

    /**
     * 通过用户权限id 查询用户管理目标集团
     * @param userPermissionId
     * @return
     */
    private List<Integer>  getGroupIdsByUserPermissionId(Integer userPermissionId) {
        List<Integer> groupsIds = new ArrayList<>();

        Example example = new Example(UserPermissionTargetGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPermissionId", userPermissionId);
        List<UserPermissionTargetGroupDetails> list = userPermissionTargetGroupDetailsMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)){
            groupsIds = list.stream()
                    .filter(Objects::nonNull)
                    .map(UserPermissionTargetGroupDetails::getGroupId)
                    .collect(Collectors.toList());
        }
        return groupsIds;
    }
}
