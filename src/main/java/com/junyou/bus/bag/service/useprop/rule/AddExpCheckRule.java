package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;
import com.junyou.gameconfig.utils.GoodsCategory;

/**
 * 获得经验
 * @author LiuYu
 * @date 2015-4-24 下午5:40:42
 */
public class AddExpCheckRule implements IUsePropCheckRule {

	private int type;//礼包类型
	
	public AddExpCheckRule(int type){
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

	@Override
	public void propHandle(IUsePropCallBack callback) {
		if(type == GoodsCategory.GD_ADD_EXP){
			callback.addExpHandle();
		}else if(type == GoodsCategory.BFB_ADD_EXP){
			callback.addPerExpHandle();
		}
	}
}
