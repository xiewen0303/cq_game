package com.junyou.stage.model.core.attribute;

import java.util.ArrayList;
import java.util.List;

import com.junyou.stage.model.core.skill.IHarm;

public class FightAttributeListenerHelper implements IAttributeListener {

	public List<IAttributeListener> listeners;
	
	public void addListener(IAttributeListener listener){
		if( listeners == null ){
			listeners = new ArrayList<IAttributeListener>();
		}
		listeners.add(listener);
	}

	@Override
	public void setMaxHp(long maxHp) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.setMaxHp(maxHp);
			}
		}
	}

	@Override
	public void setShanBi(long shanBi) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.setShanBi(shanBi);
			}
		}
	}

	@Override
	public void setMingZhong(long mingZhong) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.setMingZhong(mingZhong);
			}
		}
	}

	@Override
	public void setBaoji(long baoJi) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.setBaoji(baoJi);
			}
		}
	}

	@Override
	public void setZhanLi(long zhanLi) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.setZhanLi(zhanLi);
			}
		}
	}

	@Override
	public void setCurHp(long curHp) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.setCurHp(curHp);
			}
		}
	}


	@Override
	public void hurt(IHarm harm) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.hurt(harm);
			}
		}
	}

	@Override
	public void heal(IHarm harm) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.heal(harm);
			}
		}
	}

	@Override
	public void setKangBao(long kangBao) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.setKangBao(kangBao);
			}
		}
	}

	@Override
	public void setAttack(long attack) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.setAttack(attack);
			}
		}
	}

	@Override
	public void setDefense(long defense) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.setDefense(defense);
			}
		}
	}

	@Override
	public void setSpeed(long speed) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.setSpeed(speed);
			}
		}
	}

	@Override
	public void setChengfa(long chengfa) {
		if(null != listeners){
			for(IAttributeListener tmp : listeners){
				tmp.setChengfa(chengfa);
			}
		}
	}
}
