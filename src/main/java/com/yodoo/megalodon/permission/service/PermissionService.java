package com.yodoo.megalodon.permission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.dto.PermissionDto;
import com.yodoo.megalodon.permission.entity.Permission;
import com.yodoo.megalodon.permission.entity.PermissionGroupDetails;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.PermissionMapper;
import com.yodoo.megalodon.permission.util.JsonUtils;
import com.yodoo.megalodon.permission.util.RequestPrecondition;
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
 * @Author houzhen
 * @Date 15:21 2019/8/5
**/
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class PermissionService {

    private static Logger logger = LoggerFactory.getLogger(PermissionService.class);

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    @Autowired
    private PermissionGroupDetailsService permissionGroupDetailsService;

    /**
     * 分页查询
     * @Author houzhen
     * @Date 15:24 2019/8/5
    **/
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public PageInfoDto<PermissionDto> queryPermissionList(PermissionDto permissionDto) {
        logger.info("PermissionService.queryPermissionList permissionDto:{}", JsonUtils.obj2json(permissionDto));
        List<PermissionDto> responseLst = new ArrayList<>();
        Example example = new Example(Permission.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(permissionDto.getPermissionCode())) {
            criteria.andEqualTo("permissionCode", permissionDto.getPermissionCode());
        }
        if (!StringUtils.isEmpty(permissionDto.getPermissionName())) {
            criteria.andLike("permissionName", "%" + permissionDto.getPermissionName() + "%");
        }
        example.and(criteria);
        Page<?> pages = PageHelper.startPage(permissionDto.getPageNum(), permissionDto.getPageSize());
        List<Permission> permissionList = permissionMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(permissionList)) {
            responseLst = permissionList.stream().map(permission -> {
                PermissionDto dto = new PermissionDto();
                BeanUtils.copyProperties(permission, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return new PageInfoDto<PermissionDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), responseLst);
    }

    /**
     * 新增
     * @Author houzhen
     * @Date 15:55 2019/8/5
    **/
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer addPermission(PermissionDto permissionDto) {
        logger.info("PermissionService.addPermission permissionDto:{}", JsonUtils.obj2json(permissionDto));
        // 检验参数
        RequestPrecondition.checkArgumentsNotEmpty(permissionDto.getPermissionCode(), permissionDto.getPermissionName());
        // 查询code是否存在
        Permission permission = this.getPermissionByCode(permissionDto.getPermissionCode());
        if (permission != null) {
            throw new PermissionException(PermissionBundleKey.PERMISSION_EXIST, PermissionBundleKey.PERMISSION_EXIST_MSG);
        }
        permission = new Permission();
        BeanUtils.copyProperties(permissionDto, permission);
        return permissionMapper.insertSelective(permission);
    }

    /**
     * 修改
     * @Author houzhen
     * @Date 17:08 2019/8/5
    **/
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer updatePermission(PermissionDto permissionDto) {
        logger.info("PermissionService.updatePermission permissionDto:{}", JsonUtils.obj2json(permissionDto));
        // 检验参数
        RequestPrecondition.checkArgumentsNotEmpty(permissionDto.getPermissionCode(), permissionDto.getPermissionName());
        if (permissionDto.getId() == null || permissionDto.getId() < 0){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        Permission permissionById = permissionMapper.selectByPrimaryKey(permissionDto.getId());
        if (permissionById == null){
            throw new PermissionException(PermissionBundleKey.PERMISSION_NOT_EXIST, PermissionBundleKey.PERMISSION_NOT_EXIST_MSG);
        }
        Example example = new Example(Permission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", permissionDto.getId());
        criteria.andEqualTo("permissionCode", permissionDto.getPermissionCode());
        Permission permissionByPermissionCode = permissionMapper.selectOneByExample(example);
        if (permissionByPermissionCode != null) {
            throw new PermissionException(PermissionBundleKey.PERMISSION_EXIST, PermissionBundleKey.PERMISSION_EXIST_MSG);
        }
        BeanUtils.copyProperties(permissionDto, permissionById);
        return permissionMapper.updateByPrimaryKeySelective(permissionById);
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer deletePermission(Integer id){
        deleteParameterCheck(id);
        return permissionMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据userId查询权限
     * @Author houzhen
     * @Date 17:21 2019/8/5
    **/
    public List<Permission> getPermissionByUserId(Integer userId) {
        logger.info("PermissionService.getPermissionByUserId userId:{}", userId);
        return this.selectPermissionByUserId(userId);
    }

    /**
     * 查询可用权限
     * @Author houzhen
     * @Date 9:43 2019/8/6
    **/
    public List<PermissionDto> getAvailablePermissionByUserId(Integer userId) {
        logger.info("PermissionService.getAvailablePermissionByUserId userId:{}", userId);
        List<PermissionDto> responseList = new ArrayList<>();
        if (userId == null) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        List<Permission> permissionList = this.selectPermissionByUserId(userId);
        List<Permission> availablePermissionList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionList)) {
            List<Integer> permissionIdList = permissionList.stream().map(Permission::getId).collect(Collectors.toList());
            Example example = new Example(Permission.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andNotIn("id", permissionIdList);
            example.and(criteria);
            availablePermissionList = permissionMapper.selectByExample(example);
        } else {
            availablePermissionList = permissionMapper.selectAll();
        }
        if (!CollectionUtils.isEmpty(availablePermissionList)) {
            responseList = availablePermissionList.stream().map(permission -> {
                PermissionDto dto = new PermissionDto();
                BeanUtils.copyProperties(permission, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return responseList;
    }

    /**
     * 删除校验
     * @param permissionId
     */
    private void deleteParameterCheck(Integer permissionId) {
        if (permissionId == null || permissionId < 0){
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        List<UserPermissionDetails> userPermissionDetailsList = userPermissionDetailsService.selectUserPermissionDetailsByPermissionId(permissionId);
        List<PermissionGroupDetails> permissionGroupDetailsList = permissionGroupDetailsService.selectPermissionGroupDetailsByPermissionId(permissionId);
        if (!CollectionUtils.isEmpty(userPermissionDetailsList) || !CollectionUtils.isEmpty(permissionGroupDetailsList)){
            throw new PermissionException(PermissionBundleKey.THE_DATA_IS_STILL_IN_USE, PermissionBundleKey.THE_DATA_IS_STILL_IN_USE_MEG);
        }
    }

    private Permission getPermissionByCode(String permissionCode) {
        RequestPrecondition.checkArgumentsNotEmpty(permissionCode);
        // 查询code是否存在
        Example example = new Example(Permission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("permissionCode", permissionCode);
        example.and(criteria);
        return permissionMapper.selectOneByExample(example);
    }

    public List<Permission> selectPermissionByUserId(Integer userId) {
        if (userId == null || userId < 0) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        List<UserPermissionDetails> userPermissionDetailsList = userPermissionDetailsService.selectUserPermissionDetailsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionDetailsList)) {
            return userPermissionDetailsList.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionDetails -> {
                        return permissionMapper.selectByPrimaryKey(userPermissionDetails.getPermissionId());
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 通过id 查询，统计不存在的数量
     * @param permissionIds
     * @return
     */
    public Long selectPermissionNoExistCountByIds(Set<Integer> permissionIds) {
        Long count = null;
        if (!CollectionUtils.isEmpty(permissionIds)){
            count = permissionIds.stream()
                    .filter(Objects::nonNull)
                    .map(id -> {
                        return permissionMapper.selectByPrimaryKey(id);
                    })
                    .filter(permission -> permission == null)
                    .count();
        }
        return count;
    }
}
