package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;
/**
 * 眉心之血
 * @author LiuYu
 * @date 2015-6-2 下午4:56:40
 */
public class MXZXCheckRule implements IUsePropCheckRule {

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.tangbaoMXZXHandle();
	}

}
