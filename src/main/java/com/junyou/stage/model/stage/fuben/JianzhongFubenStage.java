package com.junyou.stage.model.stage.fuben;

import java.util.concurrent.TimeUnit;

import com.junyou.bus.fuben.entity.AbsFubenConfig;
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
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;
 

/**
 * 云浮剑冢副本
 * @author lxn
 *
 */
public class JianzhongFubenStage extends SingleFbStage{
	private int expireDelay;
	private short exitCmd;
	/**强制副本清场踢人 定时器*/
	private StageScheduleExecutor scheduleExecutor;
	private int killMonsterNum  = 0; //击杀怪物数 
	private int jingqiItemNum  = 0;// 拾取道具数
	private boolean gameOver = false;
	
	
	public JianzhongFubenStage(String id, Integer mapId, AOIManager aoiManager,AbsFubenConfig fubenConfig,PathInfoConfig pathInfoConfig,StageType stageType) {
		super(id, mapId, aoiManager, pathInfoConfig, stageType, fubenConfig.getWantedMap());
		expireDelay = fubenConfig.getTime()*1000;
		exitCmd = fubenConfig.getExitCmd();
		this.scheduleExecutor = new StageScheduleExecutor(getId());
		gameOver = false;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	//开启最终时间到，强制回收定时器
	public void startForceClearSchedule(){
		clearForceClearSchedule();
		StageTokenRunable runable = new StageTokenRunable(null, getId(), InnerCmdType.S_FORCE_EXIT_FUBEN, getId());
		scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_HUNPO_FUBEN_FORCE_EXIT, runable, getExpireCheckInterval(), TimeUnit.MILLISECONDS);
	}
	public void clearForceClearSchedule(){
		scheduleExecutor.cancelSchedule(getId(), GameConstants.COMPONENT_HUNPO_FUBEN_FORCE_EXIT);
	}
	public int getKillMonsterNum() {
		return killMonsterNum;
	}
	public void addKillMonsterNum(int killMonsterNum) {
		this.killMonsterNum += killMonsterNum;
	}
	
	public int getJingqiItemNum() {
		return jingqiItemNum;
	}
	public void addJingqiItemNum(int jingqiItemNum) {
		this.jingqiItemNum += jingqiItemNum;
	}
	@Override
	public int getExpireDelay() {
		return expireDelay;
	}

	@Override
	public int getExpireCheckInterval() {
		return GameConstants.EXPIRE_CHECK_INTERVAL;
	}

	@Override
	public void noticeClientKillInfo(Long roleId) {
//		BusMsgSender.send2One(roleId, ClientCmdType.JIANZHONG_FUBEN_CHANGE, getWantedCounts());
	}
	
	@Override
	public short getExitCmd() {
		return  exitCmd ;  //副本时间到 清场
	}
	
	@Override
	public short getFinishCmd() {
		return  InnerCmdType.S_JIANZHONG_FUBEN_OVER; //副本时间到完成
	}

	@Override
	public void noticeClientExit(Long userRoleId) {
		
		StageMsgSender.send2Bus(userRoleId, InnerCmdType.HAS_EXIT_JIANZHONG_FUBEN, null); 
	}
	
	@Override
	public void leave(IStageElement element) {
		super.leave(element);
		if(ElementType.isRole(element.getElementType())){
			//通知离场
			BusMsgSender.send2One(element.getId(), ClientCmdType.JIANZHONG_FUBEN_EXIT, AppErrorCode.OK);
		}
	}
	
	@Override
	public void enter(IStageElement element, int x, int y) {
		if (ElementType.isRole(element.getElementType())) {
			IRole role = (IRole)element;
			role.getFightAttribute().resetHp(); //进副本满血
		}
		super.enter(element, x, y);
	}


    @Override
    public short getFinishNoticeBusCmd() {
        return 0;
    }

}
