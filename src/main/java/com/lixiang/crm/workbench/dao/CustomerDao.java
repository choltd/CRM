package com.lixiang.crm.workbench.dao;

import java.util.List;

import com.lixiang.crm.workbench.domain.Customer;

public interface CustomerDao {

	Customer getCustomerByName(String company);

	int save(Customer cus);

	List<Customer> getCustomerName(String name);

}
