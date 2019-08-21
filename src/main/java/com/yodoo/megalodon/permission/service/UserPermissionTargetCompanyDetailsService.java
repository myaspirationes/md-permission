package com.yodoo.megalodon.permission.service;

import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetCompanyDetailsDto;
import com.yodoo.megalodon.permission.dto.UserPermissionTargetDto;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
import com.yodoo.megalodon.permission.entity.UserPermissionTargetCompanyDetails;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;
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
@Transactional(rollbackFor = Exception.class, transactionManager = PermissionConfig.PERMISSION_TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionTargetCompanyDetailsService {

    @Autowired
    private UserPermissionTargetCompanyDetailsMapper userPermissionTargetCompanyDetailsMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    /**
     * 更新用户管理目标公司数据
     * @param userPermissionTargetDto
     */
    public void updateUserPermissionTargetCompany(UserPermissionTargetDto userPermissionTargetDto) {
        //  参数校验
        updateUserPermissionTargetCompanyParameterCheck(userPermissionTargetDto);
        // 先删除
        Example example = getExampleByUserIdAndPermissionId(new UserPermissionDetails(userPermissionTargetDto.getUserId(), userPermissionTargetDto.getPermissionId()));
        userPermissionTargetCompanyDetailsMapper.deleteByExample(example);
        // 增加修改后的权限
        userPermissionTargetDto.getTargetIds().stream()
                .filter(Objects::nonNull)
                .map(targetCompanyId ->{
                    UserPermissionTargetCompanyDetails userPermissionTargetCompanyDetails = new UserPermissionTargetCompanyDetails();
                    userPermissionTargetCompanyDetails.setUserId(userPermissionTargetDto.getUserId());
                    userPermissionTargetCompanyDetails.setPermissionId(userPermissionTargetDto.getPermissionId());
                    userPermissionTargetCompanyDetails.setTargetCompanyId(targetCompanyId);
                    return userPermissionTargetCompanyDetailsMapper.insertSelective(userPermissionTargetCompanyDetails);
                }).count();
    }

    /**
     * 查询除ids 以外的公司
     * @param companyIdsListSet
     * @return
     */
    public List<CompanyDto> selectCompanyNotInIds(Set<Integer> companyIdsListSet) {
        if (!CollectionUtils.isEmpty(companyIdsListSet)){
            List<Company> companies = companyService.selectCompanyNotInIds(companyIdsListSet);
            if (!CollectionUtils.isEmpty(companies)){
                return companies.stream()
                        .filter(Objects::nonNull)
                        .map(company -> {
                            return copyProperties(company);
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        }
        return null;
    }

    /**
     * 通过主键查询
     * @param companyId
     * @return
     */
    public CompanyDto selectCompanyById(Integer companyId) {
        return copyProperties(companyService.selectByPrimaryKey(companyId));
    }

    /**
     * 查询获取目标公司 ids
     * @param userPermissionIdList
     * @return
     */
    public Set<Integer> getCompanyIdsByUserIdPermissionId(List<UserPermissionDetails> userPermissionIdList) {
        Set<Integer> targetCompanyIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(userPermissionIdList)){
            userPermissionIdList.stream()
                    .filter(Objects::nonNull)
                    .forEach(userPermissionDetails -> {
                        Example example = getExampleByUserIdAndPermissionId(userPermissionDetails);
                        List<UserPermissionTargetCompanyDetails> list = userPermissionTargetCompanyDetailsMapper.selectByExample(example);
                        if (!CollectionUtils.isEmpty(list)){
                            Set<Integer> collect = list.stream().filter(Objects::nonNull).map(UserPermissionTargetCompanyDetails::getTargetCompanyId).collect(Collectors.toSet());
                            if (!CollectionUtils.isEmpty(collect)){
                                targetCompanyIds.addAll(collect);
                            }
                        }
                    });
        }
        return targetCompanyIds;
    }

    /**
     * 通过用户id 和 权限 id 查询目标公司
     * @param userPermissionDetailsList
     */
    public List<UserPermissionTargetCompanyDetailsDto> getTargetCompanyDetailsByUserIdPermissionId(List<UserPermissionDetails> userPermissionDetailsList) {
        List<UserPermissionTargetCompanyDetailsDto> userPermissionTargetCompanyDetailsDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userPermissionDetailsList)){
            userPermissionDetailsList.stream()
                    .filter(Objects::nonNull)
                    .map(userPermissionDetails -> {
                        Example example = getExampleByUserIdAndPermissionId(userPermissionDetails);
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

    /**
     * 复制公司数据
     * @param company
     * @return
     */
    private CompanyDto copyProperties(Company company){
        if (company != null){
            CompanyDto companyDto = new CompanyDto();
            BeanUtils.copyProperties(company, companyDto);
            return companyDto;
        }
        return null;
    }

    /**
     * 更新目标公司参数校验
     * @param userPermissionTargetDto
     */
    private void updateUserPermissionTargetCompanyParameterCheck(UserPermissionTargetDto userPermissionTargetDto) {
        // 非空校验
        if (userPermissionTargetDto == null || userPermissionTargetDto.getUserId() == null || userPermissionTargetDto.getUserId() < 0
                || userPermissionTargetDto.getPermissionId() == null || userPermissionTargetDto.getPermissionId() < 0
                || CollectionUtils.isEmpty(userPermissionTargetDto.getTargetIds())) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
        // 查询用户权限是否存在
        List<UserPermissionDetails> list = userPermissionDetailsService.selectUserPermissionDetailsByUserIdAndPermissionId(new UserPermissionDetails(userPermissionTargetDto.getUserId(), userPermissionTargetDto.getPermissionId()));
        if (CollectionUtils.isEmpty(list)){
            throw new PermissionException(PermissionBundleKey.USER_PERMISSION_NOT_EXIST, PermissionBundleKey.USER_PERMISSION_NOT_EXIST_MSG);
        }
        // 查询公司是否存在
        Long companyNoExistCount = companyService.selectCompanyNoExistCountByIds(userPermissionTargetDto.getTargetIds());
        if (companyNoExistCount != null && companyNoExistCount > 0){
            throw new PermissionException(PermissionBundleKey.COMPANY_NOT_EXIST, PermissionBundleKey.COMPANY_NOT_EXIST_MSG);
        }
    }

    /**
     * 获取 example
     * @param userPermissionDetails
     * @return
     */
    private Example getExampleByUserIdAndPermissionId(UserPermissionDetails userPermissionDetails) {
        Example example = new Example(UserPermissionTargetCompanyDetails.class);
        Example.Criteria criteria = example.createCriteria();
        if (userPermissionDetails.getUserId() != null && userPermissionDetails.getUserId() > 0){
            criteria.andEqualTo("userId", userPermissionDetails.getUserId());
        }
        if (userPermissionDetails.getPermissionId() != null && userPermissionDetails.getPermissionId() > 0){
            criteria.andEqualTo("permissionId", userPermissionDetails.getPermissionId());
        }
        return example;
    }
}
