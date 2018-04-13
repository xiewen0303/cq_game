package com.junyou.stage.model.core.heartbeat;

import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.utils.lottery.Lottery;

/**
 * AI 心跳时间计算
 *@author  DaoZheng Yuan
 *@created 2013-3-28下午4:47:45
 */
public class AiHeartBeatTimeCarlc {

	public static int calcTFMonsterHeartBeatTime(IMonster monster,MonsterConfig monsterConfig){
//		int rank = monsterConfig.getRank();
		
		if(monsterConfig.getHeartTime() < 1000){
			return 1000+Lottery.roll(100);
		}
		
		return monsterConfig.getHeartTime()+Lottery.roll(100);
		//return 1000+Lottery.roll(1000);
	}
	
	public static int calcMonsterHeartBeatTime(IMonster monster,MonsterConfig monsterConfig){
		int rank = monsterConfig.getRank();
		long speed = monster.getFightAttribute().getSpeed();
		if(speed < 200){
			speed = 200;
		}
		
		//战斗状态
		if(monster.getStateManager().contains(StateType.FIGHT)){
			int heartTime = monsterConfig.getHeartTime();
			if(heartTime < 300){
				heartTime = 1600;
			}
			
			return heartTime + Lottery.roll(100);
		}
		//巡逻状态
		if(monster.getStateManager().contains(StateType.XUNLUO)){
			if(rank == 2){
				return 2000 + Lottery.roll(1000);
			}else{
				return 4000 + Lottery.roll(3000);
			}
		}
		
		
		if(rank == 2){
			return 500;
		}else{
			return 1000;
		}
	}
	
	public static int calcHeartBeatTime(IFighter fighter,int oldX,int oldY){
		
		//战斗状态
		if(fighter.getStateManager().contains(StateType.FIGHT)){
			int heart = fighter.getHeartTime();
			if(heart < 300){
				return 300;
			}else{
				return heart;
			}
		}
		
		//巡逻状态
		if(fighter.getStateManager().contains(StateType.XUNLUO)){
			return 10000 + Lottery.roll(5000);
		}
		float ydsd = fighter.getFightAttribute().getSpeed();
		if(ydsd < 100){
			ydsd = 100;
		}
		
		int targetX = fighter.getPosition().getX();
		int targetY = fighter.getPosition().getY();
		
		Float finalValue = 1350 * 60 / ydsd;
		if(targetX != oldX && targetY != oldY){
			//斜角计算
			finalValue = 1909 * 60 / ydsd;
		}
		
//		finalValue = finalValue * 1.2f;
		
//		System.out.println(fighter.getElementType()+","+finalValue.intValue());
		return finalValue.intValue();
		
	}
}
