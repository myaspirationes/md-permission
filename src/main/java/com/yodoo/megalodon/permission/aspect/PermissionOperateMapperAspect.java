package com.yodoo.megalodon.permission.aspect;

import com.yodoo.megalodon.permission.common.BaseEntity;
import com.yodoo.megalodon.permission.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author houzhen
 * @Date 12:14 2019/5/15
 **/
@Aspect
@Component
@Order(1)
public class PermissionOperateMapperAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 批量插入
     */
    private final Set<String> INSERTLIST_SET = new HashSet<>(Arrays.asList("insertList"));
    /**
     * 插入
     */
    private final Set<String> INSERT_SET = new HashSet<>(Arrays.asList("insert", "insertSelective",
            "insertUseGeneratedKeys"));
    /**
     * 更新
     */
    private final Set<String> UPDATE_SET = new HashSet<>(Arrays.asList("updateByPrimaryKey",
            "updateByExampleSelective", "updateByExample", "updateByPrimaryKeySelective"));


    @Pointcut("execution(* com.yodoo.megalodon.permission.mapper.*.*(..))")
    public void mapperPointCut() {
    }

    @Before("mapperPointCut()")
    public void before(JoinPoint joinPoint) {
        // 方法名称
        String methodName = joinPoint.getSignature().getName();
        //请求的参数
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            // 批量插入
            if (INSERTLIST_SET.contains(methodName)) {
                if (args[0] instanceof List) {
                    List<BaseEntity> recordList = (List<BaseEntity>) args[0];
                    for (BaseEntity entity : recordList) {
                        if (StringUtils.isEmpty(entity.getCreateUser())) {
                            entity.setCreateUser();
                        }
                        if (StringUtils.isEmpty(entity.getUpdateUser())) {
                            entity.setUpdateUser();
                        }
                    }
                }
            }
            // 插入
            else if (INSERT_SET.contains(methodName)) {
                if (args[0] instanceof BaseEntity) {
                    BaseEntity entity = (BaseEntity) args[0];
                    if (StringUtils.isEmpty(entity.getCreateUser())) {
                        entity.setCreateUser();
                    }
                    if (StringUtils.isEmpty(entity.getUpdateUser())) {
                        entity.setUpdateUser();
                    }
                }

            }
            // 更新
            else if (UPDATE_SET.contains(methodName)) {
                if (args[0] instanceof BaseEntity) {
                    BaseEntity entity = (BaseEntity) args[0];
                    if (StringUtils.isEmpty(entity.getUpdateUser())) {
                        entity.setUpdateUser();
                    }
                }
            }
        }
    }
}
