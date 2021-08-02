package com.toolbox.service;

import com.toolbox.domain.User;

import java.util.List;

public interface UserService {

    List<User> queryAll();

    int add(User user);

    User queryByName(String username);
}
