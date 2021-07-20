package com.lixiang.crm.workbench.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lixiang.crm.utils.PrintJson;
import com.lixiang.crm.utils.ServiceFactory;
import com.lixiang.crm.workbench.domain.Contacts;
import com.lixiang.crm.workbench.service.ContactsService;
import com.lixiang.crm.workbench.service.impl.ContactsServiceImpl;

public class ContactsController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getServletPath();
		if ("/workbench/contacts/getContactsListByName.do".equals(path)) {
			getContactsListByName(request, response);
		} else if ("/workbench/contacts/xxx.do".equals(path)) {
//			getCustomerName(request, response);
		}

	}

	private void getContactsListByName(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入查询联系人列表");
		String cname = request.getParameter("cname");
		ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
		List<Contacts> cList = cs.getContactsListByName(cname);
		PrintJson.printJsonObj(response, cList);
	}



}
