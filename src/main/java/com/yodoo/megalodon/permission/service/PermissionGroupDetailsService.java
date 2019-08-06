package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.entity.PermissionGroupDetails;
import com.yodoo.megalodon.permission.mapper.PermissionGroupDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * @Description ：权限组明细
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
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
        Example example = new Example(PermissionGroupDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("permissionGroupId", permissionGroupId);
        return permissionGroupDetailsMapper.selectCountByExample(example);
    }
}
