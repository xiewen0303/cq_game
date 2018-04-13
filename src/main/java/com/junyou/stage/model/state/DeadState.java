/**
 * 
 */
package com.junyou.stage.model.state;

import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.DeadInfo;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-27上午9:25:14
 */
public class DeadState extends AbsState implements IState {
	
	
	public DeadState(IHarm harm) {
		
		DeadInfo deadInfo = new DeadInfo(harm);
		setData(deadInfo);
		
	}
	
	public DeadState(){
		
	}
	
	@Override
	public StateType getType() {
		return StateType.DEAD;
	}

}
