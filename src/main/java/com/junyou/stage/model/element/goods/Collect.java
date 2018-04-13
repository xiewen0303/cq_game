package com.junyou.stage.model.element.goods;

import java.util.concurrent.TimeUnit;

import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;
import com.junyou.stage.model.core.element.AbsElement;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.state.IStateManager;
import com.junyou.stage.schedule.StageTokenRunable;

public class Collect  extends AbsElement{

	
	private String teamId;
	
	private ZiYuanConfig ziYuanConfig;
	
	public Collect(Long id,String teamId,ZiYuanConfig ziYuanConfig) {
		super(id, ziYuanConfig.getName());
		
		this.ziYuanConfig = ziYuanConfig;
		
		this.teamId = teamId;
	}

	public String getTeamId() {
        return teamId;
    }

	@Override
	public PathNodeSize getPathNodeSize() {
		return PathNodeSize._1X;
	}

	@Override
	public ElementType getElementType() {
		return ElementType.COLLECT;
	}
	
	@Override
	public short getEnterCommand() {
		return ClientCmdType.AOI_COLLECT;
	}

	@Override
	public PointTakeupType getTakeupType() {
		return PointTakeupType.GOODS;
	}
	
	/**
	 * 刷新时间间隔(秒)
	 * @return
	 */
	public int getRefreshTime(){
		return ziYuanConfig.getRefreshTime();
	}
	
	
	public Integer getConfigId(){
		return ziYuanConfig.getId();
	}
	
	public Integer getCollectConfigId(){
		return ziYuanConfig.getId();
	}
	
	/**
	 * 获取采集持续时间(毫秒)
	 * @return
	 */
	public int getCollectTime(){
		return ziYuanConfig.getCollectTime();
	}
	
	public String getCollectGoodsId(){
		return ziYuanConfig.getCollectItem();
	}
	
	public Integer getCollectItemCount(){
		return ziYuanConfig.getCollectItemCount();
	}
	
	public int getCollertType(){
		return ziYuanConfig.getType();
	}
	
	public boolean getIsNotice(){
		return ziYuanConfig.isNotice();
	}
	
	public String getCollectName(){
		return ziYuanConfig.getName();
	}
	
	public int getDisappearTime(){
		return 1000;
	}
	
	public void scheduleDisappearHandle(){
		//启动消失
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.AI_RETRIEVE, new Object[]{getId(),getElementType().getVal(),teamId});
		this.getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, getDisappearTime(), TimeUnit.MILLISECONDS);
	}

	@Override
	public void leaveStageHandle(IStage stage) {
		
		this.getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE);
		
		scheduleDisappearHandle();
	}

	@Override
	public void enterStageHandle(IStage stage) {
		
	}

	@Override
	public Object getMsgData() {
		Object[] result = new Object[]{
				ziYuanConfig.getId(),
				getId(), 
				getPosition().getX(), 
				getPosition().getY()
		};
		
		return result;
	}

	@Override
	public IStateManager getStateManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getStageData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getCamp() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void deadHandle(IHarm harm) {
		// TODO Auto-generated method stub
		
	}

	protected ZiYuanConfig getZiYuanConfig() {
		return ziYuanConfig;
	}

}
