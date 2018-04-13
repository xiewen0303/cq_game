package com.junyou.stage.model.core.hatred;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.tunnel.DirectMsgWriter;

/**
 * @description 
 * @author ShiJie Chi
 * @date 2012-7-4 上午10:28:43 
 */
public abstract class AbsHatredManager implements IHatredManager {
	
	protected IFighter fighter;
	protected IHatredRule hatredRule;
	
	protected HatredListenerHelper listenerHelper = new HatredListenerHelper();
	
	public AbsHatredManager(IFighter owner,IHatredRule hatredRule) {
		this.fighter = owner;
		this.hatredRule = hatredRule;
	}

	@Override
	public IHatred getHatredest() {
		return hatredRule.getHatredest(false);
	}

//	@Override
	private void add(IFighter element, int i) {
		
		IFighter fighter = getHatredBelongElement(element);
		
		if(fighter instanceof IRole){
			IRole role = (IRole)fighter;
			if(role.isGm()){
				return;//GM不增加仇恨
			}
		}
		
		if( hatredRule.getHatred(element) == null ){
			initHatredElement(element);
		}
		
		hatredRule.incrHatred(fighter.getId(), fighter.getElementType(), i);
	}
	
	/**
	 * 初次添加element作为仇恨对象
	 * @param element
	 */
	private void initHatredElement(IFighter element){
		listenerHelper.addHatred(element);
	}

	/**
	 * 获取仇恨归属元素
	 */
	public abstract IFighter getHatredBelongElement(IFighter element);

//	@Override
//	public void addHarmHatred(IFighter attacker, HarmType type, int val) {
//		add(attacker, val);	
//	}

	@Override
	public void refreshHatred() {
		hatredRule.refreshHatred();
	}

	@Override
	public void deadHandle() {
		hatredRule.clear();
	}

	@Override
	public void clear() {
		hatredRule.clear();
	}

	@Override
	public void addInsideHatred(Integer hatredVal) {
		
		Collection<IHatred> hatreds = hatredRule.getHatreds();
		if(null != hatreds){
			Collection<IHatred> newHatreds = new ArrayList<IHatred>();
			newHatreds.addAll(hatreds);
			
			for (IHatred tmp : newHatreds) {
				IFighter target = (IFighter)fighter.getStage().getElement(tmp.getId(), tmp.getElementType());
				if(null != target && !target.getId().equals(fighter.getId())){
					
					target.getHatredManager().addPassiveHatred(fighter,hatredVal);
					target.getFightStatistic().flushChanges( DirectMsgWriter.getInstance() );
				}
			}
			
		}
	}

	@Override
	public void addActiveHatred(IFighter target, int val) {
		add(target, val);
		addActiveHatredHandle(target,val);
	}
	
	
	/**
	 * 目标是否在仇恨列表内
	 * @param target
	 * @return true:在
	 */
	public boolean checkTargetHatred(IFighter target){
		IHatred hatred = hatredRule.getHatred(target);
		if(null == hatred){
			return false;
		}else{
			return true;
		}
	}
	

	/**
	 * 新增主动仇恨关联业务处理
	 */
	public abstract void addActiveHatredHandle(IFighter target, int val);

	@Override
	public void addPassiveHatred(IFighter target, int val) {
		add(target, val);
		addPassiveHatredHandle(target,val);
	}

	/**
	 * 新增被动仇恨关联业务处理
	 */
	public abstract void addPassiveHatredHandle(IFighter target, int val);

	@Override
	public void addListener(IHatredManagerListener listener) {
		listenerHelper.addListener(listener);
	}
	
	/**
	 * 从仇恨列表中移除目标
	 * @param targetRoleId
	 */
	public void clearHatred(long targetId){
		hatredRule.clearHatred(targetId);
	}
	
	public List<Long> getAllHaters(){
		return hatredRule.getAllHaters();
	}

}
