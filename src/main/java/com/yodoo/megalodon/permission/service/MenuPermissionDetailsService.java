package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.entity.MenuPermissionDetails;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.MenuPermissionDetailsMapper;
import com.yodoo.megalodon.permission.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author houzhen
 * @Date 13:50 2019/8/7
**/
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class MenuPermissionDetailsService {

    private static Logger logger = LoggerFactory.getLogger(MenuPermissionDetailsService.class);

    @Autowired
    private MenuPermissionDetailsMapper menuPermissionDetailsMapper;

    /**
     * 更新菜单权限
     * @Author houzhen
     * @Date 13:49 2019/8/7
    **/
    public void updateMenuPermission(List<Integer> permissionIdList, Integer menuId) {
        logger.info("MenuPermissionDetailsService#updateMenuPermission permissionIdList:{}, menuId:{}",
                JsonUtils.obj2json(permissionIdList), menuId);
        if (menuId == null) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 删除菜单权限
        this.deleteMenuPermission(menuId);
        if (!CollectionUtils.isEmpty(permissionIdList)) {
            permissionIdList.stream()
                    .filter(Objects::nonNull)
                    .map(permissionId -> {
                        MenuPermissionDetails menuPermissionDetails = new MenuPermissionDetails();
                        menuPermissionDetails.setMenuId(menuId);
                        menuPermissionDetails.setPermissionId(permissionId);
                        return menuPermissionDetailsMapper.insertSelective(menuPermissionDetails);
                    }).collect(Collectors.toList());
        }
    }

    /**
     * 根据菜单id删除
     * @Author houzhen
     * @Date 14:04 2019/8/7
    **/
    public void deleteMenuPermission(Integer menuId) {
        logger.info("MenuPermissionDetailsService#deleteMenuPermission menuId:{}", menuId);
        if (menuId == null) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        Example example = new Example(MenuPermissionDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuId", menuId);
        example.and(criteria);
        menuPermissionDetailsMapper.deleteByExample(example);
    }
}
