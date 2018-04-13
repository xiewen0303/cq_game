package com.junyou.stage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.active.export.DingShiActiveExportService;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.RoleState;
import com.junyou.bus.stagecontroll.StageStatistic;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.stagecontroll.position.AbsRolePosition;
import com.junyou.bus.stagecontroll.position.RoleNormalPosition;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.teleport.ICheckCallback;
import com.junyou.gameconfig.export.teleport.TeleportChecker;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.map.configure.export.ChuanSongConfig;
import com.junyou.gameconfig.map.configure.export.ChuanSongConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.map.configure.util.DiTuConfigUtil;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.PlotPointsPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.YabiaoPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.module.GameModType;
import com.junyou.stage.configure.export.impl.FeiXieConfig;
import com.junyou.stage.configure.export.impl.FeiXieConfigExportService;
import com.junyou.stage.export.StageExternalExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.ScopeType;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.biaoche.Biaoche;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.lottery.Lottery;
import com.kernel.spring.container.DataContainer;

@Service
public class TeleportService{
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private ChuanSongConfigExportService chuanSongConfigExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private FeiXieConfigExportService feiXieConfigExportService;
	@Autowired
	private StageExternalExportService stageExternalExportService;
	@Autowired
	private DingShiActiveExportService dingShiActiveExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	
	/**
	 * 传送前加一个无敌状态
	 * @param role
	 */
	private void changeMapAddWuDiState(IRole role){
		//传送前加一个无敌状态
		role.getStateManager().add(new NoBeiAttack());
		role.setChanging(true);
	}
	
	/**
	 * 瞬移后删除无敌状态
	 * @param role
	 */
	private void removeMapAddWuDiState(IRole role){
		role.getStateManager().remove(StateType.NO_ATTACKED);
		role.setChanging(false);
	}
	
	public Object applyWaypointTeleport(String stageId,Long roleId, String waypointId) {
		
		IStage stage = StageManager.getStage(stageId);
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		
		ChuanSongConfig csConfig = chuanSongConfigExportService.loadById(waypointId);
		if(csConfig == null){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_TELEPORT_POINT, AppErrorCode.TELEPORT_ERROR_NO_TELEPORT);
			ChuanQiLog.error("no chuansong config id={}",waypointId);
			return null;
		}
		
