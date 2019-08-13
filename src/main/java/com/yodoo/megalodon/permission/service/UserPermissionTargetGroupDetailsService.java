package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetDto;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetGroupDetailsDto;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author houzhen
 * @Date 14:45 2019/8/6
**/
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
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
    public void updateUserPermissionTargetGroups(UserPermissionTargetDto userPermissionTargetDto) {
        logger.info("UserPermissionTargetGroupDetailsService.updateUserPermissionTargetGroups userPermissionDetailsDtoList:{}", JsonUtils.obj2json(userPermissionTargetDto));
        // 参数判断
        if (userPermissionTargetDto.getUserId() == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        UserPermissionDetails userPermissionDetails = userPermissionDetailsService.selectByPrimaryKey(userPermissionTargetDto.getUserPermissionId());
        if (userPermissionDetails == null){
            throw new PermissionException(BundleKey.USER_PERMISSION_NOT_EXIST, BundleKey.USER_PERMISSION_NOT_EXIST_MSG);
        }
        Example example = new Example(UserPermissionTargetGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPermissionId",userPermissionDetails.getId());
        userPermissionTargetGroupDetailsMapper.deleteByExample(example);
        // 增加修改后的权限
        if (!CollectionUtils.isEmpty(userPermissionTargetDto.getTargetIds())) {
            userPermissionTargetDto.getTargetIds().stream()
                    .filter(Objects::nonNull)
                    .map(groupId ->{
                        return userPermissionTargetGroupDetailsMapper.insertSelective(new UserPermissionTargetGroupDetails(userPermissionDetails.getId(), groupId));
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
     * 通过用户权限 id 查询
     *
     * @param userPermissionId
     * @return
     */
    public List<UserPermissionTargetGroupDetails> selectUserPermissionTargetGroupDetailsByUserPermissionId(Integer userPermissionId) {
        Example example = new Example(UserPermissionTargetGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPermissionId", userPermissionId);
        return userPermissionTargetGroupDetailsMapper.selectByExample(example);
    }

    /**
     * 通过集团 id 查询
     *
     * @param groupId
     * @return
     */
    public Integer selectUserPermissionTargetGroupDetailsCountByGroupId(Integer groupId) {
        Example example = new Example(UserPermissionTargetGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId",groupId);
        return userPermissionTargetGroupDetailsMapper.selectCountByExample(example);
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
