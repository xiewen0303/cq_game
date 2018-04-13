package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;

/**
 * 增加修为
 * @author LiuYu
 * @date 2015-4-24 下午5:40:42
 */
public class AddXiuweiCheckRule implements IUsePropCheckRule {

	
	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.addXiuwei();
	}
}
