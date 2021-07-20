package com.lixiang.crm.workbench.dao;

import java.util.List;

import com.lixiang.crm.workbench.domain.TranHistory;

public interface TranHistoryDao {

	int save(TranHistory th);

	List<TranHistory> getHistoryByTranId(String tranId);

}
