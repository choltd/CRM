package com.lixiang.crm.workbench.dao;

import java.util.List;

import com.lixiang.crm.workbench.domain.Contacts;

public interface ContactsDao {

	int save(Contacts con);

	List<Contacts> getContactsListByName(String cname);

}
