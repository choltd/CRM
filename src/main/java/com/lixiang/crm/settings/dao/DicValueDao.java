package com.lixiang.crm.settings.dao;

import java.util.List;

import com.lixiang.crm.settings.domain.DicValue;

public interface DicValueDao {

	List<DicValue> getListByCode(String code);

}
