package com.junyou.bus.map.configure.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.map.configure.export.ActiveMapConfigExportService;
import com.junyou.bus.map.entity.ActiveMapConfig;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.active.RefbActiveFubenStage;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author LiuYu
 * 2015-8-5 下午8:47:46
 */
@Service
public class ActiveMapService {
	
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private PublicFubenStageFactory publicFubenStageFactory;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private BusScheduleExportService scheduleExportService;
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		ActiveMapConfig config = ActiveMapConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		return config.getClientData();
	}
	
	public Object[] enterMap(Long userRoleId,Integer subId,Integer version){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		ActiveMapConfig config = ActiveMapConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object[] data = new Object[]{subId,configSong.getClientVersion(),config.getClientData()};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
		if(GameSystemTime.getSystemMillTime() < config.getStartTime() || GameSystemTime.getSystemMillTime() >= config.getEndTime()){
			return AppErrorCode.NOT_IN_TIME_CANNOT_CHANGE_MAP;
		}
		
		if(stageControllExportService.inFuben(userRoleId)){
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}
		
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMapId());
		int[] birthXy = dituCoinfig.getRandomBirth();
		Object[] applyEnterData = new Object[]{dituCoinfig.getId(),birthXy[0],birthXy[1], MapType.REFB_ACTIVE_MAP_TYPE, config.getId()};
		StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		
		return null;
	}
	
	
	public IStage createStage(String stageId,Integer subId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return null;
		}
		ActiveMapConfig config = ActiveMapConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		if(GameSystemTime.getSystemMillTime() < config.getStartTime() || GameSystemTime.getSystemMillTime() >= config.getEndTime()){
			return null;
		}
		IStage istage = createStage(stageId, subId, config,config.getMapId());
		return istage;
	}

	private IStage createStage(String stageId, Integer subId,ActiveMapConfig config,Integer mapId) {
		IStage istage = null;
		synchronized (config) {
			istage = StageManager.getStage(stageId);
			if(istage == null){
				Long delay = (config.getEndTime() - GameSystemTime.getSystemMillTime())/1000;
				if(delay <= 0){
					return null;//活动已经结束
				}
				MapConfig mapConfig = mapConfigExportService.load(mapId);
				istage = publicFubenStageFactory.create(stageId, StageUtil.getLineNo(stageId), mapConfig);
				if(istage != null && StageType.isRefbActiveFuben(istage.getStageType())){
					if(delay > 0){
						delay += 30;//增加30秒,防止spring定时器时间波动,导致业务异常
						RefbActiveFubenStage stage = (RefbActiveFubenStage)istage;
						stage.setKickTime(config.getEndTime());
						StageManager.addStageCopy(stage);
						BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.INNER_REFB_ACTIVE_MAP_OVER,stageId);
						scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(),GameConstants.COMPONENET_REFB_ACTIVE_MAP + subId + "_" + stageId, runable, delay.intValue()	, TimeUnit.SECONDS);
					}
				}
			}
		}
		return istage;
	}
	
	public void mapClear(String stageId){
		IStage istage = StageManager.getStage(stageId);
		if(istage != null && StageType.isRefbActiveFuben(istage.getStageType())){
			if(istage.isCanRemove()){
				StageManager.removeCopy(istage);
			}else{
				RefbActiveFubenStage stage = (RefbActiveFubenStage)istage;
				stage.kickAll();
			}
		}
	}
	public void mapCreate(Integer subId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong == null){
			return;
		}
		ActiveMapConfig config = ActiveMapConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return;
		}
		List<Integer> mapIds = config.getSubMapIds();
		if(mapIds != null && mapIds.size() > 0){
			for (Integer mapId : mapIds) {
				String stageId = StageUtil.getStageId(mapId, 1);
				createStage(stageId, subId, config,mapId);
			}
		}
	}
	
	
}
