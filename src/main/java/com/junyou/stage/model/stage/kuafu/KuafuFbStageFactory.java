package com.junyou.stage.model.stage.kuafu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.analysis.ServerInfoConfigManager;
import com.junyou.analysis.StageInfoConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.util.DiTuConfigUtil;
import com.junyou.stage.model.core.stage.PointType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.core.stage.aoi.AoiFactory;
import com.junyou.stage.model.stage.aoi.AoiMsgListenerFactory;

/**
 * 跨服副本
 * 
 * @author LiuYu
 * @date 2015-4-2 下午6:49:44
 */
@Component
public class KuafuFbStageFactory {

	@Autowired
	private DiTuConfigExportService diTuConfigExportService;

	public KuafuFbStage create(String stageId, MapConfig mapConfig) {
		// 场景创建基本配置
		StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance()
				.getStageInfoConfig();
		// 正常AOI
		AOIManager aoiManager = new AOIManager(stageInfoConfig.getAoiWidth(),
				stageInfoConfig.getAoiHeight(), mapConfig.getPathInfoConfig()
						.getXsWidth(), mapConfig.getPathInfoConfig()
						.getXsHeight(), PointType.PIXEL, new AoiFactory(
						new AoiMsgListenerFactory()));

		DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(mapConfig
				.getMapId());

		KuafuFbStage stage = null;
		if (diTuConfig.getType().equals(DiTuConfigUtil.KUAFU_SINGLE_ARENA)) {
			stage = new KuafuArenaFbStage(stageId, mapConfig.getMapId(),
					aoiManager, mapConfig.getPathInfoConfig());
		}
		if (stage != null) {
			stage.setCanPk(!diTuConfig.isSafeMap());
			stage.setAddPk(diTuConfig.isAddPk());
		}
		return stage;
	}
}