/**
 * 
 */
package com.junyou.bus.stagecontroll.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.service.export.TencentLuoPanExportService;
import com.junyou.bus.platform.taiwan.export.TaiWanExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.stagecontroll.LoginClientVo;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.PostionType;
import com.junyou.bus.stagecontroll.RoleState;
import com.junyou.bus.stagecontroll.StageLine;
import com.junyou.bus.stagecontroll.StageStatistic;
import com.junyou.bus.stagecontroll.position.AbsRolePosition;
import com.junyou.bus.stagecontroll.position.BaguaFubenStagePosition;
import com.junyou.bus.stagecontroll.position.KuaFuDaLuanDouStagePosition;
import com.junyou.bus.stagecontroll.position.KuaFuDianFengStagePosition;
import com.junyou.bus.stagecontroll.position.KuaFuYunGongStagePosition;
import com.junyou.bus.stagecontroll.position.KuafuArenaFubenStagePosition;
import com.junyou.bus.stagecontroll.position.KuafuStagePosition;
import com.junyou.bus.stagecontroll.position.MaiguFubenStagePosition;
import com.junyou.bus.stagecontroll.position.MoreFubenStagePosition;
import com.junyou.bus.stagecontroll.position.PublicFubenPosition;
import com.junyou.bus.stagecontroll.position.RoleNormalPosition;
import com.junyou.bus.stagecontroll.position.SafePosition;
import com.junyou.bus.stagecontroll.position.ShenmoFubenPosition;
import com.junyou.bus.stagecontroll.position.StageCopyPosition;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.event.RoleInOrOutLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapLineConfig;
import com.junyou.gameconfig.map.configure.util.DiTuConfigUtil;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafuio.util.KuafuOnlineUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.module.GameModType;
import com.junyou.stage.export.RoleStageWrapper;
import com.junyou.stage.export.StageExternalExportService;
import com.junyou.stage.model.core.stage.DeadDisplay;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.utils.parameter.RequestParameterUtil;
import com.kernel.spring.container.DataContainer;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-24上午9:50:11
 */
