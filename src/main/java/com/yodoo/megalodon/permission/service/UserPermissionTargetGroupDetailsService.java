package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetGroupDetailsDto;
import com.yodoo.megalodon.permission.entity.UserPermissionTargetGroupDetails;
import com.yodoo.megalodon.permission.exception.BundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private UserPermissionDetailsService userPermissionDetailsService;

    /**
     * 变更权限用户管理目标集团
     * @Author houzhen
     * @Date 10:23 2019/8/6
     **/
    @PreAuthorize("hasAnyAuthority('permission_manage')")
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
     * 通过用户权限id 查询用户管理目标集团
     * @param userPermissionId
     * @return
     */
    public List<Integer>  getGroupIdsByUserPermissionId(Integer userPermissionId) {
        List<Integer> groupsIds = new ArrayList<>();

        Example example = getExample(userPermissionId);
        List<UserPermissionTargetGroupDetails> list = userPermissionTargetGroupDetailsMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)){
            groupsIds = list.stream()
                    .filter(Objects::nonNull)
                    .map(UserPermissionTargetGroupDetails::getGroupId)
                    .collect(Collectors.toList());
        }
        return groupsIds;
    }

    /**
     * 通过用户权限ids 查询
     * @param userPermissionIds
     * @return
     */
    public List<UserPermissionTargetGroupDetailsDto> getTargetGroupDetailsByUserPermissionIds(List<Integer> userPermissionIds) {
        List<UserPermissionTargetGroupDetailsDto> UserPermissionTargetGroupDetailsDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userPermissionIds)){
            userPermissionIds.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionId -> {
                        Example example = getExample(userPermissionId);
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
                                UserPermissionTargetGroupDetailsDtoList.addAll(collect);
                            }
                        }
                        return null;
                    }).count();
        }
        return UserPermissionTargetGroupDetailsDtoList;
    }

    private Example getExample(Integer userPermissionId){
        Example example = new Example(UserPermissionTargetGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPermissionId", userPermissionId);
        return example;
    }
}
