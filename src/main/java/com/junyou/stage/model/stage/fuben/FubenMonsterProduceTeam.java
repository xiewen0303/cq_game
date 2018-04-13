/**
 * 
 */
package com.junyou.stage.model.stage.fuben;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.element.IElement;
import com.junyou.stage.model.core.stage.AbsElementProduceTeam;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.kernel.gen.id.IdFactory;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-16上午10:35:22
 */
public class FubenMonsterProduceTeam extends AbsElementProduceTeam {
	
	private StageScheduleExecutor elementScheduler;
	
	private boolean isDelay = false;
	
	public FubenMonsterProduceTeam(String teamId,ElementType elementType,int limit,String elementId,List<Integer[]> xyPoints,Integer produceDelay) {
		super(teamId, elementType, limit, elementId, xyPoints,produceDelay);
		
		this.elementScheduler = new StageScheduleExecutor(teamId);
		if(produceDelay > 0){
			isDelay = true;
		}
		
	}

	protected IElement create(String teamId,String elementId) {
		
		MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(elementId);
		if(monsterConfig == null){
			ChuanQiLog.error("Fuben Monster is null monsterId:"+elementId);
			return	 null;
		}
		return MonsterFactory.createFubenMonster(IdFactory.getInstance().generateNonPersistentId(), monsterConfig);
		
	}
	
	@Override
	public void schedule() {
		StageTokenRunable runable = new StageTokenRunable(null, stage.getId(), InnerCmdType.AI_PRODUCE, new Object[]{stage.getId(),teamId,elementType.getVal()});
		elementScheduler.schedule(new StringBuffer().append(stage.getId()).append("_").append(teamId).toString(), GameConstants.COMPONENT_PRODUCE_TEAM, runable, getDelay(), TimeUnit.MILLISECONDS);	
	}

	@Override
	public void randomXunlouSchedule() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getRandomOneElementId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDelayProduct() {
		return isDelay;
	}
}