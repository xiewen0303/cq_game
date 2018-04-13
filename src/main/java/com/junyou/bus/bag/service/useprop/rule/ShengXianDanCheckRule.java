package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;
/**
 * 升仙丹
 * @author LiuYu
 * @date 2015-6-3 下午5:54:50
 */
public class ShengXianDanCheckRule implements IUsePropCheckRule {

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.shengxiandan();
	}
}
