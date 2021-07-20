package com.lixiang.crm.workbench.dao;

import java.util.List;
import java.util.Map;

import com.lixiang.crm.settings.domain.User;
import com.lixiang.crm.workbench.domain.Activity;

public interface ActivityDao {

	User login(Map<String, String> map);


	int save(Activity a);

	int getTotalByCondition(Map<String, Object> map);

	List<Activity> getActivityListByCondition(Map<String, Object> map);

	int delete(String[] ids);

	Activity getActivityById(String id);

	int update(Activity a);

	Activity detail(String id);

	List<Activity> getActivityCLueById(String clueId);

	List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

	List<Activity> getActivityListByName(String aname);

}
