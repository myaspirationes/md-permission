package com.yodoo.megalodon.permission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.contract.PermissionConstants;
import com.yodoo.megalodon.permission.dto.MenuDto;
import com.yodoo.megalodon.permission.entity.Menu;
import com.yodoo.megalodon.permission.entity.Permission;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.MenuMapper;
import com.yodoo.megalodon.permission.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
public class MenuService {

    private static Logger logger = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuPermissionDetailsService menuPermissionDetailsService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 条件分页查询菜单 TODO
     * @param menuDto
     * @return
     */
    public PageInfoDto<MenuDto> queryMenuList(MenuDto menuDto) {
        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        Page<?> pages = PageHelper.startPage(menuDto.getPageNum(), menuDto.getPageSize());
        List<Menu> select = menuMapper.selectByExample(example);
        List<MenuDto> collect = copyProperties(select);
        return new PageInfoDto<MenuDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 新增菜单
     * @Author houzhen
     * @Date 13:45 2019/8/7
    **/
    public void addMenu(MenuDto menuDto) {
        logger.info("MenuService#addMenu menuDto:{}", JsonUtils.obj2json(menuDto));
        // 参数校验
        addMenuParameterCheck(menuDto);
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
        Menu menu = updateMenuParameterCheck(menuDto);
        // 更新
        BeanUtils.copyProperties(menuDto, menu);
        menuMapper.updateByPrimaryKeySelective(menu);
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
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 查询菜单
        Menu menu = menuMapper.selectByPrimaryKey(id);
        if (menu == null) {
            throw new PermissionException(PermissionBundleKey.MENU_NOT_EXIST, PermissionBundleKey.MENU_NOT_EXIST_MSG);
        }
       Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", id);
        List<Menu> menuList = menuMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(menuList)){
            throw new PermissionException(PermissionBundleKey.MENU_ALREADY_USE, PermissionBundleKey.MENU_ALREADY_USE_MSG);
        }
        // 删除菜单权限
        menuPermissionDetailsService.deleteMenuPermission(menu.getId());
        // 删除菜单
        menuMapper.deleteByPrimaryKey(menu.getId());
    }

    /**
     * 添加参数校验
     * @param menuDto
     */
    private void addMenuParameterCheck(MenuDto menuDto) {
        if (menuDto == null || StringUtils.isBlank(menuDto.getMenuCode()) || StringUtils.isBlank(menuDto.getMenuName())
                || StringUtils.isBlank(menuDto.getMenuTarget()) || StringUtils.isBlank(menuDto.getOrderNo())){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        parameterCheck(menuDto);
        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuCode", menuDto.getMenuCode());
        List<Menu> menuList = menuMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(menuList)) {
            throw new PermissionException(PermissionBundleKey.MENU_EXIST, PermissionBundleKey.MENU_EXIST_MSG);
        }
    }

    /**
     * 更新参数校验
     * @param menuDto
     * @return
     */
    private Menu updateMenuParameterCheck(MenuDto menuDto) {
        if (menuDto == null || menuDto.getId() == null || menuDto.getId() < 0 || StringUtils.isBlank(menuDto.getMenuCode())
                || StringUtils.isBlank(menuDto.getMenuName()) || StringUtils.isBlank(menuDto.getMenuTarget()) || StringUtils.isBlank(menuDto.getOrderNo())){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        Menu menu = selectByPrimaryKey(menuDto.getId());
        if (menu == null) {
            throw new PermissionException(PermissionBundleKey.MENU_NOT_EXIST, PermissionBundleKey.MENU_NOT_EXIST_MSG);
        }
        parameterCheck(menuDto);

        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", menuDto.getId());
        if (menuDto.getParentId() != null && menuDto.getParentId() > 0){
            criteria.andEqualTo("parentId", menuDto.getParentId());
        }
        criteria.andEqualTo("menuCode",menuDto.getMenuCode());
        Menu menuByCode = menuMapper.selectOneByExample(example);
        if (menuByCode != null){
            throw new PermissionException(PermissionBundleKey.MENU_EXIST, PermissionBundleKey.MENU_EXIST_MSG);
        }
        return menu;
    }

    /**
     * 参数校验
     * @param menuDto
     */
    private void parameterCheck(MenuDto menuDto){
        if (!menuDto.getMenuTarget().startsWith(PermissionConstants.STARTS_WITH)){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        if (menuDto.getParentId() != null && menuDto.getParentId() < 0){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        if (menuDto.getParentId() != null && menuDto.getParentId() > 0){
            Menu menuByParentId = selectByPrimaryKey(menuDto.getParentId());
            if (menuByParentId == null){
                throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
            }
        }
        List<Integer> permissionIdList = menuDto.getPermissionIdList();
        if (!CollectionUtils.isEmpty(permissionIdList)){
            permissionIdList.stream()
                    .filter(Objects::nonNull)
                    .forEach(permissionId -> {
                        Permission permission = permissionService.selectByPrimaryKey(permissionId);
                        if (permission == null){
                            throw new PermissionException(PermissionBundleKey.PERMISSION_NOT_EXIST, PermissionBundleKey.PERMISSION_NOT_EXIST_MSG);
                        }
                    });
        }
    }

    /**
     * 通过社键查询
     * @param id
     * @return
     */
    private Menu selectByPrimaryKey(Integer id){
        return menuMapper.selectByPrimaryKey(id);
    }

    /**
     * 复制
     * @param menuList
     * @return
     */
    private List<MenuDto> copyProperties(List<Menu> menuList){
        if (!CollectionUtils.isEmpty(menuList)){
            return menuList.stream()
                    .filter(Objects::nonNull)
                    .map(menu -> {
                        return copyProperties(menu);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param menu
     * @return
     */
    private MenuDto copyProperties(Menu menu){
        if (menu != null){
            MenuDto groupsDto = new MenuDto();
            BeanUtils.copyProperties(menu, groupsDto);
            return groupsDto;
        }
        return null;
    }
}
