package com.junyou.utils.money;

import com.junyou.utils.ChuanQiConfigUtil;

public class MoneyUtil {

	/**
	 * 计算充值金额换算成的元宝
	 * @param moneyNum
	 * @return
	 */
	public static int calYb(Double moneyNum){
		Double tmp = Math.round(moneyNum * ChuanQiConfigUtil.getGameYbOdds())*1d;
		return tmp.intValue();
	}
}
