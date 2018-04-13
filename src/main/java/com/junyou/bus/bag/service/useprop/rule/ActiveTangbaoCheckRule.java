package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;
/**
 * 激活糖宝
 * @author LiuYu
 * @date 2015-5-12 下午5:15:07
 */
public class ActiveTangbaoCheckRule implements IUsePropCheckRule {

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.activeTangbao();
	}
}
