package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.entity.PermissionGroupDetails;
import com.yodoo.megalodon.permission.mapper.PermissionGroupDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description ：权限组明细
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class PermissionGroupDetailsService {

    @Autowired
    private PermissionGroupDetailsMapper permissionGroupDetailsMapper;

    /**
     * 通过权限组id 查询
     *
     * @param permissionGroupId
     * @return
     */
    public Integer selectPermissionGroupDetailsCountByPermissionGroupId(Integer permissionGroupId) {
        Example example = getPermissionGroupDetailsExampleByPermissionGroupId(permissionGroupId);
        return permissionGroupDetailsMapper.selectCountByExample(example);
    }

    /**
     * 通过权限组id 查询获取权限 ids
     * @param permissionGroupIdList
     * @return
     */
    public Set<Integer> getPermissionIds(Set<Integer> permissionGroupIdList) {
        Set<Integer> permissionIdList = new HashSet<>();
        if (!CollectionUtils.isEmpty(permissionGroupIdList)){
            permissionGroupIdList.stream()
                    .filter(Objects::nonNull)
                    .forEach(permissionGroupId -> {
                        Example example = getPermissionGroupDetailsExampleByPermissionGroupId(permissionGroupId);
                        List<PermissionGroupDetails> permissionGroupDetails = permissionGroupDetailsMapper.selectByExample(example);
                        if (!CollectionUtils.isEmpty(permissionGroupDetails)){
                            Set<Integer> collect = permissionGroupDetails.stream()
                                    .filter(Objects::nonNull)
                                    .map(PermissionGroupDetails::getPermissionId)
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toSet());
                            if (!CollectionUtils.isEmpty(collect)){
                                permissionIdList.addAll(collect);
                            }
                        }
                    });
        }
        return permissionIdList;
    }

    /**
     * 通过权限id 查询
     * @param permissionId
     * @return
     */
    public List<PermissionGroupDetails> selectPermissionGroupDetailsByPermissionId(Integer permissionId) {
        Example example = new Example(PermissionGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("permissionId", permissionId);
        return permissionGroupDetailsMapper.selectByExample(example);
    }

    /**
     * 通过id 删除
     * @param permissionGroupId
     * @return
     */
    public Integer deleteByPrimaryKey(Integer permissionGroupId){
        return permissionGroupDetailsMapper.deleteByPrimaryKey(permissionGroupId);
    }

    public void insertPermissionGroupDetails(Integer permissionGroupId, Set<Integer> permissionIds){
        if (permissionGroupId != null && permissionGroupId > 0 && !CollectionUtils.isEmpty(permissionIds)){
            permissionIds.stream()
                    .filter(Objects::nonNull)
                    .forEach(permissionId -> {
                        permissionGroupDetailsMapper.insertSelective(new PermissionGroupDetails(permissionGroupId, permissionId));
                    });
        }
    }

    private Example getPermissionGroupDetailsExampleByPermissionGroupId(Integer permissionGroupId){
        Example example = new Example(PermissionGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("permissionGroupId", permissionGroupId);
        return example;
    }
}
