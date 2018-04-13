package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;
/**
 *坐骑
 * @author lxn
 *
 */
public class WuQiSjCheckRule implements IUsePropCheckRule {

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.wuqiSj();
	}
}
