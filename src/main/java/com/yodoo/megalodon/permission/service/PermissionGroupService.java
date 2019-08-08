package com.yodoo.megalodon.permission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.PermissionGroupDto;
import com.yodoo.megalodon.permission.entity.PermissionGroup;
import com.yodoo.megalodon.permission.exception.BundleKey;
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

import java.util.ArrayList;
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
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class PermissionGroupService {

    private static Logger logger = LoggerFactory.getLogger(PermissionGroupService.class);

    @Autowired
    private PermissionGroupMapper permissionGroupMapper;

    @Autowired
    private UserGroupPermissionDetailsService userGroupPermissionDetailsService;

    @Autowired
    private PermissionGroupDetailsService permissionGroupDetailsService;

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
        List<PermissionGroupDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(select)) {
            collect = select.stream()
                    .filter(Objects::nonNull)
                    .map(permissionGroup -> {
                        PermissionGroupDto dto = new PermissionGroupDto();
                        BeanUtils.copyProperties(permissionGroup, dto);
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<PermissionGroupDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
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
        return permissionGroupMapper.insertSelective(new PermissionGroup(permissionGroupDto.getGroupCode(), permissionGroupDto.getGroupName()));
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
        return permissionGroupMapper.updateByPrimaryKeySelective(permissionGroup);
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
        return permissionGroupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public PermissionGroupDto getPermissionGroupDetails(Integer id) {
        PermissionGroup permissionGroup = selectByPrimaryKey(id);
        PermissionGroupDto permissionGroupDto = new PermissionGroupDto();
        if (permissionGroup != null) {
            BeanUtils.copyProperties(permissionGroup, permissionGroupDto);
        }
        return permissionGroupDto;
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
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 不存在不能修改
        PermissionGroup permissionGroup = selectByPrimaryKey(permissionGroupDto.getId());
        if (permissionGroup == null) {
            throw new PermissionException(BundleKey.PERMISSION_GROUP_NOT_EXIST, BundleKey.PERMISSION_GROUP_NOT_EXIST_MSG);
        }

        Example example = new Example(PermissionGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", permissionGroupDto.getId());
        criteria.andEqualTo("groupCode", permissionGroupDto.getGroupCode());
        PermissionGroup permissionGroupSelf = permissionGroupMapper.selectOneByExample(example);
        if (permissionGroupSelf != null) {
            throw new PermissionException(BundleKey.PERMISSION_GROUP_ALREADY_EXIST, BundleKey.PERMISSION_GROUP_ALREADY_EXIST_MSG);
        }
        permissionGroup.setGroupCode(permissionGroupDto.getGroupCode());
        permissionGroup.setGroupName(permissionGroupDto.getGroupName());
        return permissionGroup;
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
     * 添加参数校验
     *
     * @param permissionGroupDto
     * @return
     */
    private void addPermissionGroupParameterCheck(PermissionGroupDto permissionGroupDto) {
        if (permissionGroupDto == null || StringUtils.isBlank(permissionGroupDto.getGroupCode()) || StringUtils.isBlank(permissionGroupDto.getGroupName())) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询是否有相同的数据，有不添加
        Example example = new Example(PermissionGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupCode", permissionGroupDto.getGroupCode());
        criteria.andEqualTo("groupName", permissionGroupDto.getGroupName());
        PermissionGroup permissionGroup = permissionGroupMapper.selectOneByExample(example);
        if (permissionGroup != null) {
            throw new PermissionException(BundleKey.PERMISSION_GROUP_ALREADY_EXIST, BundleKey.PERMISSION_GROUP_ALREADY_EXIST_MSG);
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
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        PermissionGroup permissionGroup = selectByPrimaryKey(id);
        if (permissionGroup == null) {
            throw new PermissionException(BundleKey.PERMISSION_GROUP_NOT_EXIST, BundleKey.PERMISSION_GROUP_NOT_EXIST_MSG);
        }
        // 查询权限组明细，如果存在使用，不能删除
        Integer permissionGroupDetailsCount = permissionGroupDetailsService.selectPermissionGroupDetailsCountByPermissionGroupId(id);
        if (permissionGroupDetailsCount != null && permissionGroupDetailsCount > 0) {
            throw new PermissionException(BundleKey.THE_DATA_IS_STILL_IN_USE, BundleKey.THE_DATA_IS_STILL_IN_USE_MEG);
        }
        // 查询用户组权限组关系表，如果存在使用不能删除
        Integer userGroupPermissionDetailsCount = userGroupPermissionDetailsService.selectUserGroupPermissionDetailsCountByPermissionGroupId(id);
        if (userGroupPermissionDetailsCount != null && userGroupPermissionDetailsCount > 0) {
            throw new PermissionException(BundleKey.THE_DATA_IS_STILL_IN_USE, BundleKey.THE_DATA_IS_STILL_IN_USE_MEG);
        }
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
                    })
                    .filter(permissionGroup -> permissionGroup == null)
                    .count();
        }
        return count;
    }
}
