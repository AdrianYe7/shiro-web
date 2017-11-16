package com.xyz.service.user;

import com.xyz.model.User;

public interface UserService {

    void register(User user);

    void modifyPassword(User user);

    int getUserCountByName(String username);
}
