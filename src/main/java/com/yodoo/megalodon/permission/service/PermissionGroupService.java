package com.yodoo.megalodon.permission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.PermissionGroupDto;
import com.yodoo.megalodon.permission.entity.PermissionGroup;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.PermissionGroupMapper;
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

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description ：权限组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class PermissionGroupService {

    private static Logger logger = LoggerFactory.getLogger(PermissionGroupService.class);

    @Autowired
    private PermissionGroupMapper permissionGroupMapper;

    @Autowired
    private UserGroupPermissionDetailsService userGroupPermissionDetailsService;

    @Autowired
    private PermissionGroupDetailsService permissionGroupDetailsService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserGroupService userGroupService;

    /**
     * 条件分页查询
     *
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public PageInfoDto<PermissionGroupDto> queryPermissionGroupList(PermissionGroupDto permissionGroupDto) {
        PermissionGroup permissionGroupReq = new PermissionGroup();
        BeanUtils.copyProperties(permissionGroupDto, permissionGroupReq);
        Page<?> pages = PageHelper.startPage(permissionGroupDto.getPageNum(), permissionGroupDto.getPageSize());
        List<PermissionGroup> select = permissionGroupMapper.select(permissionGroupReq);
        return new PageInfoDto<PermissionGroupDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), copyProperties(select));
    }

    /**
     * 添加
     *
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer addPermissionGroup(PermissionGroupDto permissionGroupDto) {
        addPermissionGroupParameterCheck(permissionGroupDto);
        PermissionGroup permissionGroup = new PermissionGroup(permissionGroupDto.getGroupCode(), permissionGroupDto.getGroupName());
        Integer insertCount = permissionGroupMapper.insertSelective(permissionGroup);
        if (insertCount != null && insertCount > 0){
            updatePermissionGroupDetails(permissionGroup.getId(),permissionGroupDto.getPermissionIds());
        }
        return insertCount;
    }

    /**
     * 更新
     *
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer editPermissionGroup(PermissionGroupDto permissionGroupDto) {
        PermissionGroup permissionGroup = editPermissionGroupParameterCheck(permissionGroupDto);
        Integer updateCount = permissionGroupMapper.updateByPrimaryKeySelective(permissionGroup);
        if (updateCount != null && updateCount > 0){
            updatePermissionGroupDetails(permissionGroup.getId(), permissionGroupDto.getPermissionIds());
        }
        return updateCount;
    }

    /**
     * 删除：
     * 1、不存在不修改
     * 2、查询权限组明细存在不修改
     * 3、查询用户组权限组关系表存在不修改
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer deletePermissionGroup(Integer id) {
        deletePermissionGroupParameterCheck(id);
        Integer deleteCount = permissionGroupMapper.deleteByPrimaryKey(id);
        if (deleteCount != null && deleteCount > 0){
            permissionGroupDetailsService.deleteByPermissionGroupId(id);
        }
        // 更新批处理
        userGroupService.userGroupBatchProcessing(null);
        return deleteCount;
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public PermissionGroupDto getPermissionGroupDetails(Integer id) {
        return copyProperties(selectByPrimaryKey(id));
    }

    /**
     * 通过主键查询
     *
     * @param tid
     * @return
     */
    public PermissionGroup selectByPrimaryKey(Integer tid) {
        return permissionGroupMapper.selectByPrimaryKey(tid);
    }

    /**
     * 通过id 查询，统计不存在的数量
     * @param permissionGroupIds
     * @return
     */
    public Long selectPermissionGroupNoExistCountByIds(Set<Integer> permissionGroupIds) {
        Long count = null;
        if (!CollectionUtils.isEmpty(permissionGroupIds)){
            count = permissionGroupIds.stream()
                    .filter(Objects::nonNull)
                    .map(id -> {
                        return selectByPrimaryKey(id);
                    }).filter(permissionGroup -> permissionGroup == null).count();
        }
        return count;
    }

    /**
     * 复制
     * @param permissionGroupList
     * @return
     */
    private List<PermissionGroupDto> copyProperties(List<PermissionGroup> permissionGroupList){
        if (!CollectionUtils.isEmpty(permissionGroupList)){
            return permissionGroupList.stream()
                    .filter(Objects::nonNull)
                    .map(permissionGroup -> {
                        return copyProperties(permissionGroup);
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param permissionGroup
     * @return
     */
    private PermissionGroupDto copyProperties(PermissionGroup permissionGroup){
        if (permissionGroup != null){
            PermissionGroupDto permissionGroupDto = new PermissionGroupDto();
            BeanUtils.copyProperties(permissionGroup, permissionGroupDto);
            return permissionGroupDto;
        }
        return null;
    }

    /**
     * 更新参数校验
     *
     * @param permissionGroupDto
     * @return
     */
    private PermissionGroup editPermissionGroupParameterCheck(PermissionGroupDto permissionGroupDto) {
        if (permissionGroupDto == null || permissionGroupDto.getId() == null || permissionGroupDto.getId() < 0
                || StringUtils.isBlank(permissionGroupDto.getGroupCode()) || StringUtils.isBlank(permissionGroupDto.getGroupName())) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 不存在不能修改
        PermissionGroup permissionGroup = selectByPrimaryKey(permissionGroupDto.getId());
        if (permissionGroup == null) {
            throw new PermissionException(PermissionBundleKey.PERMISSION_GROUP_NOT_EXIST, PermissionBundleKey.PERMISSION_GROUP_NOT_EXIST_MSG);
        }

        Example example = new Example(PermissionGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", permissionGroupDto.getId());
        criteria.andEqualTo("groupCode", permissionGroupDto.getGroupCode());
        PermissionGroup permissionGroupSelf = permissionGroupMapper.selectOneByExample(example);
        if (permissionGroupSelf != null) {
            throw new PermissionException(PermissionBundleKey.PERMISSION_GROUP_ALREADY_EXIST, PermissionBundleKey.PERMISSION_GROUP_ALREADY_EXIST_MSG);
        }

        // 如果权限ids 不为空，查不存在的数量
        if (!CollectionUtils.isEmpty(permissionGroupDto.getPermissionIds())){
            Long permissionNoExistCount = permissionService.selectPermissionNoExistCountByIds(permissionGroupDto.getPermissionIds());
            if (permissionNoExistCount != null && permissionNoExistCount > 0){
                throw new PermissionException(PermissionBundleKey.PERMISSION_NOT_EXIST, PermissionBundleKey.PERMISSION_NOT_EXIST_MSG);
            }
        }

        permissionGroup.setGroupCode(permissionGroupDto.getGroupCode());
        permissionGroup.setGroupName(permissionGroupDto.getGroupName());
        return permissionGroup;
    }

    /**
     * 更新权限与权限组关系表
     * @param permissionGroupId
     * @param permissionIds
     */
    private void updatePermissionGroupDetails(Integer permissionGroupId, Set<Integer> permissionIds) {
        if (permissionGroupId != null && permissionGroupId > 0){
            permissionGroupDetailsService.deleteByPermissionGroupId(permissionGroupId);
        }
        if (permissionGroupId != null && permissionGroupId > 0 && !CollectionUtils.isEmpty(permissionIds)){
            permissionGroupDetailsService.insertPermissionGroupDetails(permissionGroupId, permissionIds);
        }
    }

    /**
     * 添加参数校验
     *
     * @param permissionGroupDto
     * @return
     */
    private void addPermissionGroupParameterCheck(PermissionGroupDto permissionGroupDto) {
        if (permissionGroupDto == null || StringUtils.isBlank(permissionGroupDto.getGroupCode()) || StringUtils.isBlank(permissionGroupDto.getGroupName())) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 查询是否有相同的数据，有不添加
        Example example = new Example(PermissionGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupCode", permissionGroupDto.getGroupCode());
        PermissionGroup permissionGroup = permissionGroupMapper.selectOneByExample(example);
        if (permissionGroup != null) {
            throw new PermissionException(PermissionBundleKey.PERMISSION_GROUP_ALREADY_EXIST, PermissionBundleKey.PERMISSION_GROUP_ALREADY_EXIST_MSG);
        }
        // 如果权限ids 不为空，查不存在的数量
        if (!CollectionUtils.isEmpty(permissionGroupDto.getPermissionIds())){
            Long permissionNoExistCount = permissionService.selectPermissionNoExistCountByIds(permissionGroupDto.getPermissionIds());
            if (permissionNoExistCount != null && permissionNoExistCount > 0){
                throw new PermissionException(PermissionBundleKey.PERMISSION_NOT_EXIST, PermissionBundleKey.PERMISSION_NOT_EXIST_MSG);
            }
        }
    }

    /**
     * 删除参数校验：
     * 1、不存在不修改
     * 2、查询权限组明细存在不修改
     * 3、查询用户组权限组关系表存在不修改
     *
     * @param id
     * @return
     */
    private void deletePermissionGroupParameterCheck(Integer id) {
        if (id == null || id < 0) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        PermissionGroup permissionGroup = selectByPrimaryKey(id);
        if (permissionGroup == null) {
            throw new PermissionException(PermissionBundleKey.PERMISSION_GROUP_NOT_EXIST, PermissionBundleKey.PERMISSION_GROUP_NOT_EXIST_MSG);
        }
        // 查询用户组权限组关系表，如果存在使用不能删除
        Integer userGroupPermissionDetailsCount = userGroupPermissionDetailsService.selectUserGroupPermissionDetailsCountByPermissionGroupId(id);
        if (userGroupPermissionDetailsCount != null && userGroupPermissionDetailsCount > 0) {
            throw new PermissionException(PermissionBundleKey.THE_DATA_IS_STILL_IN_USE, PermissionBundleKey.THE_DATA_IS_STILL_IN_USE_MEG);
        }
    }
}
