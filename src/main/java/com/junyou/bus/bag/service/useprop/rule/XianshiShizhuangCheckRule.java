package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;

/**
 * 限时时装
 * @author LiuYu
 * @date 2015-8-19 上午9:52:10
 */
public class XianshiShizhuangCheckRule implements IUsePropCheckRule {

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.xianshiShizhuang();
	}
}
