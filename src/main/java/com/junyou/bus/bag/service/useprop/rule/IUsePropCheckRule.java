package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;

/**
 * 使用道具检测
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-4 上午11:19:55 
 */
public interface IUsePropCheckRule {
	/**
	 * 处理
	 */
	public void propHandle(IUsePropCallBack callback);
}
