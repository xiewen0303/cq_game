package com.junyou.stage.model.stage.kuafu;

import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 跨服单人竞技副本类
 * 
 * @author chenjianye 2015年04月28日
 */
public class KuafuArenaFbStage extends KuafuFbStage {

	private StageScheduleExecutor scheduleExecutor;
	private long startTime;

	/**
	 * 多人副本基础抽象类
	 * 
	 * @param id
	 * @param mapId
	 * @param aoiManager
	 * @param pathInfoConfig
	 * @param stageType
	 *            场景类型
	 */
	public KuafuArenaFbStage(String stageId, Integer mapId,
			AOIManager aoiManager, PathInfoConfig pathInfoConfig) {
		super(stageId, mapId, 1, aoiManager, pathInfoConfig,
				StageType.KUAFU_ARENA_SINGLE);

		this.scheduleExecutor = new StageScheduleExecutor(getId());
		// 副本创建时间
		this.startTime = GameSystemTime.getSystemMillTime();
	}

	/**
	 * 获取多人副本的场景定时器
	 * 
	 * @return
	 */
	protected StageScheduleExecutor getScheduleExecutor() {
		return scheduleExecutor;
	}

	/**
	 * 获取副本开始时间
	 * 
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * 挑战者进入场景
	 */
	public void enter(IStageElement element, int x, int y) {
		if(ElementType.isRole(element.getElementType())){
			IRole role = (IRole)element;
			role.getFightAttribute().resetHp();
		}
		super.enter(element, x, y);
	}

	/**
	 * 挑战者离开场景
	 */
	public void leave(IStageElement element) {
		super.leave(element);
	}

	public int getExpireCheckInterval() {
		return GameConstants.EXPIRE_CHECK_INTERVAL;
	}

	@Override
	public boolean canFuhuo() {
		return false;
	}
	public boolean isAddPk(){
		return false;
	}
	
	@Override
	public boolean isCanRemove() {
		return getAllRoleIds()==null || getAllRoleIds().length<1;
	}
}
