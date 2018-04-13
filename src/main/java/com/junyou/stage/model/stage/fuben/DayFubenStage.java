package com.junyou.stage.model.stage.fuben;

import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.stage.DeadDisplay;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.tunnel.StageMsgSender;

public class DayFubenStage extends SingleFbStage{
	private int expireDelay;
	private short exitCmd;
	private DeadDisplay deadHiddenState; //死亡状态  1：躺着不消失，0：死掉就消失

	public DayFubenStage(String id, Integer mapId, AOIManager aoiManager,AbsFubenConfig fubenConfig,PathInfoConfig pathInfoConfig,StageType stageType) {
		super(id, mapId, aoiManager, pathInfoConfig, stageType, fubenConfig.getWantedMap());
		expireDelay = fubenConfig.getTime() * 1000;
		exitCmd = fubenConfig.getExitCmd();
		this.deadHiddenState =  DeadDisplay.EXIT;
	}
	
	/**
	 * 死亡状态  1：躺着不消失，0：死掉就消失
	 * @return
	 */
	@Override
	public DeadDisplay getDeadHiddenState() {
		return deadHiddenState;
	}
	

	public void setDeadHiddenState(DeadDisplay deadHiddenState) {
		this.deadHiddenState = deadHiddenState;
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

	@Override
	public void noticeClientKillInfo(Long roleId) {
		BusMsgSender.send2One(roleId, ClientCmdType.KILL_MONSTER_COUNT, getWantedCounts());
	}

	@Override
	public short getFinishCmd() {
		return InnerCmdType.S_FUBEN_OVER;
	}
	
	public short getFinishNoticeBusCmd(){
		if(getStageType() == StageType.FUBEN_PATA){
			return InnerCmdType.PATA_FINISH;
		}
		return InnerCmdType.S_FUBEN_FINISH;
	}

	@Override
	public void noticeClientExit(Long userRoleId) {
		if(getStageType() == StageType.FUBEN_RC){
			StageMsgSender.send2Bus(userRoleId, InnerCmdType.HAS_EXIT_FUBEN, null);
		}else if(getStageType() == StageType.FUBEN_PATA){
			StageMsgSender.send2Bus(userRoleId, InnerCmdType.HAS_EXIT_PATA_FUBEN, null);
		}
	}

}
