package com.junyou.stage.model.stage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;
import com.junyou.stage.model.core.stage.AbsElementProduceTeam;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.element.goods.CollectFacory;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.kernel.gen.id.IdFactory;

public class CollectProduceTeam  extends AbsElementProduceTeam {

	
	private StageScheduleExecutor stageScheduleExecutor;

	


	public CollectProduceTeam(String teamId,ElementType elementType,int limit,String elementId,List<Integer[]> xyPoints,Integer produceDelay) {
		super(teamId, elementType, limit, elementId, xyPoints,produceDelay * 1000);
		
		this.stageScheduleExecutor = new StageScheduleExecutor(teamId);
	}



	@Override
	protected IStageElement create(String teamId, String elementId) {
		ZiYuanConfig ziYuanConfig = StageConfigureHelper.getZiYuanConfigExportService().loadById(Integer.parseInt(elementId));
		if(null == ziYuanConfig){
		    ChuanQiLog.error("CollectProduceTeam, create collect not found ziyuan,id={}", elementId);
		    return null;
		}
		return CollectFacory.create(IdFactory.getInstance().generateNonPersistentId(),teamId, ziYuanConfig);
	}

	
	@Override
	public void schedule() {
		StageTokenRunable runnable = new StageTokenRunable(null, stage.getId(), InnerCmdType.AI_PRODUCE, new Object[]{stage.getId(), teamId, elementType.getVal()});
		stageScheduleExecutor.schedule(new StringBuilder().append(stage.getId()).append("_").append(teamId).toString(), GameConstants.COMPONENT_PRODUCE_TEAM, runnable, getDelay(), TimeUnit.MILLISECONDS);
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
		// TODO Auto-generated method stub
		return false;
	}
}
