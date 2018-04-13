package com.junyou.stage.model.stage.fuben;

import java.util.List;
import java.util.Map;
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
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;

public class JianzhongMonsterProduceTeam extends AbsElementProduceTeam{

	private StageScheduleExecutor stageScheduleExecutor;

	private boolean isXunlou;
	

	public JianzhongMonsterProduceTeam(String teamId,ElementType elementType,int limit,String elementId,List<Integer[]> xyPoints,Integer produceDelay,boolean isXunluo) {
		super(teamId, elementType, limit, elementId, xyPoints,produceDelay);
		
		this.stageScheduleExecutor = new StageScheduleExecutor(teamId);
		
		this.isXunlou = isXunluo;
		
	}

	/**
	 * @param
	 */
	protected IElement create(String teamId,String elementId) {
		
		MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(elementId);
		if(monsterConfig == null){
			ChuanQiLog.error("Monster is null monsterId:"+elementId);
			return null;
		}
		
		Long id = IdFactory.getInstance().generateNonPersistentId() * 1L;
		return MonsterFactory.create(teamId,id, monsterConfig);
	}
	
	

	@Override
	public void schedule() {
		StageTokenRunable runnable = new StageTokenRunable(null, stage.getId(), InnerCmdType.AI_PRODUCE, new Object[]{stage.getId(), teamId, elementType.getVal()});
		stageScheduleExecutor.schedule(getScheduleId(), GameConstants.COMPONENT_PRODUCE_TEAM, runnable, getDelay(), TimeUnit.MILLISECONDS);
	}
	
	private String getScheduleId(){
		return new StringBuilder().append(stage.getId()).append("_").append(teamId).toString();
	}
	
	public void cancleSchedule(){
		stageScheduleExecutor.cancelSchedule(getScheduleId(), GameConstants.COMPONENT_PRODUCE_TEAM);
	}
	
	/**
	 * 随机本组的N个怪物自动巡逻
	 */
	public void randomXunlouSchedule(){
		if(isXunlou && stage != null){
			int delaySecond = GameConstants.AUTO_TEAM_XUNLUO_SECOND + Lottery.roll(GameConstants.AUTO_TEAM_XUNLUO_SECOND);
			if(getLimit() == 1){
				delaySecond = GameConstants.AUTO_TEAM_XUNLUO_SECOND * 3 + Lottery.roll(GameConstants.AUTO_TEAM_XUNLUO_SECOND);
			}
			
			StageTokenRunable runnable = new StageTokenRunable(null, stage.getId(), InnerCmdType.AI_AUTO_XUNLOU, new Object[]{stage.getId(), teamId, elementType.getVal()});
			stageScheduleExecutor.schedule(new StringBuilder().append(stage.getId()).append("_").append(teamId).toString(), GameConstants.COMPONENT_AUTO_TEAM_XUNLUO, runnable, delaySecond, TimeUnit.SECONDS);
		}
	}

	/**
	 * 随机获取一个组里的场景元素id
	 * @return
	 */
	public Long getRandomOneElementId(){
		Map<Long, Integer[]> products = super.getProducts();
		
		Long id = null;
		if(products == null || products.size() == 0){
			return null;
		}else{
			int index = 0;
			int randomIndex = Lottery.roll(products.size());
			
			for(Map.Entry<Long, Integer[]> entry : products.entrySet()){
				if(index > randomIndex){
					break;
				}
				
				id = entry.getKey();
				index++;
			}
			return id;
		}
	}

	@Override
	public boolean isDelayProduct() {
		return false;
	}
}
