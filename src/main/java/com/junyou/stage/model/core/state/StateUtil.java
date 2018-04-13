package com.junyou.stage.model.core.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StateUtil {
	
	
	private static Map<StateType,List<StateEventType>> forbiddens = new HashMap<StateType, List<StateEventType>>();
	private static Map<StateType, List<StateType>> execludes = new HashMap<StateType, List<StateType>>();
	
	
	static{
		List<StateEventType> forbiddenEventType = new ArrayList<StateEventType>();
		List<StateType> execludeStateTypes = new ArrayList<StateType>();
		
		/**
		 * 死亡状态禁止事件配置
		 * **/
		forbiddenEventType = new ArrayList<StateEventType>();
		forbiddenEventType.add(StateEventType.ALL_BUT_REVIVE);
		forbiddens.put(StateType.DEAD, forbiddenEventType);
		
		/**
		 * 战斗状态互斥关系
		 * */
		
		execludeStateTypes = new ArrayList<StateType>();
		execludeStateTypes.add(StateType.FIGHT);
		execludeStateTypes.add(StateType.XUNLUO);
		execludeStateTypes.add(StateType.MOVE);
		execludes.put(StateType.BACK, execludeStateTypes);
		
		execludeStateTypes = new ArrayList<StateType>();
		execludeStateTypes.add(StateType.MOVE);
		execludeStateTypes.add(StateType.BACK);
		execludeStateTypes.add(StateType.FIGHT);
		execludes.put(StateType.MOVE, execludeStateTypes);
		
		execludeStateTypes = new ArrayList<StateType>();
		execludeStateTypes.add(StateType.XUNLUO);
		execludeStateTypes.add(StateType.BACK);
		execludeStateTypes.add(StateType.MOVE);
		execludes.put(StateType.FIGHT, execludeStateTypes);
		
		execludeStateTypes = new ArrayList<StateType>();
		execludeStateTypes.add(StateType.BACK);
		execludeStateTypes.add(StateType.FIGHT);
		execludeStateTypes.add(StateType.MOVE);
		execludes.put(StateType.XUNLUO, execludeStateTypes);
		
		//坐骑
		execludeStateTypes = new ArrayList<StateType>(); 
		execludeStateTypes.add(StateType.DAZUO);
		execludes.put(StateType.ZUOQI, execludeStateTypes);
		
		//打坐
		execludeStateTypes = new ArrayList<StateType>();
		execludeStateTypes.add(StateType.ZUOQI);
		execludes.put(StateType.DAZUO, execludeStateTypes);
		
		
		
		execludeStateTypes = new ArrayList<StateType>();
		execludeStateTypes.add(StateType.FIGHT);
		execludes.put(StateType.DEAD, execludeStateTypes);
		
		execludeStateTypes = new ArrayList<StateType>();
		execludeStateTypes.add(StateType.FIGHT);
		execludeStateTypes.add(StateType.BACK);
		execludeStateTypes.add(StateType.NO_ATTACK);
		execludeStateTypes.add(StateType.NO_ATTACKED);
		execludes.put(StateType.WUDI_NOMOVE, execludeStateTypes);
		
		
		/***
		 * buff状态 禁止事件
		 * */
		
		forbiddenEventType = new ArrayList<StateEventType>();
		forbiddenEventType.add(StateEventType.MOVE);
		//不可移动
		forbiddens.put(StateType.NO_MOVE, forbiddenEventType);
		
		forbiddenEventType = new ArrayList<StateEventType>();
		forbiddenEventType.add(StateEventType.ATTACK);
		//不可攻击
		forbiddens.put(StateType.NO_ATTACK, forbiddenEventType);
		
		
		forbiddenEventType = new ArrayList<StateEventType>();
		forbiddenEventType.add(StateEventType.ATTACKED);
		//不可被攻击
		forbiddens.put(StateType.NO_ATTACKED, forbiddenEventType);
		
//		forbiddenEventType = new ArrayList<StateEventType>();
//		forbiddenEventType.add(StateEventType.MOVE);
//		forbiddenEventType.add(StateEventType.ATTACK);
//		
//		forbiddens.put(StateType.KONGJU, forbiddenEventType);
//		forbiddens.put(StateType.HUNMI, forbiddenEventType);
//		forbiddens.put(StateType.BINGDONG, forbiddenEventType);
//		forbiddens.put(StateType.SHIHUA, forbiddenEventType);
//		forbiddens.put(StateType.MABI, forbiddenEventType);
//		forbiddens.put(StateType.JINGU, forbiddenEventType);
//		
//		forbiddenEventType = new ArrayList<StateEventType>();
//		forbiddenEventType.add(StateEventType.ATTACK);
//		forbiddens.put(StateType.CHENMO, forbiddenEventType);
//		forbiddens.put(StateType.BIAN_XING, forbiddenEventType);
//		
//		forbiddenEventType = new ArrayList<StateEventType>();
//		forbiddenEventType.add(StateEventType.ATTACKED);
//		forbiddens.put(StateType.WUDI, forbiddenEventType);
//		
//		forbiddenEventType = new ArrayList<StateEventType>();
//		forbiddenEventType.add(StateEventType.MOVE);
//		forbiddenEventType.add(StateEventType.ATTACKED);
//		forbiddens.put(StateType.WUDI_NOMOVE, forbiddenEventType);
		
		
	}
	
	/**
	 * 获得互斥状态
	 * @param type
	 * @return
	 */
	public static List<StateType> getExculde(StateType type) {
		return execludes.get(type);
	}

	public static boolean isForbidden(StateType stateType, StateEventType event) {
		
		List<StateEventType> forbidden = forbiddens.get(stateType);
		
		return null != forbidden && ((forbidden.contains(StateEventType.ALL_BUT_REVIVE) && !event.equals(StateEventType.REVIVE)) || forbidden.contains(event));
	}

}
