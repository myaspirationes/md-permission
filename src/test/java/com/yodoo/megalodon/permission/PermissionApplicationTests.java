package com.yodoo.megalodon.permission;

import com.yodoo.megalodon.permission.api.UserManagerApi;
import com.yodoo.megalodon.permission.dto.UserGroupDto;
import com.yodoo.megalodon.permission.entity.UserGroup;
import com.yodoo.megalodon.permission.entity.UserGroupDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PermissionApplicationTests {

    @Autowired
    private UserManagerApi userManagerApi;
}
