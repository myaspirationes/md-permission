package com.yodoo.megalodon.permission.service;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.CompanyDto;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetCompanyDetailsDto;
import com.yodoo.megalodon.permission.entity.Company;
import com.yodoo.megalodon.permission.entity.UserPermissionTargetCompanyDetails;
import com.yodoo.megalodon.permission.exception.BundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
import com.yodoo.megalodon.permission.mapper.CompanyMapper;
import com.yodoo.megalodon.permission.mapper.UserPermissionTargetCompanyDetailsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description ：用户权限管理目标公司
 * @Author ：jinjun_luo
 * @Date ： 2019/8/6 0006
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionTargetCompanyDetailsService {

    @Autowired
    private UserPermissionTargetCompanyDetailsMapper userPermissionTargetCompanyDetailsMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    /**
     * 更新用户管理目标公司数据
     * @param userPermissionTargetCompanyDetailsDtoList
     * @param userId
     */
    public void updateUserPermissionTargetCompany(List<UserPermissionTargetCompanyDetailsDto> userPermissionTargetCompanyDetailsDtoList, Integer userId) {
        // 参数判断
        if (userId == null) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 先通过用户 id 查询用户权限表，获取用户权限表 ids
        List<Integer> userPermissionIds = userPermissionDetailsService.getUserPermissionIdsByUserId(userId);
        // 通过用户权限 ids 删除 用户管理目标公司 数据
        if (!CollectionUtils.isEmpty(userPermissionIds)){
            Example example = new Example(UserPermissionTargetCompanyDetails.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("userPermissionId",userPermissionIds);
            userPermissionTargetCompanyDetailsMapper.deleteByExample(example);
        }
        // 增加修改后的权限
        if (!CollectionUtils.isEmpty(userPermissionTargetCompanyDetailsDtoList)) {
            userPermissionTargetCompanyDetailsDtoList.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionTargetCompanyDetailsDto ->{
                        return userPermissionTargetCompanyDetailsMapper.insertSelective(new UserPermissionTargetCompanyDetails(
                                userPermissionTargetCompanyDetailsDto.getUserPermissionId(), userPermissionTargetCompanyDetailsDto.getCompanyId()));
                    }).count();
        }
    }

    /**
     * 查询除ids 以外的公司
     * @param companyIdsListSet
     * @return
     */
    public List<CompanyDto> selectCompanyNotInIds(Set<Integer> companyIdsListSet) {
        List<CompanyDto> companyDtoList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(companyIdsListSet)){
            Example example = new Example(Company.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andNotIn("id",companyIdsListSet);
            List<Company> companies = companyMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(companies)){
                companyDtoList = companies.stream()
                        .filter(Objects::nonNull)
                        .map(company -> {
                            CompanyDto companyDto = new CompanyDto();
                            BeanUtils.copyProperties(companies, companyDto);
                            return companyDto;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        }
        return companyDtoList;
    }

    /**
     * 通过主键查询
     * @param companyId
     * @return
     */
    public CompanyDto selectCompanyById(Integer companyId) {
        Example example = new Example(Company.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", companyId);
        Company company = companyMapper.selectOneByExample(example);
        if (company != null){
            CompanyDto companyDto = new CompanyDto();
            BeanUtils.copyProperties(companyDto, company);
            return companyDto;
        }
        return null;
    }

    /**
     * 通过用户权限id 查询用户管理目标公司
     * @param userPermissionId
     * @return
     */
    public List<Integer>  getCompanyIdsByUserPermissionId(Integer userPermissionId) {
        Example example = getExample(userPermissionId);
        List<UserPermissionTargetCompanyDetails> list = userPermissionTargetCompanyDetailsMapper.selectByExample(example);
        List<Integer> companyIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)){
            companyIds = list.stream()
                    .filter(Objects::nonNull)
                    .map(UserPermissionTargetCompanyDetails::getCompanyId)
                    .collect(Collectors.toList());
        }
        return companyIds;
    }

    /**
     * 通过用户权限ids 查询目标公司
     * @param userPermissionIds
     */
    public List<UserPermissionTargetCompanyDetailsDto> getTargetCompanyDetailsByUserPermissionIds(List<Integer> userPermissionIds) {
        List<UserPermissionTargetCompanyDetailsDto> userPermissionTargetCompanyDetailsDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userPermissionIds)){
            userPermissionIds.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionId -> {
                        Example example = getExample(userPermissionId);
                        List<UserPermissionTargetCompanyDetails> list = userPermissionTargetCompanyDetailsMapper.selectByExample(example);
                        if (!CollectionUtils.isEmpty(list)){
                            List<UserPermissionTargetCompanyDetailsDto> collect = list.stream()
                                    .filter(Objects::nonNull)
                                    .map(userPermissionTargetCompanyDetails -> {
                                        UserPermissionTargetCompanyDetailsDto userPermissionTargetCompanyDetailsDto = new UserPermissionTargetCompanyDetailsDto();
                                        BeanUtils.copyProperties(userPermissionTargetCompanyDetails, userPermissionTargetCompanyDetailsDto);
                                        return userPermissionTargetCompanyDetailsDto;
                                    }).filter(Objects::nonNull).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(collect)){
                                userPermissionTargetCompanyDetailsDtoList.addAll(collect);
                            }
                        }
                        return null;
                    }).filter(Objects::nonNull).count();
        }
        return userPermissionTargetCompanyDetailsDtoList;

    }

    private Example getExample(Integer userPermissionId){
        Example example = new Example(UserPermissionTargetCompanyDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPermissionId", userPermissionId);
        return example;
    }
}
