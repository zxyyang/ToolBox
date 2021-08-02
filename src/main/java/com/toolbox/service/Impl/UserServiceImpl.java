package com.toolbox.service.Impl;


import com.toolbox.domain.User;
import com.toolbox.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public List<User> queryAll() {
        return null;
    }

    @Override
    public int add(User user) {
        return 0;
    }

    @Override
    public User queryByName(String username) {
        return null;
    }
}
