package com.junyou.stage.model.element.componentlistener;

import com.junyou.stage.model.core.attribute.IAttributeListener;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.monster.KuafuBossMonster;
import com.junyou.stage.model.element.monster.QiShaMonster;
import com.junyou.stage.model.element.role.IRole;

public class KuafuBossFightListener implements IAttributeListener{

	private KuafuBossMonster monster;
	
	public KuafuBossFightListener(KuafuBossMonster monster){
		this.monster = monster;
	}
	
	@Override
	public void setCurHp(long hp) {
	}

	@Override
	public void setMaxHp(long maxHp) {
	}


	@Override
	public void hurt(IHarm harm) {
		IFighter fighter = harm.getAttacker().getOwner();
		if(ElementType.isRole(fighter.getElementType())){
			IRole role = (IRole)fighter;
			monster.addHurt(role, harm.getVal());
		}
	}

	@Override
	public void heal(IHarm harm) {
	}

	@Override
	public void setShanBi(long shanBi) {
	}

	@Override
	public void setMingZhong(long mingZhong) {
	}

	@Override
	public void setBaoji(long baoJi) {
	}

	@Override
	public void setZhanLi(long zhanLi) {
	}

	@Override
	public void setKangBao(long kangBao) {
	}

	@Override
	public void setAttack(long attack) {
	}

	@Override
	public void setDefense(long defense) {
	}

	@Override
	public void setSpeed(long speed) {
	}

	@Override
	public void setChengfa(long chengfa) {
		
	}

}
