package com.lixiang.crm.workbench.dao;

import java.util.List;

import com.lixiang.crm.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {

	int unbund(String id);

	int bund(ClueActivityRelation car);

	List<ClueActivityRelation> getListByClueId(String clueId);

	int delete(ClueActivityRelation clueActivityRelation);
	

}
