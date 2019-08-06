package com.yodoo.megalodon.permission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.contract.PermissionConstants;
import com.yodoo.megalodon.permission.dto.UserDto;
import com.yodoo.megalodon.permission.entity.User;
import com.yodoo.megalodon.permission.enums.UserSexEnum;
import com.yodoo.megalodon.permission.enums.UserStatusEnum;
import com.yodoo.megalodon.permission.exception.BundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.UserMapper;
import com.yodoo.megalodon.permission.util.Md5Util;
import com.yodoo.megalodon.permission.util.RequestPrecondition;
import com.yodoo.megalodon.permission.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Date 2019/7/26 11:49
 * @Created by houzhen
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserGroupDetailsService userGroupDetailsService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    /**
     * 根据账号查询用户
     *
     * @Author houzhen
     * @Date 13:09 2019/7/26
     **/
    public User getUserByAccount(String account) {
        logger.info("UserService.getUserByAccount account:{}", account);
        // 验证参数
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(account));
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("account", account);
        example.and(criteria);
        return userMapper.selectOneByExample(example);
    }

    /**
     * 条件分页查询
     * @param userDto
     * @return
     */
    public PageInfoDto<UserDto> queryUserList(UserDto userDto) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        // 账号
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(userDto.getAccount())){
            criteria.andEqualTo("account",userDto.getAccount());
        }
        // 用户名称
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(userDto.getName())){
            criteria.andEqualTo("name",userDto.getName());
        }
        // 邮箱
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(userDto.getEmail())){
            criteria.andEqualTo("email",userDto.getEmail());
        }
        // 区域
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(userDto.getRegion())){
            criteria.andEqualTo("region",userDto.getRegion());
        }
        // 岗位
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(userDto.getPost())){
            criteria.andEqualTo("post",userDto.getPost());
        }
        // 性别：0 没指定性别，1 男， 2 女
        if (userDto.getSex() != null){
            criteria.andEqualTo("sex",userDto.getSex());
        }
        // 电话
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(userDto.getPhone())){
            criteria.andEqualTo("phone",userDto.getPhone());
        }
        Page<?> pages = PageHelper.startPage(userDto.getPageNum(), userDto.getPageSize());
        List<UserDto> collect = getUserDtoByExample(example);
        return new PageInfoDto<UserDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 添加用户
     * @param userDto
     */
    public Integer addUser(UserDto userDto) {
        // 参数校验
        addUserParameterCheck(userDto);

        // 插入用户数据
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setStatus(UserStatusEnum.USE.getCode());
        user.setPassword(Md5Util.md5Encode(PermissionConstants.DEFAULT_PASSWORD));
        Integer insertUserResponseCount = userMapper.insertSelective(user);

        // 如果用户有所属用户组，更新用户组详情
        if (!CollectionUtils.isEmpty(userDto.getUserGroupIds()) && insertUserResponseCount != null && insertUserResponseCount > 0){
            updateUserGroup(user.getId(), userDto.getUserGroupIds());
        }

        // 用户权限不为空，更新用户权限详情
        if (!CollectionUtils.isEmpty(userDto.getPermissionIds()) && insertUserResponseCount != null && insertUserResponseCount > 0){
            updateUserPermissionDetails(user.getId(), userDto.getPermissionIds());
        }
        return insertUserResponseCount;
    }

    /**
     * 修改用户
     * @param userDto
     */
    public Integer editUser(UserDto userDto) {
        // 参数校验
        User user = editUserParameterCheck(userDto);

        // 封装修改参数
        copyProperties(user, userDto);
        Integer updateUserResponseCount = userMapper.updateByPrimaryKeySelective(user);

        // 如果用户有所属用户组，更新用户组详情
        if (!CollectionUtils.isEmpty(userDto.getUserGroupIds()) && updateUserResponseCount != null && updateUserResponseCount > 0){
            updateUserGroup(user.getId(), userDto.getUserGroupIds());
        }

        // 用户权限不为空，更新用户权限详情
        if (!CollectionUtils.isEmpty(userDto.getPermissionIds()) && updateUserResponseCount != null && updateUserResponseCount > 0){
            updateUserPermissionDetails(user.getId(), userDto.getPermissionIds());
        }

        return updateUserResponseCount;
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    public Integer resetUserPassword(Integer id) {
        if (id == null || id < 0){
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        User user = checkUserExistById(id);
        user.setPassword(Md5Util.md5Encode(PermissionConstants.DEFAULT_PASSWORD));
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 启用和停用
     * @param id
     * @param status ：0：启用 1：停用
     */
    public Integer updateUserStatus(Integer id, Integer status) {
        if (id == null || id < 0 || !Arrays.asList(UserStatusEnum.USE.getCode(), UserStatusEnum.STOP.getCode()).contains(status)){
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        User user = checkUserExistById(id);
        user.setStatus(status);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询除ids 以外的用户
     * @param userIdsListSet
     * @return
     */
    public List<UserDto> selectUserNotInIds(Set<Integer> userIdsListSet) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotIn("id",userIdsListSet);
       return getUserDtoByExample(example);
    }

    /**
     * 条件查询返回列表
     * @param example
     * @return
     */
    private List<UserDto> getUserDtoByExample(Example example) {
        List<UserDto> userDtoList = new ArrayList<>();
        List<User> users = userMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(users)){
            userDtoList = users.stream()
                    .filter(Objects::nonNull)
                    .map(user -> {
                        UserDto userDto = new UserDto();
                        BeanUtils.copyProperties(user, userDto);
                        return userDto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return userDtoList;
    }

    /**
     * 修改参数
     * @param user
     * @param userDto
     */
    private void copyProperties(User user, UserDto userDto) {
        // 父id
        if (userDto.getParentId() != null && userDto.getParentId() > 0){
            user.setParentId(userDto.getParentId());
        }
        // 用户名称
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(userDto.getName())){
            user.setName(userDto.getName());
        }
        // 邮箱
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(userDto.getEmail())){
            user.setEmail(userDto.getEmail());
        }
        // 区域
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(userDto.getRegion())){
            user.setRegion(userDto.getRegion());
        }
        // 岗位
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(userDto.getPost())){
            user.setPost(userDto.getPost());
        }
        // 性别：0 没指定性别，1 男， 2 女
        if (userDto.getSex() != null && userDto.getSex() > 0){
            userDto.setSex(userDto.getSex());
        }
        // 出生日期
        if (userDto.getBirthday() != null){
            user.setBirthday(userDto.getBirthday());
        }
        // 电话
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(userDto.getPhone())){
            user.setPhone(userDto.getPhone());
        }
    }

    /**
     * 添加用户组详情数据
     * @param userId
     * @param userGroupIds
     */
    private void updateUserGroup(Integer userId, Set<Integer> userGroupIds) {
       userGroupDetailsService.deleteUserGroupByUserId(userId);
       userGroupDetailsService.insertUserGroupDetails(userId, userGroupIds);
    }

    /**
     * 添加用户权限详情
     * @param userId
     * @param permissionIds
     */
    private void updateUserPermissionDetails(Integer userId, Set<Integer> permissionIds) {
        userPermissionDetailsService.deleteUserPermissionDetailsByUserId(userId);
        userPermissionDetailsService.insertUserPermissionDetails(userId, permissionIds);
    }

    /**
     * 添加参数校验
     * @param userDto
     */
    private void addUserParameterCheck(UserDto userDto) {
        if (userDto == null || org.apache.commons.lang3.StringUtils.isBlank(userDto.getAccount()) || org.apache.commons.lang3.StringUtils.isBlank(userDto.getName())
                || org.apache.commons.lang3.StringUtils.isBlank(userDto.getEmail()) || org.apache.commons.lang3.StringUtils.isBlank(userDto.getPhone())){
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 邮件和电话校验,权限，权限组，权限
        parameterCheck(userDto);

        // 账号是否存在
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("account", userDto.getAccount());
        User userByAccount = userMapper.selectOneByExample(example);
        if (userByAccount != null){
            throw new PermissionException(BundleKey.USER_ALREADY_EXIST, BundleKey.USER_ALREADY_EXIST_MSG);
        }
    }

    /**
     * 修改用户参数校验
     * @param userDto
     * @return
     */
    private User editUserParameterCheck(UserDto userDto) {
        if (userDto == null || userDto.getId() == null || userDto.getId() < 0 || org.apache.commons.lang3.StringUtils.isBlank(userDto.getName())
                || org.apache.commons.lang3.StringUtils.isBlank(userDto.getEmail()) || org.apache.commons.lang3.StringUtils.isBlank(userDto.getPhone())){
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }

        // 邮件、电话、性别、用户组、权限校验
        parameterCheck(userDto);

        // 不存在不修改
        return checkUserExistById(userDto.getId());
    }

    /**
     * 确认用户是否存在
     * @param id
     * @return
     */
    private User checkUserExistById(Integer id) {
        User user = selectByPrimaryKey(id);
        if (user == null){
            throw new PermissionException(BundleKey.USER_NOT_EXIST, BundleKey.USER_NOT_EXIST_MSG);
        }
        return user;
    }

    /**
     * 邮件、电话校验
     * @param userDto
     */
    private void parameterCheck(UserDto userDto) {
        // 性别校验
        if (userDto.getSex() != null && !Arrays.asList(UserSexEnum.MAN.getCode(), UserSexEnum.GIRL.getCode(), UserSexEnum.SEXLESS.getCode()).contains(userDto.getSex())){
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }

        // 邮件是否正确
        if (!Pattern.compile(PermissionConstants.EMAIL_SERVER_MAILBOX_REGULAR_EXPRESSION).matcher(userDto.getEmail()).matches()){
            throw new PermissionException(BundleKey.EMAIL_FORMAT_ERROR, BundleKey.EMAIL_FORMAT_ERROR_MSG);
        }

        // 电话号码是否正确
        if (userDto.getPhone().length() != PermissionConstants.PHONE_LENGTH){
            throw new PermissionException(BundleKey.PHONE_FORMAT_ERROR, BundleKey.PHONE_FORMAT_ERROR_MSG);
        }

        // 如果用户组不为空,查询用户组不存在，不操作
        if (!CollectionUtils.isEmpty(userDto.getUserGroupIds())){
            Long userGroupNoExistCount = userGroupService.selectUserGroupNoExistCountByIds(userDto.getUserGroupIds());
            if (userGroupNoExistCount != null && userGroupNoExistCount > 0){
                throw new PermissionException(BundleKey.USER_GROUP_NOT_EXIST, BundleKey.USER_GROUP_NOT_EXIST_MSG);
            }
        }

        // 通过权限 id 查询，统计不存在的数量
        if (!CollectionUtils.isEmpty(userDto.getPermissionIds())){
            Long permissionNoExistCount = permissionService.selectPermissionNoExistCountByIds(userDto.getPermissionIds());
            if (permissionNoExistCount != null && permissionNoExistCount > 0){
                throw new PermissionException(BundleKey.PERMISSION_NOT_EXIST, BundleKey.PERMISSION_NOT_EXIST_MSG);
            }
        }
    }
}
