package com.junyou.stage.model.stage.kuafu;

import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.stage.aoi.AoiStage;

/**
 * 跨服副本基础抽象类
 * 
 * @author LiuYu
 * @date 2015-4-2 下午6:07:42
 */
public abstract class KuafuFbStage extends AoiStage {
	// 结束标识
	private boolean isEnd = false;

	public KuafuFbStage(String stageId, Integer mapId, Integer lineNo,
			AOIManager aoiManager, PathInfoConfig pathInfoConfig,StageType stageType) {
		super(stageId, mapId, lineNo, aoiManager, pathInfoConfig,
				stageType);
	}

	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);

		if (element == null) {
			return;
		}
	}

	public void leave(IStageElement element) {
		super.leave(element);

		if (element == null) {
			return;
		}
	}

	/**
	 * 更改为结束
	 */
	public void changeEnd() {
		isEnd = true;
	}

	/**
	 * 是否结束
	 * 
	 * @return
	 */
	public boolean isEnd() {
		return isEnd;
	}

	/**
	 * 是否可以删除场景
	 * 
	 * @return
	 */
	public boolean isCanRemoveStage() {
		return isEnd();
	}


	public void reStart() {
		isEnd = false;
	}

}
