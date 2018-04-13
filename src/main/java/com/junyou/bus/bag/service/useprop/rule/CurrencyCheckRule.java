package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;

/**
 * 增加对应货币值
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-4 下午2:08:40
 */
public class CurrencyCheckRule implements IUsePropCheckRule {

	private int type;//货币类型
	
	public CurrencyCheckRule(int type){
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.addCurrency(type);
	}
}
