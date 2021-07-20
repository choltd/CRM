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
import com.lixiang.crm.workbench.domain.Activity;
import com.lixiang.crm.workbench.domain.Clue;
import com.lixiang.crm.workbench.domain.Tran;
import com.lixiang.crm.workbench.service.ActivityService;
import com.lixiang.crm.workbench.service.ClueService;
import com.lixiang.crm.workbench.service.impl.ActivityServiceImpl;
import com.lixiang.crm.workbench.service.impl.ClueServiceImpl;


public class ClueController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getServletPath();
		if ("/workbench/clue/getUserList.do".equals(path)) {
			getUserList(request, response);
		} else if ("/workbench/clue/save.do".equals(path)) {
			save(request, response);
		} else if ("/workbench/clue/detail.do".equals(path)) {
			detail(request, response);
		} else if ("/workbench/clue/getActivityCLueById.do".equals(path)) {
			getActivityCLueById(request, response);
		} else if ("/workbench/clue/unbund.do".equals(path)) {
			unbund(request, response);
		} else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)) {
			getActivityListByNameAndNotByClueId(request, response);
		} else if ("/workbench/clue/bund.do".equals(path)) {
			bund(request, response);
		} else if ("/workbench/clue/getActivityListByName.do".equals(path)) {
			getActivityListByName(request, response);
		} else if ("/workbench/clue/convert.do".equals(path)) {
			convert(request, response);
		}

	}

	private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("进入转换交易");
		String clueId = request.getParameter("clueId");
		String createBy = ((User) request.getSession().getAttribute("user")).getCreateBy();
		Tran t = null;
		String flag = request.getParameter("flag");
		if ("a".equals(flag)) {
			t = new Tran();
			String money = request.getParameter("money");
			String name = request.getParameter("name");
			String expectedDate = request.getParameter("expectedDate");
			String stage = request.getParameter("stage");
			String activityId = request.getParameter("activityId");
			String id = UUIDUtil.getUUID();
			String createTime = DateTimeUtil.getSysTime();


			t.setCreateBy(createBy);
			t.setCreateTime(createTime);
			t.setExpectedDate(expectedDate);
			t.setId(id);
			t.setActivityId(activityId);
			t.setStage(stage);
			t.setName(name);
			t.setMoney(money);

		}

		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		boolean flag1 = cs.convert(clueId, t, createBy);
		if (flag1) {
			response.sendRedirect(request.getContextPath() + "/workbench/clue/index.jsp");
		}

	}

	private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入查询市场活动列表");
		String aname = request.getParameter("aname");
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		List<Activity> aList = as.getActivityListByName(aname);
		PrintJson.printJsonObj(response, aList);
	}

	private void bund(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入关联活动");
		String cid = request.getParameter("cid");
		String aids[] = request.getParameterValues("aid");
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		boolean flag = cs.bund(cid, aids);
		PrintJson.printJsonFlag(response, flag);

	}

	private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入查询线索关联活动");
		String aname = request.getParameter("aname");
		String clueId = request.getParameter("clueId");
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		Map<String, String> map = new HashMap<>();
		map.put("aname", aname);
		map.put("clueId", clueId);
		List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);
		PrintJson.printJsonObj(response, aList);

	}

	private void unbund(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入解除线索关联活动");
		String id = request.getParameter("id");
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		boolean flag = cs.unbund(id);
		PrintJson.printJsonFlag(response, flag);
	}

	private void getActivityCLueById(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入查询线索关联");
		String clueId = request.getParameter("clueId");

		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		List<Activity> aList = as.getActivityCLueById(clueId);
		PrintJson.printJsonObj(response, aList);

	}

	private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("进入线索详细业");
		String id = request.getParameter("id");
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		Clue c = cs.detail(id);
		request.setAttribute("c", c);
		request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);

	}

	private void save(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入添加线索");
		String id = UUIDUtil.getUUID();
		String fullname = request.getParameter("fullname");
		String appellation = request.getParameter("appellation");
		String owner = request.getParameter("owner");
		String company = request.getParameter("company");
		String job = request.getParameter("job");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String website = request.getParameter("website");
		String mphone = request.getParameter("mphone");
		String state = request.getParameter("state");
		String source = request.getParameter("source");
		String createBy = ((User) request.getSession().getAttribute("user")).getName();
		String createTime = DateTimeUtil.getSysTime();
		String description = request.getParameter("description");
		String contactSummary = request.getParameter("contactSummary");
		String nextContactTime = request.getParameter("nextContactTime");
		String address = request.getParameter("address");

		Clue c = new Clue();
		c.setAddress(address);
		c.setAppellation(appellation);
		c.setCompany(company);
		c.setContactSummary(contactSummary);
		c.setCreateBy(createBy);
		c.setCreateTime(createTime);
		c.setDescription(description);
		c.setEmail(email);
		c.setWebsite(website);
		c.setState(state);
		c.setSource(source);
		c.setPhone(phone);
		c.setOwner(owner);
		c.setNextContactTime(nextContactTime);
		c.setMphone(mphone);
		c.setJob(job);
		c.setId(id);
		c.setFullname(fullname);

		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		boolean flag = cs.save(c);

		PrintJson.printJsonFlag(response, flag);

	}

	private void getUserList(HttpServletRequest request, HttpServletResponse response) {
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
		List<User> userList = us.getUserList();
		PrintJson.printJsonObj(response, userList);
	}
}
