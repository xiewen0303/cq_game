package com.junyou.stage.model.element.componentlistener;

import com.junyou.constants.GameConstants;
import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.IStateManagerListener;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.tunnel.StageMsgSender;

public class TutengMonsterStateListener implements IStateManagerListener {
	
	private IMonster monster;
	private short cmd;
	
	public TutengMonsterStateListener(IMonster monster,short cmd) {
		this.monster = monster;
		this.cmd = cmd;
	}
	

	@Override
	public void addState(IState state) {
		if(StateType.DEAD.equals(state.getType()) && cmd != 0){
			StageMsgSender.send2Bus(GameConstants.DEFAULT_ROLE_ID, cmd, monster.getStage().getId());
		}
	}

	@Override
	public void removeState(IState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replaceState(IState state) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clearState(IState state) {
		// TODO Auto-generated method stub
		
	}

}
