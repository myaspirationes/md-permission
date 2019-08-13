package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.MenuDto;
import com.yodoo.megalodon.permission.entity.Menu;
import com.yodoo.megalodon.permission.exception.BundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.MenuMapper;
import com.yodoo.megalodon.permission.util.JsonUtils;
import com.yodoo.megalodon.permission.util.RequestPrecondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author houzhen
 * @Date 13:50 2019/8/7
**/
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class MenuService {

    private static Logger logger = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuPermissionDetailsService menuPermissionDetailsService;

    /**
     * 新增菜单
     * @Author houzhen
     * @Date 13:45 2019/8/7
    **/
    public void addMenu(MenuDto menuDto) {
        logger.info("MenuService#addMenu menuDto:{}", JsonUtils.obj2json(menuDto));
        // 参数校验
        RequestPrecondition.checkArgumentsNotEmpty(menuDto.getMenuCode(), menuDto.getMenuName(),
                menuDto.getMenuTarget(), menuDto.getOrderNo());
        // 验证code是否存在
        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuCode", menuDto.getMenuCode());
        example.and(criteria);
        List<Menu> menuList = menuMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(menuList)) {
            throw new PermissionException(BundleKey.MENU_EXIST, BundleKey.MENU_EXIST_MSG);
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDto, menu);
        menuMapper.insertSelective(menu);
        // 更新权限
        menuPermissionDetailsService.updateMenuPermission(menuDto.getPermissionIdList(), menu.getId());
    }

    /**
     * 更新菜单
     * @Author houzhen
     * @Date 13:45 2019/8/7
    **/
    public void updateMenu(MenuDto menuDto) {
        logger.info("MenuService#updateMenu menuDto:{}", JsonUtils.obj2json(menuDto));
        // 参数校验
        RequestPrecondition.checkArgumentsNotEmpty(menuDto.getMenuCode(), menuDto.getMenuName(),
                menuDto.getMenuTarget(), menuDto.getOrderNo());
        Menu menu = menuMapper.selectByPrimaryKey(menuDto.getId());
        if (menu == null) {
            throw new PermissionException(BundleKey.MENU_NOT_EXIST, BundleKey.MENU_NOT_EXIST_MSG);
        }
        // 更新
        BeanUtils.copyProperties(menuDto, menu, "menuCode");
        menuMapper.updateByPrimaryKey(menu);
        // 更新权限
        menuPermissionDetailsService.updateMenuPermission(menuDto.getPermissionIdList(), menu.getId());
    }

    /**
     * 删除菜单
     * @Author houzhen
     * @Date 15:25 2019/8/7
    **/
    public void deleteMenu(Integer id) {
        logger.info("MenuService#updateMenu menuDto:{}", JsonUtils.obj2json(id));
        if (id == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询菜单
        Menu menu = menuMapper.selectByPrimaryKey(id);
        if (menu == null) {
            throw new PermissionException(BundleKey.MENU_NOT_EXIST, BundleKey.MENU_NOT_EXIST_MSG);
        }
        // 删除菜单权限
        menuPermissionDetailsService.deleteMenuPermission(menu.getId());
        // 删除菜单
        menuMapper.deleteByPrimaryKey(menu.getId());
    }
}
