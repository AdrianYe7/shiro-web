package com.xyz.main;

import com.xyz.service.user.UserService;
import com.xyz.service.user.impl.UserServiceImpl;
import org.junit.Test;

public class UserServiceTest {
    private UserService userService = new UserServiceImpl();
    @Test
    public void test1() {
        int count = userService.getUserCountByName("zhangsan");
        System.out.println(count);
    }
}
