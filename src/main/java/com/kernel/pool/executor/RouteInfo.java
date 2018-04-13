package com.kernel.pool.executor;

/**
 * @description 消息路由信息载体
 * @author hehj
 * 2010-11-10 下午02:42:03
 */
public class RouteInfo {

	private String group;
	private String info;
	
	public RouteInfo(String group) {
		this.group = group;
	}

	/**
	 * @return 消息路由分组信息
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @return 消息路由组内信息
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * 设置消息路由组内信息
	 * @param info
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	
	@Override
	public String toString() {
		return group + " " + info;
	}
}
