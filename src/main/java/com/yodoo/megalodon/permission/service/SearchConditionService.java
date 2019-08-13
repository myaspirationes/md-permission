package com.yodoo.megalodon.permission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.SearchConditionDto;
import com.yodoo.megalodon.permission.entity.SearchCondition;
import com.yodoo.megalodon.permission.entity.UserGroupCondition;
import com.yodoo.megalodon.permission.exception.BundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.SearchConditionMapper;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

/**
 * @Description ：条件
 * @Author ：jinjun_luo
 * @Date ： 2019/8/8 0008
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class SearchConditionService {

    @Autowired
    private SearchConditionMapper searchConditionMapper;

    @Autowired
    private UserGroupConditionService userGroupConditionService;


    /**
     * 条件分页查询
     * @param searchConditionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public PageInfoDto<SearchConditionDto> queryUserGroupList(SearchConditionDto searchConditionDto) {
        Example example = new Example(SearchCondition.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNoneBlank(searchConditionDto.getConditionCode())){
            criteria.andEqualTo("conditionCode", searchConditionDto.getConditionCode());
        }
        if (StringUtils.isNoneBlank(searchConditionDto.getConditionName())){
            criteria.andEqualTo("conditionName", searchConditionDto.getConditionName());
        }
        Page<?> pages = PageHelper.startPage(searchConditionDto.getPageNum(), searchConditionDto.getPageSize());
        List<SearchCondition> searchConditionList = searchConditionMapper.selectByExample(example);
        List<SearchConditionDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(searchConditionList)) {
            collect = searchConditionList.stream()
                    .filter(Objects::nonNull)
                    .map(searchCondition -> {
                        SearchConditionDto searchConditionDtoResponse = new SearchConditionDto();
                        BeanUtils.copyProperties(searchCondition, searchConditionDtoResponse);
                        return searchConditionDtoResponse;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return new PageInfoDto<SearchConditionDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 添加查询条件
     * @param searchConditionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer addSearchCondition(SearchConditionDto searchConditionDto){
        addSearchConditionParameterCheck(searchConditionDto);
        return searchConditionMapper.insertSelective(new SearchCondition(searchConditionDto.getConditionCode(), searchConditionDto.getConditionName()));
    }

    /**
     * 更新查询条件
     * @param searchConditionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer editSearchCondition(SearchConditionDto searchConditionDto){
        SearchCondition searchCondition = editSearchConditionParameterCheck(searchConditionDto);
        if (StringUtils.isNoneBlank(searchConditionDto.getConditionCode())){
            searchCondition.setConditionCode(searchConditionDto.getConditionCode());
        }
        if (StringUtils.isNoneBlank(searchConditionDto.getConditionName())){
            searchCondition.setConditionName(searchConditionDto.getConditionName());
        }
        return searchConditionMapper.updateByPrimaryKeySelective(searchCondition);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer deleteSearchCondition(Integer id){
        deleteSearchConditionParameterCheck(id);
        return searchConditionMapper.deleteByPrimaryKey(id);
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    public SearchCondition selectByPrimaryKey(Integer id) {
        return searchConditionMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有条件列表
     * @return
     */
    public List<SearchCondition> getAllSearchCondition() {
        Example example = new Example(SearchCondition.class);
        example.setOrderByClause("condition_name ASC");
        Example.Criteria criteria = example.createCriteria();
        return searchConditionMapper.selectByExample(example);
    }

    /**
     * 删除校验
     * @param id
     */
    private void deleteSearchConditionParameterCheck(Integer id) {
        if (id == null || id < 0){
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        List<UserGroupCondition> userGroupConditions = userGroupConditionService.selectUserGroupConditionBySearchConditionId(id);
        if (!CollectionUtils.isEmpty(userGroupConditions)){
            throw new PermissionException(BundleKey.SEARCH_CONDITION_USE, BundleKey.SEARCH_CONDITION_USE_MSG);
        }
    }

    /**
     * 更新参数校验
     * @param searchConditionDto
     * @return
     */
    private SearchCondition editSearchConditionParameterCheck(SearchConditionDto searchConditionDto) {
        if (searchConditionDto == null || searchConditionDto.getId() == null || searchConditionDto.getId() < 0 ||
                StringUtils.isBlank(searchConditionDto.getConditionCode()) || StringUtils.isBlank(searchConditionDto.getConditionName())){
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        SearchCondition searchCondition = selectByPrimaryKey(searchConditionDto.getId());
        if (searchCondition == null){
            throw new PermissionException(BundleKey.SEARCH_CONDITION_NOT_EXIST, BundleKey.SEARCH_CONDITION_NOT_EXIST_MSG);
        }
        Example example = new Example(SearchCondition.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",searchConditionDto.getId());
        criteria.andNotEqualTo("conditionCode", searchConditionDto.getConditionCode());
        SearchCondition searchConditionResponse =  searchConditionMapper.selectOneByExample(example);
        if (searchConditionResponse != null){
            throw new PermissionException(BundleKey.SEARCH_CONDITION_ALREADY_EXIST, BundleKey.SEARCH_CONDITION_ALREADY_EXIST_MSG);
        }
        return searchCondition;
    }

    /**
     * 添加条件参数校验
     * @param searchConditionDto
     */
    private void addSearchConditionParameterCheck(SearchConditionDto searchConditionDto) {
        if (searchConditionDto == null || StringUtils.isBlank(searchConditionDto.getConditionCode()) || StringUtils.isBlank(searchConditionDto.getConditionName())){
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example example = new Example(SearchCondition.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("conditionCode",searchConditionDto.getConditionCode());
        SearchCondition searchCondition =  searchConditionMapper.selectOneByExample(example);
        if (searchCondition != null){
            throw new PermissionException(BundleKey.SEARCH_CONDITION_ALREADY_EXIST, BundleKey.SEARCH_CONDITION_ALREADY_EXIST_MSG);
        }
    }
}
