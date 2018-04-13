package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;

/**
 * 增加真气
 * @author LiuYu
 * @date 2015-4-29 下午10:01:32
 */
public class AddZhenQiCheckRule implements IUsePropCheckRule {

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.addZhenqiHandle();
	}
}
