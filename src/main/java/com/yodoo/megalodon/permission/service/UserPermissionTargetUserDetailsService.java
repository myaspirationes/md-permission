package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetDto;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetUserDetailsDto;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
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
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionTargetUserDetailsService {

    @Autowired
    private UserPermissionTargetUserDetailsMapper userPermissionTargetUserDetailsMapper;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    /**
     * 更新用户管理目标用户数据
     * @param userPermissionTargetDto
     */
    public void updateUserPermissionTargetUser(UserPermissionTargetDto userPermissionTargetDto) {
        // 参数判断
        if (userPermissionTargetDto.getUserId() == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        UserPermissionDetails userPermissionDetails = userPermissionDetailsService.selectByPrimaryKey(userPermissionTargetDto.getUserPermissionId());
        if (userPermissionDetails == null){
            throw new PermissionException(BundleKey.USER_PERMISSION_NOT_EXIST, BundleKey.USER_PERMISSION_NOT_EXIST_MSG);
        }
        // 通过用户权限 ids 删除 用户管理目标用户 数据
        Example example = new Example(UserPermissionTargetUserDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPermissionId", userPermissionDetails.getId());
        userPermissionTargetUserDetailsMapper.deleteByExample(example);
        // 增加修改后的权限
        if (!CollectionUtils.isEmpty(userPermissionTargetDto.getTargetIds())) {
            userPermissionTargetDto.getTargetIds().stream()
                    .filter(Objects::nonNull)
                    .map(userId ->{
                        return userPermissionTargetUserDetailsMapper.insertSelective(new UserPermissionTargetUserDetails(userPermissionDetails.getId(), userId));
                    }).count();
        }
    }

    /**
     * 通过用户权限id查询获取用户ids
     * @param userPermissionId
     * @return
     */
    public List<Integer> getUserIdsByUserPermissionId(Integer userPermissionId) {
        Example example = getExample(userPermissionId);
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
                        Example example = getExample(userPermissionId);
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

    private Example getExample(Integer userPermissionId){
        Example example = new Example(UserPermissionTargetUserDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPermissionId", userPermissionId);
        return example;
    }
}
