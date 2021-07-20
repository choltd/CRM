package com.lixiang.crm.workbench.service;

import java.util.List;

import com.lixiang.crm.workbench.domain.Contacts;

public interface ContactsService {

	List<Contacts> getContactsListByName(String cname);

}
