package com.junyou.stage.model.skill.buff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.junyou.constants.GameConstants;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.BuffConfig;
import com.junyou.stage.model.core.skill.BuffManagerListenerHelper;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.IBuffManager;
import com.junyou.stage.model.element.componentlistener.FightListener;

public class BuffManager implements IBuffManager {
	
	private BuffManagerListenerHelper listenerHelper = new BuffManagerListenerHelper();
	
	/**
	 * key:buff category val:IBuff
	 */
	private ConcurrentMap<String,IBuff> buffs;
	
	private ConcurrentMap<String,IBuff> readyForRecoveredBuffs;
	
	@Override
	public void addBuff(IBuff buff) {
		
		if(GameConstants.NUQI_BUFF_ID.equals(buff.getBuffId())){
			IBuff exist = getBuff(buff.getBuffCategory());
			if(exist != null){
				removeBuffAndFreeze(exist);
			}
			//怒气技能BUFF
			addBuffWithActivate(buff);
			return;
		}
		
		BuffConfig buffConfig = StageConfigureHelper.getBuffConfigExportService().loadById(buff.getBuffId());
		
		IBuff exist = getBuff(buff.getBuffCategory());
		
		if(null == exist){
			
			//不存在同类buff,则删排斥buff,加新
			
			List<String> excludes =  buffConfig.getExcludeBuffCategory();
			if(null != excludes){
				//删除排斥
				for(String exclude : excludes){
					IBuff existExclude = getBuff(exclude);
					if(null != existExclude){
						//删除并冻结旧
						removeBuffAndFreeze(existExclude);
					}
				}
			}
			//添加并激活新
			addBuffWithActivate(buff);
			
		}else{
			//cover没配置,不覆盖
			List<String> excludes =  buffConfig.getExcludeBuffCategory();
			if(excludes == null || excludes.size() == 0){
				return;
			}
			
			if(!buffConfig.canOverlap()){
				//不可叠加，则删旧，加新
				
				if(buff.getLevel() >= exist.getLevel()){
					removeBuffAndFreeze(exist);
					
					addBuffWithActivate(buff);
				}
			
			}else{
				//可叠加
				//1、新buff等级高：删旧，加新
				//2、新buff等级相等：删旧，叠加层数到新，加新
				//3、新buff等级低：无视
				
				if(buff.getLevel() > exist.getLevel()){
					removeBuffAndFreeze(exist);
					
					addBuffWithActivate(buff);
				}else if(buff.getLevel().equals(exist.getLevel())){
					removeBuffAndFreeze(exist);
					
					int newLayer = buff.getLayer() + exist.getLayer();
					if(newLayer > buffConfig.getMaxStack()){
						newLayer = buffConfig.getMaxStack();
					}
					buff.setLayer(newLayer);
					addBuffWithActivate(buff);
				}else{
					//
				}
				
				
			}
			
			
		}
		
//		pringBuff();
		
	}

	/**
	 * 移除并冻结buff
	 */
	private void removeBuffAndFreeze(IBuff existExclude) {
		
		buffs.remove(existExclude.getBuffCategory());
		
		// TODO Auto-generated method stub
		//相关统计记录
		existExclude.end();
		
		listenerHelper.removeBuff(existExclude);
	}

	/**
	 * 添加并激活buff
	 */
	private void addBuffWithActivate(IBuff buff) {
		if (null == buffs) buffs = new ConcurrentHashMap<String, IBuff>();
		
		buffs.put(buff.getBuffCategory(), buff);
		// TODO Auto-generated method stub
		//相关统计记录
		buff.start();
		
		listenerHelper.addBuff(buff);
	}

	/**
	 * 获取buff
	 * @param buff种类
	 */
	private IBuff getBuff(String buffCategory) {
		if (null == buffs) return null;
		return buffs.get(buffCategory);
	}

	@Override
	public void removeBuff(Long id, String category) {
		IBuff exist = getBuffById(id, category);
		if(null != exist){
			removeBuffAndFreeze(exist);
		}
	}

	private IBuff getBuffById(Long id, String category) {
		
		if(null != buffs){
			IBuff buff = buffs.get(category);
			if(null != buff && buff.getId().equals(id)){
				return buff;
			}
		}
		
		return null;
	}

	@Override
	public IBuff getBuff(String buffCategory, Long id) {
		return getBuffById(id, buffCategory);
	}


	@Override
	public Collection<IBuff> getBuffs() {
		
		if(null != buffs){
			return new ArrayList(buffs.values());
		}
		
		return null;
	}

	@Override
	public void startAll() {
		if(null != buffs){
			for(IBuff buff : buffs.values()){
				buff.start();
//				fighter.getFightStatistic().addBuff(buff);
				listenerHelper.addBuff(buff);
			}
			
		}
		
	}

	public void startReadyForRecoveredBuffsAll() {
		
		if(null != readyForRecoveredBuffs){
			for(IBuff buff : readyForRecoveredBuffs.values()){
				addBuff(buff);
			}
			
			readyForRecoveredBuffs = null;
		}
		
	}
	
	public void readyOrRecoverBuff(IBuff buff){
		
		if (null == readyForRecoveredBuffs) readyForRecoveredBuffs = new ConcurrentHashMap<String, IBuff>();
		readyForRecoveredBuffs.put(buff.getBuffCategory(), buff);
	}
	
	@Override
	public void recoverBuff(IBuff buff) {
		
		if (null == buffs) buffs = new ConcurrentHashMap<String, IBuff>();
		buffs.put(buff.getBuffCategory(), buff);
	}
	

	@Override
	public void clearBuffsByDead() {
		
		if (null != buffs){
			for(IBuff buff : getBuffs()){
				//死亡后不清除的BUFF
				if(buff.isDeadClear()){
					removeBuffAndFreeze(buff);
				}
			}
		}
		
	}
	
	

	@Override
	public void clearAll() {
		
		if (null != buffs){
			for(IBuff buff : getBuffs()){
				removeBuffAndFreeze(buff);
			}
			
		}
	}

	@Override
	public Object getBuffClientMsgs() {
		
		List<Object> result = null;
		
		if (null != buffs && buffs.size() > 0){
			
			result = new ArrayList<Object>();
			
			for(IBuff buff : buffs.values()){
				Object msg = buff.getClientMsg();
				if(msg != null){
					result.add(msg);
				}
			}
		}
		
		return null == result ? null : result.toArray();
	}

	@Override
	public void addListener(FightListener fightListener) {
		listenerHelper.addListener(fightListener);
	}
	
}
