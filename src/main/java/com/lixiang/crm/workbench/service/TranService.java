package com.lixiang.crm.workbench.service;

import java.util.List;
import java.util.Map;

import com.lixiang.crm.workbench.domain.Tran;
import com.lixiang.crm.workbench.domain.TranHistory;

public interface TranService {

	boolean save(Tran t, String customerName);

	Tran detail(String id);

	List<TranHistory> getHistoryByTranId(String tranId);

	boolean changeStage(Tran t);

	Map<String, Object> getCharts();

}
