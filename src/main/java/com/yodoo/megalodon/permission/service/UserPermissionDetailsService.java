package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.UserPermissionDetailsDto;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
import com.yodoo.megalodon.permission.exception.BundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.UserPermissionDetailsMapper;
import com.yodoo.megalodon.permission.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @Description ：用户权限
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserPermissionDetailsService.class);

    @Autowired
    private UserPermissionDetailsMapper userPermissionDetailsMapper;

    /**
     * 通过用户 id 查询 用户权限列表
     *
     * @param userId
     * @return
     */
    public List<UserPermissionDetails> selectUserPermissionDetailsByUserId(Integer userId) {
        Example example = new Example(UserPermissionDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        return userPermissionDetailsMapper.selectByExample(example);
    }

    /**
     * 通过用户id查询获取用户权限表 ids
     * @param userId
     * @return
     */
    public List<Integer> getUserPermissionIdsByUserId(Integer userId){
        List<UserPermissionDetails> userPermissionDetailsList = this.selectUserPermissionDetailsByUserId(userId);
        List<Integer> userPermissionIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userPermissionDetailsList)){
            userPermissionIds = userPermissionDetailsList.stream()
                    .filter(Objects::nonNull)
                    .map(UserPermissionDetails::getId)
                    .collect(Collectors.toList());
        }
        return userPermissionIds;
    }

    /**
     * 通过用户id删除
     * @param userId
     * @return
     */
    public Integer deleteUserPermissionDetailsByUserId(Integer userId) {
        UserPermissionDetails userPermissionDetails = new UserPermissionDetails();
        userPermissionDetails.setUserId(userId);
        return userPermissionDetailsMapper.delete(userPermissionDetails);
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
                    .map(permissionId -> {
                        return userPermissionDetailsMapper.insertSelective(new UserPermissionDetails(userId, permissionId));
                    }).filter(Objects::nonNull).count();
        }
    }

    /**
     * 变更权限
     * @Author houzhen
     * @Date 10:23 2019/8/6
     **/
    public void updateUserPermission (List < UserPermissionDetailsDto > userPermissionDetailsDtoList, Integer userId)
    {
        logger.info("UserPermissionDetailsService.updateUserPermission userPermissionDetailsDtoList:{}", JsonUtils.obj2json(userPermissionDetailsDtoList));
        // 参数判断
        if (userId == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 先删除旧的数据
        Example example = new Example(UserPermissionDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        example.and(criteria);
        userPermissionDetailsMapper.deleteByExample(example);
        // 增加修改后的权限
        if (!CollectionUtils.isEmpty(userPermissionDetailsDtoList)) {
            List<UserPermissionDetails> addList = userPermissionDetailsDtoList.stream().map(dto -> {
                UserPermissionDetails userPermissionDetails = new UserPermissionDetails();
                BeanUtils.copyProperties(dto, userPermissionDetails);
                return userPermissionDetails;
            }).collect(Collectors.toList());
            userPermissionDetailsMapper.insertList(addList);
        }
    }
}
