package com.lixiang.crm.settings.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lixiang.crm.settings.dao.DicTypeDao;
import com.lixiang.crm.settings.dao.DicValueDao;
import com.lixiang.crm.settings.domain.DicType;
import com.lixiang.crm.settings.domain.DicValue;
import com.lixiang.crm.settings.service.DicService;
import com.lixiang.crm.utils.SqlSessionUtil;

public class DicServiceImpl implements DicService {
	private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
	private DicValueDao dicValueDao = (DicValueDao) SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

	@Override
	public Map<String, List<DicValue>> getAll() {
		Map<String, List<DicValue>> map = new HashMap<>();
		List<DicType> dtList = dicTypeDao.getTypeList();
		for (DicType dicType : dtList) {
			String code = dicType.getCode();
			List<DicValue> dvList = dicValueDao.getListByCode(code);
			map.put(code, dvList);
		}
		return map;
	}
}
