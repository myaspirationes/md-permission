package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetDto;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetUserDetailsDto;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
import com.yodoo.megalodon.permission.entity.UserPermissionTargetUserDetails;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
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
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionTargetUserDetailsService {

    @Autowired
    private UserPermissionTargetUserDetailsMapper userPermissionTargetUserDetailsMapper;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    @Autowired
    private UserService userService;

    /**
     * 更新用户管理目标用户数据
     * @param userPermissionTargetDto
     */
    public void updateUserPermissionTargetUser(UserPermissionTargetDto userPermissionTargetDto) {
        // 参数校验
        updateUserPermissionTargetUserParameterCheck(userPermissionTargetDto);
        // 删除
        Example example = getExampleByUserIdAndPermissionId(new UserPermissionDetails(userPermissionTargetDto.getUserId(), userPermissionTargetDto.getPermissionId()));
        userPermissionTargetUserDetailsMapper.deleteByExample(example);
        // 增加修改后的权限
        userPermissionTargetDto.getTargetIds().stream()
                .filter(Objects::nonNull)
                .forEach(targetUserId ->{
                    UserPermissionTargetUserDetails userPermissionTargetUserDetails = new UserPermissionTargetUserDetails();
                    userPermissionTargetUserDetails.setUserId(userPermissionTargetDto.getUserId());
                    userPermissionTargetUserDetails.setPermissionId(userPermissionTargetDto.getPermissionId());
                    userPermissionTargetUserDetails.setTargetUserId(targetUserId);
                    userPermissionTargetUserDetailsMapper.insertSelective(userPermissionTargetUserDetails);
                });
    }

    /**
     * 通过用户id , 权限id查询获取用户ids
     * @param userPermissionIdList
     * @return
     */
    public Set<Integer> getUserIdsByUserIdAndPermissionId(List<UserPermissionDetails> userPermissionIdList) {
        Set<Integer> targetUserIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(userPermissionIdList)){
            userPermissionIdList.stream()
                    .filter(Objects::nonNull)
                    .forEach(userPermissionDetails -> {
                        Example example = getExampleByUserIdAndPermissionId(userPermissionDetails);
                        List<UserPermissionTargetUserDetails> list = userPermissionTargetUserDetailsMapper.selectByExample(example);
                        if (!CollectionUtils.isEmpty(list)){
                            Set<Integer> userIds = list.stream().filter(Objects::nonNull).map(UserPermissionTargetUserDetails::getTargetUserId).collect(Collectors.toSet());
                            if (!CollectionUtils.isEmpty(userIds)){
                                targetUserIds.addAll(userIds);
                            }
                        }
                    });
        }
        return targetUserIds;
    }

    /**
     * 通过用户id 和 权限 id 查询目标用户
     * @param userPermissionDetailsList
     * @return
     */
    public List<UserPermissionTargetUserDetailsDto> getTargetUserDetailsByUserIdPermissionId(List<UserPermissionDetails> userPermissionDetailsList) {
        List<UserPermissionTargetUserDetailsDto> userPermissionTargetUserDetailsDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userPermissionDetailsList)){
            userPermissionDetailsList.stream()
                    .filter(Objects::nonNull)
                    .forEach(userPermissionDetails -> {
                        Example example = getExampleByUserIdAndPermissionId(userPermissionDetails);
                        List<UserPermissionTargetUserDetails> targetUserList = userPermissionTargetUserDetailsMapper.selectByExample(example);
                        if (!CollectionUtils.isEmpty(targetUserList)){
                            List<UserPermissionTargetUserDetailsDto> collect = targetUserList.stream()
                                    .filter(Objects::nonNull)
                                    .map(userPermissionTargetUserDetails -> {
                                        UserPermissionTargetUserDetailsDto userPermissionTargetUserDetailsDto = new UserPermissionTargetUserDetailsDto();
                                        BeanUtils.copyProperties(userPermissionTargetUserDetails, userPermissionTargetUserDetailsDto);
                                        return userPermissionTargetUserDetailsDto;
                                    }).filter(Objects::nonNull).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(collect)){
                                userPermissionTargetUserDetailsDtoList.addAll(collect);
                            }
                        }
                    });
        }
        return userPermissionTargetUserDetailsDtoList;
    }

    /**
     * 更新目标用户参数校验
     * @param userPermissionTargetDto
     */
    private void updateUserPermissionTargetUserParameterCheck(UserPermissionTargetDto userPermissionTargetDto) {
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
        // 查询用户是否存在
        Long userNoExistCount = userService.selectUserNoExistCountByIds(userPermissionTargetDto.getTargetIds());
        if (userNoExistCount != null && userNoExistCount > 0){
            throw new PermissionException(PermissionBundleKey.USER_NOT_EXIST, PermissionBundleKey.USER_NOT_EXIST_MSG);
        }
    }

    /**
     * 获取 example
     * @param userPermissionDetails
     * @return
     */
    private Example getExampleByUserIdAndPermissionId(UserPermissionDetails userPermissionDetails) {
        Example example = new Example(UserPermissionTargetUserDetails.class);
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
