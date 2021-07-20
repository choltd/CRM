package com.lixiang.crm.workbench.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lixiang.crm.settings.domain.User;
import com.lixiang.crm.settings.service.UserService;
import com.lixiang.crm.settings.service.impl.UserServiceImpl;
import com.lixiang.crm.utils.DateTimeUtil;
import com.lixiang.crm.utils.PrintJson;
import com.lixiang.crm.utils.ServiceFactory;
import com.lixiang.crm.utils.UUIDUtil;
import com.lixiang.crm.vo.PaginationVo;
import com.lixiang.crm.workbench.domain.Activity;
import com.lixiang.crm.workbench.domain.ActivityRemark;
import com.lixiang.crm.workbench.service.ActivityService;
import com.lixiang.crm.workbench.service.impl.ActivityServiceImpl;


public class ActivityController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getServletPath();
		if ("/workbench/activity/getUserList.do".equals(path)) {
			getUserList(request, response);
		} else if ("/workbench/activity/save.do".equals(path)) {
			save(request, response);
		} else if ("/workbench/activity/pageList.do".equals(path)) {
			pageList(request, response);
		} else if ("/workbench/activity/delete.do".equals(path)) {
			delete(request, response);
		} else if ("/workbench/activity/getAll.do".equals(path)) {
			getAll(request, response);
		} else if ("/workbench/activity/update.do".equals(path)) {
			update(request, response);
		} else if ("/workbench/activity/detail.do".equals(path)) {
			detail(request, response);
		} else if ("/workbench/activity/showRemakListById.do".equals(path)) {
			showRemakListById(request, response);
		} else if ("/workbench/activity/deleteRemark.do".equals(path)) {
			deleteRemark(request, response);
		} else if ("/workbench/activity/saveRemark.do".equals(path)) {
			saveRemark(request, response);
		} else if ("/workbench/activity/updateRemark.do".equals(path)) {
			updateRemark(request, response);
		}
	}

	private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入修改备注内容");
		String id = request.getParameter("id");
		String noteContent = request.getParameter("noteContent");
		String editTime = DateTimeUtil.getSysTime();
		String editBy = ((User) request.getSession().getAttribute("user")).getName();
		String editFlag = "1";

		ActivityRemark ar = new ActivityRemark();
		ar.setId(id);
		ar.setNoteContent(noteContent);
		ar.setEditTime(editTime);
		ar.setEditBy(editBy);
		ar.setEditFlag(editFlag);

		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		boolean flag = as.updateRemark(ar);

		Map<String, Object> map = new HashMap<>();
		map.put("success", flag);
		map.put("ar", ar);

		PrintJson.printJsonObj(response, map);
	}

	private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入添加活动备注");
		String noteContent = request.getParameter("noteContent");
		String activityId = request.getParameter("activityId");
		String id = UUIDUtil.getUUID();
		String createTime = DateTimeUtil.getSysTime();
		String createBy = ((User) request.getSession().getAttribute("user")).getName();
		String editFlag = "0";

		ActivityRemark ar = new ActivityRemark();
		ar.setId(id);
		ar.setNoteContent(noteContent);
		ar.setActivityId(activityId);
		ar.setCreateTime(createTime);
		ar.setCreateBy(createBy);
		ar.setEditFlag(editFlag);

		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		boolean flag = as.saveRemark(ar);

		Map<String, Object> map = new HashMap<>();
		map.put("success", flag);
		map.put("ar", ar);

		PrintJson.printJsonObj(response, map);
	}

	private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入删除活动备注");
		String id = request.getParameter("id");
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		boolean flag = as.deleteRemark(id);
		PrintJson.printJsonFlag(response, flag);
	}

	private void showRemakListById(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入查询活动备注");
		String aid = request.getParameter("activityId");
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		List<ActivityRemark> arList = as.showRemakListById(aid);
		PrintJson.printJsonObj(response, arList);
	}

	private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("进入活动详细页");

		String id = request.getParameter("id");
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		Activity a = as.detail(id);

		request.setAttribute("a", a);
		request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);

	}

	private void update(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入修改活动");
		String id = request.getParameter("id");
		String owner = request.getParameter("owner");
		String name = request.getParameter("name");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String cost = request.getParameter("cost");
		String description = request.getParameter("description");
		String editTime = DateTimeUtil.getSysTime();
		String editBy = ((User) request.getSession().getAttribute("user")).getName();

		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

		Activity a = new Activity();
		a.setId(id);
		a.setOwner(owner);
		a.setName(name);
		a.setCost(cost);
		a.setStartDate(startDate);
		a.setEndDate(endDate);
		a.setDescription(description);
		a.setEditTime(editTime);
		a.setEditBy(editBy);

		boolean flag = as.update(a);
		PrintJson.printJsonFlag(response, flag);
	}

	private void getAll(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入查询用户信息及活动");

		String id = request.getParameter("id");
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

		Map<String, Object> map = as.getAll(id);
		PrintJson.printJsonObj(response, map);
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入删除市场活动");
		String[] ids = request.getParameterValues("id");

		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

		boolean flag = as.delete(ids);
		PrintJson.printJsonFlag(response, flag);

	}

	private void pageList(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入查询市场活动信息列表");

		String name = request.getParameter("name");
		String owner = request.getParameter("owner");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String pageNoStr = request.getParameter("pageNo");
		int pageNo = Integer.valueOf(pageNoStr);
		String pageSizeStr = request.getParameter("pageSize");
		int pageSize = Integer.valueOf(pageSizeStr);
		int skipCount = (pageNo - 1) * pageSize;

		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("owner", owner);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("skipCount", skipCount);
		map.put("pageSize", pageSize);

		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

		// 分页查询需要多处使用，创建VO类
		PaginationVo<Activity> vo = as.pageList(map);

		PrintJson.printJsonObj(response, vo);

	}

	private void save(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("添加市场活动");
		String id = UUIDUtil.getUUID();
		String owner = request.getParameter("owner");
		String name = request.getParameter("name");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String cost = request.getParameter("cost");
		String description = request.getParameter("description");
		String createTime = DateTimeUtil.getSysTime();
		String createBy = ((User) request.getSession().getAttribute("user")).getName();

		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

		Activity a = new Activity();
		a.setId(id);
		a.setOwner(owner);
		a.setName(name);
		a.setCost(cost);
		a.setStartDate(startDate);
		a.setEndDate(endDate);
		a.setDescription(description);
		a.setCreateTime(createTime);
		a.setCreateBy(createBy);

		boolean flag = as.save(a);
		PrintJson.printJsonFlag(response, flag);


	}

	private void getUserList(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("取得用户列表");
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

		List<User> uList = us.getUserList();
		PrintJson.printJsonObj(response, uList);


	}

}