@Service
public class StageControllService{

	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private StageExternalExportService stageExternalExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private TencentLuoPanExportService tencentLuoPanExportService;
	@Autowired
	private TaiWanExportService taiWanExportService;
	/**
	 * 玩家登陆
	 * @param userRoleId
	 * @return
	 */
	public void login(Long userRoleId) {
		RoleStageWrapper roleStage = stageExternalExportService.getRoleStage(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.NUQI_CHANGE, roleStage.getNuqi());
		//更改登陆状态
		RoleState roleState = new RoleState(userRoleId);
		dataContainer.putData(GameModType.STAGE_CONTRAL, userRoleId.toString(), roleState);
		
		DiTuConfig mapConfig = diTuConfigExportService.loadDiTu(roleStage.getMapId());
		if(mapConfig == null || !mapConfig.isCanCreateAoiStage()){
			mapConfig = diTuConfigExportService.loadFirstMainDiTu();
		}
		//最终进入的地图id
		Integer loginMapId = mapConfig.getId();
		
		int x = roleStage.getMapX();
		int y = roleStage.getMapY();
		
		MapConfig mapPathConfig = mapConfigExportService.load(mapConfig.getId());
		if(!mapPathConfig.isCanUsePoint(x, y)){
			//当上线时发现玩家在一个不可走点,把玩家放到当前地图的出生点
			int[] birthXy = mapConfig.getRandomBirth();
			x = birthXy[0];
			y = birthXy[1];
		}
		AbsRolePosition rolePosition = null;
		if(mapConfig.getType().equals(GameConstants.FIRST_MAP_CONFIG_TYPE) || mapConfig.getType().equals(DiTuConfigUtil.PLOT_MAP_TYPE)){
			rolePosition = new SafePosition(userRoleId,loginMapId, MapType.NEW_SAFE_MAP, x,y);
		}else{
			rolePosition = new RoleNormalPosition(userRoleId,loginMapId, 0, x,y);
		}
		roleState.setReadyForPosition(rolePosition);
		
		//反馈数据
		RoleWrapper loginRole = roleExportService.getLoginRole(userRoleId,sessionManagerExportService.isChenmi(userRoleId));
		
//		long jb = accountExportService.getCurrency(GoodsCategory.MONEY, userRoleId);
//		long yb = accountExportService.getCurrency(GoodsCategory.GOLD, userRoleId);
//		long bindYb = accountExportService.getCurrency(GoodsCategory.BGOLD, userRoleId);
		
//		//数据填充
//		LoginClientVo clientVo = new LoginClientVo();
//		clientVo.setDiTuConfig(mapConfig);
//		clientVo.setRoleStage(roleStage);
//		clientVo.setLoginRole(loginRole);
//		clientVo.setJb(jb);
//		clientVo.setYb(yb);
//		clientVo.setBindYb(bindYb);
		
		//进场景
		change(userRoleId, loginMapId);
		
		
		String via = null;
		String pf = null;
		if(PlatformConstants.isQQ()){
			via  = tencentLuoPanExportService.getUserZhuCeVia(userRoleId);
			pf  = tencentLuoPanExportService.getUserZhuCePf(userRoleId);
			if(pf == null){
				Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, loginRole.getUserId());	
				if(keyMap != null){
					pf = keyMap.get("pf");
				}
			}
			if(via == null){
				Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, loginRole.getUserId());	
				if(keyMap != null){
					via = keyMap.get("via");
				}
			}
		}
		if(PlatformConstants.isTaiWan()){
			pf  = taiWanExportService.getUserZhuCePf(userRoleId);
		}
		//打印角色登陆日志
		GamePublishEvent.publishEvent(new RoleInOrOutLogEvent(LogPrintHandle.ROLE_IN_OR_OUT, LogPrintHandle.ROLE_IO_IN, loginRole.getUserId(), userRoleId.toString(), loginRole.getName(), loginRole.getLastLoginIp(),via,pf));
			
	}
	
	/**
	 * 离开通知场景
	 * @param roleId
	 * @param needData
	 */
	private void leaveNotifyStage(Long roleId,Object needData){
		BusMsgSender.send2Stage(roleId, InnerCmdType.S_Leave_Stage_cmd, needData);
	}
	
	
	public void exit(Long roleId) {
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		if(roleState == null){
			return;
		}
		
		AbsRolePosition curPosition = roleState.getCurPosition();
		if(null == curPosition){
			ChuanQiLog.error("StageControllService:exit with no curStageId! roleId:" + roleId);
			return;
		}
		String curStageId = curPosition.getStageId();
		
		//角色场景数据保存
		stageExternalExportService.syncRoleStageData(roleId, curPosition,null, true);
		
		//离开通知场景
		leaveNotifyStage(roleId, new Object[]{curStageId,roleId});
		
		decrLineNo(curPosition);
		
		//清除登陆状态
		dataContainer.removeData(GameModType.STAGE_CONTRAL, roleId.toString());
		//移除GM隐身标记
		dataContainer.removeData(GameConstants.COMPONENT_GM_HANLE_YS, roleId.toString());
		
	}
	

	
	public void change(Long roleId,Integer mapId) {
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		//保存当前场景数据
		AbsRolePosition curPosition = roleState.getCurPosition();
		if(curPosition != null){
			stageExternalExportService.syncRoleStageData(roleId, curPosition,mapId,false);
			
			//通知当前场景退出
			if(null != curPosition.getStageId()){
				decrLineNo(curPosition);
			}
		}
		
		AbsRolePosition toPosition = roleState.getReadyToPosition();
		if(toPosition == null){
			ChuanQiLog.debug("change readMsg is null ");
			return;
		}
		
		Integer toMapId = toPosition.getMapId();
		
		/**
		 * 进入新场景
		 */
		// b:不跨节点，进入新场景
		Integer lineNo = getStageLine(toPosition.getLineNo(), toMapId);
		toPosition.setLineNo(lineNo);
		
		
		if(null != curPosition){
			if( MapType.usedForOfflineSave(curPosition.getMapType()) ){
				toPosition.setHisRolePosition(curPosition);
			}else{
				toPosition.setHisRolePosition(curPosition.getHisRolePosition());
			}
			curPosition.setHisRolePosition(null);
		}
		
		// 通知新场景进入
		toPosition = checkStageAndEnter(toPosition,curPosition);
		
		//更改角色登陆状态
		roleState.setReadyForPosition(null);
		roleState.setCurPosition(toPosition);
		
		if(MapType.usedForOfflineSave(toPosition.getMapType())){
			roleState.setOfflineSavePosition(new RoleNormalPosition(roleId, toPosition.getMapId(), toPosition.getLineNo(), toPosition.getX(), toPosition.getY()));
		}
		
		//统计地图人数
		incrLineNo(toPosition);
	}
	
	private int getKuafuStageLine(AbsRolePosition readyToPosition){
		Integer mapId = readyToPosition.getMapId();
		
		StageStatistic stageStatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, mapId.toString());
		if (null == stageStatistic) {
			synchronized (this) {
				
				stageStatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, mapId.toString());
				
				if (null == stageStatistic) {
					MapLineConfig config = diTuConfigExportService.loadMapLineByMapId(mapId);
					
					stageStatistic = new StageStatistic(config);
					dataContainer.putData(GameConstants.DATA_CONTAINER_STAGELINE, mapId.toString(), stageStatistic);
				}
			}
		}
		
		synchronized (stageStatistic) {
			Integer readyLine = readyToPosition.getLineNo();
			if(null == readyLine || 0 == readyLine || stageStatistic.isFullLine(readyLine)){
				readyLine = stageStatistic.getMinLoadLine().getLineNo();
			}
			return readyLine;
		}
	}
	
	/** 增加跨服地图人数 */
	private void incrKuafuLineNo(AbsRolePosition rolePosition) {
		
		Integer mapId = rolePosition.getMapId();
		Integer readyLine = rolePosition.getLineNo();
		
		StageStatistic stageStatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, mapId.toString());
		synchronized (stageStatistic) {
			stageStatistic.incrRoleCount(readyLine);
			
			//如果当前线路已满
			if( stageStatistic.isFullLine(readyLine) ){
				stageStatistic.checkLinesFullAndIncr();
			}
		}
	}
	
	//跨服业务需要变动，暂未修改
	public void kuafuChangeMap(Long roleId,String mapId) {
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		if( roleState == null ){
			return;
		}
		
		AbsRolePosition toPosition = roleState.getReadyToPosition();
		
		//分线统计
		Integer lineNo = getKuafuStageLine( toPosition );
		toPosition.setLineNo(lineNo);
		
		AbsRolePosition curPosition = roleState.getCurPosition();
		//通知当前场景退出
		if(null != curPosition){
			//离开通知场景
			Object[] leaveData = new Object[]{curPosition.getStageId(),true};
			if(MapType.KUAFU_FUBEN_MAP.equals(curPosition.getMapType())){
				leaveData[1] = false;
			}
//			leaveNotifyStage(roleId, leaveData);
			
			decrLineNo(curPosition);
		}
		
		
		//创建地图
		toPosition = checkStageAndEnter(toPosition,curPosition);
		
		//更改角色登陆状态
		roleState.setReadyForPosition(null);
		roleState.setCurPosition(toPosition);
		//统计地图人数
		incrKuafuLineNo(toPosition);
		
	}

	
	/**
	 * @param stageId 地图id
	 * @param createIfEmpty 为空时，创建场景
	 */
	private AbsRolePosition checkStageAndEnter(AbsRolePosition toPosition,AbsRolePosition curPosition) {
		if(toPosition.getPostionType() == PostionType.STAGE_COPY_POSITION){
			//副本场景
			StageCopyPosition stageCopyPosition = (StageCopyPosition)toPosition;
			stageExternalExportService.createFuben(stageCopyPosition);
		}else if(toPosition.getPostionType() == PostionType.SAFE_POSITION){
			//安全场景
			SafePosition safePosition = (SafePosition)toPosition;
			stageExternalExportService.checkAndCreateSafeStage(safePosition);
//		}else if(toPosition.getPostionType() == PostionType.KUAFU_STAGE_POSITION){
//			//跨服场景
//			KuafuStagePosition kuafuStagePosition = (KuafuStagePosition)toPosition;
//			stageExternalExportService.checkAndCreateKuafuFuben(kuafuStagePosition);
		}else if(toPosition.getPostionType() == PostionType.MORE_COPY_POSITION){
			// 多人副本场景
			MoreFubenStagePosition morePosition = (MoreFubenStagePosition)toPosition;
			stageExternalExportService.checkAndCreateMoreFuben(morePosition);
		}else if(toPosition.getPostionType() == PostionType.BAGUA_COPY_POSITION){
			// 八卦副本场景
			BaguaFubenStagePosition morePosition = (BaguaFubenStagePosition)toPosition;
			stageExternalExportService.checkAndCreateBaguaFuben(morePosition);
		}else if(toPosition.getPostionType() == PostionType.MAIGU_COPY_POSITION){
			// 埋骨之地副本场景
			MaiguFubenStagePosition morePosition = (MaiguFubenStagePosition)toPosition;
			stageExternalExportService.checkAndCreateMaiguFuben(morePosition);
		}else if(toPosition.getPostionType() == PostionType.KUAFUDALUANDOU_COPY_POSITION){
			// 跨服大乱斗场景
			KuaFuDaLuanDouStagePosition morePosition = (KuaFuDaLuanDouStagePosition)toPosition;
			stageExternalExportService.checkAndCreateDaLuanDouFuben(morePosition);
		}else if(toPosition.getPostionType() == PostionType.KUAFU_DIANFENG_STAGE_POSITION){
            // 跨服巅峰之战场景
		    KuaFuDianFengStagePosition dfPosition = (KuaFuDianFengStagePosition)toPosition;
            stageExternalExportService.checkAndCreateDianFengStage(dfPosition);
        }else if(toPosition.getPostionType() == PostionType.KUAFU_YUNGONG_STAGE_POSITION){
            // 跨服云宫之巅场景
            KuaFuYunGongStagePosition ygPosition = (KuaFuYunGongStagePosition)toPosition;
            stageExternalExportService.checkAndCreateYunGongStage(ygPosition);
        }else if(toPosition.getPostionType() == PostionType.KUAFU_ARENA_SINGLE){
			// 跨服单人竞技
			KuafuArenaFubenStagePosition kuafuArenaSingleposition = (KuafuArenaFubenStagePosition)toPosition;
			stageExternalExportService.checkAndCreateKuafuArenaSingle(kuafuArenaSingleposition);
		}else if(toPosition.getPostionType() == PostionType.PUBLIC_FUBEN_POSITION){
			// 公共副本场景
			PublicFubenPosition position = (PublicFubenPosition)toPosition;
			if(!stageExternalExportService.checkPublicFubenStageIsExist(position)){
				//公共副本异常，返回原地图
				toPosition = curPosition;
			}
		}else{
			//一般地图创建
			try {
				stageExternalExportService.checkAndCreateStage(toPosition.getStageId(),toPosition.getMapId(),toPosition.getLineNo());
			} catch (Exception e) {
				toPosition = curPosition;//地图进入失败，返回原地图
			}
		}
		//leaveStageId,enterStageId,X,Y
		Object[] data = new Object[]{null,toPosition.getStageId(),toPosition.getX(),toPosition.getY()};
		if(curPosition != null){
			data[0] = curPosition.getStageId();
		}
		BusMsgSender.send2Stage(toPosition.getRoleId(), InnerCmdType.S_LEAVE_AFTER_ENTER_CMD, data);
		return toPosition;
	}
	
	
    public void innerApplyChangeMap(Long userRoleId, Object[] data) {
        RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, userRoleId.toString());
        int len = data == null ? 0 : data.length;
        Integer mapId = len > 0 ? RequestParameterUtil.object2Integer(data[0]) : null;
        Integer x = len > 1 ? RequestParameterUtil.object2Integer(data[1]) : null;
        Integer y = len > 2 ? RequestParameterUtil.object2Integer(data[2]) : null;
        Integer mapType = len > 3 ? RequestParameterUtil.object2Integer(data[3]) : null;

        AbsRolePosition absRolePosition = null;
        if (mapType != null && MapType.isFubenMap(mapType)) {
            Integer fubenId = len > 4 ? RequestParameterUtil.object2Integer(data[4]) : null;
            Object d = len > 5 ? data[5] : null;
            StageCopyPosition s = new StageCopyPosition(userRoleId, mapId, mapType, x, y, fubenId, d);
            if (MapType.FUBEN_MAP.equals(mapType)){
            	s.setDeadDisplay(len > 6 ? (DeadDisplay)data[6] : DeadDisplay.NOEXIT);
            }
            absRolePosition = s;
        } else if (mapType != null && MapType.isSafeMap(mapType)) {
            absRolePosition = new SafePosition(userRoleId, mapId, mapType, x, y);
        } else if (mapType != null && MapType.MORE_FUBEN_MAP.equals(mapType)) {
            Integer fubenId = len > 4 ? RequestParameterUtil.object2Integer(data[4]) : null;
            Integer teamId = len > 5 ? RequestParameterUtil.object2Integer(data[5]) : null;
            absRolePosition = new MoreFubenStagePosition(userRoleId, mapId, mapType, x, y, fubenId, teamId);
        } else if (mapType != null && MapType.BAGUA_FUBEN_MAP.equals(mapType)) {
            Integer fubenId = len > 4 ? RequestParameterUtil.object2Integer(data[4]) : null;
            Integer teamId = len > 5 ? RequestParameterUtil.object2Integer(data[5]) : null;
            Integer ceng = len > 7 ? RequestParameterUtil.object2Integer(data[7]) : null;
            absRolePosition = new BaguaFubenStagePosition(userRoleId, mapId, mapType, x, y, fubenId, teamId, ceng);
        } else if (mapType != null && MapType.MAIGU_FUBEN_MAP.equals(mapType)) {
            Integer fubenId = len > 4 ? RequestParameterUtil.object2Integer(data[4]) : null;
            Integer teamId = len > 5 ? RequestParameterUtil.object2Integer(data[5]) : null;
            absRolePosition = new MaiguFubenStagePosition(userRoleId, mapId, mapType, x, y, fubenId, teamId);
        } else if (mapType != null && MapType.KUAFU_AREBA_SINGLE.equals(mapType)) {
            Long matchId = len > 6 ? RequestParameterUtil.object2Long(data[6]) : null;
            absRolePosition = new KuafuArenaFubenStagePosition(userRoleId, mapId, mapType, x, y, matchId);
        } else if (mapType != null && MapType.SHENMO_FUBEN_MAP.equals(mapType)) {
            Long matchId = len > 6 ? RequestParameterUtil.object2Long(data[6]) : null;
            absRolePosition = new ShenmoFubenPosition(userRoleId, mapId, mapType, x, y, matchId);
        } else if (mapType != null && MapType.isPublicFubenMap(mapType)) {
            Integer activeType = len > 4 ? RequestParameterUtil.object2Integer(data[4]) : null;
            absRolePosition = new PublicFubenPosition(userRoleId, mapId, mapType, x, y, activeType);
        } else if (mapType != null && MapType.KUAFUDALUANDOU_FUBEN_MAP.equals(mapType)) {// 跨服大乱斗
            Integer teamId = len > 5 ? RequestParameterUtil.object2Integer(data[5]) : null;
            absRolePosition = new KuaFuDaLuanDouStagePosition(userRoleId, mapId, mapType, x, y, teamId);
        } else if (mapType != null && MapType.KUAFU_DIANFENG_MAP.equals(mapType)) {// 跨服巅峰之战
            Integer loop = len > 4 ? RequestParameterUtil.object2Integer(data[4]) : null;
            Integer room = len > 5 ? RequestParameterUtil.object2Integer(data[5]) : null;
            absRolePosition = new KuaFuDianFengStagePosition(userRoleId, mapId, mapType, x, y, loop, room);
        } else if (mapType != null && MapType.KUAFU_YUNGONG_MAP.equals(mapType)) {// 跨服云宫之巅
            absRolePosition = new KuaFuYunGongStagePosition(userRoleId, mapId, mapType, x, y);
        } else {
            absRolePosition = new RoleNormalPosition(userRoleId, mapId, 0, x, y);
        }
        roleState.setReadyForPosition(absRolePosition);

        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_Change_Stage_cmd, mapId);
    }

	public void applyLeaveStageMap(Long userRoleId) {
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, userRoleId.toString());
		
		AbsRolePosition absRolePosition = roleState.getCurPosition().getHisRolePosition();
		Integer mapId = null;
		if(absRolePosition == null){
			DiTuConfig diTuConfig = diTuConfigExportService.loadFirstMainDiTu();
			mapId = diTuConfig.getId();
			int[] birthXy = diTuConfig.getRandomBirth();
			roleState.setReadyForPosition(new RoleNormalPosition(userRoleId, mapId, 0, birthXy[0], birthXy[1]));
		}else{
			roleState.setReadyForPosition(new RoleNormalPosition(userRoleId, absRolePosition.getMapId(), 0, absRolePosition.getX(), absRolePosition.getY()));
			mapId = absRolePosition.getMapId();
		}
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_Change_Stage_cmd, mapId);
	}
	
	public void kuafuApplyEnter(Long roleId,Integer mapId, int x, int y,int mapType){
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		if(null == roleState){
			roleState = new RoleState(roleId);
			dataContainer.putData(GameModType.STAGE_CONTRAL, roleId.toString(), roleState);
		}
		
		KuafuStagePosition toPosition = new KuafuStagePosition(roleId, mapId, mapType, x, y, null);
		roleState.setReadyForPosition(toPosition);
		
		KuafuManager.startKuafu(roleId);
		
		BusMsgSender.send2BusInner(roleId, InnerCmdType.S_Change_Stage_cmd, mapId);
	}
	
	public void kuafuApplyLeavel(Long roleId) {
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		
		AbsRolePosition historyPosition = roleState.getCurPosition().getHisRolePosition();
		Integer mapId = historyPosition.getMapId();
		int x = historyPosition.getX();
		int y = historyPosition.getY();
		Integer lineNo = historyPosition.getLineNo();
		
		RoleNormalPosition rolePosition = new RoleNormalPosition(roleId,mapId, lineNo, x,y);
		roleState.setReadyForPosition(rolePosition);
		
		KuafuManager.removeKuafu(roleId);
		KuafuRoleServerManager.getInstance().removeBind(roleId);
		
		BusMsgSender.send2BusInner(roleId, InnerCmdType.S_Change_Stage_cmd, mapId);
	}
	
	public void kuafuLogout(Long roleId) {
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		
		if(null == roleState){
			ChuanQiLog.error("StageControllService:exit with no curPosition! roleId->"+roleId);
			return;
		}
		
		AbsRolePosition curPosition = roleState.getCurPosition();
		if(null == curPosition){
			ChuanQiLog.error("StageControllService:exit with no curStageId! roleId->"+roleId);
			return;
		}
		
		//通知离开场景
		leaveNotifyStage(roleId, new Object[]{curPosition.getStageId(),true});
		//清除登录状态
		dataContainer.removeData(GameModType.STAGE_CONTRAL, roleId.toString());
		//地图人数变化
		decrLineNo(curPosition);
		//更改为离线状态
		KuafuOnlineUtil.changeSomeoneOffline(roleId);
		
	}
	
	//跨服业务需要变动，暂未修改
	public void enterKuafuLocalMap(Long roleId, Integer toKuafuMapId) {
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		if( roleState == null ){
			ChuanQiLog.error("someone wannt to enter kuafu server, but error, roleId:{"+roleId+"}, mapid:{"+toKuafuMapId+"}");
			return;
		}
		
		AbsRolePosition curPosition = roleState.getCurPosition();
		//保存当前场景数据
		if(null != curPosition){
			stageExternalExportService.syncRoleStageData(roleId, curPosition,toKuafuMapId,false);
		
			//离开通知场景
//			leaveNotifyStage(roleId, new Object[]{curPosition.getStageId()});
			
			decrLineNo(curPosition);
		}
		
		AbsRolePosition toPosition = roleState.getReadyToPosition();
		if(toPosition == null){
			ChuanQiLog.debug("change readMsg is null ");
			return;
		}
		
		// b:不跨节点，进入新场景
		Integer lineNo = getStageLine(toPosition.getLineNo(), toKuafuMapId);
		toPosition.setLineNo(lineNo);
		
		DiTuConfig mapConfig = diTuConfigExportService.loadDiTu(toKuafuMapId);
		if(mapConfig == null){
			mapConfig = diTuConfigExportService.loadFirstMainDiTu();
			toKuafuMapId = mapConfig.getId();
		}
		
		toPosition = checkStageAndEnter(toPosition,curPosition);
		
		//更改角色登陆状态
		roleState.setReadyForPosition(null);
		roleState.setCurPosition(toPosition);
		
		if(MapType.usedForOfflineSave(toPosition.getMapType())){
			roleState.setOfflineSavePosition(new RoleNormalPosition(roleId, toPosition.getMapId(), toPosition.getLineNo(), toPosition.getX(), toPosition.getY()));
		}
		
		//统计地图人数
		incrLineNo(toPosition);
	}
	

	
	public Integer getCurMapId(Long roleId) {
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		if(null != roleState){
			
			AbsRolePosition rolePosition = roleState.getCurPosition();
			if(rolePosition != null){
				return rolePosition.getMapId();
			}
		}
		
		return null;
	}
	


	/**
	 * 传奇项目,目前没有用到切换线的业务,先不删除
	 * @param roleId
	 * @param lineNo
	 * @return
	 */
	public BusMsgQueue applyChangeLine(Long roleId, Integer lineNo) {
		
		BusMsgQueue queue = new BusMsgQueue();
		
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		AbsRolePosition curPosition = roleState.getCurPosition();
		if(curPosition.getLineNo().equals(lineNo)){
//			queue.addMsg(roleId, StageControllCommands.APPLY_CHANGE_LINE, AppErrorCode.TELEPORT_SAME_LINE_ERROR);
			return queue;
		}
		
		//副本中无法操作
		if(curPosition.isCopyMap()){
//			queue.addMsg(roleId, StageControllCommands.APPLY_CHANGE_LINE, AppErrorCode.IN_FUBEN_ERROR);
			return queue;
		}
		
		//场景状态
		Object errorCode = null;
		if(null != (errorCode = stageExternalExportService.teleportCheck(roleId,curPosition.getStageId()))){
//			queue.addMsg(roleId, StageControllCommands.APPLY_CHANGE_LINE, new Object[]{0,errorCode});
			return queue;
		}		
		
		StageStatistic stageSatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, curPosition.getMapId().toString());
		synchronized(stageSatistic){
			
			//线路已满,请换其它线路
			if(stageSatistic.isFullLine(lineNo)){
//				queue.addMsg(roleId, StageControllCommands.APPLY_CHANGE_LINE, AppErrorCode.LINE_FULL_ERROR);
				return queue;
			}
			
			if(stageSatistic.containLine(lineNo)){
				
				Integer[] xyPoint = stageExternalExportService.getPosition(roleId,curPosition.getStageId());
				
//				queue.addMsg(roleId, StageControllCommands.APPLY_CHANGE_LINE, StageControllOutputWrapper.applyChangeLine(lineNo));
				
				RoleNormalPosition toPosition = new RoleNormalPosition(roleId, curPosition.getMapId(), lineNo, xyPoint[0], xyPoint[1]);
				roleState.setReadyForPosition(toPosition);
				
				//内部先处理地图流程
				change(roleId, curPosition.getMapId());
			}
		}
		
		return queue;
	}
	public Object getMapLines(Long roleId) {
		
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		StageStatistic stageSatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, roleState.getCurPosition().getMapId().toString());
		synchronized(stageSatistic){
			
			Object[] lineInofs = stageSatistic.getLineInfos();
			return lineInofs;
		}
	}
	
	
	private void incrLineNo(AbsRolePosition rolePosition) {
		//暂时副本地图没有做计数(后面有需要再打开)
		if(!rolePosition.isStatisticalLineNo()){
			return;
		}
		
		Integer mapId = rolePosition.getMapId();
		Integer readyLine = rolePosition.getLineNo();
		
		StageStatistic stageStatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, mapId.toString());
		synchronized (stageStatistic) {
			stageStatistic.incrRoleCount(readyLine);
		}
	}
	
	private void decrLineNo(AbsRolePosition rolePosition) {
		//暂时副本地图没有做计数(后面有需要再打开)
		if(!rolePosition.isStatisticalLineNo()){
			return;
		}
		
		Integer mapId = rolePosition.getMapId();
		Integer readyLine = rolePosition.getLineNo();
		
		StageStatistic stageStatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, mapId.toString());
		synchronized (stageStatistic) {
			stageStatistic.decrRoleCount(readyLine);
		}
	}
	
	/**
	 * 获取最佳场景分线
	 * @param readyLine 优先推荐的分线
	 * @param mapId 地图id
	 */
	private Integer getStageLine(Integer readyLine, Integer mapId) {
		
		StageStatistic stageStatistic = null;
		stageStatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, mapId.toString());
		if (null == stageStatistic) {
			synchronized (this) {

				stageStatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, mapId.toString());
				
				if (null == stageStatistic) {
					MapLineConfig config = diTuConfigExportService.loadMapLineByMapId(mapId);
					if(config==null){
						ChuanQiLog.error("MapLineConfig is null,mapId={}",mapId);
					}
					stageStatistic = new StageStatistic(config);
					dataContainer.putData(GameConstants.DATA_CONTAINER_STAGELINE,mapId.toString(), stageStatistic);
				}

			}
		}
		
		synchronized (stageStatistic) {
			
			if(null != readyLine && 0 != readyLine && stageStatistic.containLine(readyLine)){
				return readyLine;
			}else{
				StageLine stageLine = stageStatistic.getMinLoadLine();
				if(stageLine.isNew()){
					//同步创建地图
//					stageExportService.create(getStageId(mapId, stageLine.getLineNo()),mapId,stageLine.getLineNo());
				}
				return stageLine.getLineNo();
			}
			
		}
		
		
	}
	
	public String[] getStageIdsByMapId(String mapId){
		
		int lines = getCurLines(mapId);
		
		if( lines == 0 ) return null;
		
		String[] result = new String[lines];
		String tmp = mapId + "_";
		for (int i = 1; i <= lines; i++) {
			result[i-1] = tmp + i;
		}
		
		return result;
	}
	
	public Integer getCurLines(String mapId) {
		
		StageStatistic stageStatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, mapId);
		if(null != stageStatistic){
			Object[] lineInfos = stageStatistic.getLineInfos();
			if(null != lineInfos){
				return lineInfos.length;
			}
		}
		
		return 0;
	}


	
	public String getCurStageId(Long roleId) {
		
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		if(null != roleState && roleState.getCurPosition() != null){
			return roleState.getCurPosition().getStageId();
		}
		
		return null;
	}
	
	public boolean inFuben(Long roleId){
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		if(roleState != null && roleState.isInFuben()){
			return true;
		}
			
		return false;
	}
	
	/**
	 * 修改副本状态
	 * @param roleId
	 * @param fuben	是否是在副本中
	 */
	public void changeFuben(Long roleId,boolean fuben){
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		if(roleState != null){
			roleState.setInFuben(fuben);
		}
	}
	
	public boolean isChanging(Long roleId){
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		if(roleState == null){
			return false;
			
		}
		String stageId = roleState.getCurPosition().getStageId();
		IStage stage = StageManager.getStage(stageId);
		if(stage==null){
			return false;
		}
		IRole role = stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return false;
		}
		return role.isChanging();
	}
	
	public void setChanging(Long roleId,boolean changing){
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, roleId.toString());
		if(roleState == null){
			return;
		}
		String stageId = roleState.getCurPosition().getStageId();
		IStage stage = StageManager.getStage(stageId);
		if(stage==null){
			return ;
		}
		IRole role = stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return ;
		}
		role.setChanging(changing);
	}

}
