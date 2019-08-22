package com.yodoo.megalodon.permission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.GroupsDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.GroupsService;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.contract.PermissionConstants;
import com.yodoo.megalodon.permission.dto.UserDto;
import com.yodoo.megalodon.permission.entity.SearchCondition;
import com.yodoo.megalodon.permission.entity.User;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
import com.yodoo.megalodon.permission.enums.UserSexEnum;
import com.yodoo.megalodon.permission.enums.UserStatusEnum;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.UserMapper;
import com.yodoo.megalodon.permission.util.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Date 2019/7/26 11:49
 * @author by houzhen
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserGroupDetailsService userGroupDetailsService;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    @Autowired
    private UserPermissionTargetGroupDetailsService userPermissionTargetGroupDetailsService;

    @Autowired
    private UserPermissionTargetCompanyDetailsService userPermissionTargetCompanyDetailsService;

    @Autowired
    private UserPermissionTargetUserDetailsService userPermissionTargetUserDetailsService;

    @Autowired
    private GroupsService groupsService;

    /**
     * 根据账号查询用户
     *
     * @Author houzhen
     * @Date 13:09 2019/7/26
     **/
    public User getUserByAccount(String account) {
        logger.info("UserService.getUserByAccount account:{}", account);
        // 验证参数
        if (StringUtils.isBlank(account)){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("account", account);
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
        if (StringUtils.isNoneBlank(userDto.getAccount())){
            criteria.andEqualTo("account",userDto.getAccount());
        }
        // 用户名称
        if (StringUtils.isNoneBlank(userDto.getName())){
            criteria.andEqualTo("name",userDto.getName());
        }
        // 邮箱
        if (StringUtils.isNoneBlank(userDto.getEmail())){
            criteria.andEqualTo("email",userDto.getEmail());
        }
        // 区域
        if (StringUtils.isNoneBlank(userDto.getRegion())){
            criteria.andEqualTo("region",userDto.getRegion());
        }
        // 岗位
        if (StringUtils.isNoneBlank(userDto.getPost())){
            criteria.andEqualTo("post",userDto.getPost());
        }
        // 性别：0 没指定性别，1 男， 2 女
        if (userDto.getSex() != null){
            criteria.andEqualTo("sex",userDto.getSex());
        }
        // 电话
        if (StringUtils.isNoneBlank(userDto.getPhone())){
            criteria.andEqualTo("phone",userDto.getPhone());
        }
        Page<?> pages = PageHelper.startPage(userDto.getPageNum(), userDto.getPageSize());
        return new PageInfoDto<UserDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), getUserDtoByExample(example));
    }

    /**
     * 添加用户
     * @param userDto
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer addUser(UserDto userDto) {
        // 参数校验
        addUserParameterCheck(userDto);

        // 插入用户数据
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setStatus(UserStatusEnum.USE.getCode());
        user.setPassword(Md5Util.md5Encode(PermissionConstants.DEFAULT_PASSWORD));
        Integer insertUserResponseCount = userMapper.insertSelective(user);
        // 更新用户组表与用户关系表
        if (insertUserResponseCount != null && insertUserResponseCount > 0){
            userGroupDetailsService.updateUserGroupDetails(user.getId(), userDto.getUserGroupIds());
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
        if (updateUserResponseCount != null && updateUserResponseCount > 0){
            userGroupDetailsService.updateUserGroupDetails(user.getId(), userDto.getUserGroupIds());
        }
        return updateUserResponseCount;
    }

    /**
     * 修改密码
     * @param userDto
     * @return
     */
    public Integer updateUserPassword(UserDto userDto) {
        if (userDto == null || userDto.getId() == null || userDto.getId() < 0 || StringUtils.isBlank(userDto.getPassword())){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        User user = checkUserExistById(userDto.getId());
        user.setPassword(Md5Util.md5Encode(userDto.getPassword()));
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    public Integer resetUserPassword(Integer id) {
        if (id == null || id < 0){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
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
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        User user = checkUserExistById(id);
        user.setStatus(status);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 根据用户id查询目标集团
     * @Author houzhen
     * @Date 14:56 2019/8/6
     **/
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<GroupsDto> getTargetGroupsByUserId(Integer userId) {
        if (userId == null) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 通过用户id 查询用户权限表
        List<UserPermissionDetails> userPermissionIdList = userPermissionDetailsService.selectUserPermissionDetailsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionIdList)){
            // 通过用户id和权限id 查询用户管理目标集团，获取集团 ids
            Set<Integer> groupIds = userPermissionTargetGroupDetailsService.getGroupIdsByUserIdAndPermissionId(userPermissionIdList);
            if (!CollectionUtils.isEmpty(groupIds)){
                return groupIds.stream()
                        .filter(Objects::nonNull)
                        .map(groupId -> {
                            return groupsService.selectGroupById(groupId);
                        }).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }
        return null;
    }

    /**
     * 查询可选目标集团
     * @Author houzhen
     * @Date 9:43 2019/8/6
     **/
    public List<GroupsDto> getAvailableGroupsByUserId(Integer userId) {
        if (userId == null) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 公司 ids
        Set<Integer> groupsIdsListSet = new HashSet<>();
        // 通过用户id查询用户权限表
        List<UserPermissionDetails> userPermissionDetailsList = userPermissionDetailsService.selectUserPermissionDetailsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionDetailsList)){
            // 通过用户id和权限id 查询用户管理目标集团，获取集团 ids
            groupsIdsListSet = userPermissionTargetGroupDetailsService.getGroupIdsByUserIdAndPermissionId(userPermissionDetailsList);
        }
        return groupsService.selectGroupsNotInIds(groupsIdsListSet);
    }

    /**
     * 通过用户id查询用户已管理目标公司
     * @param userId
     * @return
     */
    public List<CompanyDto> getUserManageTargetCompanyListByUserId(Integer userId){
        if (userId == null || userId < 0) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 通过用户id 查询用户权限表
        List<UserPermissionDetails> userPermissionIdList = userPermissionDetailsService.selectUserPermissionDetailsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionIdList)){
            // 获取管理的公司ids
            Set<Integer> companyIds = userPermissionTargetCompanyDetailsService.getCompanyIdsByUserIdPermissionId(userPermissionIdList);
            if (!CollectionUtils.isEmpty(companyIds)){
                return companyIds.stream()
                        .filter(Objects::nonNull)
                        .map(companyId -> {
                            // 查询公司信息
                            return userPermissionTargetCompanyDetailsService.selectCompanyById(companyId);
                        }).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }
        return null;
    }

    /**
     * 查询没有被当前用户管理的公司(可管理的公司)
     * @param userId
     * @return
     */
    public List<CompanyDto> getAvailableUserManageTargetCompanyListByUserId(Integer userId){
        if (userId == null || userId < 0) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        Set<Integer> companyIdsListSet = new HashSet<>();
        // 通过用户id 查询用户权限表
        List<UserPermissionDetails> userPermissionIdList = userPermissionDetailsService.selectUserPermissionDetailsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionIdList)){
            // 获取管理的公司ids
            companyIdsListSet = userPermissionTargetCompanyDetailsService.getCompanyIdsByUserIdPermissionId(userPermissionIdList);
        }
        return userPermissionTargetCompanyDetailsService.selectCompanyNotInIds(companyIdsListSet);
    }

    /**
     * 通过用户id 查询用户管理目标用户列表
     * @param userId
     * @return
     */
    public List<UserDto> selectUserManageTargetUserListByUserId(Integer userId){
        if (userId == null || userId < 0) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 通过用户id 查询用户权限表
        List<UserPermissionDetails> userPermissionIdList = userPermissionDetailsService.selectUserPermissionDetailsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionIdList)) {
            // 获取管理的公司ids
            Set<Integer> targetUserIdList = userPermissionTargetUserDetailsService.getUserIdsByUserIdAndPermissionId(userPermissionIdList);
            if (!CollectionUtils.isEmpty(targetUserIdList)){
                return targetUserIdList.stream()
                        .filter(Objects::nonNull)
                        .map(targetUserId -> {
                            return this.getUserDtoByUserId(targetUserId);
                        }).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }
        return null;
    }

    /**
     * 查询没有被当前用户管理的公司(可管理的公司)
     * @param userId
     * @return
     */
    public List<UserDto> getAvailableUserManageTargetUserListByUserId(Integer userId){
        if (userId == null || userId < 0) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        Set<Integer> targetUserIdList = new HashSet<>();
        // 通过用户id 查询用户权限表
        List<UserPermissionDetails> userPermissionIdList = userPermissionDetailsService.selectUserPermissionDetailsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionIdList)) {
            // 获取管理的公司ids
            targetUserIdList = userPermissionTargetUserDetailsService.getUserIdsByUserIdAndPermissionId(userPermissionIdList);
        }
        return selectUserNotInIds(targetUserIdList);
    }

    /**
     * 条件查询用户 TODO
     * @param searchConditionListMap
     * @return
     */
    public Set<Integer> selectUserListByCondition(Map<String, List<SearchCondition>> searchConditionListMap) {
        Set<Integer> userIdList = new HashSet<>();
        if (!CollectionUtils.isEmpty(searchConditionListMap)){
            Set<String> keySet = searchConditionListMap.keySet();
            if (!CollectionUtils.isEmpty(keySet)){
                StringBuilder requestSqlBuffer = new StringBuilder();
                keySet.stream()
                        .filter(Objects::nonNull)
                        .forEach(key -> {
                            StringBuilder sqlBuffer = new StringBuilder();
                            List<SearchCondition> searchConditionList = searchConditionListMap.get(key);
                            if (!CollectionUtils.isEmpty(searchConditionList)){
                                sqlBuffer.append(key);
                                sqlBuffer.append(" ");
                                sqlBuffer.append("IN");
                                sqlBuffer.append("(");
                                searchConditionList.stream()
                                        .filter(Objects::nonNull)
                                        .forEach(searchCondition -> {
                                            if (searchCondition.getConditionValue().matches(PermissionConstants.SEARCH_CONDITION_VALUE)){
                                                sqlBuffer.append(searchCondition.getConditionValue());
                                            }else {
                                                sqlBuffer.append("'");
                                                sqlBuffer.append(searchCondition.getConditionValue());
                                                sqlBuffer.append("'");
                                            }
                                            sqlBuffer.append(",");
                                        });
                                String substring = sqlBuffer.toString().substring(0, sqlBuffer.length() - 1);
                                requestSqlBuffer.append(substring);
                                requestSqlBuffer.append(")");
                                requestSqlBuffer.append(" AND ");
                            }

                        });
                if (StringUtils.isNoneBlank(requestSqlBuffer)){
                    String requestSqlSubBuffer = requestSqlBuffer.toString().substring(0, requestSqlBuffer.length() - 5);
                    List<User> userList = userMapper.selectByCondition(requestSqlSubBuffer);
                    if (!CollectionUtils.isEmpty(userList)){
                        userIdList = userList.stream().filter(Objects::nonNull).map(User::getId).collect(Collectors.toSet());
                    }
                }
            }
        }
        return userIdList;
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
     * 通过用户id 查询
     * @param requestUserId
     * @return
     */
    public UserDto getUserDtoByUserId(Integer requestUserId) {
        User user = selectByPrimaryKey(requestUserId);
        return user != null ? copyProperties(user) : null;
    }

    /**
     * 查询不存在的数据量
     * @param userIds
     * @return
     */
    public Long selectUserNoExistCountByIds(Set<Integer> userIds) {
        Long count = null;
        if (!CollectionUtils.isEmpty(userIds)){
            count = userIds.stream()
                    .filter(Objects::nonNull)
                    .map(id -> {
                        return selectByPrimaryKey(id);
                    })
                    .filter(groups -> groups == null)
                    .count();
        }
        return count;
    }

    /**
     * 查询除ids 以外的用户
     * @param userIdsListSet
     * @return
     */
    private List<UserDto> selectUserNotInIds(Set<Integer> userIdsListSet) {
        if (!CollectionUtils.isEmpty(userIdsListSet)){
           List<User> userList = userMapper.selectUserNotInIds(userIdsListSet);
            return copyProperties(userList);
        }
        return null;
    }

    /**
     * 条件查询返回列表
     * @param example
     * @return
     */
    private List<UserDto> getUserDtoByExample(Example example) {
        List<User> users = userMapper.selectByExample(example);
        return copyProperties(users);
    }

    /**
     * 复制user
     * @param users
     * @return
     */
    private List<UserDto> copyProperties(List<User> users){
        if (!CollectionUtils.isEmpty(users)){
            return users.stream()
                    .filter(Objects::nonNull)
                    .map(user -> {
                        return copyProperties(user);
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制user
     * @param user
     * @return
     */
    private UserDto copyProperties(User user){
        if (user != null){
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user,userDto);
            return userDto;
        }
        return null;
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
        if (StringUtils.isNoneBlank(userDto.getName())){
            user.setName(userDto.getName());
        }
        // 邮箱
        if (StringUtils.isNoneBlank(userDto.getEmail())){
            user.setEmail(userDto.getEmail());
        }
        // 区域
        if (StringUtils.isNoneBlank(userDto.getRegion())){
            user.setRegion(userDto.getRegion());
        }
        // 岗位
        if (StringUtils.isNoneBlank(userDto.getPost())){
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
        if (StringUtils.isNoneBlank(userDto.getPhone())){
            user.setPhone(userDto.getPhone());
        }
    }

    /**
     * 添加参数校验
     * @param userDto
     */
    private void addUserParameterCheck(UserDto userDto) {
        if (userDto == null || StringUtils.isBlank(userDto.getAccount()) || StringUtils.isBlank(userDto.getName())
                || StringUtils.isBlank(userDto.getEmail()) || StringUtils.isBlank(userDto.getPhone())){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 邮件和电话校验,权限，权限组，权限
        parameterCheck(userDto);
        // 账号是否存在
        User user = new User();
        user.setAccount(userDto.getAccount());
        User userResponse = userMapper.selectOne(user);
        if (userResponse != null){
            throw new PermissionException(PermissionBundleKey.USER_ALREADY_EXIST, PermissionBundleKey.USER_ALREADY_EXIST_MSG);
        }
    }

    /**
     * 修改用户参数校验
     * @param userDto
     * @return
     */
    private User editUserParameterCheck(UserDto userDto) {
        if (userDto == null || userDto.getId() == null || userDto.getId() < 0 || StringUtils.isBlank(userDto.getName())
                || StringUtils.isBlank(userDto.getEmail()) || StringUtils.isBlank(userDto.getPhone())){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
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
            throw new PermissionException(PermissionBundleKey.USER_NOT_EXIST, PermissionBundleKey.USER_NOT_EXIST_MSG);
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
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }

        // 邮件是否正确
        if (!Pattern.compile(PermissionConstants.EMAIL_SERVER_MAILBOX_REGULAR_EXPRESSION).matcher(userDto.getEmail()).matches()){
            throw new PermissionException(PermissionBundleKey.EMAIL_FORMAT_ERROR, PermissionBundleKey.EMAIL_FORMAT_ERROR_MSG);
        }

        // 电话号码是否正确
        if (userDto.getPhone().length() != PermissionConstants.PHONE_LENGTH){
            throw new PermissionException(PermissionBundleKey.PHONE_FORMAT_ERROR, PermissionBundleKey.PHONE_FORMAT_ERROR_MSG);
        }

        // 如果用户组不为空,查询用户组不存在，不操作
        if (!CollectionUtils.isEmpty(userDto.getUserGroupIds())){
            Long userGroupNoExistCount = userGroupService.selectUserGroupNoExistCountByIds(userDto.getUserGroupIds());
            if (userGroupNoExistCount != null && userGroupNoExistCount > 0){
                throw new PermissionException(PermissionBundleKey.USER_GROUP_NOT_EXIST, PermissionBundleKey.USER_GROUP_NOT_EXIST_MSG);
            }
        }
    }
}