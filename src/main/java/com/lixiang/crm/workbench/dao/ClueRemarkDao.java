package com.lixiang.crm.workbench.dao;

import java.util.List;

import com.lixiang.crm.workbench.domain.ClueRemark;

public interface ClueRemarkDao {

	List<ClueRemark> getRemarkById(String clueId);

	int delete(ClueRemark clueRemark);

}
