package com.lixiang.crm.workbench.service.impl;

import java.util.List;

import com.lixiang.crm.utils.SqlSessionUtil;
import com.lixiang.crm.workbench.dao.ContactsDao;
import com.lixiang.crm.workbench.domain.Contacts;
import com.lixiang.crm.workbench.service.ContactsService;

public class ContactsServiceImpl implements ContactsService {

	private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);

	@Override
	public List<Contacts> getContactsListByName(String cname) {
		List<Contacts> cList = contactsDao.getContactsListByName(cname);
		return cList;
	}

}
