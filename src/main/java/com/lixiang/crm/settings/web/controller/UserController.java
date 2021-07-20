package com.lixiang.crm.settings.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lixiang.crm.settings.domain.User;
import com.lixiang.crm.settings.service.UserService;
import com.lixiang.crm.settings.service.impl.UserServiceImpl;
import com.lixiang.crm.utils.MD5Util;
import com.lixiang.crm.utils.PrintJson;
import com.lixiang.crm.utils.ServiceFactory;

public class UserController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getServletPath();
		if ("/settings/user/login.do".equals(path)) {
			login(request, response);
		} else if ("/settings/user/xxx.do".equals(path)) {

		}
	}

	public void login(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入用户控制器");
		String loginAct = request.getParameter("loginAct");
		String loginPwd = request.getParameter("loginPwd");

		loginPwd = MD5Util.getMD5(loginPwd);
		// 获取登录ip
		String ip = request.getRemoteAddr();
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

		try {
			// 验证登录
			User user = us.login(loginAct, loginPwd, ip);
			request.getSession().setAttribute("user", user);

			// 将登录成功的结果返回
			PrintJson.printJsonFlag(response, true);

		} catch (Exception e) {
			e.printStackTrace();

			String msg = e.getMessage();
			Map<String, Object> map = new HashMap<>();
			map.put("success", false);
			map.put("msg", msg);

			// 将登录失败及原因返回
			PrintJson.printJsonObj(response, map);

		}

	}

}