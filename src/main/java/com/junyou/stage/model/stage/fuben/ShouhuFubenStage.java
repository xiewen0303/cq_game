package com.junyou.stage.model.stage.fuben;

import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.fuben.entity.ShouhuFubenConfig;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.TutengMonster;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * @author LiuYu
 * @date 2015-4-28 上午11:50:56 
 */
public class ShouhuFubenStage extends SingleFbStage{
	private int expireDelay;
	private short exitCmd;
	private TutengMonster npc;
	private ShouhuFubenConfig config;

	public ShouhuFubenStage(String id, Integer mapId, AOIManager aoiManager,AbsFubenConfig fubenConfig,PathInfoConfig pathInfoConfig,StageType stageType) {
		super(id, mapId, aoiManager, pathInfoConfig, stageType, fubenConfig.getWantedMap());
		expireDelay = fubenConfig.getTime() * 1000 + 5000;
		exitCmd = fubenConfig.getExitCmd();
		config = (ShouhuFubenConfig)fubenConfig;
	}

	@Override
	public int getExpireDelay() {
		return expireDelay;
	}

	@Override
	public short getExitCmd() {
		return exitCmd;
	}

	@Override
	public int getExpireCheckInterval() {
		return GameConstants.EXPIRE_CHECK_INTERVAL;
	}
	
	public void setConfig(ShouhuFubenConfig config) {
		this.config = config;
		expireDelay = config.getTime() * 1000;
	}

	public ShouhuFubenConfig getConfig() {
		return config;
	}

	@Override
	public void noticeClientKillInfo(Long roleId) {
		if(npc != null){
			BusMsgSender.send2One(roleId, ClientCmdType.FUBEN_SHOUHU_SYSTEM_SEND, config.getId());
		}
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if(ElementType.isTuTeng(element.getElementType())){
			npc = (TutengMonster)element;
		}
		else if(ElementType.isMonster(element.getElementType())){
			IMonster monster = (IMonster)element;
			monster.getHatredManager().addActiveHatred(npc,1);
		}
	}

	@Override
	public short getFinishCmd() {
		return InnerCmdType.SHOUHU_FUBEN_FINISH;
	}

	/**
	 * 是否已失败
	 * @return
	 */
	public boolean isFail(){
		if(npc == null){
			return true;
		}
		return npc.getStateManager().isDead();
	}

	@Override
	public void noticeClientExit(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.FUBEN_SHOUHU_EXIT, AppErrorCode.OK);
	}

    @Override
    public short getFinishNoticeBusCmd() {
        return 0;
    }
	
}
