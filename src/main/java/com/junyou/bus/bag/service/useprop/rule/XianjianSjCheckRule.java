package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;
/**
 * 糖宝武器  天工
 */
public class XianjianSjCheckRule implements IUsePropCheckRule {
	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.tbWuqiSj();
	}
}
