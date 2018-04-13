package com.junyou.stage.model.element.componentlistener;

import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.IStateManagerListener;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.IRole;

/**
 * 打坐监听器
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-14 下午2:25:30
 */
public class PetListener implements IStateManagerListener{
	
	private IRole role;
	 
	public PetListener(IRole role) {
		super();
		this.role = role;
	}


	@Override
	public void removeState(IState state) {
		if(state.getType() == StateType.DEAD){
			if(role.getPet() != null && role.getStage().isCanHasTangbao()){
				role.getStage().enter(role.getPet(), role.getPosition().getX(), role.getPosition().getY());
			}
		}
	}

	public IRole getRole() {
		return role;
	}

	public void setRole(IRole role) {
		this.role = role;
	}

	@Override
	public void clearState(IState state) {
	}


	@Override
	public void addState(IState state) {
		
	}


	@Override
	public void replaceState(IState state) {
		
	} 
}
