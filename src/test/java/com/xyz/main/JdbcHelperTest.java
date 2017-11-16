package com.xyz.main;

import static com.xyz.utils.JdbcHelper.*;

import com.xyz.service.user.impl.UserServiceImpl;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class JdbcHelperTest {

    @Test
    public void test() {
        String sql = "select count(1) from shiro_user where username = ?";
        Integer count = querySingle(int.class, sql, "zhangsan");
        System.out.println(count);
    }

    @Test
    public void test1() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        boolean b = int.class.isPrimitive();
        System.out.println(b);
        Class<Integer> type = Integer.TYPE;
        System.out.println(type);

        String s = String.class.getDeclaredConstructor().newInstance();
        System.out.println(s.equals(""));

        System.out.println(new Double(0));

        Class<Integer> aClass = int.class;
    }
}
