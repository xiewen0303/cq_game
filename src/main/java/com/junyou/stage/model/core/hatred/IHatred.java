package com.junyou.stage.model.core.hatred;

import com.junyou.stage.model.core.stage.ElementType;

public interface IHatred {

	/**
	 * 获取仇恨者id
	 */
	Long getId();

	/**
	 * 获取仇恨者元素类型
	 */
	ElementType getElementType();

	/**
	 * 增加仇恨值
	 */
	void add(int i);

	/**
	 * 是否满足6秒过期原则
	 */
	boolean out6sRule();

	/**
	 * 获取仇恨值
	 */
	int getVal();
	
	/**
	 * 是否满足expireTime过期原则
	 * @param expireTime 过期周期 ms
	 */
	boolean expired(int expireTime);

	/**
	 * 最后一次更新时间
	 */
	long getlasttime();

}
