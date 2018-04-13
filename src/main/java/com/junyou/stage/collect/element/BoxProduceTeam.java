package com.junyou.stage.collect.element;

import java.util.ArrayList;
import java.util.List;

import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.stage.AbsElementProduceTeam;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;

public class BoxProduceTeam extends AbsElementProduceTeam {

	//private StageScheduleExecutor stageScheduleExecutor;
	
	public BoxProduceTeam(String teamId,ElementType elementType,int limit,String elementId,List<Integer[]> xyPoints,long produceDelay) {
		super(teamId, elementType, limit, elementId, xyPoints,(int)produceDelay );
		
		//this.stageScheduleExecutor = new StageScheduleExecutor(teamId);
	}
	
	
	@Override
	public void schedule() {
		// TODO Auto-generated method stub

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

	@Override
	protected IStageElement create(String teamId, String elementId) {
		
		return StageConfigureHelper.getBoxExportService().createBox();
	}
	
	@Override
	public void clear() {
		List<Long> ids = new ArrayList<>(getProducts().keySet());
		for(Long stageElementId : ids){
			IStageElement element = stage.getElement(stageElementId, elementType);
			if(null != element){
				retrieveNoSchedule(element);
			}
		}
	}

}
