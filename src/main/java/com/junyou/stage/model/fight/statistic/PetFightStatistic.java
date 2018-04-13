package com.junyou.stage.model.fight.statistic;

import com.junyou.cmd.ClientCmdType;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.StageFightOutputWrapper;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.tunnel.IMsgWriter;

public class PetFightStatistic  extends AbsFightStatistic {

	public PetFightStatistic(IFighter fighter) {
		super(fighter);
		
	}
	@Override
	public void flushChanges(IMsgWriter msgWriter) {
		
		//分客户端，队伍，公会推送变化
		try{
			
			IStage stage = fighter.getStage();
			Pet pet = (Pet) fighter;
			
			Long[] roleIds = null;
			if(stage != null){
				roleIds = fighter.getStage().getSurroundRoleIds(fighter.getPosition());
			}else{
				roleIds = new Long[]{pet.getOwner().getId()};
			}
			
			
			if(hpChanged){
				//通知周围玩家血量
				msgWriter.writeMsg(roleIds, ClientCmdType.HP_CHANGE, StageFightOutputWrapper.hpChange(fighter));
			}
			
			fightStateChange(msgWriter, pet);
			
			attributeChange(msgWriter);

			if(null != addBuffs || null != removeBuffs){
				//通知周围玩家buff改变
//				msgWriter.writeMsg(roleIds, StageCommands.ADD_BUFF,StageFightOutputWrapper.buffChange(fighter,addBuffs,removeBuffs));
			}
			
			
			
		}catch(Exception e){
			ChuanQiLog.error("flush error",e);
		}finally{
			clear();
		}
				
	}
	
	private void fightStateChange(IMsgWriter msgWriter,Pet pet){
		
		if(outFightState){
//			msgWriter.writeMsg(pet.getOwner().getId(), StageCommands.FIGHT_STATE_END, StageOutputWrapper.fightEnd(fighter));
		}
		
		if(!dead){
			if(inFightState){
//				msgWriter.writeMsg(pet.getOwner().getId(), StageCommands.FIGHT_STATE_START, StageFightOutputWrapper.inFight(fighter));
			}
		}
	}
	
	private void attributeChange(IMsgWriter msgWriter){
		Pet pet = (Pet) fighter;
		
		if(null != changedAttribute ){
//			属性变化
			msgWriter.writeMsg(pet.getOwner().getId(), ClientCmdType.TANGBAO_ATTRIBUTE_CHANGE, changedAttribute);
		
		}
		
	}
}
