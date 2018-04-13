package com.junyou.stage.model.element.goods;

import com.junyou.bus.hczbs.entity.Zhengbasai;
import com.junyou.cmd.ClientCmdType;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.zhengbasai.HcZhengBaSaiStage;

public class HcZBSQizi extends Collect {

	private String stageId;

	public HcZBSQizi(Long id, String teamId, ZiYuanConfig ziYuanConfig, String stageId) {
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
			if (stage.getStageType() == StageType.HCZBS_WAR) {
				HcZhengBaSaiStage tStage = (HcZhengBaSaiStage) stage;
				Long ownerGuildId = tStage.getOwnerGuildId();
				if (ownerGuildId != null && ownerGuildId.longValue() != 0) {
					Object[] guildInfo = StageConfigureHelper.getGuildExportService().getGuildBaseInfo(ownerGuildId);
					if (guildInfo != null) {
						guildName = (String) guildInfo[0];
					}
				}
			} else {
				Zhengbasai  zhengbasai  = StageConfigureHelper.getHcZhengBaSaiExportService().loadZhengbasai();
				if (zhengbasai != null && zhengbasai.getGuildId() != null && zhengbasai.getGuildId().longValue() != 0L) {
					Object[] guildInfo = StageConfigureHelper.getGuildExportService().getGuildBaseInfo(zhengbasai.getGuildId());
					if (guildInfo != null) {
						guildName = (String) guildInfo[0];
					}else{
						//帮派解散了
						guildName = zhengbasai.getGuildName();
					}
				}
			}
		}
		Object[] result = new Object[] { getConfigId(), getId(), getPosition().getX(), getPosition().getY(), guildName };
		return result;
	}

}
