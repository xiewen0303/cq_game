package com.junyou.bus.branchtask.configure;

public interface IOpenCondition {
	/**
	 * 是否开启
	 * @return
	 */
	boolean isOpen(Object params);
	/**
	 * 是否为开启条件
	 * @param conditionType {@link ConditionType }
	 * @return
	 */
	boolean isType(int conditionType);
}
