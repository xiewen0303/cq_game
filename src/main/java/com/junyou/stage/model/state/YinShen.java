/**
 * 
 */
package com.junyou.stage.model.state;

import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.StateType;

/**
 * 隐身
 * @author DaoZheng Yuan
 * 2013-11-1 下午1:28:09
 */
public class YinShen extends AbsState implements IState {
	
	private Long buffGuid;
	private String buffConfigId;
	
	
	
	@Override
	public StateType getType() {
		return StateType.YINSHEN;
	}

	public Long getBuffGuid() {
		return buffGuid;
	}

	public void setBuffGuid(Long buffGuid) {
		this.buffGuid = buffGuid;
	}

	public String getBuffConfigId() {
		return buffConfigId;
	}

	public void setBuffConfigId(String buffConfigId) {
		this.buffConfigId = buffConfigId;
	}

}
