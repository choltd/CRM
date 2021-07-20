package com.lixiang.crm.settings.dao;

import java.util.List;
import java.util.Map;

import com.lixiang.crm.settings.domain.User;

public interface UserDao {

	User login(Map<String, String> map);

	List<User> getUserList();

}
