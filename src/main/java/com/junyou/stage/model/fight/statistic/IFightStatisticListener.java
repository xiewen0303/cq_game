package com.junyou.stage.model.fight.statistic;

import java.util.List;
import java.util.Map;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.tunnel.IMsgWriter;

public interface IFightStatisticListener {
	
	/**
	 * 
	 */
	void dead(IFighter owner, IHarm harm,IMsgWriter msgWriter);
	
	void attributeChanged(IFighter owner,Map<String,Object> attributes,IMsgWriter msgWriter);

	void hpMpChanged(IFighter owner,IMsgWriter msgWriter);

	void buffChanged(IFighter owner, List<IBuff> addBuffs, List<IBuff> removeBuffs,IMsgWriter msgWriter);
	
	void stateChanged(IFighter owner,List<IState> addStates,List<IState> replacedStates,List<IState> clearStates,IMsgWriter msgWriter);
	
	void stateChanged(IFighter owner,IState addState,IState replacedState,IState clearState,IMsgWriter msgWriter);

}
