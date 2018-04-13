package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;
/**
 * 糖宝战甲 天裳
 * @author lxn
 *
 */
public class ZhanjiaSjCheckRule implements IUsePropCheckRule {

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.tbZhanjiaSj();
	}
}
