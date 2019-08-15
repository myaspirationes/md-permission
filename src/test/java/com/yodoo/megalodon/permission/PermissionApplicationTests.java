package com.yodoo.megalodon.permission;

import com.yodoo.megalodon.permission.config.PermissionConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PermissionConfig.class })
public class PermissionTests {


    @Test
    public void test(){
        System.out.println("========");
    }
}
