/**
 * 
 */
package com.junyou.stage.model.state;

import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.StateType;

/**
 * @description 不可攻击状态
 * @author ShiJie Chi
 * @created 2011-12-27上午9:25:14
 */
public class NoAttack extends AbsState implements IState {
	
	@Override
	public StateType getType() {
		return StateType.NO_ATTACK;
	}

}
