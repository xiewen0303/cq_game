package com.junyou.stage.model.element.componentlistener;

import java.util.Map; 
import com.junyou.bus.zuoqi.manage.ZuoQi;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.IStateManagerListener;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactoryHelper;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.stage.utils.StageHelper;
import com.junyou.utils.KuafuConfigPropUtil;

public class ZuoQiListener implements IStateManagerListener{
	
	private IRole role;
	 
	public ZuoQiListener(IRole role) {
		super();
		this.role = role;
	}

	@Override
	public void addState(IState state) {
		 if(state.getType() == StateType.ZUOQI){
			 
			 ZuoQi zuoqi = role.getZuoQi();
			 //添加移动速度
			 Map<String, Long> seedAttrs = StageHelper.getZuoQiExportService().getZuoQiSeedAttr(zuoqi);
			 role.getFightAttribute().setBaseAttribute(BaseAttributeType.ZUOQI_SEED, seedAttrs);
			 if(role.getStage() != null){
				 //输出变化
				 role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			 }
		 }
	}

	@Override
	public void removeState(IState state) {
		 if(state.getType() == StateType.ZUOQI){
			 RoleFactoryHelper.clearEquip(role,BaseAttributeType.ZUOQI_SEED,true);	 
			 //输出变化
			 role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		 }
	}

	@Override
	public void replaceState(IState state) {
		//替换掉坐骑状态
		if(state.getType() == StateType.ZUOQI){
			role.getStateManager().remove(StateType.ZUOQI);
			role.getZuoQi().setGetOn(false);
			removeState(state);
			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.ZUOQI_DOWN, role.getId());
			//通知bus修改为下坐骑状态
			if(KuafuConfigPropUtil.isKuafuServer()){
				KuafuMsgSender.send2KuafuSource(role.getId(), InnerCmdType.ZUOQI_BUS_STATE, GameConstants.ZQ_DOWN);
			}else{
				StageMsgSender.send2Bus(role.getId(), InnerCmdType.ZUOQI_BUS_STATE, GameConstants.ZQ_DOWN);
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
}
