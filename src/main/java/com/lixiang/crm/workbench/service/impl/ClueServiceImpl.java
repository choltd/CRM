package com.lixiang.crm.workbench.service.impl;

import java.util.List;

import com.lixiang.crm.utils.DateTimeUtil;
import com.lixiang.crm.utils.SqlSessionUtil;
import com.lixiang.crm.utils.UUIDUtil;
import com.lixiang.crm.workbench.dao.ClueActivityRelationDao;
import com.lixiang.crm.workbench.dao.ClueDao;
import com.lixiang.crm.workbench.dao.ClueRemarkDao;
import com.lixiang.crm.workbench.dao.ContactsActivityRelationDao;
import com.lixiang.crm.workbench.dao.ContactsDao;
import com.lixiang.crm.workbench.dao.ContactsRemarkDao;
import com.lixiang.crm.workbench.dao.CustomerDao;
import com.lixiang.crm.workbench.dao.CustomerRemarkDao;
import com.lixiang.crm.workbench.dao.TranDao;
import com.lixiang.crm.workbench.dao.TranHistoryDao;
import com.lixiang.crm.workbench.domain.Clue;
import com.lixiang.crm.workbench.domain.ClueActivityRelation;
import com.lixiang.crm.workbench.domain.ClueRemark;
import com.lixiang.crm.workbench.domain.Contacts;
import com.lixiang.crm.workbench.domain.ContactsActivityRelation;
import com.lixiang.crm.workbench.domain.ContactsRemark;
import com.lixiang.crm.workbench.domain.Customer;
import com.lixiang.crm.workbench.domain.CustomerRemark;
import com.lixiang.crm.workbench.domain.Tran;
import com.lixiang.crm.workbench.domain.TranHistory;
import com.lixiang.crm.workbench.service.ClueService;

public class ClueServiceImpl implements ClueService {

	// 线索
	private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
	private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession()
			.getMapper(ClueActivityRelationDao.class);
	private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
	// 顾客
	private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
	private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
	// 联系
	private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
	private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
	private ContactsActivityRelationDao contactsAcitivityRelationDao = SqlSessionUtil.getSqlSession()
			.getMapper(ContactsActivityRelationDao.class);
	// 交易
	private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
	private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

	@Override
	public boolean save(Clue c) {
		boolean flag = true;
		int count = clueDao.save(c);
		if (count != 1) {
			flag = false;
		}
		return flag;
	}

	@Override
	public Clue detail(String id) {
		Clue c = clueDao.detail(id);
		return c;
	}

