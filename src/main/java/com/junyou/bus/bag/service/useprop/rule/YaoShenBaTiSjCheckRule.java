package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;
public class YaoShenBaTiSjCheckRule implements IUsePropCheckRule {

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.yaoShenSj();
	}
}
