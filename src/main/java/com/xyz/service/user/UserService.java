package com.xyz.service.user;

import com.xyz.model.Role;
import com.xyz.model.User;

import java.util.List;

public interface UserService {

    void register(User user);

    void modifyPassword(User user);

    int getUserCountByName(String username);

    List<Role> getAllRoles();
}
