package com.lixiang.crm.workbench.service.impl;

import java.util.List;

import com.lixiang.crm.utils.SqlSessionUtil;
import com.lixiang.crm.workbench.dao.CustomerDao;
import com.lixiang.crm.workbench.domain.Customer;
import com.lixiang.crm.workbench.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {

	private CustomerDao custmoerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

	@Override
	public List<Customer> getCustomerName(String name) {
		List<Customer> cList = custmoerDao.getCustomerName(name);
		return cList;
	}

}
