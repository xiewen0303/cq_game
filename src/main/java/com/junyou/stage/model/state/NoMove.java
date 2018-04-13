/**
 * 
 */
package com.junyou.stage.model.state;

import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.StateType;

/**
 * @description 不可移动状态
 * @author ShiJie Chi
 * @created 2011-12-27上午9:25:14
 */
public class NoMove extends AbsState implements IState {
	
	@Override
	public StateType getType() {
		return StateType.NO_MOVE;
	}

}
