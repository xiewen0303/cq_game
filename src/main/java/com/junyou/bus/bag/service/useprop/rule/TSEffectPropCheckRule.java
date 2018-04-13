package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;
/**
 * 特殊效果道具
 * @author LiuYu
 * @date 2015-4-14 下午3:54:16
 */
public class TSEffectPropCheckRule implements IUsePropCheckRule {

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.tsXiaoGuoPropHandle();
	}
}
