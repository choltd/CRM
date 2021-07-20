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
import com.lixiang.crm.workbench.domain.Customer;
import com.lixiang.crm.workbench.domain.Tran;
import com.lixiang.crm.workbench.domain.TranHistory;
import com.lixiang.crm.workbench.service.CustomerService;
import com.lixiang.crm.workbench.service.TranService;
import com.lixiang.crm.workbench.service.impl.CustomerServiceImpl;
import com.lixiang.crm.workbench.service.impl.TranServiceImpl;

public class TranController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getServletPath();
		if ("/workbench/transaction/add.do".equals(path)) {
			add(request, response);
		} else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
			getCustomerName(request, response);
		} else if ("/workbench/transaction/save.do".equals(path)) {
			save(request, response);
		} else if ("/workbench/transaction/detail.do".equals(path)) {
			detail(request, response);
		} else if ("/workbench/transaction/getHistoryByTranId.do".equals(path)) {
			getHistoryByTranId(request, response);
		} else if ("/workbench/transaction/changeStage.do".equals(path)) {
			changeStage(request, response);
		} else if ("/workbench/transaction/getCharts.do".equals(path)) {
			getCharts(request, response);
		}

	}

	private void getCharts(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入制作统计图");
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		Map<String, Object> map = ts.getCharts();

		PrintJson.printJsonObj(response, map);

	}

	private void changeStage(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入阶段改变");
		String id = request.getParameter("id");
		String stage = request.getParameter("stage");
		String money = request.getParameter("money");
		String expectedDate = request.getParameter("expectedDate");
		String editBy = ((User) request.getSession().getAttribute("user")).getName();
		String editTime = DateTimeUtil.getSysTime();

		Tran t = new Tran();
		t.setId(id);
		t.setMoney(money);
		t.setExpectedDate(expectedDate);
		t.setStage(stage);
		t.setEditBy(editBy);
		t.setEditTime(editTime);

		Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
		String possibility = pMap.get(stage);
		t.setPossibility(possibility);

		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		boolean flag = ts.changeStage(t);
		Map<String, Object> map = new HashMap<>();
		map.put("success", flag);
		map.put("t", t);
		PrintJson.printJsonObj(response, map);

	}

	private void getHistoryByTranId(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入查询阶段历史");
		String tranId = request.getParameter("tranId");
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		List<TranHistory> thList = ts.getHistoryByTranId(tranId);
		Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
		for (TranHistory th : thList) {
			String stage = th.getStage();
			String possibility = pMap.get(stage);
			th.setPossibility(possibility);
		}
		PrintJson.printJsonObj(response, thList);

	}

	private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("进入交易详细页");
		String id = request.getParameter("id");
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		Tran t = ts.detail(id);

		String stage = t.getStage();
		Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
		String possibility = pMap.get(stage);
		// 扩充possibility字段
		t.setPossibility(possibility);
		request.setAttribute("t", t);
		request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

	}

	private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("进入添加交易");
		String id = UUIDUtil.getUUID();
		String owner = request.getParameter("owner");
		String money = request.getParameter("money");
		String name = request.getParameter("name");
		String expectedDate = request.getParameter("expectedDate");
		String customerName = request.getParameter("customerName");
		String stage = request.getParameter("stage");
		String type = request.getParameter("type");
		String source = request.getParameter("source");
		String activityId = request.getParameter("activityId");
		String contactsId = request.getParameter("contactsId");
		String createBy = ((User) request.getSession().getAttribute("user")).getName();
		String createTime = DateTimeUtil.getSysTime();
		String description = request.getParameter("description");
		String contactSummary = request.getParameter("contactSummary");
		String nextContactTime = request.getParameter("nextContactTime");

		Tran t = new Tran();
		t.setId(id);
		t.setOwner(owner);
		t.setMoney(money);
		t.setName(name);
		t.setExpectedDate(expectedDate);
		t.setStage(stage);
		t.setType(type);
		t.setSource(source);
		t.setActivityId(activityId);
		t.setContactsId(contactsId);
		t.setCreateBy(createBy);
		t.setCreateTime(createTime);
		t.setDescription(description);
		t.setContactSummary(contactSummary);
		t.setNextContactTime(nextContactTime);

		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		boolean flag = ts.save(t, customerName);
		if (flag) {
			// 重定向
			response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
		}

	}

	private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入查询客户名称");
		String name = request.getParameter("name");
		CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
		List<Customer> cList = cs.getCustomerName(name);
		PrintJson.printJsonObj(response, cList);
	}

	private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("进入跳转交易添加");
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
		List<User> uList = us.getUserList();

		request.setAttribute("uList", uList);
		request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
	}

}
