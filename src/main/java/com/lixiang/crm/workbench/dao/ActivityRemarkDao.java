package com.lixiang.crm.workbench.dao;

import java.util.List;

import com.lixiang.crm.workbench.domain.ActivityRemark;

public interface ActivityRemarkDao {


	int deleteById(String[] ids);

	int getCountByAid(String[] ids);

	List<ActivityRemark> showRemakListById(String aid);

	boolean deleteRemark(String id);

	int saveRemark(ActivityRemark ar);

	int updateRemark(ActivityRemark ar);

}
