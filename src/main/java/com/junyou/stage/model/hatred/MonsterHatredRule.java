package com.junyou.stage.model.hatred;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.hatred.IHatredRule;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.ScopeType;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.pet.Pet;

public class MonsterHatredRule implements IHatredRule{
	
	private IMonster monster;
	
	private IHatred hatredest;
	
	private IHatred lastHatred;
	
	private Map<Long,IHatred> hatredMap;
	
	private Map<Long,IHatred> petHatredMap;
	
	private int hatredExpireTime = 6000000;//默认6秒仇恨无更新则过期
	
	private float hatredestReplaceRate = 1.1f;//默认最高仇恨可覆盖比率1.1
	
	/**
	 * 获取最大仇恨
	 */
	public IHatred getHatredest(boolean withRefresh){
		
		if(withRefresh){
			refreshHatred();
		}
		
		return hatredest;
	}
	
	/**
	 * 增加指定id仇恨
	 * @param id 
	 * @param hatredVal
	 */
	public void incrHatred(Long id,ElementType elementType,int hatredVal){
		
		if(null == hatredMap){
			hatredMap = new HashMap<Long, IHatred>();
		}
		
		IHatred hatred = hatredMap.get(id);
		if(null == hatred){
			hatred = new Hatred(id,elementType);
			hatredMap.put(id, hatred);
			this.hatredest = hatred;
			
			if(hatredVal < 0){
				hatredVal = 1;
			}
			
			hatred.add(hatredVal);
		}else{
			
			if(hatredVal < 0){
				hatredMap.remove(id);
				
				hatred = null;
				this.hatredest = null;
			}
			
			if(hatred != null){
				hatred.add(hatredVal);
			}
		}
		
		lastHatred = hatred;
		
	}
	
	/**
	 * 刷新仇恨
	 */
	public void refreshHatred(){
		
		if(null != hatredest){
			if(outOfHatredRule(hatredest) || hatredest.expired(hatredExpireTime)){
				hatredMap.remove(hatredest.getId());
				hatredest = null;
			}
		}
		
		if(null != hatredMap && hatredMap.size() > 0){
			
			Iterator<IHatred> iterator = hatredMap.values().iterator();
			
			while(iterator.hasNext()){
				
				IHatred tmp = iterator.next();
				if(outOfHatredRule(tmp) || tmp.expired(hatredExpireTime)){
					iterator.remove();
				}else{
					if(null == hatredest || (!hatredest.equals(tmp) && tmp.getVal() >= canReplaceHatredestVal(hatredest.getVal()))){
						this.hatredest = tmp;
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	public int canReplaceHatredestVal(int val) {
		return (int)(val * hatredestReplaceRate);
	}

	/**
	 * 设置仇恨过期时间
	 * @param duration 持续时间
	 */
	public void setHatredExpireTime(int duration){
		this.hatredExpireTime = duration;
	}
	
	/**
	 * 设置最高仇恨可覆盖比率
	 */
	public void setHatredestReplaceRate(float rate){
		this.hatredestReplaceRate = rate;
	}
	
	/**
	 * 清除所有仇恨
	 */
	public void clear(){
		hatredMap = null;
		hatredest = null;
		lastHatred = null;
	}
	
	/**
	 * 获取所有仇恨
	 */
	public Collection<IHatred> getHatreds(){
		if(null != hatredMap){
			return hatredMap.values();
		}
		
		return null;
	}

	@Override
	public boolean isActived() {
		return null != hatredMap && hatredMap.size() > 0;
	}

	@Override
	public IHatred getLastHatred() {
		return lastHatred;
	}

	@Override
	public IHatred getHatred(IFighter target) {
		return null != hatredMap ? hatredMap.get(target.getId()) : null;
	}
	
	public MonsterHatredRule(IMonster monster) {
		this.monster = monster;
	}

	/**
	 * 是否脱离仇恨额外规则.
	 */
	protected boolean outOfHatredRule(IHatred hatred) {
		
		IFighter fighter = (IFighter)monster.getStage().getElement(hatred.getId(), hatred.getElementType());
		MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(monster.getMonsterId());
		
		return null == fighter || fighter.getStateManager().isDead() || !monster.getStage().inScope(fighter.getPosition(), monster.getBornPosition(), monsterConfig.getMaxrange(),ScopeType.GRID);
		
	}

	@Override
	public void incrHatred(IFighter element, int hatredVal) {
		if(null == hatredMap){
			hatredMap = new HashMap<Long, IHatred>();
		}
		
		Long id = element.getId();
		ElementType elementType = element.getElementType();
		
		IHatred hatred = hatredMap.get(id);
		if(null == hatred){
			hatred = new Hatred(id,elementType);
			hatredMap.put(id, hatred);
			this.hatredest = hatred;
			
			if(hatredVal < 0){
				hatredVal = 1;
			}
			//仇恨对象是宠物
			if(elementType == ElementType.PET){
				if(null == petHatredMap){
					petHatredMap = new HashMap<Long, IHatred>();
				}
				Pet pet = (Pet)element;
				Long userRoleId = pet.getOwner().getId();
				IHatred petHatred = petHatredMap.get(userRoleId);
				if(null != petHatred){
					hatredVal += petHatred.getVal();
				}
				
				petHatredMap.put(userRoleId, hatred);
			}
			
			hatred.add(hatredVal);
			
		}else{
			
			if(hatredVal < 0){
				hatredMap.remove(id);
				
				hatred = null;
				this.hatredest = null;
			}
			
			if(hatred != null){
				hatred.add(hatredVal);
			}
		}
		
		lastHatred = hatred;
	}

	@Override
	public IHatred getHatred(long targetRoleId) {
		return null != hatredMap ? hatredMap.get(targetRoleId) : null;
	}
	
	public void clearHatred(long targetRoleId){
		hatredMap.remove(targetRoleId);
		if(hatredest != null && hatredest.getId().equals(targetRoleId)){
			refreshHatred();
		}
	}

	@Override
	public List<Long> getAllHaters() {
		return null;
	}
	
	
}
