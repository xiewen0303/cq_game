package com.junyou.stage.model.element.componentlistener;

import com.junyou.cmd.InnerCmdType;
import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.IStateManagerListener;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * 打坐监听器
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-14 下午2:25:30
 */
public class DaZuoListener implements IStateManagerListener{
	
	private IRole role;
	 
	public DaZuoListener(IRole role) {
		super();
		this.role = role;
	}

	@Override
	public void addState(IState state) {
		if(state.getType() == StateType.DAZUO){
			//暂时业务处理都放到外面了
		}
	}

	@Override
	public void removeState(IState state) {
		if(state.getType() == StateType.DAZUO){
			//暂时业务处理都放到外面了
		}
	}

	@Override
	public void replaceState(IState state) {
		if(state.getType() == StateType.DAZUO){
			//取消打坐
			StageMsgSender.send2StageInner(role.getId(), role.getStage().getId(), InnerCmdType.INNER_DAZUO_CANCEL, null);
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
}
