package com.junyou.stage.model.state;

import java.util.HashMap;
import java.util.Map;

import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.StateType;

public class BuffStateFactory {
	
	
	private static Map<Integer,StateType> stateTypes = new HashMap<Integer, StateType>();
	static{
		
		stateTypes.put(20, StateType.YINSHEN);
		stateTypes.put(40, StateType.NO_MOVE);
		stateTypes.put(41, StateType.NO_ATTACK);
		stateTypes.put(42, StateType.NO_ATTACKED);
		
//		stateTypes.put(21, StateType.KONGJU);
//		stateTypes.put(22, StateType.HUNMI);
//		stateTypes.put(23, StateType.BINGDONG);
//		stateTypes.put(24, StateType.SHIHUA);
//		stateTypes.put(25, StateType.MABI);
//		stateTypes.put(26, StateType.JINGU);
//		stateTypes.put(27, StateType.JITUI);
//		stateTypes.put(28, StateType.WUDI_NOMOVE);
//		stateTypes.put(29, StateType.BIAN_XING);
//		stateTypes.put(31, StateType.CHENMO);
//		stateTypes.put(32, StateType.WUDI);
		
	}
	
	
	private static Map<Integer,Class<? extends IState>> states = new HashMap<Integer, Class<? extends IState>>();
	static{
		
		states.put(20, YinShen.class);
		states.put(40, NoMove.class);
		states.put(41, NoAttack.class);
		states.put(42, NoBeiAttack.class);
		
		
//		states.put(25, MaBi.class);
//		states.put(21, KongJuState.class);
//		states.put(22, HunMi.class);
//		states.put(23, BingDong.class);
//		states.put(24, ShiHua.class);
//		states.put(26, JinGu.class);
//		states.put(27, JiTui.class);
//		states.put(28,WuDiNoMove.class);
//		states.put(29, BianXing.class);
//		states.put(31, ChenMo.class);
//		states.put(32, WuDi.class);
		
	}
	
	public static IState create(Integer stateType) {
		
		Class<? extends IState> stateClass = states.get(stateType);
		if(null != stateClass){
			
			try {
				return stateClass.newInstance();
			} catch (InstantiationException e) {
				
			} catch (IllegalAccessException e) {
				
			}
		}
		
		return null;
	}
	
	public static StateType getType(Integer stateType){
		return stateTypes.get(stateType);
	}

}
