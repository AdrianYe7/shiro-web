package com.xyz.service.user.impl;

import com.xyz.model.Role;
import com.xyz.model.User;
import com.xyz.service.user.UserService;
import com.xyz.utils.JdbcHelper;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.List;

public class UserServiceImpl implements UserService {

    @Override
    public void register(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        SimpleHash simpleHash = new SimpleHash("MD5", password, ByteSource.Util.bytes(username + password), 1024);
        password = simpleHash.toHex();
        String sql = "insert into shiro_user(username, password, status, role_id) values(?, ?, ?, ?)";
        JdbcHelper.insert(sql, username, password, 1, 2);
    }

    @Override
    public void modifyPassword(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        SimpleHash simpleHash = new SimpleHash("MD5", password, ByteSource.Util.bytes(username + password), 1024);
        String sql = "update shiro_user set password = ? where username = ?";
        JdbcHelper.update(sql, password, username);
    }

    @Override
    public int getUserCountByName(String username) {
        String sql = "select count(1) from shiro_user where username = ?";
        Integer count = JdbcHelper.querySingle(Integer.class, sql, username);
        return count;
    }

    @Override
    public List<Role> getAllRoles() {
        String sql = "select id, role_name from shiro_role";
        List<Role> roles = JdbcHelper.queryList(Role.class, sql);
        return roles;
    }

}
