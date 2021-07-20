package com.lixiang.crm.settings.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lixiang.crm.exception.LoginException;
import com.lixiang.crm.settings.dao.UserDao;
import com.lixiang.crm.settings.domain.User;
import com.lixiang.crm.settings.service.UserService;
import com.lixiang.crm.utils.DateTimeUtil;
import com.lixiang.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {
	private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

	@Override
	public User login(String loginAct, String loginPwd, String ip) throws LoginException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginAct", loginAct);
		map.put("loginPwd", loginPwd);
		User user = userDao.login(map);

		if (user == null) {
			throw new LoginException("用户名密码错误");
		}

		String expireTime = user.getExpireTime();
		String sysTime = DateTimeUtil.getSysTime();
		if (expireTime.compareTo(sysTime) < 0) {
			throw new LoginException("用户已失效");
		}

		String lockState = user.getLockState();
		if ("0".equals(lockState)) {
			throw new LoginException("账号已锁定");
		}

		String ips = user.getAllowIps();
		if (!ips.contains(ip)) {
			throw new LoginException("ip地址受限");
		}

		return user;
	}

	@Override
	public List<User> getUserList() {
		List<User> uList = userDao.getUserList();
		return uList;
	}
}
