package com.lixiang.crm.workbench.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lixiang.crm.utils.DateTimeUtil;
import com.lixiang.crm.utils.SqlSessionUtil;
import com.lixiang.crm.utils.UUIDUtil;
import com.lixiang.crm.workbench.dao.CustomerDao;
import com.lixiang.crm.workbench.dao.TranDao;
import com.lixiang.crm.workbench.dao.TranHistoryDao;
import com.lixiang.crm.workbench.domain.Customer;
import com.lixiang.crm.workbench.domain.Tran;
import com.lixiang.crm.workbench.domain.TranHistory;
import com.lixiang.crm.workbench.service.TranService;

public class TranServiceImpl implements TranService {
	private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
	private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
	private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

	@Override
	public boolean save(Tran t, String customerName) {
		/*
		 * 交易添加业务： 缺少客户id 先处理客户相关的需求 1.根据customerName，查询表中是否有该客户，有就取出客户id，没有就为该客户新建客户信息
		 * 2.执行添加交易操作 3.创建交易历史
		 */
		boolean flag = true;
		Customer cus = customerDao.getCustomerByName(customerName);
		if (cus == null) {
			cus = new Customer();
			cus.setId(UUIDUtil.getUUID());
			cus.setName(t.getName());
			cus.setOwner(t.getOwner());
			cus.setNextContactTime(t.getNextContactTime());
			cus.setCreateBy(t.getCreateBy());
			cus.setCreateTime(DateTimeUtil.getSysTime());
			int count1 = customerDao.save(cus);
			if (count1 != 1) {
				flag = false;
			}
		}
		t.setCustomerId(cus.getId());

		int count2 = tranDao.save(t);
		if (count2 != 1) {
			flag = false;
		}

		TranHistory th = new TranHistory();
		th.setId(UUIDUtil.getUUID());
		th.setTranId(t.getId());
		th.setStage(t.getStage());
		th.setMoney(t.getMoney());
		th.setExpectedDate(t.getExpectedDate());
		th.setCreateBy(t.getCreateBy());
		th.setCreateTime(DateTimeUtil.getSysTime());

		int count3 = tranHistoryDao.save(th);
		if (count3 != 1) {
			flag = false;
		}

		return flag;
	}

	@Override
	public Tran detail(String id) {
		Tran t = tranDao.detail(id);
		return t;
	}

	@Override
	public List<TranHistory> getHistoryByTranId(String tranId) {
		List<TranHistory> thList = tranHistoryDao.getHistoryByTranId(tranId);
		return thList;
	}

	@Override
	public boolean changeStage(Tran t) {
		boolean flag = true;
		int count1 = tranDao.changeStage(t);
		if (count1 != 1) {
			flag = false;
		}
		TranHistory th = new TranHistory();
		th.setId(UUIDUtil.getUUID());
		th.setMoney(t.getMoney());
		th.setStage(t.getStage());
		th.setCreateBy(t.getEditBy());
		th.setExpectedDate(t.getExpectedDate());
		th.setCreateTime(DateTimeUtil.getSysTime());
		th.setTranId(t.getId());

		int count2 = tranHistoryDao.save(th);
		if (count2 != 1) {
			flag = false;
		}
		return flag;
	}

	@Override
	public Map<String, Object> getCharts() {
		// 获得dataList
		List<Map<String, Object>> dataList = tranDao.getCharts();
		Map<String, Object> map = new HashMap<>();
		map.put("dataList", dataList);
		return map;
	}
}
