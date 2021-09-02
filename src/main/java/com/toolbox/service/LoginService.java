package com.toolbox.service;

import com.toolbox.vo.UserVO;

public interface LoginService {

	UserVO getUserByName(String userName);
}
