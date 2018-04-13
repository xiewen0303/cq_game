package com.junyou.stage.model.element.componentlistener;

import org.springframework.stereotype.Component;

import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.IStateManagerListener;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.state.YinShen;
import com.junyou.stage.tunnel.DirectMsgWriter;

/**
 * @description 角色状态异常监听器
 * @author ShiJie Chi
 * @date 2012-8-10 上午11:01:36 
 */
@Component
public class RoleStateExceptionListener implements IStateManagerListener {
	
	private IRole role;
	
	public RoleStateExceptionListener(){
		
	}
	
	public RoleStateExceptionListener(IRole role) {
		this.role = role;
	}

	@Override
	public void addState(IState state) {
		StateType type = state.getType();
		switch (type) {
		
		case DEAD:
			
//			if(role.getStateManager().contains(StateType.ZUOQI)){
//				//死亡坐骑被强制清除
//				StageMsgSender.send2Bus(role.getId(), StageCommands.UNDER_ZUOQI, new Object[]{role.getId()});
//			}
			//开启强制复活
			role.startBackFuHuoSchedule();
			break;
			
//		case YINSHEN:
//			
//			//隐身会被强制清除战斗状态
//			role.getStateManager().remove(StateType.FIGHT);
//			break;	
//			
//		case FIGHT:
//			
//			//战斗状态删除隐身状态
//			role.getStateManager().remove(StateType.YINSHEN);
		}
		
			
		
	}

	@Override
	public void removeState(IState state) {
		StateType type = state.getType();
		switch (type) {
			
		case YINSHEN:
			
			//隐身去除时,buff也要删除
			YinShen ys = (YinShen) state;
			role.getBuffManager().removeBuff(ys.getBuffGuid(), ys.getBuffConfigId());
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			break;
		}
	}

	@Override
	public void replaceState(IState state) {
		
		StateType type = state.getType();
	}

	@Override
	public void clearState(IState state) {
		// TODO Auto-generated method stub
		
	}

}