		DiTuConfig config = diTuConfigExportService.loadDiTu(csConfig.getTargetMapId());
		if(config == null){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_TELEPORT_POINT, AppErrorCode.TELEPORT_ERROR[1]);
			ChuanQiLog.error("no chuansong target map config id={}",csConfig.getTargetMapId());
			return null;
		}
		
		if(role == null){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_TELEPORT_POINT, AppErrorCode.TELEPORT_ERROR_NO_ROLE);
			ChuanQiLog.error("chuansong error role is null");
			return null;
		}else if(role.getStateManager().isDead()){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_TELEPORT_POINT, AppErrorCode.TELEPORT_ERROR_ROLE_DEAD);
			ChuanQiLog.error("chuansong eroor role is dead");
			return null;
		}
		if(role.isChanging()){
			ChuanQiLog.error("chuansong eroor role is changing");
			return null;
		}
		
		//传送点不在当前地图
		if(!stage.getMapId().equals(csConfig.getCurMapId())){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_TELEPORT_POINT, AppErrorCode.TELEPORT_ERROR_CUR_MAP_NO_TELEPORT);
			ChuanQiLog.error("chuansong not in this map id={}",csConfig.getCurMapId());
			return null;
		}
		
		//地图传送配置验证 
		TeleportChecker teleportChecker = config.getTeleportChecker();
		if(null != teleportChecker){
			ICheckCallback callback = roleExportService.teleportCheck(roleId,teleportChecker);
			if(!callback.isSuccess()){
				StageMsgSender.send2One(roleId, ClientCmdType.APPLY_TELEPORT_POINT, callback.getErrorCode());
				return null;
			}
		}
		
		Integer mapId = csConfig.getTargetMapId();
		Integer x = csConfig.getTargetX();
		Integer y = csConfig.getTargetY();
		//传送前加一个无敌状态
		changeMapAddWuDiState(role);
		if(!mapId.equals(stage.getMapId())){
			
			//切换地图镖车处理
			biaoCheChangeMapHandle(role, stage,mapId);
			
			//随机出生处理
			if(config.isRandomBirth()){
				List<int[]> points = config.getRandomPoints();
				
				if(points != null && points.size() > 0){
					int randomValue = Lottery.roll(points.size());
					int[] tmp = points.get(randomValue);
					
					x = tmp[0];
					y = tmp[1];
				}
			}
			if(config.getType().equals(DiTuConfigUtil.REFB_ACTIVE_MAP_TYPE)){
				return new Object[]{mapId,x,y,MapType.REFB_ACTIVE_MAP_TYPE};
			}else{
				return new Object[]{mapId,x,y};
			}
			
		}else{
			stage.teleportTo(role, x, y);
			//2.瞬移后删除无敌状态
			removeMapAddWuDiState(role);
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{roleId,x,y});
		}
		
		return null;
	}
	/**
	 * 获取分线信息
	 * @param stageId
	 */
	public Object[] applyLineInfo(String stageId){
		StageStatistic stageStatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, StageUtil.getMapId(stageId));
		if(stageStatistic != null){
			return stageStatistic.getLineInfos();
		}
		return null;
	}
	/**
	 * 申请换线
	 * @param userRoleId
	 * @param stageId
	 * @param line
	 */
	public Object[] applyChangeLine(Long userRoleId,String stageId,Integer line){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.STAGE_IS_NOT_EXIST;//场景不存在
		}
		if(stage.isCopy()){
			return AppErrorCode.COPY_CANNOT_CHANGE_LINE;//副本内无法换线
		}
		if(stage.getLineNo().equals(line)){
			return AppErrorCode.IS_SAME_LINE;//已在此线
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;//角色不存在
		}
		if(role.getStateManager().isDead()){
			return AppErrorCode.TELEPORT_CHANGE_LINE_ERROR_ROLE_DEAD;//死亡状态
		}
		StageStatistic stageStatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, stage.getMapId().toString());
		if(stageStatistic == null){
			return AppErrorCode.STAGE_IS_NOT_EXIST;//场景不存在
		}
		if(!stageStatistic.containLine(line)){
			return AppErrorCode.LINE_IS_NULL;//分线不存在
		}
		if(stageStatistic.isFullLine(line)){
			return AppErrorCode.LINE_IS_FULL;//分线已满 
		}
		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, userRoleId.toString());
		AbsRolePosition curPosition = roleState.getCurPosition();
		if(curPosition != null){
			stageExternalExportService.syncRoleStageData(userRoleId,curPosition,stage.getMapId(),false);
			//通知当前场景退出
			if(null != curPosition.getStageId()){
				synchronized (stageStatistic) {
					stageStatistic.decrRoleCount(stage.getLineNo());
				}
			}
		}
		RoleNormalPosition toPosition = new RoleNormalPosition(userRoleId, curPosition.getMapId(), line,role.getPosition().getX(),role.getPosition().getY());
		roleState.setCurPosition(toPosition);
		synchronized (stageStatistic) {
			stageStatistic.incrRoleCount(line);
		}
		String newStageId = StageUtil.getStageId(stage.getMapId(), line);
		Object[] data = new Object[]{stageId,newStageId,role.getPosition().getX(),role.getPosition().getY()}; 
		Biaoche biaoche = role.getBiaoche();
		if(biaoche != null){
			biaoCheChangeMapHandle(role, stage, stage.getMapId());
		}
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.S_LEAVE_AFTER_ENTER_CMD, data);
		return null;//成功
	}
	
	
	/**
	 * 飞鞋传送
	 * @param stageId
	 * @param roleId
	 * @param flyShoesId
	 * @return
	 */
	public Object applyFlyShoesTeleport(String stageId,Long roleId, String flyShoesId){
		if(!ActiveUtil.isCanUseFX()){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_FLY_SHOES,AppErrorCode.ATCIVE_CANNOT_FLY[1]);
			return null;
		}
		//跨服业务期间是不能使用飞鞋的
		if(KuafuManager.kuafuIng(roleId)){
			return null;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return null;
		}
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return null;
		}
		if(role.isChanging()){
			return null;
		}
		
		FeiXieConfig config = feiXieConfigExportService.loadById(flyShoesId);
		//飞鞋配置不存在
		if(config == null){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_FLY_SHOES,AppErrorCode.TELEPORT_FLY_CONFG);
			return null;
		}
		//地图不存在
		DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(config.getMapId());
		if(diTuConfig == null){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_FLY_SHOES, AppErrorCode.TELEPORT_ERROR[1]);
			return null;
		}
		//地图传送配置验证 
		TeleportChecker teleportChecker = diTuConfig.getTeleportChecker();
		if(null != teleportChecker){
			ICheckCallback callback = roleExportService.teleportCheck(roleId,teleportChecker);
			if(!callback.isSuccess()){
				StageMsgSender.send2One(roleId, ClientCmdType.APPLY_FLY_SHOES, callback.getErrorCode());
				return null;
			}
		}
		
		int[] xys = config.getZuobiaoByLottery();
		//没有配置飞鞋坐标
		if(xys == null){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_FLY_SHOES, AppErrorCode.TELEPORT_FLY_ZB_CONFG);
			return null;
		}
		
		//此次飞鞋，不是免费
		if(!config.isFree()){
			int feixie = roleVipInfoExportService.getVipTequan(roleId, GameConstants.VIP_FEIXIE);
			//特权配置错误
			if(feixie < 1){//没有飞鞋特权
				if(ObjectUtil.strIsEmpty(ModulePropIdConstant.FEI_XIE_ITEM_ID)){
					StageMsgSender.send2One(roleId, ClientCmdType.APPLY_FLY_SHOES, AppErrorCode.CONFIG_ERROR[1]);
					return null;
				}
				BagSlots reslut = roleBagExportService.removeBagItemByGoodsId(ModulePropIdConstant.FEI_XIE_ITEM_ID, 1, roleId, GoodsSource.FEIXIE, true, true);
				if(!reslut.isSuccee()){
					StageMsgSender.send2One(roleId, ClientCmdType.APPLY_FLY_SHOES, AppErrorCode.TELEPORT_FLY_NO_ITEMS);
					return null;
				}
			}
		}
		
		int x = xys[0];
		int y = xys[1];
		//传送前加一个无敌状态
		changeMapAddWuDiState(role);
		if(!config.getMapId().equals(stage.getMapId())){
			
			return new Object[]{config.getMapId(),x,y};
		}else{
			//1.给切换的人推送 100
			StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x,y,stage.getLineNo()});
			
			stage.teleportTo(role, x, y);
			
			//2.瞬移后删除无敌状态
			removeMapAddWuDiState(role);
			
			//2.给当前AOI区域内的其它人推送702 单位瞬间移动
			StageMsgSender.send2Many(stage.getNoSelfSurroundRoleIds(role.getPosition(), role.getId()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{roleId,x,y});
//			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{roleId,x,y});
		}
		
		//修炼任务
  		BusMsgSender.send2BusInner(role.getId(), InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.USE_FEIXIAO, null});
		
		return null;
	}
	/**
	 * 回城卷轴
	 * @param stageId
	 * @param roleId
	 * @param guid
	 * @return
	 */
	public Object applyTownPortalTeleport(String stageId,Long roleId,Long guid){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return null;
		}
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return null;
		}
		if(role.isChanging()){
			return null;
		}
		RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(roleId, guid);
		if(roleItemExport == null){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_TOWN_PORTAL, AppErrorCode.NOT_FOUND_GOOODS[1]);
			return null;
		}
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItemExport.getGoodsId());
		if(goodsConfig == null){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_TOWN_PORTAL, AppErrorCode.CONFIG_ERROR_NUMBER);
			return null;
		}
		DiTuConfig diTuConfig = null;
		int x = 0;
		int y = 0;
		if(GoodsCategory.isTP(goodsConfig.getCategory())){//回城卷轴
			if(roleItemExport.getCount() < 1){
				StageMsgSender.send2One(roleId, ClientCmdType.APPLY_TOWN_PORTAL, AppErrorCode.GOODS_NOT_ENOUGH[1]);
				return null;
			}
			diTuConfig = diTuConfigExportService.loadDiTu(goodsConfig.getData1());
			if(diTuConfig == null){
				StageMsgSender.send2One(roleId, ClientCmdType.APPLY_TOWN_PORTAL, AppErrorCode.CONFIG_ERROR_NUMBER);
				return null;
			}
			int[] xys = diTuConfig.getRandomBirth();
			x = xys[0];
			y = xys[1];
		}else{
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_TOWN_PORTAL, AppErrorCode.CONFIG_ERROR_NUMBER);
			return null;
		}
		
		if(diTuConfig != null){
			roleBagExportService.removeBagItemByGuid(guid, 1, roleId, GoodsSource.USE_ITEM, true, true);
			//传送前加一个无敌状态
			changeMapAddWuDiState(role);
			if(!stage.getMapId().equals(diTuConfig.getId())){
				
				return new Object[]{diTuConfig.getId(),x,y};
			}else{
				//1.给切换的人推送 100
				StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x,y,stage.getLineNo()});
				
				stage.teleportTo(role, x, y);
				
				//2.瞬移后删除无敌状态
				removeMapAddWuDiState(role);
				
				//2.给当前AOI区域内的其它人推送702 单位瞬间移动
				StageMsgSender.send2Many(stage.getNoSelfSurroundRoleIds(role.getPosition(), role.getId()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{roleId,x,y});
			}
		}
		return null;
	}
	
	/**
	 * 申请每日活动地图传送
	 * @param userRoleId
	 * @param stageId
	 * @param line
	 */
	public Object[] applyGotoActiveMap(Long userRoleId,String stageId,Integer activeId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.STAGE_IS_NOT_EXIST;//场景不存在
		}
		if(stage.isCopy()){
			return AppErrorCode.COPY_CANNOT_CHANGE_MAP;//副本内无法换线
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;//角色不存在
		}
		DingShiActiveConfig config = dingShiActiveExportService.getActiveInTime(activeId);
		if(config == null){
			return AppErrorCode.NOT_IN_TIME_CANNOT_CHANGE_MAP;//不在活动期间内
		}
		int mapId = config.getMapId();
		if(mapId < 1){
			return AppErrorCode.ACTIVE_CANNOT_CHANGE_MAP;//当前活动无传送地址
		}
		int x = config.getX();
		int y = config.getY();
		
		changeMapAddWuDiState(role);
		if(!stage.getMapId().equals(mapId)){
			StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, new Object[]{mapId,x,y});
		}else{
			//1.给切换的人推送 100
			StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x,y,stage.getLineNo()});
			
			stage.teleportTo(role, x, y);
			
			//2.瞬移后删除无敌状态
			removeMapAddWuDiState(role);
			
			//3.给当前AOI区域内的其它人推送702 单位瞬间移动
			StageMsgSender.send2Many(stage.getNoSelfSurroundRoleIds(role.getPosition(), role.getId()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{userRoleId,x,y});
		}
		return null;
	}
	
	/**
	 * 飞到镖车附近
	 * @param stageId
	 * @param roleId
	 * @return
	 */
	public void flyToBiaoChe(String stageId,Long userRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return ;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		Biaoche biaoche = dataContainer.getData(GameConstants.COMPONENT_BIAOCHE_NMAE, userRoleId+"");
		if(biaoche == null){
			return;
		}
		
		YabiaoPublicConfig ybPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YABIAO);
		if(ybPublicConfig == null){
			return;
		}
		
		//判断地图
		Integer mapId = biaoche.getStage().getMapId();
		DiTuConfig config = diTuConfigExportService.loadDiTu(mapId);
		if(config == null){
			StageMsgSender.send2One(userRoleId, ClientCmdType.FLY_BIAOCHE, AppErrorCode.TELEPORT_ERROR);
			return;
		}
		
		//判断传送到镖车需要的游戏币
		if(ybPublicConfig.getNeedMoney() > 0){
			Object[] errorCode = accountExportService.isEnought(GoodsCategory.MONEY, ybPublicConfig.getNeedMoney(), userRoleId);
			if(errorCode != null){
				StageMsgSender.send2One(userRoleId, ClientCmdType.FLY_BIAOCHE, errorCode);
				return;
			}else{
				accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, ybPublicConfig.getNeedMoney(), userRoleId, LogPrintHandle.CONSUME_YABIAO, true, LogPrintHandle.CBZ_YABIAO);
			}
		}
		
		//通知客户端传送成功
		StageMsgSender.send2One(userRoleId, ClientCmdType.FLY_BIAOCHE, AppErrorCode.OK);
		
		Point point = biaoche.getStage().getSurroundValidPoint(biaoche.getPosition(),false, biaoche.getTakeupType());
		int x = point.getX();
		int y = point.getY();
		//传送前加一个无敌状态
		changeMapAddWuDiState(role);
		if(!biaoche.getStage().getId().equals(stage.getId())){
			
//			biaoCheChangeMapHandle(role, stage,mapId);
			RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, userRoleId.toString());
			AbsRolePosition absRolePosition = new RoleNormalPosition(userRoleId,mapId, biaoche.getStage().getLineNo(), x,y);
			roleState.setReadyForPosition(absRolePosition);
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_Change_Stage_cmd, mapId);
		}else{
			//1.给切换的人推送 100
			StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x,y,stage.getLineNo()});
			
			stage.teleportTo(role, x, y);
			//2.瞬移后删除无敌状态
			removeMapAddWuDiState(role);
			//3.给当前AOI区域内的其它人推送702 单位瞬间移动
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()),  ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{userRoleId,x,y});
		}
		
	}
	
	/**
	 * 镖车切地图业务
	 * @param role
	 * @param stage
	 */
	private void biaoCheChangeMapHandle(IRole role,IStage stage,Integer targetMapId){
		YabiaoPublicConfig yaBiaoPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YABIAO);
		if(yaBiaoPublicConfig == null){
			return;
		}
		
		int maxGz = yaBiaoPublicConfig.getMaxCell();
		Biaoche biaoche = role.getBiaoche();
		if(biaoche == null){
			return;
		}
		
		DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(targetMapId);
		//目标地图镖车不允许进
		if(null == diTuConfig  || !diTuConfig.isBiaoCheCanEnter()){
			return;
		}
		
		if(role.getStage() == null || biaoche.getStage() == null){
			return;
		}
		
		//同地图
		if(role.getStage().getId().equals(biaoche.getStage().getId())){
			
			if(role.getStage().inScope(role.getPosition(), biaoche.getPosition(), maxGz, ScopeType.GRID)){
				biaoche.getStage().leave(biaoche);
				biaoche.leaveStageHandle(stage);
				
				biaoche.setChangeMap(true);
			}
		}
