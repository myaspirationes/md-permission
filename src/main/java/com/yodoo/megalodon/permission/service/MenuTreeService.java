package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.dto.MenuDto;
import com.yodoo.megalodon.permission.entity.Menu;
import com.yodoo.megalodon.permission.entity.MenuPermissionDetails;
import com.yodoo.megalodon.permission.entity.Permission;
import com.yodoo.megalodon.permission.mapper.MenuMapper;
import com.yodoo.megalodon.permission.mapper.MenuPermissionDetailsMapper;
import com.yodoo.megalodon.permission.util.BeanCopyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 构建参数树service
 *
 * @Author houzhen
 * @Date 15:14 2019/7/3
 **/
@Service
public class MenuTreeService {

    private static Logger logger = LoggerFactory.getLogger(MenuTreeService.class);

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MenuPermissionDetailsMapper menuPermissionDetailsMapper;

    /**
     * 查询整颗权限树
     * @Author houzhen
     * @Date 13:28 2019/8/7
    **/
    public List<MenuDto> getAllMenuTree() {
        // 查询全部菜单
        List<Menu> allMenuList = menuMapper.selectAll();
        if (CollectionUtils.isEmpty(allMenuList)) {
            return null;
        }
        // 排序树
        this.sortMenu(allMenuList);
        // 构建树
        return this.buildMenuListToTree(allMenuList);
    }

    /**
     * 根据用户id查询菜单树
     *
     * @Author houzhen
     * @Date 11:09 2019/7/9
     **/
    public List<MenuDto> getMenuTree(Integer userId) {
        logger.info("MenuTreeService#getTreeMenu userId:{}", userId);
        if (userId == null) {
            return null;
        }
        // 查询全部菜单
        List<Menu> allMenuList = menuMapper.selectAll();
        if (CollectionUtils.isEmpty(allMenuList)) {
            return null;
        }
        // 查询权限菜单
        List<Menu> menuPermissionList = this.getPermissionMenu(allMenuList, userId);

        // 补全菜单集合
        List<Menu> menuList = this.completeMenu(allMenuList, menuPermissionList);
        // 排序树
        this.sortMenu(menuList);
        // 构建树
        return this.buildMenuListToTree(menuList);
    }

    private List<Menu> getPermissionMenu(List<Menu> allMenuList, Integer userId) {
        List<Menu> rsMenuList = new ArrayList<>();
        // 获取用户权限
        List<Permission> userPermissionList = permissionService.selectPermissionByUserId(userId);
        if (CollectionUtils.isEmpty(userPermissionList)) {
            return rsMenuList;
        }
        Set<Integer> userPermissionSet = userPermissionList.stream().map(Permission::getId).collect(Collectors.toSet());

        // 查询有权限的菜单
        List<MenuPermissionDetails> menuPermissionDetailsList = menuPermissionDetailsMapper.selectAll();
        if (CollectionUtils.isEmpty(menuPermissionDetailsList)) {
            return rsMenuList;
        }
        Map<Integer, List<MenuPermissionDetails>> menuPermissionList = menuPermissionDetailsList.stream()
                .collect(Collectors.groupingBy(MenuPermissionDetails::getMenuId));
        // 遍历符合权限的菜单
        for (Menu menu : allMenuList) {
            if (menuPermissionList.containsKey(menu.getId())) {
                boolean addFlag = true;
                List<MenuPermissionDetails> tempList = menuPermissionList.get(menu.getId());
                for (MenuPermissionDetails temp : tempList) {
                    if (!userPermissionSet.contains(temp.getPermissionId())) {
                        addFlag = false;
                        break;
                    }
                }
                if (addFlag) {
                    rsMenuList.add(menu);
                }
            }
        }
        return rsMenuList;
    }


