package com.yodoo.megalodon.permission.api;

import com.yodoo.megalodon.permission.dto.MenuDto;
import com.yodoo.megalodon.permission.service.MenuService;
import com.yodoo.megalodon.permission.service.MenuTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author houzhen
 * @Date 14:42 2019/8/8
**/
@Component
public class MenuManagerApi {

    @Autowired
    private MenuTreeService menuTreeService;

    @Autowired
    private MenuService menuService;

    /**
     * 查询菜单树
     * @Author houzhen
     * @Date 14:46 2019/8/8
    **/
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<MenuDto> queryMenuTree(){
        return menuTreeService.getAllMenuTree();
    }

    /**
     * 新增菜单
     * @Author houzhen
     * @Date 14:47 2019/8/8
    **/
    public void addMenu(MenuDto menuDto) {
        menuService.addMenu(menuDto);
    }

    /**
     * 更新菜单
     * @Author houzhen
     * @Date 14:48 2019/8/8
    **/
    public void updateMenu(MenuDto menuDto) {
        menuService.updateMenu(menuDto);
    }

    /**
     * 删除菜单
     * @Author houzhen
     * @Date 14:49 2019/8/8
    **/
    public void deleteMenu(MenuDto menuDto) {
        menuService.deleteMenu(menuDto);
    }
}
