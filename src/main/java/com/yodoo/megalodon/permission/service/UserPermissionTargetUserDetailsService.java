package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetUserDetailsDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    /**
     * 更新用户管理目标用户数据
     * @param userPermissionTargetUserDetailsDtoList
     * @param userId
     */
    public void updateUserPermissionTargetUser(List<UserPermissionTargetUserDetailsDto> userPermissionTargetUserDetailsDtoList, Integer userId) {
        // 参数判断
        if (userId == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 先通过用户 id 查询用户权限表，获取用户权限表 ids
        List<Integer> userPermissionIds = userPermissionDetailsService.getUserPermissionIdsByUserId(userId);
        // 通过用户权限 ids 删除 用户管理目标用户 数据
        if (!CollectionUtils.isEmpty(userPermissionIds)){
            Example example = new Example(UserPermissionTargetUserDetails.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("userPermissionId",userPermissionIds);
            userPermissionTargetUserDetailsMapper.deleteByExample(example);
        }
        // 增加修改后的权限
        if (!CollectionUtils.isEmpty(userPermissionTargetUserDetailsDtoList)) {
            userPermissionTargetUserDetailsDtoList.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionTargetUserDetailsDto ->{
                        return userPermissionTargetUserDetailsMapper.insertSelective(new UserPermissionTargetUserDetails(
                                userPermissionTargetUserDetailsDto.getUserPermissionId(), userPermissionTargetUserDetailsDto.getUserId()));
                    }).count();
        }
    }

    /**
     * 通过用户权限id查询获取用户ids
     * @param userPermissionId
     * @return
     */
    public List<Integer> getUserIdsByUserPermissionId(Integer userPermissionId) {
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
     * 通过用户权限 ids 查询目标用户
     * @param userPermissionIds
     * @return
     */
    public List<UserPermissionTargetUserDetailsDto> getTargetUserDetailsByUserPermissionIds(List<Integer> userPermissionIds) {
        List<UserPermissionTargetUserDetailsDto> userPermissionTargetUserDetailsDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userPermissionIds)){
            userPermissionIds.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionId -> {
                        Example example = new Example(UserPermissionTargetUserDetails.class);
                        Example.Criteria criteria = example.createCriteria();
                        criteria.andEqualTo("userPermissionId", userPermissionId);
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
                        return null;
                    }).filter(Objects::nonNull).count();
        }
        return userPermissionTargetUserDetailsDtoList;
    }
}
