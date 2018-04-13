package com.junyou.stage.model.element.biaoche;

import com.junyou.cmd.ClientCmdType;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.StageFightOutputWrapper;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.fight.statistic.AbsFightStatistic;
import com.junyou.stage.tunnel.IMsgWriter;

public class BiaoCheFightStatistic  extends AbsFightStatistic {

	public BiaoCheFightStatistic(IFighter fighter) {
		super(fighter);
		
	}
	@Override
	public void flushChanges(IMsgWriter msgWriter) {
		
		//分客户端，队伍，公会推送变化
		try{
			
			IStage stage = fighter.getStage();
			Biaoche biaoChe = (Biaoche) fighter;
			
			Long[] roleIds = null;
			if(stage != null){
				roleIds = fighter.getStage().getSurroundRoleIds(fighter.getPosition());
			}else{
				roleIds = new Long[]{biaoChe.getOwner().getId()};
			}
			
			
			if(hpChanged){
				//通知周围玩家血量
				msgWriter.writeMsg(roleIds, ClientCmdType.HP_CHANGE, StageFightOutputWrapper.hpChange(fighter));
			}
			
			fightStateChange(msgWriter, biaoChe);
			
		}catch(Exception e){
			ChuanQiLog.error("flush error",e);
		}finally{
			clear();
		}
				
	}
	
	private void fightStateChange(IMsgWriter msgWriter,Biaoche biaoChe){
		
		if(outFightState){
//			msgWriter.writeMsg(biaoChe.getOwner().getId(), StageCommands.FIGHT_STATE_END, StageOutputWrapper.fightEnd(fighter));
		}
		
		if(!dead){
			if(inFightState){
//				msgWriter.writeMsg(biaoChe.getOwner().getId(), StageCommands.FIGHT_STATE_START, StageFightOutputWrapper.inFight(fighter));
			}
		}
	}
}
