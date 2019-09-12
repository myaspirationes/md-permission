//package com.yodoo.megalodon.permission;
//
//import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
//import com.yodoo.feikongbao.provisioning.domain.system.dto.GroupsDto;
//import com.yodoo.megalodon.permission.api.MenuManagerApi;
//import com.yodoo.megalodon.permission.api.PermissionManagerApi;
//import com.yodoo.megalodon.permission.api.UserManagerApi;
//import com.yodoo.megalodon.permission.common.PageInfoDto;
//import com.yodoo.megalodon.permission.config.PermissionConfig;
//import com.yodoo.megalodon.permission.dto.*;
//import com.yodoo.megalodon.permission.entity.Permission;
//import com.yodoo.megalodon.permission.entity.SearchCondition;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.util.CollectionUtils;
//
//import java.util.*;
//
////@RunWith(SpringJUnit4ClassRunner.class)
////@ContextConfiguration(classes = {PermissionConfig.class })
//public class PermissionApplicationTests {
//
//    private static Logger logger = LoggerFactory.getLogger(PermissionApplicationTests.class);
//
//    @Autowired
//    private MenuManagerApi menuManagerApi;
//
//    @Autowired
//    private PermissionManagerApi permissionManagerApi;
//
//    @Autowired
//    private UserManagerApi userManagerApi;
//
//    //////////////////////////////////////  menuManagerApi 开始 //////////////////////////////////////////////////////////////
//    /**
//     * 条件分页查询菜单
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void queryMenuList(){
////        PageInfoDto<MenuDto> menuDtoPageInfoDto = menuManagerApi.queryMenuList(new MenuDto());
//        PageInfoDto<MenuDto> menuDtoPageInfoDto = null;
//        if (menuDtoPageInfoDto != null && !CollectionUtils.isEmpty(menuDtoPageInfoDto.getList())){
//            menuDtoPageInfoDto.getList().stream()
//                    .forEach(menuDto -> {
//                        logger.info("query menu list message: menuCode => {}, menuName => {}",menuDto.getMenuCode(),menuDto.getMenuName());
//                    });
//        }
//    }
//
//    /**
//     * 查询菜单树
//     **/
//    @Test
//    @WithMockUser(username = "99", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void getAllMenuTree(){
////        List<MenuDto> allMenuTree = menuManagerApi.getAllMenuTree();
//        List<MenuDto> allMenuTree = null;
//        if (!CollectionUtils.isEmpty(allMenuTree)){
//            allMenuTree.stream()
//                    .forEach(menuDto -> {
//                        logger.info("query menu list message: menuCode => {}, menuName => {}",menuDto.getMenuCode(),menuDto.getMenuName());
//                    });
//        }
//    }
//
//    /**
//     * 新增菜单
//     **/
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void addMenu() {
//        MenuDto menuDto = new MenuDto();
//        menuDto.setParentId(0);
//        menuDto.setMenuCode("test_002");
//        menuDto.setMenuName("test_002");
//        menuDto.setMenuTarget("/jinjun_test");
//        menuDto.setOrderNo("10000.01");
//        menuDto.setPermissionIdList(Arrays.asList(1,2,3));
//        // menuManagerApi.addMenu(menuDto);
//    }
//
//    /**
//     * 更新菜单
//     **/
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void updateMenu() {
//        MenuDto menuDto = new MenuDto();
//        menuDto.setId(22);
//        menuDto.setParentId(0);
//        menuDto.setMenuCode("test_007");
//        menuDto.setMenuName("test_007");
//        menuDto.setMenuTarget("jinjun_test");
//        menuDto.setOrderNo("10000.01");
//        menuDto.setPermissionIdList(Arrays.asList(1,2,3));
//
//       // menuManagerApi.updateMenu(menuDto);
//    }
//
//    /**
//     * 删除菜单
//     **/
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void deleteMenu() {
//        Integer id = 21;
//        // menuManagerApi.deleteMenu(id);
//    }
//
//    /**
//     * 根据用户id查询菜单树
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void getMenuTree(){
//        Integer userId = 1;
////        List<MenuDto> menuTree = menuManagerApi.getMenuTree(userId);
//        List<MenuDto> menuTree = null;
//        if (!CollectionUtils.isEmpty(menuTree)){
//            menuTree.stream()
//                    .forEach(menuDto -> {
//                        logger.info("query menu list message: menuCode => {}, menuName => {}",menuDto.getMenuCode(),menuDto.getMenuName());
//                    });
//        }
//
//    }
//
//    //////////////////////////////////////  menuManagerApi 结束 //////////////////////////////////////////////////////////////
//
//
//    //////////////////////////////////////  permissionManagerApi 开始 //////////////////////////////////////////////////////////////
//
//    /**
//     * 在用户列表中点击权限,获取目标集团、目标公司、目标用户的list
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void actionPermissionInUserList(){
//        Integer userId = 1;
////        ActionPermissionInUserListDto actionPermissionInUserListDto = permissionManagerApi.actionPermissionInUserList(userId);
//        ActionPermissionInUserListDto actionPermissionInUserListDto = null;
//        if (actionPermissionInUserListDto != null && !CollectionUtils.isEmpty(actionPermissionInUserListDto.getUserPermissionTargetCompanyDetailsDtoList())){
//            actionPermissionInUserListDto.getUserPermissionTargetCompanyDetailsDtoList().stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("action user permission target company details message : userId => {}, permissionId => {}",dto.getUserId(),dto.getPermissionId());
//                    });
//        }
//        if (actionPermissionInUserListDto != null && !CollectionUtils.isEmpty(actionPermissionInUserListDto.getUserPermissionTargetGroupDetailsDtoList())){
//            actionPermissionInUserListDto.getUserPermissionTargetGroupDetailsDtoList().stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("action user permission target company details message : userId => {}, permissionId => {}",dto.getUserId(),dto.getPermissionId());
//                    });
//        }
//        if (actionPermissionInUserListDto != null && !CollectionUtils.isEmpty(actionPermissionInUserListDto.getUserPermissionTargetUserDetailsDtoList())){
//            actionPermissionInUserListDto.getUserPermissionTargetUserDetailsDtoList().stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("action user permission target company details message : userId => {}, permissionId => {}",dto.getUserId(),dto.getPermissionId());
//                    });
//        }
//    }
//
//    /**
//     * 根据用户id查询已管理的目标集团
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void getTargetGroupsByUserId(){
//        Integer userId = 1;
////        List<GroupsDto> dtoList = permissionManagerApi.getTargetGroupsByUserId(userId);
//        List<GroupsDto> dtoList = null;
//        if (!CollectionUtils.isEmpty(dtoList)){
//            dtoList.stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("get target groups message : groupName => {}, groupCode => {}",dto.getGroupName(),dto.getGroupCode());
//                    });
//        }
//    }
//
//    /**
//     * 通过用户id查询可管理的目标集团
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void getAvailableGroupsByUserId(){
//        Integer userId = 1;
////        List<GroupsDto> dtoList = permissionManagerApi.getAvailableGroupsByUserId(userId);
//        List<GroupsDto> dtoList = null;
//        if (!CollectionUtils.isEmpty(dtoList)){
//            dtoList.stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("get available groups message : groupName => {}, groupCode => {}",dto.getGroupName(),dto.getGroupCode());
//                    });
//        }
//    }
//
//    /**
//     * 变更用户管理目标集团 todo
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void updateUserPermissionTargetGroups(){
//        UserPermissionTargetDto userPermissionTargetDto = new UserPermissionTargetDto();
//        userPermissionTargetDto.setUserId(1);
//        userPermissionTargetDto.setPermissionId(1);
//        Set<Integer> targetIds = new HashSet<>();
//        targetIds.add(1);
//        userPermissionTargetDto.setTargetIds(targetIds);
//        // permissionManagerApi.updateUserPermissionTargetGroups(userPermissionTargetDto);
//    }
//
//    /**
//     * 通过用户id查询已管理的目标公司
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void getUserManageTargetCompanyListByUserId(){
//        Integer userId = 1;
////        List<CompanyDto> dtoList = permissionManagerApi.getUserManageTargetCompanyListByUserId(userId);
//        List<CompanyDto> dtoList = null;
//        if (!CollectionUtils.isEmpty(dtoList)){
//            dtoList.stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("get user manage target company list message : companyName => {}, companyCode => {}",dto.getCompanyName(),dto.getCompanyCode());
//                    });
//        }
//    }
//
//    /**
//     * 通过用户id查询可管理的目标公司
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void getAvailableUserManageTargetCompanyListByUserId(){
//        Integer userId = 1;
////        List<CompanyDto> dtoList = permissionManagerApi.getAvailableUserManageTargetCompanyListByUserId(userId);
//        List<CompanyDto> dtoList = null;
//        if (!CollectionUtils.isEmpty(dtoList)){
//            dtoList.stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("get user manage target company list message : companyName => {}, companyCode => {}",dto.getCompanyName(),dto.getCompanyCode());
//                    });
//        }
//    }
//
//    /**
//     * 更新用户管理目标公司数据 todo
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void updateUserPermissionTargetCompany(){
//        UserPermissionTargetDto userPermissionTargetDto = new UserPermissionTargetDto();
//        userPermissionTargetDto.setUserId(1);
//        userPermissionTargetDto.setPermissionId(1);
//        Set<Integer> targetIds = new HashSet<>();
//        targetIds.add(5);
//        userPermissionTargetDto.setTargetIds(targetIds);
////        permissionManagerApi.updateUserPermissionTargetCompany(userPermissionTargetDto);
//    }
//
//    /**
//     * 通过用户id查询已管理的目标用户
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void selectUserManageTargetUserListByUserId(){
//        Integer userId = 1;
////        List<UserDto> dtoList = permissionManagerApi.selectUserManageTargetUserListByUserId(userId);
//        List<UserDto> dtoList = null;
//        if (!CollectionUtils.isEmpty(dtoList)){
//            dtoList.stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("get user manage target user list message : account => {}, name => {}",dto.getAccount(),dto.getName());
//                    });
//        }
//    }
//
//    /**
//     * 通过用户id查询可管理的目标用户
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void getAvailableUserManageTargetUserListByUserId(){
//        Integer userId = 1;
////        List<UserDto> dtoList = permissionManagerApi.getAvailableUserManageTargetUserListByUserId(userId);
//        List<UserDto> dtoList = null;
//        if (!CollectionUtils.isEmpty(dtoList)){
//            dtoList.stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("get available user manage target user list message : account => {}, name => {}",dto.getAccount(),dto.getName());
//                    });
//        }
//    }
//
//    /**
//     * 更新用户管理目标用户数据 todo
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void updateUserPermissionTargetUser(){
//        UserPermissionTargetDto userPermissionTargetDto = new UserPermissionTargetDto();
//        userPermissionTargetDto.setUserId(1);
//        userPermissionTargetDto.setPermissionId(1);
//        Set<Integer> targetIds = new HashSet<>();
//        targetIds.add(1);
//        userPermissionTargetDto.setTargetIds(targetIds);
////         permissionManagerApi.updateUserPermissionTargetUser(userPermissionTargetDto);
//    }
//
//    /**
//     * 条件分页查询权限列表
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void queryPermissionList(){
//        PermissionDto permissionDto = new PermissionDto();
////        PageInfoDto<PermissionDto> pageInfoDto = permissionManagerApi.queryPermissionList(permissionDto);
//        PageInfoDto<PermissionDto> pageInfoDto = null;
//        if (pageInfoDto != null && !CollectionUtils.isEmpty(pageInfoDto.getList())){
//            pageInfoDto.getList().stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("query permission list message : permissionCode => {}, permissionName => {}",dto.getPermissionCode(),dto.getPermissionName());
//                    });
//        }
//    }
//
//    /**
//     * 添加权限
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void addPermission() {
//        PermissionDto permissionDto = new PermissionDto();
//        permissionDto.setPermissionCode("test");
//        permissionDto.setPermissionName("test");
//        // permissionManagerApi.addPermission(permissionDto);
//    }
//
//    /**
//     * 修改权限
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void updatePermission() {
//        PermissionDto permissionDto = new PermissionDto();
//        permissionDto.setId(14);
//        permissionDto.setPermissionCode("test8");
//        permissionDto.setPermissionName("test8");
//        // permissionManagerApi.updatePermission(permissionDto);
//    }
//
//    /**
//     * 删除权限
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void deletePermission(){
//        Integer id = 14;
//        // permissionManagerApi.deletePermission(id);
//    }
//
//    /**
//     * 根据userId查询权限
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void getPermissionByUserId() {
//        Integer userId = 1;
////        List<Permission> list = permissionManagerApi.getPermissionByUserId(userId);
//        List<Permission> list = null;
//        if (!CollectionUtils.isEmpty(list)){
//            list.stream()
//                    .filter(Objects::nonNull)
//                    .forEach(permission -> {
//                        logger.info("get permission by user id message : permissionCode => {}, permissionName => {}",permission.getPermissionCode(),permission.getPermissionName());
//                    });
//        }
//    }
//
//    /**
//     * 条件分页查询权限组列表
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void queryPermissionGroupList(){
//        PermissionGroupDto permissionGroupDto = new PermissionGroupDto();
////        PageInfoDto<PermissionGroupDto> pageInfoDto = permissionManagerApi.queryPermissionGroupList(permissionGroupDto);
//        PageInfoDto<PermissionGroupDto> pageInfoDto = null;
//        if (pageInfoDto != null && !CollectionUtils.isEmpty(pageInfoDto.getList())){
//            pageInfoDto.getList().stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("get permission by user id message : groupCode => {}, groupName => {}",dto.getGroupCode(),dto.getGroupName());
//                    });
//        }
//    }
//
//    /**
//     * 添加权限组：
//     * 1、选权限
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void addPermissionGroup() {
//        PermissionGroupDto permissionGroupDto = new PermissionGroupDto();
//        permissionGroupDto.setGroupCode("test");
//        permissionGroupDto.setGroupName("test");
//        //permissionManagerApi.addPermissionGroup(permissionGroupDto);
//    }
//
//    /**
//     * 更新权限组：
//     * 1、更新权限
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void editPermissionGroup() {
//        PermissionGroupDto permissionGroupDto = new PermissionGroupDto();
//        permissionGroupDto.setId(99);
//        permissionGroupDto.setGroupCode("test");
//        permissionGroupDto.setGroupName("test");
//        //permissionManagerApi.editPermissionGroup(permissionGroupDto);
//    }
//
//    /**
//     * 删除权限组
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void deletePermissionGroup() {
//        Integer id = 99;
//        // permissionManagerApi.deletePermissionGroup(id);
//    }
//
//    /**
//     * 查询权限组详情
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void getPermissionGroupDetails() {
//        Integer id = 1;
////        PermissionGroupDto dto = permissionManagerApi.getPermissionGroupDetails(id);
//        PermissionGroupDto dto = null;
//        if (dto != null){
//            logger.info("get permission group details message : groupCode => {}, groupName => {}",dto.getGroupCode(),dto.getGroupName());
//        }
//    }
//
//    /**
//     * 条件分页查询 条件查询表
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void queryUserGroupList(){
//        SearchConditionDto searchConditionDto = new SearchConditionDto();
////        PageInfoDto<SearchConditionDto> pageInfoDto = permissionManagerApi.queryUserGroupList(searchConditionDto);
//        PageInfoDto<SearchConditionDto> pageInfoDto = null;
//        if (pageInfoDto != null && !CollectionUtils.isEmpty(pageInfoDto.getList())){
//            pageInfoDto.getList().stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("get permission group details message : conditionName => {}, conditionCode => {}, conditionValue => {}",dto.getConditionCode(),dto.getConditionName(),dto.getConditionValue());
//                    });
//        }
//    }
//
//    /**
//     * 添加 条件查询表
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void addSearchCondition(){
//        SearchConditionDto searchConditionDto = new SearchConditionDto();
//        searchConditionDto.setConditionName("test");
//        searchConditionDto.setConditionCode("test");
//        searchConditionDto.setConditionValue("test");
//        //permissionManagerApi.addSearchCondition(searchConditionDto);
//    }
//
//    /**
//     * 更新条件查询表
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void editSearchCondition(){
//        SearchConditionDto searchConditionDto = new SearchConditionDto();
//        searchConditionDto.setId(99);
//        searchConditionDto.setConditionName("test");
//        searchConditionDto.setConditionCode("test");
//        searchConditionDto.setConditionValue("test");
//        //permissionManagerApi.editSearchCondition(searchConditionDto);
//    }
//
//    /**
//     * 删除条件查询表
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void deleteSearchCondition(){
//        Integer id = 99;
//        //permissionManagerApi.deleteSearchCondition(id);
//    }
//
//    /**
//     * 通过权限组id 查询
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void selectPermissionGroupDetailsCountByPermissionGroupId(){
//        Integer permissionGroupId = 1;
////        Integer count = permissionManagerApi.selectPermissionGroupDetailsCountByPermissionGroupId(permissionGroupId);
//        Integer count = null;
//        if (count != null){
//            logger.info("get permission group details count message : count => {}",count);
//        }
//    }
//
//    /**
//     * 查询所有条件
//     */
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void getAllSearchCondition(){
////        List<SearchCondition> searchConditionList = permissionManagerApi.getAllSearchCondition();
//        List<SearchCondition> searchConditionList = null;
//        if (!CollectionUtils.isEmpty(searchConditionList)){
//            searchConditionList.stream()
//                    .filter(Objects::nonNull)
//                    .forEach(dto -> {
//                        logger.info("get permission group details message : conditionName => {}, conditionCode => {}, conditionValue => {}",dto.getConditionCode(),dto.getConditionName(),dto.getConditionValue());
//                    });
//        }
//    }
//
//    //////////////////////////////////////  permissionManagerApi 结束 //////////////////////////////////////////////////////////////
//    @Test
//    @WithMockUser(username = "1", password = "admin", authorities = {"user_manage","permission_manage"})
//    public void  updatePassword(){
//        UserDto userDto = new UserDto();
//        userDto.setId(1);
//        userDto.setPassword("admin");
////        userManagerApi.updateUserPassword(userDto);
//    }
//}
