package com.junyou.stage.model.element.goods;

import com.junyou.bus.territory.entity.Territory;
import com.junyou.cmd.ClientCmdType;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.territory.TerritoryStage;

public class TerritoryQizi extends Collect {

	private String stageId;

	public TerritoryQizi(Long id, String teamId, ZiYuanConfig ziYuanConfig,
			String stageId) {
		super(id, teamId, ziYuanConfig);
		this.stageId = stageId;
	}

	public void scheduleDisappearHandle() {
		super.scheduleDisappearHandle();
	}

	@Override
	public short getEnterCommand() {
		return ClientCmdType.AOI_TERRITORY_FLAG;
	}

	@Override
	public Object getMsgData() {
		String guildName = null;
		IStage stage = StageManager.getStage(stageId);
		if (stage != null) {
			if (stage.getStageType() == StageType.TERRITORY_WAR) {
				TerritoryStage tStage = (TerritoryStage)stage;
				Long ownerGuildId = tStage.getOwnerGuildId();
				if(ownerGuildId != null && ownerGuildId.longValue() !=0){
					Object[] guildInfo = StageConfigureHelper
							.getGuildExportService().getGuildBaseInfo(
									ownerGuildId);
					if (guildInfo != null) {
						guildName = (String) guildInfo[0];
					}
				}
			} else {
				Territory territory = StageConfigureHelper
						.getTerritoryExportService().loadTerritoryByMapId(
								stage.getMapId());
				if (territory != null && territory.getGuildId() != null
						&& territory.getGuildId().longValue() != 0L) {
					Object[] guildInfo = StageConfigureHelper
							.getGuildExportService().getGuildBaseInfo(
									territory.getGuildId());
					if (guildInfo != null) {
						guildName = (String) guildInfo[0];
					}
				}
			}
		}
		Object[] result = new Object[] { getConfigId(), getId(),
				getPosition().getX(), getPosition().getY(), guildName };
		return result;
	}

}
