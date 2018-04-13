package com.junyou.stage.model.skill.buff.specialeffect;

import com.junyou.cmd.ClientCmdType;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.skill.buff.Buff;
import com.junyou.stage.tunnel.StageMsgSender;

public class SpecialEffectHandlerAdapter implements ISpecialEffectHandler{

	@Override
	public void triggerHandle(IBuff buff) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void overHandle(Buff buff) {
		IFighter owner = buff.getOwner();
		if(owner != null && owner.getStage() != null){
			StageMsgSender.send2Many(owner.getStage().getSurroundRoleIds(owner.getPosition()), ClientCmdType.XIAOCHU_BUFF, new Object[]{owner.getId(),buff.getId()});
		}
	}

	public void setTargetIds(Object[] targetIds) {
		// TODO Auto-generated method stub
		
	}
	

}