	@Override
	public boolean unbund(String id) {
		boolean flag = true;
		int count = clueActivityRelationDao.unbund(id);
		if (count != 1) {
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean bund(String cid, String[] aids) {
		boolean flag = true;
		for (String aid : aids) {
			ClueActivityRelation car = new ClueActivityRelation();
			car.setId(UUIDUtil.getUUID());
			car.setClueId(cid);
			car.setActivityId(aid);

			int count = clueActivityRelationDao.bund(car);
			if (count != 1) {
				flag = false;
			}
		}

		return flag;

	}

	@Override
	public boolean convert(String clueId, Tran t, String createBy) {
		boolean flag = true;
		String createTime = DateTimeUtil.getSysTime();
//		(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
		Clue c = clueDao.getById(clueId);
		String company = c.getCompany();
//		(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
		Customer cus = customerDao.getCustomerByName(company);
		if (cus == null) {
			cus = new Customer();
			cus.setId(UUIDUtil.getUUID());
			cus.setOwner(c.getOwner());
			cus.setName(company);
			cus.setCreateBy(createBy);
			cus.setCreateTime(createTime);
			cus.setPhone(c.getPhone());
			cus.setWebsite(c.getWebsite());
			cus.setDescription(c.getDescription());
			cus.setContactSummary(c.getContactSummary());
			cus.setNextContactTime(c.getNextContactTime());
			cus.setAddress(c.getAddress());

			int count1 = customerDao.save(cus);
			if (count1 != 1) {
				flag = false;
			}

		}
//		(3) 通过线索对象提取联系人信息，保存联系人
		Contacts con = new Contacts();
		con.setId(UUIDUtil.getUUID());
		con.setOwner(c.getOwner());
		con.setSource(c.getSource());
		con.setCustomerId(cus.getId());
		con.setFullname(c.getFullname());
		con.setAppellation(c.getAppellation());
		con.setEmail(c.getEmail());
		con.setMphone(c.getMphone());
		con.setJob(c.getJob());
		con.setCreateBy(createBy);
		con.setCreateTime(createTime);
		con.setDescription(c.getDescription());
		con.setNextContactTime(c.getNextContactTime());
		con.setContactSummary(c.getContactSummary());
		con.setAddress(c.getAddress());
		int count2 = contactsDao.save(con);
		if (count2 != 1) {
			flag = false;
		}
//		(4) 线索备注转换到客户备注以及联系人备注
		List<ClueRemark> clueRemarkList = clueRemarkDao.getRemarkById(clueId);
		for (ClueRemark clueRemark : clueRemarkList) {
			String noteContent = clueRemark.getNoteContent();
			CustomerRemark customerRemark = new CustomerRemark();
			customerRemark.setId(UUIDUtil.getUUID());
			customerRemark.setNoteContent(noteContent);
			customerRemark.setCreateBy(createBy);
			customerRemark.setCreateTime(createTime);
			customerRemark.setCustomerId(cus.getId());
			customerRemark.setEditFlag("0");
			int count3 = customerRemarkDao.save(customerRemark);
			if (count3 != 1) {
				flag = false;
			}

			ContactsRemark contactsRemark = new ContactsRemark();
			contactsRemark.setId(UUIDUtil.getUUID());
			contactsRemark.setNoteContent(noteContent);
			contactsRemark.setCreateBy(createBy);
			contactsRemark.setCreateTime(createTime);
			contactsRemark.setContactsId(con.getId());
			contactsRemark.setEditFlag("0");
			int count4 = contactsRemarkDao.save(contactsRemark);
			if (count4 != 1) {
				flag = false;
			}

		}
//		(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
		List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
		for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
			String activityId = clueActivityRelation.getActivityId();
			ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
			contactsActivityRelation.setId(UUIDUtil.getUUID());
			contactsActivityRelation.setActivityId(activityId);
			contactsActivityRelation.setContactsId(con.getId());

			int count5 = contactsAcitivityRelationDao.save(contactsActivityRelation);
			if (count5 != 1) {
				flag = false;
			}
		}
//		(6) 如果有创建交易需求，创建一条交易
		if (t != null) {
			t.setSource(c.getSource());
			t.setOwner(c.getOwner());
			t.setDescription(c.getDescription());
			t.setNextContactTime(c.getNextContactTime());
			t.setContactsId(con.getId());
			t.setCustomerId(cus.getId());
			t.setContactSummary(c.getContactSummary());

			int count6 = tranDao.save(t);
			if (count6 != 1) {
				flag = false;
			}
//			(7) 如果创建了交易，则创建一条该交易下的交易历史
			TranHistory th = new TranHistory();
			th.setId(UUIDUtil.getUUID());
			th.setTranId(t.getId());
			th.setCreateBy(createBy);
			th.setCreateTime(createTime);
			th.setMoney(t.getMoney());
			th.setExpectedDate(t.getExpectedDate());
			th.setStage(t.getStage());

			int count7 = tranHistoryDao.save(th);
			if (count7 != 1) {
				flag = false;
			}
		}

//		(8) 删除线索备注
		
		for (ClueRemark clueRemark : clueRemarkList) {
			int count8 = clueRemarkDao.delete(clueRemark);
			if (count8 != 1) {
				flag = false;
			}
		}
		// (9)删除线索和市场活动的关系
		for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
			int count9 = clueActivityRelationDao.delete(clueActivityRelation);
			if (count9 != 1) {
				flag = false;
			}
		}
		  
		// (10) 删除线索
		int count10 = clueDao.delete(clueId);
		if (count10 != 1) {
			flag = false;
		}

		return flag;
	}

}
