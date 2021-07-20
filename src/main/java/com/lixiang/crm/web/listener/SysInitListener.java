package com.lixiang.crm.web.listener;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.lixiang.crm.settings.domain.DicValue;
import com.lixiang.crm.settings.service.DicService;
import com.lixiang.crm.settings.service.impl.DicServiceImpl;
import com.lixiang.crm.utils.ServiceFactory;

public class SysInitListener implements ServletContextListener {

	/*
	 * 当服务器启动，上下文作用域对象创建 立即执行该方法 event：取得监听的对象
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("服务器缓存处理数据字典开始");

		ServletContext application = event.getServletContext();

		DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

		Map<String, List<DicValue>> map = ds.getAll();
		Set<String> set = map.keySet();
		for (String key : set) {
			application.setAttribute(key, map.get(key));
		}

		// ----------------------------------------------------
		// 处理Stage2possibility.properties文件

		Map<String, String> pMap = new HashMap<>();
		ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
		Enumeration<String> keys = rb.getKeys();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			pMap.put(key, value);
		}

		application.setAttribute("pMap", pMap);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
