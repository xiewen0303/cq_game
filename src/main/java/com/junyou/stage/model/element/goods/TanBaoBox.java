package com.junyou.stage.model.element.goods;

import java.util.concurrent.TimeUnit;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;
import com.junyou.stage.schedule.StageTokenRunable;

public class TanBaoBox  extends Collect{
	

	public TanBaoBox(Long id, String teamId, ZiYuanConfig ziYuanConfig) {
		super(id, teamId, ziYuanConfig);
	}
	
	public void scheduleDisappearHandle(){
		//启动消失
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.DINGSHI_TANBAO_REFRESH_BOX, new Object[]{getConfigId(),getStage().getId(),getPosition().getX(),getPosition().getY()});
		this.getScheduler().schedule(getId().toString(), GameConstants.COMPONENET_DINGSHI_TANBAO, runable, getRefreshTime(), TimeUnit.SECONDS);
	}
	
	public Object[] getSuccessMsg(){
		return getZiYuanConfig().getSuccessMsg();
	}
	
}
