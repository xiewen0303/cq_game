package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;

/**
 * 打开获得对应礼包
 * 
 * @author chenjianye
 * @date 2015-04-23
 */
public class OpenGiftCardCheckRule implements IUsePropCheckRule {

	private int type;//礼包类型
	
	public OpenGiftCardCheckRule(int type){
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

	@Override
	public void propHandle(IUsePropCallBack callback) {
		callback.openGiftCardPropHandler(type);
	}
}
