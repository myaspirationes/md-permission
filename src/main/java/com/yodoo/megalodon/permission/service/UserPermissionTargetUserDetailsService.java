package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.UserDto;
import com.yodoo.megalodon.permission.entity.User;
import com.yodoo.megalodon.permission.entity.UserPermissionTargetUserDetails;
import com.yodoo.megalodon.permission.exception.BundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.UserPermissionTargetUserDetailsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description ：用户管理目标用户
 * @Author ：jinjun_luo
 * @Date ： 2019/8/6 0006
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionTargetUserDetailsService {

    @Autowired
    private UserPermissionTargetUserDetailsMapper userPermissionTargetUserDetailsMapper;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    @Autowired
    private UserService userService;


    /**
     * 通过用户id 查询用户管理目标用户列表
     * @param userId
     * @return
     */
    public List<UserDto> selectUserManageTargetUserListByUserId(Integer userId){
        if (userId == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        List<UserDto> userDtoList = new ArrayList<>();
        // 通过用户id 查询用户权限表，获取用户权限表id
        List<Integer> userPermissionIds = userPermissionDetailsService.getUserPermissionIdsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionIds)){
            userPermissionIds.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionId -> {
                        // 通过用户权限id 查询用户管理目标公司,获取公司 ids
                        List<Integer> userIds = this.getUserIdsByUserPermissionId(userPermissionId);
                        if (!CollectionUtils.isEmpty(userIds)) {
                            // 通过公司id 查询公司表信息
                            List<UserDto> collect= userIds.stream()
                                    .filter(Objects::nonNull)
                                    .map(id -> {
                                        return this.getUserDtoByUserId(id);
                                    }).filter(Objects::nonNull).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(collect)){
                                userDtoList.addAll(collect);
                            }
                        }
                        return null;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return userDtoList;
    }

    /**
     * 查询没有被当前用户管理的公司(可管理的公司)
     * @param userId
     * @return
     */
    public List<UserDto> getAvailableUserManageTargetUserListByUserId(Integer userId){
        if (userId == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 通过用户id查询用户权限表，获取用户权限表ids
        List<Integer> userPermissionIds = userPermissionDetailsService.getUserPermissionIdsByUserId(userId);
        // 公司 ids
        Set<Integer> userIdsListSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(userPermissionIds)){
            List<List<Integer>> userIdsList = userPermissionIds.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionId -> {
                        // 通过用户权限表id 查询用户管理目标公司表，获取公司ids
                        return this.getUserIdsByUserPermissionId(userPermissionId);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            // 整合用户 ids
            if (!CollectionUtils.isEmpty(userIdsList)){
                for (List<Integer> userIds : userIdsList) {
                    userIdsListSet.addAll(userIds);
                }
            }
        }
        return userService.selectUserNotInIds(userIdsListSet);
    }

    /**
     * 通过用户权限id查询获取用户ids
     * @param userPermissionId
     * @return
     */
    private List<Integer> getUserIdsByUserPermissionId(Integer userPermissionId) {
        Example example = new Example(UserPermissionTargetUserDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPermissionId", userPermissionId);
        List<UserPermissionTargetUserDetails> targetUserList = userPermissionTargetUserDetailsMapper.selectByExample(example);
        List<Integer> userIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(targetUserList)){
            userIds = targetUserList.stream()
                    .filter(Objects::nonNull)
                    .map(UserPermissionTargetUserDetails::getUserId)
                    .collect(Collectors.toList());
        }
        return userIds;
    }

    /**
     * 通过用户id 查询
     * @param requestUserId
     * @return
     */
    private UserDto getUserDtoByUserId(Integer requestUserId) {
        User user = userService.selectByPrimaryKey(requestUserId);
        if (user != null){
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            return userDto;
        }
        return null;
    }
}
