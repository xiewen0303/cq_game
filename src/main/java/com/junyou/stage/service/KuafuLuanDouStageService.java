package com.junyou.stage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafuluandou.configure.DaLuanDouHuoDongBiaoConfig;
import com.junyou.bus.kuafuluandou.configure.DaLuanDouHuoDongBiaoConfigExportService;
import com.junyou.bus.kuafuluandou.constants.KuaFuDaLuanDouConstants;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.RoleState;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuLuanDouPublicConfig;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.module.GameModType;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.model.stage.kuafuluandou.KuaFuLuanDouRank;
import com.junyou.stage.model.stage.kuafuluandou.KuafuLuanDouStage;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;
import com.kernel.spring.container.DataContainer;

@Service
public class KuafuLuanDouStageService {
	@Autowired
	private PublicFubenStageFactory publicFubenStageFactory;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private DaLuanDouHuoDongBiaoConfigExportService daLuanDouHuoDongBiaoConfigExportService;
	
	public void kuafuDaLuanDouSendRoleData(Integer fangjianId, Long userRoleId,String name,Object roleData) {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("no redis config");
			return;
		}
		DaLuanDouHuoDongBiaoConfig ldConfig = daLuanDouHuoDongBiaoConfigExportService.loadById(KuaFuDaLuanDouConstants.LUANDOU_ID);
		if(ldConfig == null){
			ChuanQiLog.error("no DaLuanDouHuoDongBiaoConfig config");
			return;
		}
		String stageId = StageUtil.getStageId(ldConfig.getMap(), fangjianId);
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			synchronized (this) {
				stage = StageManager.getStage(stageId);
				if (stage == null) {
					stage = createKuafuLuanDouStage(ldConfig.getMap(), stageId);
					KuafuLuanDouStage kStage = (KuafuLuanDouStage) stage;
					kStage.setDaLuanDouHuoDongBiaoConfig(ldConfig);
					kStage.startForceKickSchedule();
					kStage.schedulerRefreshRank();
				}
			}
		}
		if (stage == null) {
			ChuanQiLog.error("can not create or get stage in kuafuluandou");
			return;
		}
		dataContainer.putData(GameConstants.COMPONENET_KUAFU_DATA,userRoleId.toString(), roleData);
		IRole role = null;
		try {
			role = RoleFactory.createKuaFu(userRoleId, null, dataContainer.getData(GameConstants.COMPONENET_KUAFU_DATA,userRoleId.toString()));
		} catch (Exception ex) {
			KuafuMsgSender.send2KuafuSource(userRoleId,InnerCmdType.KUAFULUANDOU_ENTER_FAIL,KuaFuDaLuanDouConstants.ENTER_FAIL_REASON_1);

			BusMsgSender.send2BusInner(userRoleId,InnerCmdType.INNER_KUAFU_LEAVE, null);

			ChuanQiLog.error("error exit when create Kuafu role ", ex);
			return;
		}
		synchronized (stage) {
			KuafuLuanDouStage kStage = (KuafuLuanDouStage) stage;
			if (!kStage.isRegister(userRoleId)) {
				// 判断人家是否已经达到上限
				/*String num = redis.get(RedisKey.getKuafuBossStageRoleNumKey(ChuanQiConfigUtil.getServerId()));
				if (num != null) {
					if (Integer.parseInt(num) >= getPublicConfig().getMaxpople()) {
						KuafuMsgSender.send2KuafuSource(userRoleId,InnerCmdType.KUAFULUANDOU_ENTER_FAIL,KuaFuDaLuanDouConstants.ENTER_FAIL_REASON_2);
						BusMsgSender.send2BusInner(userRoleId,InnerCmdType.INNER_KUAFU_LEAVE, null);
						ChuanQiLog.error("kuafu luandou stage max people");
						return;
					}
				}*/
				//初始化角色积分数据
				KuaFuLuanDouRank rank = new KuaFuLuanDouRank();
				rank.setMingci(0);
				rank.setScore(0);
				rank.setUserRoleId(userRoleId);
				rank.setName(name);
				kStage.initUserScore(rank);
				//初始化跨服redis角色积分数据
				String member = userRoleId.toString();
				Long isBaoMing = redis.zrevrank(RedisKey.getRedisKuafuLuanDouFangJianJiFenKey(fangjianId), member);
				if(isBaoMing == null){//玩家存在排名，说明redis里面已经存在数据了,则不再需要再初始化了
					redis.zadd(RedisKey.getRedisKuafuLuanDouFangJianJiFenKey(fangjianId), 0, member);
				}
				
			}
			publicRoleStateExportService.change2PublicOnline(userRoleId);
			RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, userRoleId.toString());
			if (null == roleState) {
				roleState = new RoleState(userRoleId);
				dataContainer.putData(GameModType.STAGE_CONTRAL,
				userRoleId.toString(), roleState);
			}
			int[] birthPoint = kStage.getRevivePoint();
			Object[] applyEnterData = new Object[] { ldConfig.getMap(),birthPoint[0], birthPoint[1], MapType.KUAFUDALUANDOU_FUBEN_MAP,null, fangjianId, null };
			KuafuMsgSender.send2KuafuSource(role.getId(),ClientCmdType.KUAFU_LUANDOU_CANCEL_LOADING, null);
			// 传送前加一个无敌状态
			role.getStateManager().add(new NoBeiAttack());
			role.setChanging(true);
			
			StageMsgSender.send2StageControl(role.getId(),InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
			KuafuMsgSender.send2KuafuSource(role.getId(),InnerCmdType.KUAFULUANDOU_ENTER_XIAOHEIWU, null);
			// 往redis里写数据
			/*redis.incr(RedisKey.getKuafuBossStageRoleNumKey(ChuanQiConfigUtil
					.getServerId()));
			redis.set(RedisKey.getKuafuBossBindServerIdKey(userRoleId),
					ChuanQiConfigUtil.getServerId());*/
			ChuanQiLog.info("userRoleId={} enter kuafudaluandou", userRoleId);
		}
	}

	public PublicFubenStage createKuafuLuanDouStage(int mapId, String stageId) {
		MapConfig mapConfig = mapConfigExportService.load(mapId);
		if (mapConfig == null) {
			ChuanQiLog.error("no map config mapid={}", mapId);
			return null;
		}
		PublicFubenStage stage = publicFubenStageFactory.createKuafuLuanDouStage(stageId, mapConfig);
		StageManager.addStageCopy(stage);
		return stage;
	}

	private KuafuLuanDouPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.KUAFU_DALUANDOU);
	}



	public void kuafuLuanDouExit(Long userRoleId) {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("no redis config");
			return;
		}
		DaLuanDouHuoDongBiaoConfig ldConfig = daLuanDouHuoDongBiaoConfigExportService.loadById(KuaFuDaLuanDouConstants.LUANDOU_ID);
		if(ldConfig == null){
			ChuanQiLog.error("no DaLuanDouHuoDongBiaoConfig config");
			return;
		}
		int mapId = ldConfig.getMap();
		String stageId = StageUtil.getStageId(mapId, getKuaFuFangJianId(redis, userRoleId.toString()));
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFUDALUANDOU) {
			return;
		}
		KuafuLuanDouStage kStage = (KuafuLuanDouStage) stage;
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role != null) {
			kStage.leave(role);
		} else {
			return;
		}
		KuafuMsgSender.send2KuafuSource(userRoleId,InnerCmdType.KUAFULUANDOU_LEAVE_FB, null);
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE,null);
		ChuanQiLog.info("userRoleId={} exit kuafuluandou stage", userRoleId);
	}
	/**
	 * 获取玩家的乱斗房间ID
	 * @param userRoleId
	 * @return
	 */
	private int getKuaFuFangJianId(Redis redis,String userRoleId){
		Long rank = redis.zrevrank(RedisKey.KUAFU_LUANDOU_BAOMING_DATA_KEY, userRoleId);
		if(rank != null && rank < KuaFuDaLuanDouConstants.HAIXUAN_MINGE){
			rank = rank + 1;
			int fangjian = rank.intValue() % 8;
			if(fangjian == 0){
				fangjian = 8;
			}
			return fangjian;
		}else{
			return -1;
		}
	}
	public void kuafuLuanDouRank(String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		if (stage.getStageType() != StageType.KUAFUDALUANDOU) {
			return;
		}
		KuafuLuanDouStage kStage = (KuafuLuanDouStage) stage;
		Object[] rank = kStage.getKuaFuLuanDouRank();
		KuafuMsgSender.send2Many(kStage.getRoleIds().toArray(), ClientCmdType.KUAFU_LUANDOU_PAIHENG, rank);
		kStage.schedulerRefreshRank();
	}

	/**
	 * 活动结束，强制踢人
	 * @param stageId
	 */
	public void kuafudaluandouForceKick(String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		if (stage.getStageType() != StageType.KUAFUDALUANDOU) {
			return;
		}
		Object[] userRoleIds = stage.getAllRoleIds();
		for (Object id : userRoleIds) {
			IRole role = stage.getElement((Long) id, ElementType.ROLE);
			stage.leave(role);
			KuafuMsgSender.send2KuafuSource((Long) id,InnerCmdType.KUAFULUANDOU_LEAVE_FB, null);
			BusMsgSender.send2BusInner((Long) id,InnerCmdType.INNER_KUAFU_LEAVE, null);
		}
		KuafuLuanDouStage kStage = (KuafuLuanDouStage) stage;
		kStage.stop();
		/*Redis redis = GameServerContext.getRedis();
		List<Long> roleIds = kStage.getRoleIds();
		for (Long e : roleIds) {
			String kuafuBossBindServerIdKey = RedisKey.getKuafuBossBindServerIdKey(e);
			redis.del(kuafuBossBindServerIdKey);
		}
		String serverId = ChuanQiConfigUtil.getServerId();
		redis.set(RedisKey.getKuafuBossStageRoleNumKey(serverId), "0");
		ChuanQiLog.info("force kick all role stageId={}", stageId);
		if (kStage.isCanRemove()) {
			StageManager.removeCopy(kStage);
		}
		String bossDeadFlagKey = RedisKey.getKuafuBossDeadFlag(ChuanQiConfigUtil.getServerId());
		redis.del(bossDeadFlagKey);*/
	}
	
	public void kuafuDaLuanDouFuhuo(Long userRoleId){
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("no redis config");
			return;
		}
		DaLuanDouHuoDongBiaoConfig ldConfig = daLuanDouHuoDongBiaoConfigExportService.loadById(KuaFuDaLuanDouConstants.LUANDOU_ID);
		if(ldConfig == null){
			ChuanQiLog.error("no DaLuanDouHuoDongBiaoConfig config");
			return;
		}
		int mapId = ldConfig.getMap();
		String stageId = StageUtil.getStageId(mapId, getKuaFuFangJianId(redis, userRoleId.toString()));
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFUDALUANDOU) {
			return;
		}
		StageMsgSender.send2StageInner( userRoleId, stageId, InnerCmdType.KUAFULUANDOU_AUTO_FUHUO, null);
	}
	
	public void kuafuLuanDouAddJiFen(String stageId,Long userRoleId){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}
		if (stage.getStageType() != StageType.KUAFUDALUANDOU) {
			return;
		}
		KuafuLuanDouStage kStage = (KuafuLuanDouStage) stage;
		//增加房间排行榜积分
		KuaFuLuanDouRank rank = kStage.getMyKuaFuLuanDouRank(userRoleId);
		rank.setScore(rank.getScore() +1);
		//看看玩家是在哪个房间
		int fangjianId = getFangJianIdByStageId(stageId);
		if(fangjianId == 0){
			return;
		}
		//增加redis结算积分
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("no redis config");
			return;
		}
		String member = userRoleId.toString();
		Long isOn = redis.zrevrank(RedisKey.getRedisKuafuLuanDouFangJianJiFenKey(fangjianId), member);
		if(isOn == null){//玩家存在排名，说明redis里面已经存在数据了,则直接加积分
			redis.zadd(RedisKey.getRedisKuafuLuanDouFangJianJiFenKey(fangjianId), 1, member);
		}else{
			redis.zincrby(RedisKey.getRedisKuafuLuanDouFangJianJiFenKey(fangjianId), 1, member);
		}
		
	}
	
	/**
	 * 根据场景ID获取房间ID
	 * @return
	 */
	private int getFangJianIdByStageId(String stageId){
		if(stageId == null || "".equals(stageId)){
			return 0;
		}
		String[] str = stageId.split("_");
		if(str.length < 2){
			return 0;
		}
		return Integer.parseInt(str[1]);
		
	}
	
}
