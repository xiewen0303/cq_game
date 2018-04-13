package com.junyou.stage.model.element.componentlistener;

import com.junyou.gameconfig.constants.AttributeType;
import com.junyou.stage.model.core.attribute.IAttributeListener;
import com.junyou.stage.model.core.fight.IFightStatistic;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.IBuffManagerListener;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.IStateManagerListener;

public class FightListener implements IStateManagerListener, IBuffManagerListener, IAttributeListener{

	private IFightStatistic fightStatistic;
	
	public FightListener(IFightStatistic fightStatistic){
		this.fightStatistic = fightStatistic;
	}
	
	@Override
	public void setCurHp(long hp) {
		fightStatistic.hpChange();
	}

	@Override
	public void setMaxHp(long maxHp) {
		Object val = maxHp;
		fightStatistic.setAttribute(AttributeType.MaxHpIndex,val);
	}


	@Override
	public void addBuff(IBuff buff) {
		fightStatistic.addBuff(buff);
	}

	@Override
	public void removeBuff(IBuff buff) {
		fightStatistic.removeBuff(buff);
	}

	@Override
	public void addState(IState state) {
		fightStatistic.addState(state);
	}

	@Override
	public void removeState(IState state) {
		fightStatistic.removeState(state);
	}

	@Override
	public void replaceState(IState state) {
		fightStatistic.replaceState(state);
	}

	@Override
	public void hurt(IHarm harm) {
		fightStatistic.addHarm(harm);
	}

	@Override
	public void heal(IHarm harm) {
		fightStatistic.addHarm(harm);
	}

	@Override
	public void setShanBi(long shanBi) {
		Object val = shanBi;
		fightStatistic.setAttribute(AttributeType.ShanBiIndex,val);
	}

	@Override
	public void setMingZhong(long mingZhong) {
		Object val = mingZhong;
		fightStatistic.setAttribute(AttributeType.MingZhongIndex,val);
	}

	@Override
	public void setBaoji(long baoJi) {
		Object val = baoJi;
		fightStatistic.setAttribute(AttributeType.BaoJiIndex,val);
	}

	@Override
	public void setZhanLi(long zhanLi) {
		fightStatistic.setAttribute(AttributeType.ZplusIndex, zhanLi);
	}

	@Override
	public void setKangBao(long kangBao) {
		fightStatistic.setAttribute(AttributeType.KangBaoIndex, kangBao);
	}

	@Override
	public void setAttack(long attack) {
		fightStatistic.setAttribute(AttributeType.AttackIndex, attack);
	}

	@Override
	public void setDefense(long defense) {
		fightStatistic.setAttribute(AttributeType.DefenseIndex, defense);
	}

	@Override
	public void setSpeed(long speed) {
		fightStatistic.setAttribute(AttributeType.SpeedIndex, speed);
		fightStatistic.speedChange();
	}

	@Override
	public void setChengfa(long chengfa) {
		
	}

	@Override
	public void clearState(IState state) {
		fightStatistic.replaceState(state);
	}

}
