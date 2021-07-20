package com.lixiang.crm.workbench.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lixiang.crm.settings.dao.UserDao;
import com.lixiang.crm.settings.domain.User;
import com.lixiang.crm.utils.SqlSessionUtil;
import com.lixiang.crm.vo.PaginationVo;
import com.lixiang.crm.workbench.dao.ActivityDao;
import com.lixiang.crm.workbench.dao.ActivityRemarkDao;
import com.lixiang.crm.workbench.domain.Activity;
import com.lixiang.crm.workbench.domain.ActivityRemark;
import com.lixiang.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
	private ActivityDao activityDao = (ActivityDao) SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
	private ActivityRemarkDao activityRemarkDao = (ActivityRemarkDao) SqlSessionUtil.getSqlSession()
			.getMapper(ActivityRemarkDao.class);
	private UserDao userDao = (UserDao) SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

	@Override
	public boolean save(Activity a) {
		boolean flag = true;
		int count = activityDao.save(a);

		if (count != 1) {
			flag = false;
		}
		return flag;
	}

	@Override
	public PaginationVo<Activity> pageList(Map<String, Object> map) {
		int total = activityDao.getTotalByCondition(map);
		List<Activity> dataList = activityDao.getActivityListByCondition(map);

		PaginationVo<Activity> vo = new PaginationVo<>();
		vo.setTotal(total);
		vo.setDataList(dataList);

		return vo;
	}

	@Override
	public boolean delete(String[] ids) {

		boolean flag = true;
		// 查询活动备注表
		int count1 = activityRemarkDao.getCountByAid(ids);
		// 删除关联的备注
		int count2 = activityRemarkDao.deleteById(ids);

		if (count1 != count2) {
			flag = false;
		}
		// 删除活动
		int count3 = activityDao.delete(ids);

		if (count3 != ids.length) {
			flag = false;
		}
		return flag;

	}

	@Override
	public Map<String, Object> getAll(String id) {
		Map<String, Object> map = new HashMap<>();
		// 查询用户
		List<User> uList = userDao.getUserList();
		// 根据id查单条
		Activity a = activityDao.getActivityById(id);
		map.put("uList", uList);
		map.put("a", a);
		return map;
	}

	@Override
	public boolean update(Activity a) {
		boolean flag = true;
		int count = activityDao.update(a);

		if (count != 1) {
			flag = false;
		}
		return flag;
	}

	@Override
	public Activity detail(String id) {
		Activity a = activityDao.detail(id);

		return a;
	}

	@Override
	public List<ActivityRemark> showRemakListById(String aid) {
		List<ActivityRemark> arList = activityRemarkDao.showRemakListById(aid);
		return arList;
	}

	@Override
	public boolean deleteRemark(String id) {
		boolean flag = activityRemarkDao.deleteRemark(id);
		return flag;
	}

	@Override
	public boolean saveRemark(ActivityRemark ar) {
		boolean flag = true;

		int count = activityRemarkDao.saveRemark(ar);
		if (count != 1) {
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean updateRemark(ActivityRemark ar) {
		boolean flag = true;

		int count = activityRemarkDao.updateRemark(ar);
		if (count != 1) {
			flag = false;
		}
		return flag;
	}

	@Override
	public List<Activity> getActivityCLueById(String clueId) {
		List<Activity> aList = activityDao.getActivityCLueById(clueId);
		return aList;
	}

	@Override
	public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {
		List<Activity> aList = activityDao.getActivityListByNameAndNotByClueId(map);
		return aList;
	}

	@Override
	public List<Activity> getActivityListByName(String aname) {
		List<Activity> aList = activityDao.getActivityListByName(aname);
		return aList;
	}
}
