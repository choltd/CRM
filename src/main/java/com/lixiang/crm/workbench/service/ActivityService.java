package com.lixiang.crm.workbench.service;

import java.util.List;
import java.util.Map;

import com.lixiang.crm.vo.PaginationVo;
import com.lixiang.crm.workbench.domain.Activity;
import com.lixiang.crm.workbench.domain.ActivityRemark;

public interface ActivityService {

	boolean save(Activity a);

	PaginationVo<Activity> pageList(Map<String, Object> map);

	boolean delete(String[] ids);

	Map<String, Object> getAll(String id);

	boolean update(Activity a);

	Activity detail(String id);

	List<ActivityRemark> showRemakListById(String aid);

	boolean deleteRemark(String id);

	boolean saveRemark(ActivityRemark ar);

	boolean updateRemark(ActivityRemark ar);

	List<Activity> getActivityCLueById(String clueId);

	List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

	List<Activity> getActivityListByName(String aname);

}