//		else if(targetMapId.equals(biaoche.getStage().getMapId())){ Liuyu:取消使用
//			//进入镖车的地图
//			Point roleTargetPoint = new Point(targetX,targetY);
//			
//			if(role.getStage().inScope(roleTargetPoint, biaoche.getPosition(), maxGz, ScopeType.GRID)){
//				biaoche.getStage().leave(biaoche);
//				biaoche.leaveStageHandle(stage);
//				
//				biaoche.setChangeMap(true);
//			}
//		}
	}
	public Object applyPlotWaypointTeleport(String stageId, Long userRoleId, int id) {
		
		PlotPointsPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.JUQINGPOINT);
		if(config == null){
			ChuanQiLog.error("公共数据表配置不存在,key="+PublicConfigConstants.JUQINGPOINT);
			BusMsgSender.send2One(userRoleId, ClientCmdType.APPLY_TELEPORT_PLOT_STAGE,AppErrorCode.NO_FIND_CONFIG[1]);
			return null;
		}
		
		int[] data = config.getData(id);
		if(data == null || data.length < 3){
			ChuanQiLog.error("公共数据表配置不存在,key="+PublicConfigConstants.JUQINGPOINT+"\t id:",id+"\tdataLen="+data.length);
			BusMsgSender.send2One(userRoleId, ClientCmdType.APPLY_TELEPORT_PLOT_STAGE,AppErrorCode.NO_FIND_CONFIG[1]);
			return null;
		}
		
		int toMapId = data[0];
		int toMapX = data[1];
		int toMapY = data[2];
		
		MapConfig mapConfig = mapConfigExportService.load(toMapId);
		
		if(mapConfig == null) {
			ChuanQiLog.error("MapConfig配置不存在,mapId"+toMapId);
			BusMsgSender.send2One(userRoleId, ClientCmdType.APPLY_TELEPORT_PLOT_STAGE,AppErrorCode.NO_FIND_CONFIG[1]);
			return null;
		}
		
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(toMapId);
		if(dituCoinfig == null){
			ChuanQiLog.error("DiTuConfig配置不存在,mapId"+toMapId);
			BusMsgSender.send2One(userRoleId, ClientCmdType.APPLY_TELEPORT_PLOT_STAGE,AppErrorCode.NO_FIND_CONFIG[1]);
			return null;
		}
		
		if(dituCoinfig.getType() != DiTuConfigUtil.BRITH_MAP_TYPE  && dituCoinfig.getType() != DiTuConfigUtil.PLOT_MAP_TYPE){
			ChuanQiLog.error("不能进入非剧情地图,mapId"+toMapId+"\t Type:" + dituCoinfig.getType());
			BusMsgSender.send2One(userRoleId, ClientCmdType.APPLY_TELEPORT_PLOT_STAGE,AppErrorCode.NO_FIND_CONFIG[1]);
			return null;
		}
		
		IStage stage = StageManager.getStage(stageId);
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		
		if(role == null){
			StageMsgSender.send2One(userRoleId, ClientCmdType.APPLY_TELEPORT_POINT, AppErrorCode.TELEPORT_ERROR_NO_ROLE);
			ChuanQiLog.error("chuansong error role is null");
			return null;
		}else if(role.getStateManager().isDead()){
			StageMsgSender.send2One(userRoleId, ClientCmdType.APPLY_TELEPORT_POINT, AppErrorCode.TELEPORT_ERROR_ROLE_DEAD);
			ChuanQiLog.error("chuansong eroor role is dead");
			return null;
		}
		if(role.isChanging()){
			ChuanQiLog.error("chuansong eroor role is changing");
			return null;
		}
		
		Object[] applyEnterData = new Object[]{toMapId, toMapX, toMapY, MapType.NEW_SAFE_MAP};
		return applyEnterData;
	}
}