package com.junyou.state.jingji.entity;

import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.fight.IFightResult;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;

/**
 * 竞技场战斗
 * @author LiuYu
 * @date 2015-3-19 下午4:10:22
 */
public class JingjiFight {
	private long curTime;
	private boolean end;
	private IJingjiFighter loser;
	
	public void setEnd() {
		this.end = true;
	}

	public long getCurTime() {
		return curTime;
	}

	public void setCurTime(long curTime) {
		this.curTime = curTime;
	}
	
	private IJingjiFighter attFighter;
	private IJingjiFighter defFighter;
	private JingjiFightResport fightResport = new JingjiFightResport();
	/**
	 * 初始化选手
	 * @param att	先手方
	 * @param def	后手方
	 */
	public void init(IJingjiFighter att,IJingjiFighter def){
		att.setJingjiFight(this);
		def.setJingjiFight(this);
		if (att.getZpuls() > def.getZpuls()) {
			attFighter = att;
			defFighter = def;
		}else{
			attFighter = def;
			defFighter = att;
		}
	}
	
	public String startFight(){
		while(GameConstants.MAX_FIGHT_TIME > curTime && !end){
			ISkill attSkill = attFighter.getSkill();
			if(attSkill != null){
				IFightResult fightResult = attSkill.getFightResultCalculator().calculate(attSkill, attFighter, defFighter);
				IHarm harm = fightResult.getHarm();
				if(harm != null){
					defFighter.getFightAttribute().acceptHarm(harm);
					JingjiFightRound fightRound = new JingjiFightRound(harm, attFighter, curTime, attSkill);
					fightResport.addRound(fightRound);
					if(end){
						break;
					}
				}else{
					ChuanQiLog.error("att harm is null");
				}
			}
			ISkill defSkill = defFighter.getSkill();
			if(defSkill != null){
				IFightResult fightResult = defSkill.getFightResultCalculator().calculate(defSkill, defFighter, attFighter);
				IHarm harm = fightResult.getHarm();
				if(harm != null){
					attFighter.getFightAttribute().acceptHarm(harm);
					JingjiFightRound fightRound = new JingjiFightRound(harm, defFighter, curTime, defSkill);
					fightResport.addRound(fightRound);
					if(end){
						break;
					}
				}else{
					ChuanQiLog.error("att harm is null");
				}
			}
			int minCd = Math.min(attFighter.getMinCd(), defFighter.getMinCd());
			curTime += minCd;
		}
		long aHp = attFighter.getCurHp();
		long bHp = defFighter.getCurHp();
		if(aHp < bHp){
			loser = attFighter;
		}else{
			loser = defFighter;
		}
		return fightResport.getResport(curTime, loser);
	}

	public IJingjiFighter getLoser() {
		return loser;
	}
	
}
