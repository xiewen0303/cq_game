/**
 * 
 */
package com.junyou.stage.model.core.state;

/**
 * @description 状态类型
 *
 * @author ShiJie Chi
 * @created 2011-12-27上午9:58:01
 */
public enum StateType {
	LIVE(0),
	DEAD(2),
	XUNLUO(3),
	BACK(4),
	FIGHT(5),
	ZUOQI(12),
	YINSHEN(20),  //隐身
	NO_MOVE(40),
	NO_ATTACK(41),
	NO_ATTACKED(42),
	KONGJU(21),
	HUNMI(22),
	BINGDONG(23),
	SHIHUA(24),
	MABI(25),
	JINGU(26),
	JITUI(27),
	WUDI_NOMOVE(28),
	BIAN_XING(29),
	CHENMO(31),
	WUDI(32),
	DAZUO(33),
	MOVE(34);
	
	public final Integer val;
	
	private StateType(Integer val) {
		this.val = val;
	}
	
	public Integer getVal(){
		return val;
	}
	
	
}
