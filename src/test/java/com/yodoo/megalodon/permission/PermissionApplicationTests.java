package com.yodoo.megalodon.permission;

import com.yodoo.megalodon.permission.api.MenuManagerApi;
import com.yodoo.megalodon.permission.api.PermissionManagerApi;
import com.yodoo.megalodon.permission.api.UserManagerApi;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.MenuDto;
import com.yodoo.megalodon.permission.dto.PermissionDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PermissionConfig.class })
public class PermissionApplicationTests {

    private static Logger logger = LoggerFactory.getLogger(PermissionApplicationTests.class);

    @Autowired
    private MenuManagerApi menuManagerApi;
    
    @Autowired
    private PermissionManagerApi permissionManagerApi;
    
    @Autowired
    private UserManagerApi userManagerApi;

    /**
     * 条件分页查询菜单
     */
    @Test
    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
    public void queryMenuList(){
        PageInfoDto<MenuDto> menuDtoPageInfoDto = menuManagerApi.queryMenuList(new MenuDto());
        if (menuDtoPageInfoDto != null && !CollectionUtils.isEmpty(menuDtoPageInfoDto.getList())){
            menuDtoPageInfoDto.getList().stream()
                    .forEach(menuDto -> {
                        logger.info("query menu list message: menuCode => {}, menuName => {}",menuDto.getMenuCode(),menuDto.getMenuName());
                    });
        }
    }

    /**
     * 查询菜单树
     * @Author houzhen
     * @Date 14:46 2019/8/8
     **/
    @Test
    @WithMockUser(username = "99", password = "admin", authorities = {"user_manage","permission_manage"})
    public void getAllMenuTree(){
        List<MenuDto> allMenuTree = menuManagerApi.getAllMenuTree();
        if (!CollectionUtils.isEmpty(allMenuTree)){
            allMenuTree.stream()
                    .forEach(menuDto -> {
                        logger.info("query menu list message: menuCode => {}, menuName => {}",menuDto.getMenuCode(),menuDto.getMenuName());
                    });
        }
    }

    /**
     * 新增菜单
     **/
    @Test
    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
    public void addMenu() {
        MenuDto menuDto = new MenuDto();
        menuDto.setParentId(0);
        menuDto.setMenuCode("test_001");
        menuDto.setMenuName("test_001");
        menuDto.setMenuTarget("/jinjun_test");
        menuDto.setOrderNo("10000.01");
        menuDto.setPermissionIdList(Arrays.asList(1,2,3));
//        menuManagerApi.addMenu(menuDto);
    }

    /**
     * 更新菜单
     * @Author houzhen
     * @Date 14:48 2019/8/8
     **/
    @Test
    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
    public void updateMenu() {
        MenuDto menuDto = new MenuDto();
        menuDto.setId(9);
        menuDto.setParentId(0);
        menuDto.setMenuCode("test_001");
        menuDto.setMenuName("test_001");
        menuDto.setMenuTarget("jinjun_test");
        menuDto.setOrderNo("10000.01");
        menuDto.setPermissionIdList(Arrays.asList(1,2,3));

//        menuManagerApi.updateMenu(menuDto);
    }

    /**
     * 删除菜单
     **/
    @Test
    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
    public void deleteMenu() {
        Integer id = 9;
//        menuManagerApi.deleteMenu(id);
    }

    /**
     * 根据用户id查询菜单树
     */
    @Test
    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
    public void getMenuTree(){
        Integer userId = 99;
        List<MenuDto> menuTree = menuManagerApi.getMenuTree(userId);
        if (!CollectionUtils.isEmpty(menuTree)){
            menuTree.stream()
                    .forEach(menuDto -> {
                        logger.info("query menu list message: menuCode => {}, menuName => {}",menuDto.getMenuCode(),menuDto.getMenuName());
                    });
        }

    }

    @Test
    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
    public void test(){
//        PageInfoDto<PermissionDto> permissionDtoPageInfoDto = permissionManagerApi.queryPermissionList(new PermissionDto());
//        if (permissionDtoPageInfoDto != null && !CollectionUtils.isEmpty(permissionDtoPageInfoDto.getList())){
//            permissionDtoPageInfoDto.getList().stream()
//                    .filter(Objects::nonNull)
//                    .forEach(permissionDto -> {
//                        logger.info("permission message: permissionCode => {}, permissionName => {}",permissionDto.getPermissionCode(),permissionDto.getPermissionName());
//                    });
//        }
    }
}
