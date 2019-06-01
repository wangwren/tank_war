package com.wangwren.tank;

import java.io.IOException;
import java.util.Properties;

public class PropertyMgr {

	private static final Properties props = new Properties();
	
	//构造方法私有，不允许创建对象
	private PropertyMgr() {}
	
	static {
		try {
			//加载配置文件
			props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取配置文件中的属性值
	 * @param key
	 * @return
	 */
	public static Object getProp(String key) {
		if(props == null) {
			return null;
		}
		return props.get(key);
	}
}