    private List<MenuDto> buildMenuListToTree(List<Menu> menuList) {
        List<MenuDto> menuDtoeList = new ArrayList<MenuDto>();
        // 迭代取所有父级节点
        if (!CollectionUtils.isEmpty(menuList)) {
            List<MenuDto> tempList = new ArrayList<MenuDto>();
            menuList.forEach(entity -> {
                MenuDto menuDto = new MenuDto();
                BeanUtils.copyProperties(entity, menuDto);
                tempList.add(menuDto);
            });
            menuDtoeList = this.buildMenuDtoToTree(tempList);
        }
        return menuDtoeList;
    }

    private List<MenuDto> buildMenuDtoToTree(List<MenuDto> list) {
        List<MenuDto> roots = new ArrayList<MenuDto>();
        if (!CollectionUtils.isEmpty(list)) {
            roots = findRoots(list);
            List<MenuDto> notRoots = this.removeListFromList(list, roots);
            for (MenuDto root : roots) {
                root.setChildren(findChildren(root, notRoots));
            }
        }
        return roots;
    }

    private List<MenuDto> findRoots(List<MenuDto> allNodes) {
        List<MenuDto> results = new ArrayList<MenuDto>();
        for (MenuDto node : allNodes) {
            if (null == node.getParentId() || 0 == node.getParentId()) {
                results.add(node);
            }
        }
        return results;
    }

    private List<MenuDto> findChildren(MenuDto root, List<MenuDto> notRoots) {
        List<MenuDto> children = new ArrayList<MenuDto>();

        for (MenuDto comparedOne : notRoots) {
            if (comparedOne.getParentId().intValue() == root.getId().intValue()) {
                children.add(comparedOne);
            }
        }
        List<MenuDto> notChildren = this.removeListFromList(notRoots, children);
        for (MenuDto child : children) {
            List<MenuDto> tmpChildren = findChildren(child, notChildren);
            child.setChildren(tmpChildren);
        }
        return children;
    }

    private List<Menu> completeMenu(List<Menu> allMenuList, List<Menu> menuPermissionList) {
        Map<Integer, Menu> allMenuMap = allMenuList.stream().collect(Collectors.toMap(Menu::getId, a -> a, (k1, k2) -> k1));
        Set<Integer> targetMenuIdSet = menuPermissionList.stream().map(Menu::getId).collect(Collectors.toSet());
        List<Menu> targetMenuList = BeanCopyUtils.copyList(menuPermissionList, Menu.class);
        for (Menu menu : menuPermissionList) {
            this.addParentMenu(targetMenuList, targetMenuIdSet, menu, allMenuMap);
        }
        return targetMenuList;
    }

    private void addParentMenu(List<Menu> targetMenuList, Set<Integer> targetMenuIdSet, Menu menu, Map<Integer, Menu> allMenuMap) {
        if (menu.getParentId() != null && 0 != menu.getParentId().intValue() && !targetMenuIdSet.contains(menu.getParentId())) {
            Menu parentMenu = allMenuMap.get(menu.getParentId());
            targetMenuList.add(parentMenu);
            targetMenuIdSet.add(parentMenu.getId());
            this.addParentMenu(targetMenuList, targetMenuIdSet, parentMenu, allMenuMap);
        }
    }

    private void sortMenu(List<Menu> menuList) {
        Collections.sort(menuList, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                String orderNo1 = o1.getOrderNo();
                String orderNo2 = o2.getOrderNo();
                return orderNo1.compareToIgnoreCase(orderNo2);
            }
        });
    }

    private List<MenuDto> removeListFromList(List<MenuDto> list, List<MenuDto> rootList) {
        if (CollectionUtils.isEmpty(list) || CollectionUtils.isEmpty(rootList)) {
            return list;
        }
        Set<Integer> idSet = rootList.stream().map(MenuDto::getId).collect(Collectors.toSet());

        List<MenuDto> rsList = list.stream().filter(menuResponse -> !idSet.contains(menuResponse.getId()))
                .collect(Collectors.toList());
        return rsList;
    }
}
