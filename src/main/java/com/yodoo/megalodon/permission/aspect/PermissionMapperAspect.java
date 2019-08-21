package com.yodoo.megalodon.permission.aspect;

import com.yodoo.feikongbao.provisioning.contract.ProvisioningConstants;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.CompanyMapper;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.GroupsMapper;
import com.yodoo.feikongbao.provisioning.domain.system.security.ProvisioningUserDetails;
import com.yodoo.megalodon.permission.mapper.UserGroupMapper;
import com.yodoo.megalodon.permission.mapper.UserMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author houzhen
 * @Date 12:14 2019/5/15
 **/
//@Aspect
//@Component
//@Order(2)
public class PermissionMapperAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String METHOD_NAME_STARTS_WITH = "select";

    /**
     * 目标数据
     */
    private final Map<String, String> TARGET_CONDITION_MAP = new HashMap<String, String>() {
        {
            // 集团
            put("GroupsMapper",
                    " id in (select target.target_group_id from user_permission_target_group_details target inner join " +
                            "user_permission_details permission on target.user_id = permission.user_id and target.permission_id = permission.permission_id " +
                            "where permission.user_id=${userId})");
            // 公司
            put("CompanyMapper",
                    " id in (select target.target_company_id from user_permission_target_company_details target inner join " +
                            "user_permission_details permission on target.user_id = permission.user_id and target.permission_id = permission.permission_id " +
                            "where permission.user_id=${userId})");
            // 用户
            put("UserMapper",
                    " id in (select target.target_user_id from user_permission_target_user_details target inner join " +
                            "user_permission_details permission on target.user_id = permission.user_id and target.permission_id = permission.permission_id " +
                            "where permission.user_id=${userId})");
            // 用户组
            put("UserGroupMapper",
                    " id in (select target.user_group_id from user_permission_target_user_group_details target inner join " +
                            "user_permission_details permission on target.user_id = permission.user_id and target.permission_id = permission.permission_id " +
                            "where permission.user_id=${userId})");
        }
    };

    @Pointcut("execution(* com.yodoo.megalodon.permission.mapper.*.*(..))")
    public void mapperPointCut() {
    }

    @Before("mapperPointCut()")
    public void before(JoinPoint joinPoint) {
        // 目标类
        Object targetObj = joinPoint.getTarget();
        //请求的参数
        Object[] args = joinPoint.getArgs();
        // 方法名称
        String methodName = joinPoint.getSignature().getName();
        if (args != null && args.length > 0 && methodName.startsWith(METHOD_NAME_STARTS_WITH)) {
            // 集团目标
            if (targetObj instanceof GroupsMapper) {
                this.setConditionToExample(args, "GroupsMapper");
            }
            // 公司
            else if (targetObj instanceof CompanyMapper) {
                this.setConditionToExample(args, "CompanyMapper");
            }
            // 用户
            else if (targetObj instanceof UserMapper) {
                this.setConditionToExample(args, "UserMapper");
            }
            // 用户组
            else if (targetObj instanceof UserGroupMapper) {
                this.setConditionToExample(args, "UserGroupMapper");
            }
        }
    }

    private void setConditionToExample(Object[] args, String code) {
        // session中获取用户信息
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        Object userObj = session.getAttribute(ProvisioningConstants.AUTH_USER);
        if (userObj != null) {
            ProvisioningUserDetails userInfo = (ProvisioningUserDetails) userObj;
            Integer userId = userInfo.getId();
            for (Object obj : args) {
                if (obj instanceof Example) {
                    Example example = (Example) obj;
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andCondition(TARGET_CONDITION_MAP.get(code).replace("${userId}", String.valueOf(userId)));
                    example.and(criteria);
                    break;
                }
            }
        }
    }
}
