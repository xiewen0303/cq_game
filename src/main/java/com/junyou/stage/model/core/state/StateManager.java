/**
 * 
 */
package com.junyou.stage.model.core.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-28上午2:18:34
 */
public class StateManager implements IStateManager {
//	
//	private IFighter fighter;
//	
//	public StateManager(IFighter fighter) {
//		this.fighter = fighter;
//	}
	private StateManagerListenerHelper stateManagerListenerHelper = new StateManagerListenerHelper();
	/**
	 * 当前状态集合
	 */
	private Map<StateType,IState> stateMap = new HashMap<StateType, IState>();

	@Override
	public void add(IState state) {

		IState exist = getState(state.getType());
		if(null == exist){
			List<StateType> excludes = StateUtil.getExculde(state.getType());
			if(null != excludes){
				
				for(StateType type : excludes){
					IState exclude = getState(type);
					if(null != exclude){
						stateMap.remove(type);
//						exclude.replace();
//						fighter.getFightStatistic().stateReplaced(exclude);
						stateManagerListenerHelper.replaceState(exclude);
					}
				}
			}
			
			stateMap.put(state.getType(), state);
//			fighter.getFightStatistic().addState(state);
			stateManagerListenerHelper.addState(state);
			
		}
	}

	/**
	 * 获取状态
	 * @param type
	 */
	private IState getState(StateType type) {
		if(null != stateMap){
			return stateMap.get(type);
		}
		return null;
	}


	@Override
	public boolean remove(StateType stateType) {
		IState state = getState(stateType);
		if(null != state){
			stateMap.remove(stateType);
//			state.clear();
//			fighter.getFightStatistic().stateClear(state);
			stateManagerListenerHelper.removeState(state);
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean isForbidden(StateEventType event) {
		
		if(null != stateMap){
			
			for(StateType stateType : stateMap.keySet()){
				if(StateUtil.isForbidden(stateType,event)){
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public boolean contains(StateType stateType) {
		
		return null != getState(stateType);
	}

	@Override
	public boolean isDead() {
		return null != stateMap && stateMap.containsKey(StateType.DEAD);
	}

	public boolean isYinShen(){
		return null != stateMap && stateMap.containsKey(StateType.YINSHEN);
	}
	
	/**
	 * 清除所有状态
	 */
	private void clearAll() {
		if(null != stateMap){
			
			List<IState> list = new ArrayList<>(stateMap.values());
			stateMap.clear();
			for (IState iState : list) {
				stateManagerListenerHelper.clearState(iState);
			}
		}
	}

	@Override
	public boolean getClientState() {
		
		if(null != getState(StateType.DEAD)){
			return false;
		}
		
		return true;
	}

	@Override
	public void clear() {
		clearAll();
	}

	@Override
	public void addListener(IStateManagerListener listener) {
		stateManagerListenerHelper.addListener(listener);
	}
	
}
