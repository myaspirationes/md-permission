package com.yodoo.megalodon.permission;

import com.yodoo.megalodon.permission.api.PermissionManagerApi;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.config.PermissionConfig;
import com.yodoo.megalodon.permission.dto.PermissionDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PermissionConfig.class })
public class PermissionApplicationTests {

    private static Logger logger = LoggerFactory.getLogger(PermissionApplicationTests.class);

    @Autowired
    private PermissionManagerApi permissionManagerApi;

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"user_manage","permission_manage"})
    public void test(){
        PageInfoDto<PermissionDto> permissionDtoPageInfoDto = permissionManagerApi.queryPermissionList(new PermissionDto());
        if (permissionDtoPageInfoDto != null && !CollectionUtils.isEmpty(permissionDtoPageInfoDto.getList())){
            permissionDtoPageInfoDto.getList().stream()
                    .filter(Objects::nonNull)
                    .forEach(permissionDto -> {
                        logger.info("permission message: permissionCode => {}, permissionName => {}",permissionDto.getPermissionCode(),permissionDto.getPermissionName());
                    });
        }
    }
}
