package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;
/**
 * 赎罪药水
 * @author LiuYu
 * @date 2015-6-3 下午5:54:50
 */
public class ShuZuiYaoShuiCheckRule implements IUsePropCheckRule {

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.shuzuiyaoshui();
	}
}
