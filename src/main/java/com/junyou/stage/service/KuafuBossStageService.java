package com.junyou.stage.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.activityboss.manage.BossHurtRank;
import com.junyou.bus.kuafu_boss.configure.KuafuBossChengZhangConfigExportService;
import com.junyou.bus.kuafu_boss.configure.KuafuBossRankRewardConfig;
import com.junyou.bus.kuafu_boss.configure.KuafuBossRankRewardConfigExportService;
import com.junyou.bus.kuafu_boss.constants.KuafubossConstants;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.RoleState;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuBossPublicConfig;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.module.GameModType;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.model.stage.kuafuboss.KuafuBossStage;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;
import com.kernel.spring.container.DataContainer;

@Service
public class KuafuBossStageService {
	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
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
	private KuafuBossRankRewardConfigExportService kuafuBossRankRewardConfigExportService;
	@Autowired
	private KuafuBossChengZhangConfigExportService kuafuBossChengZhangConfigExportService;

	public void kuafuBossSendRoleData(Integer activeId, Long userRoleId,
			Object roleData) {
		DingShiActiveConfig config = dingShiActiveConfigExportService
				.getConfig(activeId);
		if (config == null) {
			ChuanQiLog.error("no active config activeId={}", activeId);
			return;
		}
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("no redis config");
			return;
		}
		KuafuBossPublicConfig publicConfig = getPublicConfig();
		String stageId = StageUtil.getStageId(publicConfig.getMapid(), 1);
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			synchronized (this) {
				stage = StageManager.getStage(stageId);
				if (stage == null) {
					stage = createKuafuBossStage(publicConfig, stageId);
					KuafuBossStage kStage = (KuafuBossStage) stage;
					kStage.setDingShiActiveConfig(config);
					kStage.startForceKickSchedule();
				}
			}
		}
		if (stage == null) {
			ChuanQiLog.error("can not create or get stage in kuafuboss");
			return;
		}
		dataContainer.putData(GameConstants.COMPONENET_KUAFU_DATA,
				userRoleId.toString(), roleData);
		IRole role = null;
		try {
			role = RoleFactory.createKuaFu(userRoleId, null, dataContainer
					.getData(GameConstants.COMPONENET_KUAFU_DATA,
							userRoleId.toString()));
		} catch (Exception ex) {
			KuafuMsgSender.send2KuafuSource(userRoleId,
					InnerCmdType.KUAFUBOSS_ENTER_FAIL,
					KuafubossConstants.ENTER_FAIL_REASON_1);

			BusMsgSender.send2BusInner(userRoleId,
					InnerCmdType.INNER_KUAFU_LEAVE, null);

			ChuanQiLog.error("error exit when create Kuafu role ", ex);
			return;
		}
		synchronized (stage) {
			KuafuBossStage kStage = (KuafuBossStage) stage;
			if (!kStage.isRegister(userRoleId)) {
				// 判断人家是否已经达到上限
				String num = redis.get(RedisKey
						.getKuafuBossStageRoleNumKey(ChuanQiConfigUtil
								.getServerId()));
				if (num != null) {
					if (Integer.parseInt(num) >= getPublicConfig()
							.getMaxpople()) {
						KuafuMsgSender.send2KuafuSource(userRoleId,
								InnerCmdType.KUAFUBOSS_ENTER_FAIL,
								KuafubossConstants.ENTER_FAIL_REASON_2);
						BusMsgSender.send2BusInner(userRoleId,
								InnerCmdType.INNER_KUAFU_LEAVE, null);
						ChuanQiLog.error("kuafu boss stage max people");
						return;
					}
				}
			}
			publicRoleStateExportService.change2PublicOnline(userRoleId);
			RoleState roleState = dataContainer.getData(
					GameModType.STAGE_CONTRAL, userRoleId.toString());
			if (null == roleState) {
				roleState = new RoleState(userRoleId);
				dataContainer.putData(GameModType.STAGE_CONTRAL,
						userRoleId.toString(), roleState);
			}
			int[] birthPoint = kStage.getRevivePoint();
			Object[] applyEnterData = new Object[] { publicConfig.getMapid(),
					birthPoint[0], birthPoint[1], MapType.KUAFUBOSS_FUBEN_MAP,
					null, null, null };
			KuafuMsgSender.send2KuafuSource(role.getId(),
					ClientCmdType.KUAFU_BOSS_CANCEL_LOADING, null);
			// 传送前加一个无敌状态
			role.getStateManager().add(new NoBeiAttack());
			role.setChanging(true);
			
			StageMsgSender.send2StageControl(role.getId(),
					InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
			KuafuMsgSender.send2KuafuSource(role.getId(),
					InnerCmdType.KUAFUBOSS_ENTER_XIAOHEIWU, null);
			// 往redis里写数据
			redis.incr(RedisKey.getKuafuBossStageRoleNumKey(ChuanQiConfigUtil
					.getServerId()));
			redis.set(RedisKey.getKuafuBossBindServerIdKey(userRoleId),
					ChuanQiConfigUtil.getServerId());
			ChuanQiLog.info("userRoleId={} enter kuafuboss", userRoleId);
		}
	}

	private PublicFubenStage createKuafuBossStage(
			KuafuBossPublicConfig publicConfig, String stageId) {
		MapConfig mapConfig = mapConfigExportService.load(publicConfig
				.getMapid());
		if (mapConfig == null) {
			ChuanQiLog.error("no map config mapid={}", publicConfig.getMapid());
			return null;
		}
		PublicFubenStage stage = publicFubenStageFactory.createKuafuBossStage(
				stageId, mapConfig);
		StageManager.addStageCopy(stage);
		return stage;
	}

	private KuafuBossPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.KUAFU_BOSS);
	}

	public void activeEnd() {

	}

	public void addExpDingshi(String stageId, Long userRoleId, Long exp) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFUBOSS) {
			return;
		}

		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;// 角色不存在
		}
		KuafuBossStage kStage = (KuafuBossStage) stage;
		KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.INNER_ADD_EXP,
				new Object[] { exp, ClientCmdType.KUAFU_BOSS_EXP_ADD });
		kStage.startAddExpSchedule(userRoleId);
	}

	public void kuafuBossExit(Long userRoleId) {
		int mapId = getPublicConfig().getMapid();
		String stageId = StageUtil.getStageId(mapId, 1);
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFUBOSS) {
			return;
		}
		KuafuBossStage kStage = (KuafuBossStage) stage;
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role != null) {
			kStage.leave(role);
		} else {
			return;
		}
		KuafuMsgSender.send2KuafuSource(userRoleId,
				InnerCmdType.KUAFUBOSS_LEAVE_FB, null);
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE,
				null);
		ChuanQiLog.info("userRoleId={} exit kuafuboss stage", userRoleId);
	}

	public void kuafuBossRank(String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		if (stage.getStageType() != StageType.KUAFUBOSS) {
			return;
		}
		KuafuBossStage kStage = (KuafuBossStage) stage;
		kStage.getKuafuBossMonster().noticeRankData();
		kStage.getKuafuBossMonster().schedulerRefreshRank();
	}

	public void kuafubossForceKick(String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		if (stage.getStageType() != StageType.KUAFUBOSS) {
			return;
		}
		Object[] userRoleIds = stage.getAllRoleIds();
		for (Object id : userRoleIds) {
			IRole role = stage.getElement((Long) id, ElementType.ROLE);
			stage.leave(role);
			KuafuMsgSender.send2KuafuSource((Long) id,
					InnerCmdType.KUAFUBOSS_LEAVE_FB, null);
			BusMsgSender.send2BusInner((Long) id,
					InnerCmdType.INNER_KUAFU_LEAVE, null);
		}
		KuafuBossStage kStage = (KuafuBossStage) stage;
		kStage.stop();
		Redis redis = GameServerContext.getRedis();
		List<Long> roleIds = kStage.getRoleIds();
		for (Long e : roleIds) {
			String kuafuBossBindServerIdKey = RedisKey
					.getKuafuBossBindServerIdKey(e);
			redis.del(kuafuBossBindServerIdKey);
		}
		String serverId = ChuanQiConfigUtil.getServerId();
		redis.set(RedisKey.getKuafuBossStageRoleNumKey(serverId), "0");
		ChuanQiLog.info("force kick all role stageId={}", stageId);
		if (kStage.isCanRemove()) {
			StageManager.removeCopy(kStage);
		}
		String bossDeadFlagKey = RedisKey
				.getKuafuBossDeadFlag(ChuanQiConfigUtil.getServerId());
		redis.del(bossDeadFlagKey);
	}

	public void kuafubossDead(String stageId, Long killerId) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		if (stage.getStageType() != StageType.KUAFUBOSS) {
			return;
		}
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("redis is null when kuaboss dead");
			return;
		}

		KuafuBossStage kStage = (KuafuBossStage) stage;
		kStage.setBossAlive(false);
		String bossDeadFlagKey = RedisKey
				.getKuafuBossDeadFlag(ChuanQiConfigUtil.getServerId());
		redis.setex(bossDeadFlagKey, 30*60, "1");
		// 击杀奖励
		String killerRewardKey = RedisKey.getKuafuBossRewardKey(killerId);
		redis.lpush(killerRewardKey, "0");
		KuafuMsgSender.send2KuafuSource(killerId,
				InnerCmdType.KUAFUBOSS_REWARD_MAIL, 0);
		ChuanQiLog.info("kuafuboss killerId ={}", killerId);

		// 排名奖励
		List<BossHurtRank> ranks = kStage.getKuafuBossMonster().getRanks();
		List<Long> rewardedUserRoleIds = new ArrayList<Long>();
		int rank = 1;
		for (BossHurtRank e : ranks) {
			Long userRoleId = e.getUserRoleId();
			KuafuBossRankRewardConfig rewardConfig = kuafuBossRankRewardConfigExportService
					.loadByRank(rank);
			if(rewardConfig!=null){
				rewardedUserRoleIds.add(userRoleId);
				String key = RedisKey.getKuafuBossRewardKey(userRoleId);
				redis.lpush(key, String.valueOf(rank));
				if (publicRoleStateExportService.isPublicOnline(userRoleId)) {
					KuafuMsgSender.send2KuafuSource(userRoleId,
							InnerCmdType.KUAFUBOSS_REWARD_MAIL, rank);
				}
			}
			ChuanQiLog
					.info("kuafuboss rank={} userRoleId={}", rank, userRoleId);
			rank++;
		}
		Set<Long> allRoleIds = kStage.getKuafuBossMonster().getHurtRoleIds();
		for (Long e : allRoleIds) {
			if (!rewardedUserRoleIds.contains(e)) {
				String key = RedisKey.getKuafuBossRewardKey(e);
				redis.lpush(key, "-1");
				if (publicRoleStateExportService.isPublicOnline(e)) {
					KuafuMsgSender.send2KuafuSource(e,
							InnerCmdType.KUAFUBOSS_REWARD_MAIL, -1);
				}
			}
		}
		String killerName = kStage.getKuafuBossMonster().getBossHurtRank(
				killerId) != null ? kStage.getKuafuBossMonster()
				.getBossHurtRank(killerId).getRoleName() : "";
		Object[] allOnlineRoleIds = kStage.getAllRoleIds();
		for (Object e : allOnlineRoleIds) {
			KuafuMsgSender.send2KuafuSource((Long) e,
					ClientCmdType.KUAFU_BOSS_DEAD, killerName);
		}
		String flag = redis.get(RedisKey.KUAFU_BOSS_DEAD_FLAG);
		if(flag==null){
			redis.set(RedisKey.KUAFU_BOSS_DEAD_FLAG, "1");
			long currentTime = GameSystemTime.getSystemMillTime();
			String startTimeStr = redis.get(RedisKey.KUAFU_BOSS_START_TIME);
			long startTime = Long.parseLong(startTimeStr);
			if((currentTime-startTime)<(getPublicConfig().getAttacktime()*60*1000)){
				int currentId = 1;
				if(kStage.getBossId()!=null){
					currentId = kStage.getBossId();
				}
				int nextId = currentId + 1;
				if(kuafuBossChengZhangConfigExportService.getBossIdById(nextId)!=null){
					redis.set(RedisKey.KUAFU_BOSS_NEXT_ID, String.valueOf(nextId));
				}
			}
		}
	}
	
	public void kuafuBossFuhuo(Long userRoleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFUBOSS) {
			return;
		}
		StageMsgSender.send2StageInner( userRoleId, stageId, InnerCmdType.KUAFUBOSS_AUTO_FUHUO, null);
	}
}
