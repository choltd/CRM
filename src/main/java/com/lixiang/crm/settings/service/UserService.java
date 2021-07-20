package com.lixiang.crm.settings.service;

import java.util.List;

import com.lixiang.crm.exception.LoginException;
import com.lixiang.crm.settings.domain.User;

public interface UserService {

	User login(String loginAct, String loginPwd, String ip) throws LoginException;

	List<User> getUserList();

}
