package com.junyou.stage.collect.element;

import com.junyou.cmd.ClientCmdType;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.stage.model.core.element.AbsElement;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.state.IStateManager;

/** 可采集的箱子实体 */
public class ActivityBox extends AbsElement {
	
	private int configId;
	
	private String awardId;
	
	private String stageId;

	private IStateManager iStateManager;
	
	/**采集需要角色最低等级*/
	private int needRoleLevel;
	
	/**消失时间*/
	private long vanishTime;

	/**需采集时长(MS)*/
	private long collectDuration;

	public ActivityBox(Long id, String awardId) {
		super(id, null);
		this.awardId = awardId;
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
	public Object getMsgData() {
		Object[] result = new Object[] {
			getConfigId(),
			getId(),
			getPosition().getX(),
			getPosition().getY()
			
		};
		return result;
	}

	@Override
	public PathNodeSize getPathNodeSize() {
		return PathNodeSize._1X;
	}

	@Override
	public IStateManager getStateManager() {
		return iStateManager;
	}

	@Override
	public void leaveStageHandle(IStage stage) {
		// TODO

	}
	
	@Override
	public void deadHandle(IHarm harm) {
		// TODO Auto-generated method stub
		
	}
	
	public String getStageId() {
		return stageId;
	}

	public void setStageId(String stageId) {
		this.stageId = stageId;
	}

	public long getVanishTime() {
		return vanishTime;
	}

	public void setVanishTime(long vanishTime) {
		this.vanishTime = vanishTime;
	}

	public IStateManager getiStateManager() {
		return iStateManager;
	}

	public void setiStateManager(IStateManager iStateManager) {
		this.iStateManager = iStateManager;
	}



	public int getNeedRoleLevel() {
		return needRoleLevel;
	}

	public void setNeedRoleLevel(int needRoleLevel) {
		this.needRoleLevel = needRoleLevel;
	}

	public long getCollectDuration() {
		return collectDuration;
	}

	public void setCollectDuration(long collectDuration) {
		this.collectDuration = collectDuration;
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
	public PointTakeupType getTakeupType() {
		return PointTakeupType.GOODS;
	}

	@Override
	public void enterStageHandle(IStage stage) {
		// TODO Auto-generated method stub
		
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public String getAwardId() {
		return awardId;
	}

	public void setAwardId(String awardId) {
		this.awardId = awardId;
	}

}
