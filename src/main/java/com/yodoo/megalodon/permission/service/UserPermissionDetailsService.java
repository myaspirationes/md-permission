package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.ActionPermissionInUserListDto;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetCompanyDetailsDto;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetGroupDetailsDto;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetUserDetailsDto;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.UserPermissionDetailsMapper;
import com.yodoo.megalodon.permission.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description ：用户权限
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserPermissionDetailsService.class);

    @Autowired
    private UserPermissionDetailsMapper userPermissionDetailsMapper;

    @Autowired
    private UserPermissionTargetGroupDetailsService userPermissionTargetGroupDetailsService;

    @Autowired
    private UserPermissionTargetCompanyDetailsService userPermissionTargetCompanyDetailsService;

    @Autowired
    private UserPermissionTargetUserDetailsService userPermissionTargetUserDetailsService;

    @Autowired
    private UserPermissionTargetUserGroupDetailsService userPermissionTargetUserGroupDetailsService;

    /**
     * 通过用户 id 查询 用户权限列表
     *
     * @param userId
     * @return
     */
    public List<UserPermissionDetails> selectUserPermissionDetailsByUserId(Integer userId) {
        return userPermissionDetailsMapper.selectUserPermissionDetailsByUserId(userId);
    }

    /**
     * 通过权限id查询
     * @param permissionId
     * @return
     */
    public List<UserPermissionDetails> selectUserPermissionDetailsByPermissionId(Integer permissionId) {
        return userPermissionDetailsMapper.selectByExample(getExample(new UserPermissionDetails(null, permissionId)));
    }


    /**
     * 通过用户id查询获取用户权限表 ids
     * @param userId
     * @return
     */
    public List<Integer> getUserPermissionIdsByUserId(Integer userId){
        List<UserPermissionDetails> userPermissionDetailsList = this.selectUserPermissionDetailsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionDetailsList)){
            return userPermissionDetailsList.stream().filter(Objects::nonNull).map(UserPermissionDetails::getId).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 通过用户id删除
     * @param userId
     * @return
     */
    public Integer deleteUserPermissionDetailsByUserId(Integer userId) {
        return userPermissionDetailsMapper.deleteByExample(getExample(new UserPermissionDetails(userId, null)));
    }

    /**
     * 添加
     * @param userId
     * @param permissionIds
     */
    public void insertUserPermissionDetails(Integer userId, Set<Integer> permissionIds) {
        if (!CollectionUtils.isEmpty(permissionIds)) {
            permissionIds.stream()
                    .filter(Objects::nonNull)
                    .forEach(permissionId -> {
                        userPermissionDetailsMapper.insertSelective(new UserPermissionDetails(userId, permissionId));
                    });
        }
    }

    /**
     * 更新
     * @param userId
     * @param permissionIds
     */
    public void updateUserPermission(Integer userId, Set<Integer> permissionIds) {
        if (userId != null && userId > 0){
            deleteUserPermissionDetailsByUserId(userId);
        }
        if (userId != null && userId > 0 && !CollectionUtils.isEmpty(permissionIds)){
            insertUserPermissionDetails(userId, permissionIds);
        }
    }

    /**
     * 通过ids 删除
     * @param userPermissionIds
     */
    public void deleteUserPermissionDetailsByIds(Set<Integer> userPermissionIds){
        if (!CollectionUtils.isEmpty(userPermissionIds)){
            userPermissionIds.stream()
                    .filter(Objects::nonNull)
                    .forEach(userPermissionId -> {
                        userPermissionDetailsMapper.deleteByPrimaryKey(userPermissionId);
                    });
        }
    }

    /**
     * 变更权限
     * @Author houzhen
     * @Date 10:23 2019/8/6
     **/
    public void updateUserPermission (Integer userGroupId, Set<Integer> userIds, Set<Integer> permissionIds) {
        logger.info("UserPermissionDetailsService.updateUserPermission userGroupId : {},userIds:{},permissionIds:{}", userGroupId,JsonUtils.obj2json(userIds),JsonUtils.obj2json(permissionIds));
        // 参数判断
        if (userGroupId == null && userGroupId < 0) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 通过用户组获取用户权限ids
        Set<Integer> userPermissionIds = userPermissionTargetUserGroupDetailsService.getUserPermissionIdsByUserGroupId(userGroupId);
        // 删除用户权限表数据
        deleteUserPermissionDetailsByIds(userPermissionIds);
        // 用户ids和权限ids不为空
        Set<Integer> userPermissionDetailsIds = new HashSet<>();
        // 插入用户权限表数据
        if (!CollectionUtils.isEmpty(userIds) && !CollectionUtils.isEmpty(permissionIds)){
            userIds.stream()
                    .filter(Objects::nonNull)
                    .forEach(userId -> {
                        permissionIds.stream()
                                .filter(Objects::nonNull)
                                .forEach(permissionId -> {
                                    UserPermissionDetails userPermissionDetails = new UserPermissionDetails(userId, permissionId);
                                    userPermissionDetailsMapper.insertSelective(userPermissionDetails);
                                    userPermissionDetailsIds.add(userPermissionDetails.getId());
                                });
                    });
        }

        // 更新用户组权限关系表
        if (userGroupId != null && userGroupId > 0 && !CollectionUtils.isEmpty(userPermissionDetailsIds)){
            userPermissionTargetUserGroupDetailsService.updateUserPermissionTargetUserGroupDetails(userGroupId, userPermissionDetailsIds);
        }
    }

    /**
     * 在用户列表中点击权限
     * @param userId
     */
    public ActionPermissionInUserListDto actionPermissionInUserList(Integer userId) {
        ActionPermissionInUserListDto actionPermissionInUserListDto = new ActionPermissionInUserListDto();
        // 获取用户权限 ids
        List<UserPermissionDetails> userPermissionDetailsList = selectUserPermissionDetailsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionDetailsList)){
            // 目标集团
            List<UserPermissionTargetGroupDetailsDto> userPermissionTargetGroupDetailsDto = userPermissionTargetGroupDetailsService.getTargetGroupDetailsByUserIdAndPermissionId(userPermissionDetailsList);
            if (!CollectionUtils.isEmpty(userPermissionTargetGroupDetailsDto)){
                actionPermissionInUserListDto.setUserPermissionTargetGroupDetailsDtoList(userPermissionTargetGroupDetailsDto);
            }
            // 目标公司
            List<UserPermissionTargetCompanyDetailsDto> userPermissionTargetCompanyDetailsDto = userPermissionTargetCompanyDetailsService.getTargetCompanyDetailsByUserIdPermissionId(userPermissionDetailsList);
            if (!CollectionUtils.isEmpty(userPermissionTargetCompanyDetailsDto)){
                actionPermissionInUserListDto.setUserPermissionTargetCompanyDetailsDtoList(userPermissionTargetCompanyDetailsDto);
            }
            // 目标用户
            List<UserPermissionTargetUserDetailsDto> userPermissionTargetUserDetailsDto = userPermissionTargetUserDetailsService.getTargetUserDetailsByUserIdPermissionId(userPermissionDetailsList);
            if (!CollectionUtils.isEmpty(userPermissionTargetUserDetailsDto)){
                actionPermissionInUserListDto.setUserPermissionTargetUserDetailsDtoList(userPermissionTargetUserDetailsDto);
            }
        }
        return actionPermissionInUserListDto;
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    public UserPermissionDetails selectByPrimaryKey(Integer id){
        return userPermissionDetailsMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过用户 id 和权限 id 查询
     * @param userPermissionDetails
     * @return
     */
    public List<UserPermissionDetails> selectUserPermissionDetailsByUserIdAndPermissionId(UserPermissionDetails userPermissionDetails) {
        return userPermissionDetailsMapper.selectByExample(getExample(userPermissionDetails));
    }

    /**
     * 获取 example
     * @param userPermissionDetails
     * @return
     */
    private Example getExample(UserPermissionDetails userPermissionDetails){
        Example example = new Example(UserPermissionDetails.class);
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
